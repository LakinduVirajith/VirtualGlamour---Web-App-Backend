package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.SizeRecommendationDTO;
import com.web.app.virtual.glamour.enums.Gender;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SizeRecommendationService {

    ResponseEntity<ResponseMessage> addDirectSize(SizeRecommendationDTO recommendationDTO) throws NotFoundException, BadRequestException;

    ResponseEntity<ResponseMessage> addMeasurements(SizeRecommendationDTO recommendationDTO) throws NotFoundException, BadRequestException;

    ResponseEntity<ResponseMessage> addImage(Gender gender, MultipartFile file) throws NotFoundException;

    SizeRecommendationDTO getSizeDetails() throws NotFoundException;
}
