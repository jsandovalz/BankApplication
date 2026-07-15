package com.devsu.hackerearth.backend.account.mapper;

import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;

@Component
public class AccountMapper {

    public AccountDto toDto(Account  account) {
        return new AccountDto(account.getId(), account.getNumber(), account.getType(), 
            account.getInitialAmount(), account.isActive(), account.getClientId());

    }

    public Account toEntity(AccountDto accountDto) {
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setNumber(accountDto.getNumber());
        account.setType(accountDto.getType());
        account.setInitialAmount(accountDto.getInitialAmount());
        account.setActive(accountDto.isActive());
        account.setClientId(accountDto.getClientId());

        return account;
    }
}
