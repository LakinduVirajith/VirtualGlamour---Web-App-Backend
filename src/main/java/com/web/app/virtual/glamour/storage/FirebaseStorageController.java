package com.web.app.virtual.glamour.storage;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.exception.InternalServerException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/storage")
@Tag(name = "Firebase Storage Controller")
public class FirebaseStorageController {

    private final FirebaseStorageService storageService;

    @Operation(summary = "Upload Product Image", description = "Upload a Product image for a main product. Attach the image file using the 'file' parameter.")
    @PostMapping("product/upload/{id}")
    public ResponseEntity<ResponseMessage> uploadImage(@PathVariable("id") Long productId, @RequestParam String productName, @RequestParam("file") MultipartFile file) throws IOException, NotFoundException, InternalServerException {
        return storageService.uploadImage(productId, productName, file);
    }

    @Operation(summary = "Upload Product Option Image", description = "Upload a Product option image. Attach the image file using the 'file' parameter.")
    @PostMapping("product/option/upload/{id}")
    public ResponseEntity<ResponseMessage> uploadOptionImage(@PathVariable("id") Long optionalId, @RequestParam String optionName, @RequestParam("file") MultipartFile file) throws IOException, NotFoundException, InternalServerException {
        return storageService.uploadOptionImage(optionalId, optionName, file);
    }
}
