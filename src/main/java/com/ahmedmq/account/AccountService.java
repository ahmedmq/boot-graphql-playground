package com.ahmedmq.account;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

	private final AccountRepository accountRepository;

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public List<Account> accounts() {
		return accountRepository.findAll();
	}

	public Account createAccount(CreateAccountInput input) {
		return accountRepository.save(new Account(null, input.getType(), BigDecimal.valueOf(0.0)));
	}
}
