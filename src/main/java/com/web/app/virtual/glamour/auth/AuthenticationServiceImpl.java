package com.web.app.virtual.glamour.auth;

import com.web.app.virtual.glamour.common.CommonFunctions;
import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.config.jwt.JwtService;
import com.web.app.virtual.glamour.entity.AuthToken;
import com.web.app.virtual.glamour.entity.ForgotOTP;
import com.web.app.virtual.glamour.entity.User;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.ForbiddenException;
import com.web.app.virtual.glamour.exception.InternalServerException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.repository.AuthTokenRepository;
import com.web.app.virtual.glamour.repository.ForgotOTPRepository;
import com.web.app.virtual.glamour.repository.UserRepository;
import com.web.app.virtual.glamour.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;
    private final ForgotOTPRepository otpRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final CommonFunctions commonFunctions;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) throws ForbiddenException, NotFoundException, InternalServerException {
        Optional<User> userCondition = userRepository.findByEmail(request.getEmail());

        // NOT FOUND EXCEPTION
        if(userCondition.isEmpty()){
            throw new NotFoundException("Invalid user name or password");
        }

        // NOT ACTIVATE EXCEPTION
        if(!userCondition.get().getIsActive()){
            regenerateActivationToken(userCondition.get());
            throw new ForbiddenException("Your account is not activated. Please check your email to verify your account first.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user  = userCondition.get();

        // GENERATE ACCESS-TOKEN
        var jwtToken = jwtService.generateToken(user);
        saveToken(user, jwtToken);

        // GENERATE REFRESH-TOKEN
        var refreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok().body(AuthenticationResponse.builder()
                .statusCode(200).
                status(HttpStatus.OK).
                message("User authenticated successfully").
                accessToken(jwtToken).
                refreshToken(refreshToken).build());
    }

    @Override
    public ResponseEntity<AuthenticationResponse> refreshToken(String refreshToken) throws BadRequestException, NotFoundException, InternalServerException {
        final String userEmail;
        userEmail = jwtService.extractUsername(refreshToken);

        // INVALID TOKEN EXCEPTION
        if(userEmail.isEmpty()){
            throw new BadRequestException("Invalid token provided");
        }

        // INVALID ACCOUNT EXCEPTION
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent() && !optionalUser.get().getIsActive()) {
            throw new BadRequestException("Not a valid account");
        }

        // INVALID USER EXCEPTION
        var user = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // GENERATE TOKEN
        AuthenticationResponse authResponse = null;
        if (jwtService.isTokenValid(refreshToken, user)) {
            var accessToken = jwtService.generateToken(user);
            saveToken(user, accessToken);

            authResponse = AuthenticationResponse.builder().
                    statusCode(200).
                    status(HttpStatus.OK).
                    message("Using refresh token user authenticated successfully").
                    accessToken(accessToken).
                    refreshToken(refreshToken).build();
        }

        // SERVER ERROR EXCEPTION
        if(authResponse == null){
            throw new InternalServerException("Something went wrong with generating the token");
        }
        return ResponseEntity.ok().body(authResponse);
    }

    @Override
    public ResponseEntity<ResponseMessage> logout() throws BadRequestException, NotFoundException {
        var token = commonFunctions.getToken();
        Optional<AuthToken> optionalToken = authTokenRepository.findByToken(token);

        if(optionalToken.isPresent()){
            var existToken = optionalToken.get();
            existToken.setExpired(true);
            existToken.setRevoked(true);
            authTokenRepository.save(existToken);
        }else{
            throw new BadRequestException("Invalid logout");
        }

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("User logout successfully");
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public ResponseEntity<ResponseMessage> forgotPassword(String email) throws NotFoundException, InternalServerException {
        Optional<User> userCondition = userRepository.findByEmail(email);

        // INVALID USER EXCEPTION
        if(userCondition.isEmpty()){
            throw new NotFoundException("No user found with the provided email address.");
        }

        // GENERATE OTP
        Random random = new Random();
        int otpNumber = random.nextInt(9000) + 1000;
        String otp = Integer.toString(otpNumber);

        // OTP VALID FOR 4 MINTS
        LocalDateTime otpExpiry = LocalDateTime.now().plusMinutes(4);

        User user = userCondition.get();
        // IF ALREADY HAVE OTP ELSE NO ANY OTP
        if(user.getForgotOTP() != null){
            user.getForgotOTP().setOtpNumber(otp);
            user.getForgotOTP().setOtpExpiry(otpExpiry);

            otpRepository.save(user.getForgotOTP());
        }else{
            ForgotOTP forgotOTP = ForgotOTP.builder()
                    .otpNumber(otp).otpExpiry(otpExpiry).user(user).build();
            user.setForgotOTP(forgotOTP);

            userRepository.save(user);
        }

        // EMAIL SERVICE CALL
        Boolean result = emailService.sendOTPMail(user);
        if(!result){
            throw new InternalServerException("Something went wrong while sending the OTP email");
        }

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("OTP generated successfully");
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public ResponseEntity<ResponseMessage> OTPValidation(String email, String otp) throws NotFoundException, BadRequestException {
        Optional<User> userCondition = userRepository.findByEmail(email);

        // INVALID USER EXCEPTION
        if(userCondition.isEmpty()){
            throw new NotFoundException("No user found with the provided email address.");
        }
        User user = userCondition.get();

        // INVALID OTP EXCEPTION
        if(!user.getForgotOTP().getOtpNumber().equals(otp)){
            throw new BadRequestException("Invalid OTP. Please double-check and try again.");
        }

        // OTP EXPIRED EXCEPTION
        if(user.getForgotOTP().getOtpExpiry().isBefore(LocalDateTime.now())){
            throw new BadRequestException("The OTP has expired. Please request a new one.");
        }

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("OTP validated successfully");
        return ResponseEntity.ok().body(successResponse);
    }

    private void saveToken(User user, String jwtToken) {
        Optional<AuthToken> OptionalToken = authTokenRepository.findByUserUserId(user.getUserId());

        AuthToken token;
        // IF ALREADY HAVE TOKEN ELSE NO ANY TOKEN
        if(OptionalToken.isPresent()){
            token = OptionalToken.get();
            token.setToken(jwtToken);
            token.setExpired(false);
            token.setRevoked(false);

            authTokenRepository.save(token);
        }else{
            token = AuthToken.builder().user(user).token(jwtToken).expired(false).revoked(false).build();
            user.setAuthToken(token);

            userRepository.save(user);
        }
    }

    private void regenerateActivationToken(User user) throws InternalServerException {
        // GENERATE ACTIVATION TOKEN
        String token = UUID.randomUUID().toString().substring(0, 32);

        // ACTIVATION LINK VALID FOR 10 MINTS
        LocalDateTime tokenExpiry = LocalDateTime.now().plusMinutes(10);

        user.getActivationToken().setActivationToken(token);
        user.getActivationToken().setActivationTokenExpiry(tokenExpiry);
        userRepository.save(user);

        // EMAIL SERVICE CALL
        Boolean result = emailService.sendActivationMail(user, user.getActivationToken());
        if(!result){
            throw new InternalServerException("Something went wrong while sending the activation email");
        }
    }
}
