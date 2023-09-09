package com.web.app.virtual.glamour.dto;

import com.web.app.virtual.glamour.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeRecommendationDTO {

    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    private String shirtSize;

    private String trouserSize;

    private Float shoulderWidth;

    private Float bustSize;

    private Float waistSize;

    private Float hipSize;
}
