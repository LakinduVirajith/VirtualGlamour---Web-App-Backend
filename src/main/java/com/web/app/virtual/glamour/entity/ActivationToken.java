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
public class ActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Column(length = 32)
    @NotNull
    private String activationToken;

    @NotNull
    private LocalDateTime activationTokenExpiry;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "at_user_id")
    private User user;
}
