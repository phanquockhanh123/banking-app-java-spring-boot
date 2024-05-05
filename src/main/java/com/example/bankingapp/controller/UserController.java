package com.example.bankingapp.controller;

import com.example.bankingapp.dto.BankResponse;
import com.example.bankingapp.dto.UserRequest;
import com.example.bankingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest request) {
        return  userService.createAccount(request);
    }
}
