package com.ahmedmq.customer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.ahmedmq.account.Account;
import com.ahmedmq.account.AccountType;
import com.ahmedmq.config.GraphQLConfig;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@GraphQlTest(CustomerController.class)
@Import(GraphQLConfig.class)
public class CustomerControllerTest {

	@Autowired
	GraphQlTester graphQLTester;

	@MockBean
	CustomerService customerService;

	Customer TEST_CUSTOMER = new Customer("Mark", "Wood");

	@Test
	void given_graphql_endpoint_shouldReturnAllCustomers() {

		String document = """
						query{
									customers{
											customerId
											firstName
											lastName
											}
						}
			""";

		TEST_CUSTOMER.setCustomerId(1);
		when(customerService.customers())
				.thenReturn(of(TEST_CUSTOMER));

		graphQLTester.document(document)
				.execute()
				.path("customers")
				.entityList(Customer.class)
				.hasSize(1);

	}


	@Test
	void given_graphQL_endpoint_should_create_customer() {
		String document = """
    			mutation addCustomer($input: CreateCustomerInput){
    			 	createCustomer(input: $input){
    			 		customerId
    			 		firstName
    			 		lastName	
    			 	}
    			} 	
				""";

		Map<String, Object> input = new HashMap<>();
		input.put("firstName", TEST_CUSTOMER.getFirstName());
		input.put("lastName", TEST_CUSTOMER.getLastName());

		TEST_CUSTOMER.setCustomerId(1);
		when(customerService.createCustomer(any()))
				.thenReturn(TEST_CUSTOMER);

		graphQLTester.document(document)
				.variable("input", input)
				.execute()
				.path("createCustomer")
				.entity(Customer.class)
				.satisfies(customer -> {
					assertThat(customer.getCustomerId()).isNotNull();
					assertThat(customer.getFirstName()).isEqualTo(TEST_CUSTOMER.getFirstName());
					assertThat(customer.getLastName()).isEqualTo(TEST_CUSTOMER.getLastName());
				});

	}

	@Test
	void given_graphql_endpoint_should_add_account_to_customer() {
		String document = """
     			mutation addAccount($customerId: Int, $accountId: Int){
     				linkAccount(customerId: $customerId, accountId: $accountId){
     					customerId
     					firstName
     					lastName
     					accounts{
     					  accountId
     					  type
     					  balance
     					}
     				}
     			}
				""";

		Customer customer = new Customer("Mark", "Wood");
		customer.setCustomerId(1);
		Account account = new Account(AccountType.SAVINGS, BigDecimal.valueOf(0.0));
		account.setAccountId(1);
		customer.getAccounts().add(account);

		when(customerService.linkAccount(anyInt(), anyInt())).thenReturn(customer);

		graphQLTester.document(document)
				.variable("customerId", 1)
				.variable("accountId", 1)
				.execute()
				.path("linkAccount")
				.entity(Customer.class)
				.satisfies(c -> {
					assertThat(c.getCustomerId()).isEqualTo(1);
					assertThat(c.getFirstName()).isEqualTo("Mark");
					assertThat(c.getLastName()).isEqualTo("Wood");
					assertThat(c.getAccounts().size()).isEqualTo(1);
					Account a = c.getAccounts().stream().findFirst().orElseGet(Account::new);
					assertThat(a.getAccountId()).isEqualTo(1);
					assertThat(a.getType()).isEqualTo(AccountType.SAVINGS);
					assertThat(a.getBalance()).isEqualTo(BigDecimal.valueOf(0.0));
				});
	}
}
