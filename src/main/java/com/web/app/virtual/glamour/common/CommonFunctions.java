package com.web.app.virtual.glamour.common;

import com.web.app.virtual.glamour.config.jwt.JwtService;
import com.web.app.virtual.glamour.entity.User;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonFunctions {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static String token;
    public void storeJWT(String jwt) {
        token = jwt;
    }

    public String getToken() throws NotFoundException {
        if(!token.isEmpty()){
            return token;
        }else{
            throw new NotFoundException("Token not found");
        }
    }

    public String getUserEmail(){
        return jwtService.extractUsername(token);
    }

    public Long getUserId() throws NotFoundException {
        Optional<User> user = userRepository.findByEmail(getUserEmail());

        if(user.isPresent()){
            return user.get().getUserId();
        }else{
            throw new NotFoundException("User not found");
        }
    }

    public User getUser() throws NotFoundException {
        Optional<User> user = userRepository.findByEmail(getUserEmail());

        if(user.isPresent()){
            return user.get();
        }else{
            throw new NotFoundException("User not found");
        }
    }

}
