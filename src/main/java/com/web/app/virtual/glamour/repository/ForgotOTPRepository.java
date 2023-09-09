package com.web.app.virtual.glamour.repository;

import com.web.app.virtual.glamour.entity.ForgotOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotOTPRepository extends JpaRepository<ForgotOTP, Long> {
}
