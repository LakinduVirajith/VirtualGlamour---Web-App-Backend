package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.common.CommonFunctions;
import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.SizeRecommendationDTO;
import com.web.app.virtual.glamour.entity.SizeRecommendation;
import com.web.app.virtual.glamour.entity.User;
import com.web.app.virtual.glamour.enums.Gender;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.repository.SizeRecommendationRepository;
import com.web.app.virtual.glamour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SizeRecommendationServiceImpl implements SizeRecommendationService{

    private final SizeRecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final CommonFunctions commonFunctions;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<ResponseMessage> addDirectSize(SizeRecommendationDTO recommendationDTO) throws NotFoundException, BadRequestException {
        User user = commonFunctions.getUser();

        SizeRecommendation recommendation;
        ResponseMessage successResponse = new ResponseMessage();
        // ADD OR UPDATE SIZE DETAILS
        if(user.getSizeRecommendation() == null){
            recommendation = SizeRecommendation.builder()
                    .gender(recommendationDTO.getGender())
                    .shirtSize(recommendationDTO.getShirtSize())
                    .trouserSize(recommendationDTO.getTrouserSize())
                    .user(user)
                    .build();

            SizeRecommendation formattedRecommendation = this.setSizesAccordingToFormat(recommendation);
            user.setSizeRecommendation(formattedRecommendation);
            userRepository.save(user);
            successResponse.setMessage("User size has been added successfully");
        }else{
            recommendation = user.getSizeRecommendation();

            recommendation.setGender(recommendationDTO.getGender());
            recommendation.setShirtSize(recommendationDTO.getShirtSize());
            recommendation.setTrouserSize(recommendationDTO.getTrouserSize());
            recommendation.setShoulderWidth(null);
            recommendation.setBustSize(null);
            recommendation.setWaistSize(null);
            recommendation.setHipSize(null);

            SizeRecommendation formattedRecommendation = this.setSizesAccordingToFormat(recommendation);
            recommendationRepository.save(formattedRecommendation);
            successResponse.setMessage("User size has been updated successfully");
        }

        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseMessage> addMeasurements(SizeRecommendationDTO recommendationDTO) throws NotFoundException, BadRequestException {
        User user = commonFunctions.getUser();

        // EXCEPTION HANDLE
        if(recommendationDTO.getGender() == Gender.Male){
            if(recommendationDTO.getShoulderWidth() == null || recommendationDTO.getWaistSize() == null ){
                throw new BadRequestException("Invalid details provided. Both ShoulderWidth and WaistSize are required.");
            }
        }else if(recommendationDTO.getGender() == Gender.Female){
            if(recommendationDTO.getBustSize() == null || recommendationDTO.getWaistSize() == null || recommendationDTO.getHipSize() == null){
                throw new BadRequestException("Invalid details provided. BustSize, WaistSize, and HipSize are all required.");
            }
        }

        ResponseMessage successResponse = new ResponseMessage();
        SizeRecommendation recommendation;
        // ADD OR UPDATE SIZE DETAILS
        if(user.getSizeRecommendation() == null){
            SizeRecommendation sizeRecommendation = new SizeRecommendation();
            modelMapper.map(recommendationDTO, sizeRecommendation);

            recommendation  = this.getRecommendation(sizeRecommendation);
            user.setSizeRecommendation(recommendation);
            userRepository.save(user);
            successResponse.setMessage("User size has been added successfully");
        }else{
            SizeRecommendation sizeRecommendation = user.getSizeRecommendation();
            modelMapper.map(recommendationDTO, sizeRecommendation);

            recommendation  = this.getRecommendation(sizeRecommendation);
            recommendationRepository.save(recommendation);
            successResponse.setMessage("User size has been updated successfully");
        }

        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public ResponseEntity<ResponseMessage> addImage(Gender gender, MultipartFile file) throws NotFoundException {
        User user = commonFunctions.getUser();

        ResponseMessage successResponse = new ResponseMessage();
//        SizeRecommendation recommendation;

        // TODO: Retrieve API data and process it

        // ADD OR UPDATE SIZE DETAILS
        if(user.getSizeRecommendation() == null){
//            SizeRecommendation sizeRecommendation = new SizeRecommendation();
//
//            sizeRecommendation.setGender(gender);
//            sizeRecommendation.setChestSize();
//            sizeRecommendation.setBustSize();
//            sizeRecommendation.setWaistSize();
//            sizeRecommendation.setHipSize();
//
//            recommendation  = this.getRecommendation(sizeRecommendation);
            successResponse.setMessage("User size has been added successfully");
        }else{
//            SizeRecommendation sizeRecommendation = user.getSizeRecommendation();
//
//            sizeRecommendation.setGender(gender);
//            sizeRecommendation.setChestSize();
//            sizeRecommendation.setBustSize();
//            sizeRecommendation.setWaistSize();
//            sizeRecommendation.setHipSize();
//
//            recommendation  = this.getRecommendation(sizeRecommendation);
            successResponse.setMessage("User size has been updated successfully");
        }
//        recommendationRepository.save(recommendation);

        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("This feature is not yet implemented.");
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public SizeRecommendationDTO getSizeDetails() throws NotFoundException {
        User user = commonFunctions.getUser();

        SizeRecommendationDTO recommendationDTO = new SizeRecommendationDTO();
        // CHECK IF SIZE HAVE OR NOT
        if(user.getSizeRecommendation() != null){
            SizeRecommendation recommendation = user.getSizeRecommendation();
            modelMapper.map(recommendation, recommendationDTO);
        }else{
            throw new NotFoundException("No user sizes found");
        }

        return recommendationDTO;
    }

    private SizeRecommendation setSizesAccordingToFormat(SizeRecommendation recommendation) throws BadRequestException {
        if(recommendation.getGender().equals(Gender.Male)){
            //MALE SHIRT SIZE FORMATTER
            String shirtSize = recommendation.getShirtSize();

            shirtSize = switch (shirtSize) {
                case "4XL", "4xl", "17.5" -> "4XL (17.5)";
                case "3XL", "3xl", "17" -> "3XL (17)";
                case "2XL", "2xl", "16.5" -> "2XL (16.5)";
                case "XL", "xl", "16" -> "XL (16)";
                case "L", "l", "15.5" -> "L (15.5)";
                case "M", "m", "15" -> "M (15)";
                case "S", "s", "14.5" -> "S (14.5)";
                case "XS", "xs", "14" -> "XS (14)";
                default -> shirtSize;
            };
            if(recommendation.getShirtSize().equals(shirtSize)){
                throw new BadRequestException("Invalid shirt size provided. Please provide a valid shirt size");
            }
            recommendation.setShirtSize(shirtSize);

            //MALE TROUSER SIZE FORMATTER
            String trouserSize = recommendation.getTrouserSize();
            trouserSize = switch (trouserSize) {
                case "4XL", "4xl", "42" -> "4XL (42)";
                case "3XL", "3xl", "40" -> "3XL (40)";
                case "2XL", "2xl", "38" -> "2XL (38)";
                case "XL", "xl", "36" -> "XL (36)";
                case "L", "l", "34" -> "L (34)";
                case "M", "m", "32" -> "M (32)";
                case "S", "s", "30" -> "S (30)";
                case "XS", "xs", "28" -> "XS (28)";
                default -> trouserSize;
            };
            if(recommendation.getTrouserSize().equals(trouserSize)){
                throw new BadRequestException("Invalid trouser size provided. Please provide a valid trouser size");
            }
            recommendation.setTrouserSize(trouserSize);

        }else if(recommendation.getGender().equals(Gender.Female)){
            //FEMALE SHIRT SIZE FORMATTER
            String shirtSize = recommendation.getShirtSize();

            shirtSize = switch (shirtSize) {
                case "XL", "xl", "16" -> "XL (16)";
                case "L", "l", "14", "15" -> "L (14-16)";
                case "M", "m", "10", "11", "12" -> "M (10-12)";
                case "S", "s", "6", "7", "8" -> "S (6-8)";
                case "XS", "xs", "2", "3", "4" -> "L (2-4)";
                default -> shirtSize;
            };
            if(recommendation.getShirtSize().equals(shirtSize)){
                throw new BadRequestException("Invalid shirt size provided. Please provide a valid shirt size");
            }
            recommendation.setShirtSize(shirtSize);

            //FEMALE TROUSER SIZE FORMATTER
            String trouserSize = recommendation.getTrouserSize();
            trouserSize = switch (trouserSize) {
                case "4XL", "4xl", "44" -> "4XL (44)";
                case "3XL", "3xl", "42" -> "3XL (42)";
                case "2XL", "2xl", "40" -> "2XL (40)";
                case "XL", "xl", "38" -> "XL (38)";
                case "L", "l", "36" -> "L (36)";
                case "M", "m", "34" -> "M (34)";
                case "S", "s", "32" -> "S (32)";
                case "XS", "xs", "30" -> "XS (30)";
                default -> trouserSize;
            };
            if(recommendation.getTrouserSize().equals(trouserSize)){
                throw new BadRequestException("Invalid trouser size provided. Please provide a valid trouser size");
            }
            recommendation.setTrouserSize(trouserSize);
        }

        return recommendation;
    }

    private SizeRecommendation getRecommendation(SizeRecommendation sizeRecommendation) {
        // SHIRT SIZE BASED ON GENDER
        String shirtSize = null;
        if(sizeRecommendation.getGender() == Gender.Male){
            shirtSize = this.getShirtMaleSize(sizeRecommendation.getShoulderWidth());
        }else if(sizeRecommendation.getGender() == Gender.Female){
            shirtSize = this.getShirtFemaleSize(sizeRecommendation.getBustSize(), sizeRecommendation.getWaistSize(), sizeRecommendation.getHipSize());
        }
        sizeRecommendation.setShirtSize(shirtSize);

        // TROUSER SIZE BASED ON GENDER
        String trousersSize = null;
        if(sizeRecommendation.getGender() == Gender.Male){
            trousersSize = this.getTrousersMaleSize(sizeRecommendation.getWaistSize());
        }else if(sizeRecommendation.getGender() == Gender.Female){
            trousersSize = this.getTrousersFemaleSize(sizeRecommendation.getHipSize());
        }
        sizeRecommendation.setTrouserSize(trousersSize);

        // REMOVE UNNECESSARY DATA
        if(sizeRecommendation.getGender() == Gender.Male){
            sizeRecommendation.setBustSize(null);
            sizeRecommendation.setHipSize(null);
        }else if(sizeRecommendation.getGender() == Gender.Female){
            sizeRecommendation.setShoulderWidth(null);
        }
        return sizeRecommendation;
    }

    private String getShirtMaleSize(Float shoulderWidth) {
        if (shoulderWidth >= 43.9) {
            return "4XL (17.5)";
        } else if (shoulderWidth >= 42.6) {
            return "3XL (17)";
        } else if (shoulderWidth >= 41.3) {
            return "2XL (16.5)";
        } else if (shoulderWidth >= 40.0) {
            return "XL (16)";
        } else if (shoulderWidth >= 38.7) {
            return "L (15.5)";
        } else if (shoulderWidth >= 37.4) {
            return "M (15)";
        } else if (shoulderWidth >= 36.0) {
            return "S (14.5)";
        } else {
            return "XS (14)";
        }
    }

    private String getShirtFemaleSize(Float bustSize, Float waistSize, Float hipSize) {
        if (bustSize >= 37.0) {
            if (waistSize >= 29.5) {
                if (hipSize >= 39.5) {
                    return "XL (16)";
                } else {
                    return "L (14-16)";
                }
            } else {
                if (hipSize >= 39.5) {
                    return "M (10-12)";
                } else {
                    return "S (6-8)";
                }
            }
        } else {
            if (waistSize >= 29.5) {
                if (hipSize >= 39.5) {
                    return "M (10-12)";
                } else {
                    return "S (6-8)";
                }
            } else {
                if (hipSize >= 39.5) {
                    return "S (6-8)";
                } else {
                    return "XS (2-4)";
                }
            }
        }
    }

    private String getTrousersMaleSize(Float waistSize) {
        if (waistSize >= 70 && waistSize < 75) {
            return "XS (28)";
        } else if (waistSize >= 75 && waistSize < 80) {
            return "S (30)";
        } else if (waistSize >= 80 && waistSize < 85) {
            return "M (32)";
        } else if (waistSize >= 85 && waistSize < 90) {
            return "L (34)";
        } else if (waistSize >= 90 && waistSize < 95) {
            return "XL (36)";
        } else if (waistSize >= 95 && waistSize < 100) {
            return "2XL (38)";
        } else if (waistSize >= 100 && waistSize < 105) {
            return "3XL (40)";
        } else {
            return "4XL (42)";
        }
    }

    private String getTrousersFemaleSize(Float hipSize) {
        if (hipSize >= 80 && hipSize < 85) {
            return "XS (30)";
        } else if (hipSize >= 85 && hipSize < 90) {
            return "S (32)";
        } else if (hipSize >= 90 && hipSize < 95) {
            return "M (34)";
        } else if (hipSize >= 95 && hipSize < 100) {
            return "L (36)";
        } else if (hipSize >= 100 && hipSize < 105) {
            return "XL (38)";
        } else if (hipSize >= 105 && hipSize < 110) {
            return "2XL (40)";
        } else if (hipSize >= 110 && hipSize < 115) {
            return "3XL (42)";
        } else {
            return "4XL (44)";
        }
    }
}
