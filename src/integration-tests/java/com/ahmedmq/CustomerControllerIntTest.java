package com.ahmedmq;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.ahmedmq.account.Account;
import com.ahmedmq.account.AccountRepository;
import com.ahmedmq.account.AccountType;
import com.ahmedmq.customer.Customer;
import com.ahmedmq.customer.CustomerRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.graphql.test.tester.WebGraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureHttpGraphQlTester
public class CustomerControllerIntTest {

	@Autowired
	private WebGraphQlTester graphQlTester;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	AccountRepository accountRepository;

	@Test
	void given_graphQL_endpoint_shouldReturnAllCustomers() {

		String document = """
							query {
							  customers {
							    id
							    firstName
							    lastName
							    }
							}
				""";

		customerRepository.save(new Customer("Mark", "Wood"));

		graphQlTester.document(document)
				.execute()
				.path("customers")
				.entityList(Customer.class)
				.satisfies(customer -> {
					assertThat(customer.get(0).getId()).isEqualTo(1);
					assertThat(customer.get(0).getFirstName()).isEqualTo("Mark");
					assertThat(customer.get(0).getLastName()).isEqualTo("Wood");
				});

	}

	@Test
	void given_graphql_endpoint_create_customer_should_return_new_customer() {
		String document = """
    			mutation newCustomer($input: CreateCustomerInput){
    				createCustomer(input: $input){
    					id
    				}
    			}
				""";

		Map<String,Object> input = new HashMap<>();
		input.put("firstName", "Ethan");
		input.put("lastName", "Hunt");

		graphQlTester.document(document)
				.variable("input", input)
				.execute()
				.path("createCustomer")
				.entity(Customer.class)
				.satisfies(customer -> {
					System.out.println(customer);
					assertThat(customer.getId()).isNotNull();
					assertThat(customer.getFirstName()).isNull();
					assertThat(customer.getLastName()).isNull();

				});

	}

	@Test
	void given_graphql_endpoint_link_account_should_link_account_to_customer() {
		String document = """
     			mutation addAccount($customerId: Int, $accountId: Int){
     				linkAccount(customerId: $customerId, accountId: $accountId){
     					id
     					firstName
     					lastName
     					accounts{
     					  id
     					  type
     					  balance
     					}
     				}
     			}
				""";

		Customer savedCustomer = customerRepository.save(new Customer("Mark", "Wood"));
		Account savedAccount = accountRepository.save(new Account(AccountType.SAVINGS, BigDecimal.valueOf(0.0)));

		graphQlTester.document(document)
				.variable("customerId", savedCustomer.getId())
				.variable("accountId", savedAccount.getId())
				.execute()
				.path("linkAccount")
				.entity(Customer.class)
				.satisfies(c -> {
					assertThat(c.getId()).isEqualTo(savedCustomer.getId());
					assertThat(c.getFirstName()).isEqualTo("Mark");
					assertThat(c.getLastName()).isEqualTo("Wood");
					assertThat(c.getAccounts().size()).isEqualTo(1);
					Account a = c.getAccounts().stream().findFirst().orElseGet(Account::new);
					assertThat(a.getId()).isEqualTo(savedAccount.getId());
					assertThat(a.getType()).isEqualTo(AccountType.SAVINGS);
					assertThat(a.getBalance()).isEqualTo(BigDecimal.valueOf(0.0));
				});
	}
}
