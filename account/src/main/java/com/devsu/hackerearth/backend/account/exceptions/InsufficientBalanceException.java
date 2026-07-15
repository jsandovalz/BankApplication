package com.devsu.hackerearth.backend.account.exceptions;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message) {
        super(message);
    }
    
}
