package com.web.app.virtual.glamour.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotOTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otpId;

    @Column(length = 4)
    @NotNull
    private String otpNumber;

    @NotNull
    private LocalDateTime otpExpiry;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "otp_user_id")
    private User user;
}
