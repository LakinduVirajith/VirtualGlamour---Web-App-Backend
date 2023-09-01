package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.UserDTO;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.ConflictException;
import com.web.app.virtual.glamour.exception.InternalServerException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<ResponseMessage> register(UserDTO userDTO) throws ConflictException, InternalServerException;

    ResponseEntity<ResponseMessage> activate(String token) throws BadRequestException, NotFoundException, ConflictException;

    ResponseEntity<ResponseMessage> deactivate() throws NotFoundException, BadRequestException;

    ResponseEntity<ResponseMessage> restPassword(String email, String password) throws NotFoundException;
}
