package com.ahmedmq;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.ahmedmq.account.Account;
import com.ahmedmq.account.AccountRepository;
import com.ahmedmq.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import static com.ahmedmq.account.AccountType.SAVINGS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureHttpGraphQlTester
public class AccountControllerIntTest {

	@Autowired
	HttpGraphQlTester httpGraphQlTester;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	CustomerRepository customerRepository;

	@BeforeEach
	public void setup(){
		customerRepository.deleteAll();
		accountRepository.deleteAll();
	}

	@Test
	void given_graphQL_endpoint_accounts_api_should_return_all_accounts() {
		String document = """
				query {
				   accounts {
				     id
				     type
				     balance
				   }
				}
				""";

		Account account = accountRepository.save(new Account(SAVINGS, BigDecimal.valueOf(0.0)));

		httpGraphQlTester.document(document)
				.execute()
				.path("accounts[0]")
				.entity(Account.class)
				.satisfies(acc -> {
					assertThat(acc.getId()).isNotNull();
					assertThat(acc.getType()).isEqualTo(SAVINGS);
					assertThat(acc.getBalance()).isEqualTo(BigDecimal.valueOf(0.0));
				});
	}

	@Test
	void given_graphql_endpoint_create_account_should_return_new_account() {

		String document = """
    			mutation addAccount($createAccountInput: CreateAccountInput){
    				createAccount(input: $createAccountInput){
    					id
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
					assertThat(account.getId()).isNotNull();
					assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(0.0));
					assertThat(account.getType()).isEqualTo(SAVINGS);
				});

	}
}
