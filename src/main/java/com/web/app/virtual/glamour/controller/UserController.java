package com.web.app.virtual.glamour.controller;

import com.web.app.virtual.glamour.auth.AuthenticationRequest;
import com.web.app.virtual.glamour.auth.AuthenticationResponse;
import com.web.app.virtual.glamour.auth.AuthenticationService;
import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.UserDTO;
import com.web.app.virtual.glamour.exception.*;
import com.web.app.virtual.glamour.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User Controllers")
public class UserController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    @Operation(summary = "User Registration", description = "Register a new user. Provide necessary details to create a user account.")
    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@Valid @RequestBody UserDTO userDTO) throws ConflictException, InternalServerException {
        return userService.register(userDTO);
    }

    @Operation(summary = "Activate User Account", description = "Activate a user account using an activation token received via email.")
    @GetMapping("/activate")
    public ResponseEntity<ResponseMessage> userActivate(@RequestParam("token") String token) throws NotFoundException, BadRequestException, ConflictException {
        return userService.activate(token);
    }

    @Operation(summary = "Deactivate Account", description = "Deactivate a user account. you must login to the system first")
    @PutMapping("/deactivate")
    public ResponseEntity<ResponseMessage> userDeactivate() throws NotFoundException, BadRequestException {
        return userService.deactivate();
    }

    @Operation(summary = "User Authentication", description = "Authenticate a user by providing valid credentials.")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) throws ForbiddenException, NotFoundException, InternalServerException {
        return authenticationService.authenticate(request);
    }

    @Operation(summary = "Refresh Access Token", description = "Refresh the access token by providing a valid refresh token. This endpoint allows you to obtain a new access token using a valid refresh token, which helps in maintaining user authentication without requiring the user to log in again.")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(String refreshToken) throws NotFoundException, BadRequestException, InternalServerException {
        return authenticationService.refreshToken(refreshToken);
    }

    @Operation(summary = "Logout", description = "Invalidate the user's authentication token to log out.")
    @PutMapping("/logout")
    public ResponseEntity<ResponseMessage> logout() throws BadRequestException, NotFoundException {
        return authenticationService.logout();
    }

    @Operation(summary = "Forgot Password OTP Email", description = "Send a One-Time Password (OTP) to the user's email for password reset.")
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseMessage> forgotPassword(String email) throws InternalServerException, NotFoundException {
        return authenticationService.forgotPassword(email);
    }

    @Operation(summary = "Validate OTP", description = "Validate the provided OTP (One-Time Password) for reset password verification.")
    @PostMapping("/otp-validation")
    public ResponseEntity<ResponseMessage> OTPValidation(String email, String OTP) throws NotFoundException, BadRequestException {
        return authenticationService.OTPValidation(email, OTP);
    }

    @Operation(summary = "Reset Password with OTP", description = "Initiate password reset by sending an OTP to the user's email. Authenticate with the OTP and set a new password.")
    @PostMapping("/rest-password")
    public ResponseEntity<ResponseMessage> restPassword(String email, String password) throws NotFoundException {
        return userService.restPassword(email, password);
    }
}
