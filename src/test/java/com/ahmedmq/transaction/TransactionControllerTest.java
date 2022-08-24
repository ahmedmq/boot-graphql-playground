package com.ahmedmq.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ahmedmq.config.GraphQLConfig;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@GraphQlTest(TransactionController.class)
@Import(GraphQLConfig.class)
public class TransactionControllerTest {

	@Autowired
	GraphQlTester graphQlTester;

	@MockBean
	TransactionService transactionService;

	@Test
	void shouldReturnAllTransactionsForExistentAccountId() {

		String document = """
							query getTransactions($input: TransactionSearchInput){
							   transactions(input: $input) {
							      transactionId
							      accountId
							      customerId
							      type
							      amount
							      balance
							      description
							      transactionDateTime
							  }
							}      
				""";

		Transaction testTransaction = new Transaction(1, 1, 1, "DEPOSIT", BigDecimal.TEN, BigDecimal.TEN, "Deposit", LocalDateTime.now());
		when(transactionService.transactions(any()))
				.thenReturn(List.of(testTransaction));

		Map<String, Object> transactionSearchInput = new HashMap<>();
		transactionSearchInput.put("accountId", 1);
		transactionSearchInput.put("customerId", null);


		graphQlTester.document(document)
				.variable("input", transactionSearchInput)
				.execute()
				.path("transactions[0]")
				.entity(Transaction.class)
				.satisfies(transaction -> {
					assertThat(transaction).usingRecursiveComparison().isEqualTo(testTransaction);
				});

	}
}
