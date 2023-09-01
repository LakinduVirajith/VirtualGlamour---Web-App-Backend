package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.entity.ActivationToken;
import com.web.app.virtual.glamour.entity.ForgotOTP;
import com.web.app.virtual.glamour.entity.User;

public interface EmailService {

    Boolean sendActivationMail(User user, ActivationToken activationToken);

    Boolean sendOTPMail(User user);
}
