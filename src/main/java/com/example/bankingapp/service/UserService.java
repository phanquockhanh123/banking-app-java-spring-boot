package com.example.bankingapp.service;

import com.example.bankingapp.dto.BankResponse;
import com.example.bankingapp.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
}
