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
							      items {
							       	id 
							       	accountId
								   	customerId
								  	type
								  	amount
								  	balance
								  	description
								  	transactionDateTime
							      }
							      totalItems
							  }
							}      
				""";

		Transaction testTransaction = new Transaction(1L, 1L, 1L, TransactionType.DEPOSIT, BigDecimal.TEN, BigDecimal.TEN, "Deposit", LocalDateTime.now());
		when(transactionService.transactions(any()))
				.thenReturn(new TransactionList(List.of(testTransaction), 1));

		Map<String, Object> transactionSearchInput = new HashMap<>();
		transactionSearchInput.put("accountId", 1);
		transactionSearchInput.put("customerId", null);


		graphQlTester.document(document)
				.variable("input", transactionSearchInput)
				.execute()
				.path("transactions")
				.entity(TransactionList.class)
				.satisfies(System.out::println)
				.satisfies(t -> {
					assertThat(t.getTotalItems()).isEqualTo(1);
					assertThat(t.getItems().get(0)).usingRecursiveComparison().isEqualTo(testTransaction);
				});

	}
}
