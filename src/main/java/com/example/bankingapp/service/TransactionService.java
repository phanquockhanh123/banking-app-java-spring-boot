package com.example.bankingapp.service;

import com.example.bankingapp.dto.TransactionDto;
import com.example.bankingapp.entity.TransactionEntity;

public interface TransactionService {
    void saveTransaction(TransactionDto transaction);
}
