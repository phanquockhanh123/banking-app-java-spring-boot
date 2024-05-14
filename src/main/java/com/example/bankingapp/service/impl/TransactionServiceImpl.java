package com.example.bankingapp.service.impl;

import com.example.bankingapp.dto.TransactionDto;
import com.example.bankingapp.entity.TransactionEntity;
import com.example.bankingapp.repository.TransactionRepository;
import com.example.bankingapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();
        transactionRepository.save(transactionEntity);
        System.out.println("Transaction saved success ");
    }
}
