package com.example.bankingapp.service;

import com.example.bankingapp.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
