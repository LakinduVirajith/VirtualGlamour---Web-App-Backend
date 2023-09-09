package com.web.app.virtual.glamour.controller;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.SizeRecommendationDTO;
import com.web.app.virtual.glamour.enums.Gender;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.service.SizeRecommendationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user/size")
@RequiredArgsConstructor
@Tag(name = "Size Recommendation Controllers")
public class SizeRecommendationController {

    private final SizeRecommendationService recommendationService;

    @PostMapping("/add/direct")
    public ResponseEntity<ResponseMessage> addDirectSize(@Valid @RequestBody SizeRecommendationDTO recommendationDTO) throws NotFoundException, BadRequestException {
        return recommendationService.addDirectSize(recommendationDTO);
    }

    @PostMapping("/add/measurements")
    public ResponseEntity<ResponseMessage> addMeasurements(@Valid @RequestBody SizeRecommendationDTO recommendationDTO) throws NotFoundException, BadRequestException {
        return recommendationService.addMeasurements(recommendationDTO);
    }

    @PostMapping("/add/image")
    public ResponseEntity<ResponseMessage> addImage(@RequestParam Gender gender, @RequestParam("file") MultipartFile file) throws NotFoundException {
        return recommendationService.addImage(gender, file);
    }

    @GetMapping("/")
    public SizeRecommendationDTO getSizeDetails() throws NotFoundException {
        return recommendationService.getSizeDetails();
    }
}
