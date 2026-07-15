package com.devsu.hackerearth.backend.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devsu.hackerearth.backend.account.controller.AccountController;
import com.devsu.hackerearth.backend.account.exceptions.InsufficientBalanceException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.service.AccountService;
import com.devsu.hackerearth.backend.account.service.TransactionService;

@SpringBootTest
public class sampleTest {

	private AccountService accountService = mock(AccountService.class);
	private AccountController accountController = new AccountController(accountService);

	@Autowired
	private AccountService realAccountService;

	@Autowired
	private TransactionService realTransactionService;

	@Test
	void createAccountTest() {
		// Arrange
		AccountDto newAccount = new AccountDto(1L, "number", "savings", 0.0, true, 1L);
		AccountDto createdAccount = new AccountDto(1L, "number", "savings", 0.0, true, 1L);
		when(accountService.create(newAccount)).thenReturn(createdAccount);

		// Act
		ResponseEntity<AccountDto> response = accountController.create(newAccount);

		// Assert
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(createdAccount, response.getBody());
	}

	// F5: entidad de dominio Account
	@Test
	void accountDomainTest(){
		Account account = new Account();
		account.setNumber("001-001");
		account.setType("credit");
		account.setInitialAmount(100.0);
		account.setActive(true);
		account.setClientId(1L);

		assertEquals("001-001",account.getNumber());
		assertEquals(100.0,account.getInitialAmount());
		assertTrue(account.isActive());

	}

	// F6: Prueba de integacion
	// F3: saldo no disponible
	@Test
	void  insufficientBalanceIntegrationTest() {

		AccountDto newAccount = new AccountDto(null, "002-002","credit" , 100.0, true, 1L);
		AccountDto created = realAccountService.create(newAccount);

		TransactionDto debit = new TransactionDto(null, null, "debit", -500.0, 0, created.getId());

		assertThrows(InsufficientBalanceException.class, ()-> realTransactionService.create(debit));

	}
}

