package com.example.bankingapp.dto;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class TransactionDto {
        private String transactionType;
        private BigDecimal amount;
        private String accountNumber;
        private String status;
}
