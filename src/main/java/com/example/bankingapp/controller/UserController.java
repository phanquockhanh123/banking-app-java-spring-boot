package com.example.bankingapp.controller;

import com.example.bankingapp.dto.*;
import com.example.bankingapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")

@Tag(name="User Account Management APIs")
public class UserController {
    @Autowired
    UserService userService;
    @Operation(
            summary = "Create account",
            description = "New acount is created"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status CREATED"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest request) {
        return  userService.createAccount(request);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "balance enquiry"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
        return  userService.balanceEnquiry(request);
    }
    @Operation(
            summary = "Name Enquiry",
            description = "Name enquiry"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request) {
        return  userService.nameEnquiry(request);
    }
    @Operation(
            summary = "Credit",
            description = "credit account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @PostMapping("/credit")
    public BankResponse credit(@RequestBody CreditDebitRequest request) {
        return  userService.creditAccount(request);
    }
    @Operation(
            summary = "Debit",
            description = "Debit"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @PostMapping("/debit")
    public BankResponse debit(@RequestBody CreditDebitRequest request) {
        return  userService.debitAccount(request);
    }
    @Operation(
            summary = "Transfer",
            description = "Transfer"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request) {
        return  userService.transfer(request);
    }
}
