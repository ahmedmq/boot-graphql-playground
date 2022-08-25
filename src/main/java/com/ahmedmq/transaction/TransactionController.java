package com.ahmedmq.transaction;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@QueryMapping
	public TransactionList transactions(@Argument TransactionSearchInput input) {
		return transactionService.transactions(input);
	}
}
