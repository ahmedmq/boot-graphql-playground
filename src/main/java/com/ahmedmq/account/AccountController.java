package com.ahmedmq.account;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@QueryMapping
	public List<Account> accounts(){
		return accountService.accounts();
	}

	@MutationMapping
	public Account createAccount(@Argument CreateAccountInput input){
		return accountService.createAccount(input);
	}


}
