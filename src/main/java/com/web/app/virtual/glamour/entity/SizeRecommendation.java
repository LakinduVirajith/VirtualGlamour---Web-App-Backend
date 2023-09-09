package com.web.app.virtual.glamour.entity;

import com.web.app.virtual.glamour.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sizeId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @NotNull
    private String shirtSize;

    @NotNull
    private String trouserSize;

    private Float shoulderWidth;

    private Float bustSize;

    private Float waistSize;

    private Float hipSize;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sr_user_id")
    private User user;
}
