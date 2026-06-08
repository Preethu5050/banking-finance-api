package com.preethan.banking_finance_api;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String type; // DEPOSIT or WITHDRAWAL
    private Double amount;
    private Double balanceAfter;
    private LocalDateTime timestamp;
}