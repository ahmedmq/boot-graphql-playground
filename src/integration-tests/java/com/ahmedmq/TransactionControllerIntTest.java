package com.ahmedmq;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.ahmedmq.transaction.Transaction;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureHttpGraphQlTester
public class TransactionControllerIntTest {

	@Autowired
	HttpGraphQlTester httpGraphQlTester;

	@Test
	void should_Return_All_Transactions() {
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

		Map<String, Object> transactionSearchInput = new HashMap<>();
		transactionSearchInput.put("accountId", 1);
		transactionSearchInput.put("customerId", null);

		httpGraphQlTester.document(document)
				.variable("input", transactionSearchInput)
				.execute()
				.path("transactions[0]")
				.entity(Transaction.class)
				.satisfies(transaction -> {
					assertThat(transaction.getTransactionId()).isEqualTo(1);
					assertThat(transaction.getAccountId()).isEqualTo(1);
					assertThat(transaction.getCustomerId()).isEqualTo(1);
					assertThat(transaction.getType()).isEqualTo("DEPOSIT");
					assertThat(transaction.getAmount()).isEqualTo(BigDecimal.valueOf(10.0));
					assertThat(transaction.getBalance()).isEqualTo(BigDecimal.valueOf(10.0));
					assertThat(transaction.getTransactionDateTime()).isNotNull();
				});

	}
}
