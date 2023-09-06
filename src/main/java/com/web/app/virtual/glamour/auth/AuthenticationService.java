package com.web.app.virtual.glamour.auth;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.ForbiddenException;
import com.web.app.virtual.glamour.exception.InternalServerException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) throws ForbiddenException, NotFoundException, InternalServerException;

    ResponseEntity<AuthenticationResponse> refreshToken(String refreshToken) throws BadRequestException, NotFoundException, InternalServerException;

    ResponseEntity<ResponseMessage> logout() throws BadRequestException, NotFoundException;

    ResponseEntity<ResponseMessage> forgotPassword(String email) throws NotFoundException, InternalServerException;

    ResponseEntity<ResponseMessage> OTPValidation(String email, String otp) throws NotFoundException, BadRequestException;
}
