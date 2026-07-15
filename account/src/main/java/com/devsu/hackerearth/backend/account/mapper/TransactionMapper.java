package com.devsu.hackerearth.backend.account.mapper;

import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(transaction.getId(),transaction.getDate(),transaction.getType(),
            transaction.getAmount(),transaction.getBalance(),transaction.getAccountId());

    }
    
}
