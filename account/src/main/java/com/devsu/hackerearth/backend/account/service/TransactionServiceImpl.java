package com.devsu.hackerearth.backend.account.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.exceptions.AccountInactiveException;
import com.devsu.hackerearth.backend.account.exceptions.InsufficientBalanceException;
import com.devsu.hackerearth.backend.account.exceptions.ResourceNotFoundException;
import com.devsu.hackerearth.backend.account.mapper.TransactionMapper;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.ClientCache;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.ClientCacheRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ClientCacheRepository clientCacheRepository;
    private final TransactionMapper transactionMapper;

	public TransactionServiceImpl(TransactionRepository transactionRepository, 
                                    AccountRepository accountRepository, 
                                    ClientCacheRepository clientCacheRepository, 
                                    TransactionMapper transactionMapper) {
		this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.clientCacheRepository = clientCacheRepository;
        this.transactionMapper = transactionMapper;
	}

    @Override
    public List<TransactionDto> getAll() {
        // Get all transactions
		return transactionRepository.findAll().stream()
            .map(transactionMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getById(Long id) {
        // Get transactions by id
		return transactionMapper.toDto(transactionRepository.findById(id).
            orElseThrow(()-> new ResourceNotFoundException("Transaccion no encontrada con id: "+id)));
    }

    
    @Override
    @Transactional
    public TransactionDto create(TransactionDto transactionDto) {
        // Create transaction
        Account account = accountRepository.findById(transactionDto.getAccountId())
            .orElseThrow(()-> new ResourceNotFoundException("Cuenta no encontrada con id: "+transactionDto.getAccountId()));

        if(!account.isActive()) {
            throw new AccountInactiveException("La cuenta no esta activa");
        }
        
        double currentBalance = getCurrentBalance(account);
        double newBalance = currentBalance + transactionDto.getAmount();

        //F3: si el movimiento deja el saldo negativo, no hay saldo disponible
        if(newBalance <0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }

        Transaction transaction = new Transaction();
        transaction.setDate(transactionDto.getDate() != null? transactionDto.getDate() : new Date());
        transaction.setType(transactionDto.getType());
        transaction.setAmount(transactionDto.getAmount());

        //F2: se regostra el saldo actualizado
        transaction.setBalance(newBalance);
        transaction.setAccountId(account.getId());

		return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Override
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(Long clientId, Date dateTransactionStart,
            Date dateTransactionEnd) {
        // Report
        List<Transaction> transactions = transactionRepository
            .findAllByAccountClientIdAndDateBetween(clientId, dateTransactionStart, dateTransactionEnd);

        String clientName = clientCacheRepository.findById(clientId)
                    .map(ClientCache::getName)
                    .orElse("Desconocido");

        return transactions.stream()
            .map(t-> {
                Account account = accountRepository.findById(t.getAccountId())
                    .orElseThrow(()-> new ResourceNotFoundException("Cuenta no encontrada con id: "+t.getAccountId()));
                return new BankStatementDto(t.getDate(), clientName, account.getNumber(), account.getType(),
                account.getInitialAmount(), account.isActive(), t.getType(), t.getAmount(),t.getBalance());
            }).collect(Collectors.toList());
    }

    @Override
    public TransactionDto getLastByAccountId(Long accountId) {
        // If you need it
        Transaction last = transactionRepository.findTopByAccountIdOrderByDateDescIdDesc(accountId);
		return last !=null ? transactionMapper.toDto(last) : null;
    }

    private double getCurrentBalance(Account account) {
        Transaction last = transactionRepository.findTopByAccountIdOrderByDateDescIdDesc(account.getId());
        return last != null ? last.getBalance() : account.getInitialAmount();
    }
    
}
