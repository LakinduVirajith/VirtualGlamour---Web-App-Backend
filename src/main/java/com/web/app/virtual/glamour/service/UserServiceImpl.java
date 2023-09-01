package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.common.CommonFunctions;
import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.UserDTO;
import com.web.app.virtual.glamour.entity.ActivationToken;
import com.web.app.virtual.glamour.entity.User;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.ConflictException;
import com.web.app.virtual.glamour.exception.InternalServerException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.repository.ActivationTokenRepository;
import com.web.app.virtual.glamour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ActivationTokenRepository activationTokenRepository;
    private final EmailService emailService;
    private final CommonFunctions commonFunctions;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<ResponseMessage> register(UserDTO userDTO) throws ConflictException, InternalServerException {
        Optional<User> emailCondition = userRepository.findByEmail(userDTO.getEmail());

        // EMAIL CONFLICT EXCEPTION
        if(emailCondition.isPresent()){
            throw new ConflictException("Email already exists");
        }

        // ENCODE PASSWORD USING PASSWORD-ENCODER
        String encodedPassword = encodePassword(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        // GENERATE ACTIVATION TOKEN AND SET IT'S EXPIRY DATE
        String token = UUID.randomUUID().toString().substring(0, 32);
        LocalDateTime tokenExpiry = LocalDateTime.now().plusMinutes(10);

        ActivationToken activationToken = ActivationToken.builder()
                .activationToken(token).activationTokenExpiry(tokenExpiry).build();

        userDTO.setIsActive(false);
        User user = new User();
        modelMapper.map(userDTO, user);
        user.setActivationToken(activationToken);
        user = userRepository.save(user);

        activationToken.setTokenId(user.getActivationToken().getTokenId());
        activationToken.setUser(user);
        activationTokenRepository.save(activationToken);

        // EMAIL SERVICE CALL
        Boolean result = emailService.sendActivationMail(user, activationToken);
        if(!result){
            throw new InternalServerException("Something went wrong while sending the activation email");
        }

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("User registered successfully");
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public ResponseEntity<ResponseMessage> activate(String token) throws NotFoundException, BadRequestException, ConflictException {
        ActivationToken activationToken = activationTokenRepository.findByActivationToken(token)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // ALREADY ACTIVATED EXCEPTION
        if(activationToken.getUser().getIsActive()){
            throw new ConflictException("User is already activated");
        }

        LocalDateTime activationTokenExpiry = activationToken.getActivationTokenExpiry();
        // TOKEN EXPIRED EXCEPTION
        if (activationTokenExpiry.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Activation token has expired");
        }

        activationToken.getUser().setIsActive(true);
        activationTokenRepository.save(activationToken);

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("User activated successfully");
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public ResponseEntity<ResponseMessage> deactivate() throws NotFoundException, BadRequestException {
        User user = commonFunctions.getUser();

        if(user == null){
            throw new NotFoundException("User not found");
        }

        // ALREADY DEACTIVATED EXCEPTION
        if(!user.getIsActive()){
            throw new BadRequestException("User is already deactivated");
        }

        user.setIsActive(false);
        userRepository.save(user);

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("User deactivated successfully");
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public ResponseEntity<ResponseMessage> restPassword(String email, String password) throws NotFoundException {
        Optional<User> userCondition = userRepository.findByEmail(email);

        // INVALID USER EXCEPTION
        if(userCondition.isEmpty()){
            throw new NotFoundException("No user found with the provided email address.");
        }
        User user = userCondition.get();

        // ENCODE PASSWORD USING PASSWORD-ENCODER
        String encodedPassword = encodePassword(password);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("Password reset successfully");
        return ResponseEntity.ok().body(successResponse);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
