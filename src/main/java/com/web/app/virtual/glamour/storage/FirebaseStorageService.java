package com.web.app.virtual.glamour.storage;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.exception.InternalServerException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FirebaseStorageService {

    ResponseEntity<ResponseMessage> uploadImage(Long productId, String productName , MultipartFile file) throws IOException, NotFoundException, InternalServerException;

    ResponseEntity<ResponseMessage> uploadOptionImage(Long optionalId, String optionName, MultipartFile file) throws IOException, NotFoundException, InternalServerException;
}
