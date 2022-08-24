package com.ahmedmq;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.ahmedmq.account.Account;
import com.ahmedmq.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureHttpGraphQlTester
public class AccountControllerIntTest {

	@Autowired
	HttpGraphQlTester httpGraphQlTester;

	@Autowired
	AccountRepository accountRepository;

	@BeforeEach
	public void setup(){
		accountRepository.deleteAll();
	}

	@Test
	void given_graphQL_endpoint_accounts_api_should_return_all_accounts() {
		String document = """
				query {
				   accounts {
				     accountId
				     type
				     balance
				   }
				}
				""";

		accountRepository.save(new Account(null, "SAVINGS", BigDecimal.valueOf(0.0)));

		httpGraphQlTester.document(document)
				.execute()
				.path("accounts[0]")
				.entity(Account.class)
				.satisfies(account -> {
					assertThat(account.getAccountId()).isEqualTo(1);
					assertThat(account.getType()).isEqualTo("SAVINGS");
					assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(0.0));
				});
	}

	@Test
	void given_graphql_endpoint_create_account_should_return_new_account() {

		String document = """
    			mutation addAccount($createAccountInput: CreateAccountInput){
    				createAccount(input: $createAccountInput){
    					accountId
    					type
    					balance
    				}	
    			}
				""";

		Map<String, Object> input = new HashMap<>();
		input.put("type", "SAVINGS");

		httpGraphQlTester.document(document)
				.variable("createAccountInput", input)
				.execute()
				.path("createAccount")
				.entity(Account.class)
				.satisfies(account -> {
					assertThat(account.getAccountId()).isNotNull();
					assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(0.0));
					assertThat(account.getType()).isEqualTo("SAVINGS");
				});

	}
}
