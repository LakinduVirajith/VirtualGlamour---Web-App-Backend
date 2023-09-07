package com.web.app.virtual.glamour.storage;

import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.entity.Product;
import com.web.app.virtual.glamour.entity.ProductStock;
import com.web.app.virtual.glamour.exception.InternalServerException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.repository.ProductOptionsRepository;
import com.web.app.virtual.glamour.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FirebaseStorageServiceImpl implements FirebaseStorageService{

    private final FirebaseApp firebaseApp;
    private final ProductRepository productRepository;
    private final ProductOptionsRepository optionsRepository;

    @Value("${firebase.storage.folders.path}")
    private String folderName;

    @Value("${firebase.storage.bucket}")
    private String storageBucket;

    @Override
    public ResponseEntity<ResponseMessage> uploadImage(Long productId, String productName , MultipartFile file) throws IOException, NotFoundException, InternalServerException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new NotFoundException("Product with ID " + productId + " not found");
        }

        Product product = productOptional.get();
        if(product.getProductImages() != null){
            this.deleteImage(product);
        }

        String pathName = folderName + "/" + productId + "/" + productName;

        StorageClient storageClient = StorageClient.getInstance(firebaseApp);
        storageClient.bucket().create(pathName, file.getInputStream());
        String imageKey = storageClient.bucket().get(pathName).getMediaLink();

        if(imageKey != null){
            product.setProductImages(pathName);
            productRepository.save(product);
        }else {
            throw new InternalServerException("Oops! Something went wrong. Please try again later.");
        }

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("Product image has been uploaded successfully");
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public ResponseEntity<ResponseMessage> uploadOptionImage(Long optionalId, String optionName, MultipartFile file) throws IOException, NotFoundException, InternalServerException {
        Optional<ProductStock> stockOptional = optionsRepository.findById(optionalId);
        if(stockOptional.isEmpty()){
            throw new NotFoundException("Product optional with optional ID " + optionalId + " not found");
        }

        ProductStock productStock = stockOptional.get();
        if(productStock.getSizeImage() != null){
            this.deleteOptionalImage(productStock);
        }

        String pathName = folderName + "/" + productStock.getProduct().getProductId() + "/" + optionalId + "/" + optionName;

        StorageClient storageClient = StorageClient.getInstance(firebaseApp);
        storageClient.bucket().create(pathName, file.getInputStream());
        String imageKey = storageClient.bucket().get(pathName).getMediaLink();

        if(imageKey != null){
            productStock.setSizeImage(pathName);
            optionsRepository.save(productStock);
        }else {
            throw new InternalServerException("Oops! Something went wrong. Please try again later.");
        }

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("Product optional image has been uploaded successfully");
        return ResponseEntity.ok().body(successResponse);
    }

    public void deleteImage(Product product) throws InternalServerException {
        Bucket bucket = StorageClient.getInstance().bucket(storageBucket);
        boolean result =  bucket.get(product.getProductImages()).delete();

        if(result){
            product.setProductImages(null);
            productRepository.save(product);
        }else {
            throw new InternalServerException("Oops! Something went wrong. Please try again later.");
        }
    }

    public void deleteOptionalImage(ProductStock productStock) throws InternalServerException {
        Bucket bucket = StorageClient.getInstance().bucket(storageBucket);
        boolean result =  bucket.get(productStock.getSizeImage()).delete();

        if(result){
            productStock.setSizeImage(null);
            optionsRepository.save(productStock);
        }else {
            throw new InternalServerException("Oops! Something went wrong. Please try again later.");
        }
    }
}
