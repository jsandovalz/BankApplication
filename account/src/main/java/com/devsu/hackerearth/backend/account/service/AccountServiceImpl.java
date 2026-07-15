package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.exceptions.DuplicateResourceException;
import com.devsu.hackerearth.backend.account.exceptions.ResourceNotFoundException;
import com.devsu.hackerearth.backend.account.mapper.AccountMapper;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

	public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
		this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
	}

    @Override
    public List<AccountDto> getAll() {
        // Get all accounts
		return accountRepository.findAll().stream()
            .map(accountMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(Long id) {
        // Get accounts by id
		return accountMapper.toDto(findOrThrow(id));
    }

    @Override
    @Transactional
    public AccountDto create(AccountDto accountDto) {
        // Create account
        if(accountRepository.existsByNumber(accountDto.getNumber())) {
            throw new DuplicateResourceException("Ya existe una cuenta con el numero: " + accountDto.getNumber());
        }

        Account account = accountMapper.toEntity(accountDto);
        account.setId(null);

		return accountMapper.toDto(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountDto update(AccountDto accountDto) {
        // Update account
        Account  existing = findOrThrow(accountDto.getId());
        existing.setNumber(accountDto.getNumber());
        existing.setType(accountDto.getType());
        existing.setInitialAmount(accountDto.getInitialAmount());
        existing.setActive(accountDto.isActive());
        existing.setClientId(accountDto.getClientId());

		return accountMapper.toDto(accountRepository.save(existing));
    }

    @Override
    @Transactional
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        // Partial update account
        Account existing = findOrThrow(id);
        existing.setActive(partialAccountDto.isActive());

		return accountMapper.toDto(accountRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Delete account
        findOrThrow(id);
        accountRepository.deleteById(id);
    }

    private Account findOrThrow(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Cuenta no encontrada con id: "+ id));
    }

    
    
}
