package com.ahmedmq.account;


import java.math.BigDecimal;
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

import static com.ahmedmq.account.AccountType.SAVINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@GraphQlTest(AccountController.class)
@Import(GraphQLConfig.class)
public class AccountControllerTest {

	@Autowired
	GraphQlTester graphQlTester;

	@MockBean
	AccountService accountService;

	Account TEST_ACCOUNT = new Account(SAVINGS, BigDecimal.valueOf(0.0));

	@Test
	void should_Return_All_Accounts() {

		String document = """
				query {
				   accounts {
				     accountId
				     type
				     balance
				   }
				}
				""";

		TEST_ACCOUNT.setAccountId(1);
		when(accountService.accounts()).thenReturn(List.of(TEST_ACCOUNT));

		graphQlTester.document(document)
				.execute()
				.path("accounts")
				.entityList(Account.class)
				.hasSize(1);

	}

	@Test
	void given_GraphQL_endpoint_should_create_account() {

		String document = """
    			mutation addAccount($input: CreateAccountInput){
    				createAccount(input: $input){
    					accountId
    					type
    					balance
    				}
    			}
				""";

		Map<String,Object> input = new HashMap<>();
		input.put("type", TEST_ACCOUNT.getType());

		TEST_ACCOUNT.setAccountId(1);
		when(accountService.createAccount(any(CreateAccountInput.class))).thenReturn(TEST_ACCOUNT);

		graphQlTester.document(document)
				.variable("input", input)
				.execute()
				.path("createAccount")
				.entity(Account.class)
				.satisfies(account -> {
					assertThat(account.getAccountId()).isNotNull();
					assertThat(account.getBalance()).isEqualTo(TEST_ACCOUNT.getBalance());
					assertThat(account.getType()).isEqualTo(TEST_ACCOUNT.getType());
				});
	}
}
