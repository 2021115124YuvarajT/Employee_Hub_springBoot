package com.luv2code.springboot.thymeleafdemo.service;

public interface EmailService {
    void sendNewPasswordEmail(String to, String newPassword);
    void sendPasswordResetEmail(String to, String resetUrl);
}
