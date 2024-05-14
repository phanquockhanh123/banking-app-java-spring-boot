package com.example.bankingapp.service.impl;

import com.example.bankingapp.entity.TransactionEntity;
import com.example.bankingapp.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BankStatementImpl {
    /**
     * retrive list of transactions within a date range given an account number
     * generate a file pdf transactions
     * send the file via mail
     */


}
