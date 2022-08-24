package com.ahmedmq;

import java.util.HashMap;
import java.util.Map;

import com.ahmedmq.customer.Customer;
import com.ahmedmq.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
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

	@BeforeEach
	void setup(){
		customerRepository.deleteAll();
	}

	@Test
	void given_graphQL_endpoint_shouldReturnAllCustomers() {

		String document = """
							query {
							  customers {
							    customerId
							    firstName
							    lastName
							    }
							}
				""";

		customerRepository.save(new Customer(null, "Mark", "Wood"));

		graphQlTester.document(document)
				.execute()
				.path("customers")
				.entityList(Customer.class)
				.satisfies(customer -> {
					assertThat(customer.get(0).getCustomerId()).isEqualTo(1);
					assertThat(customer.get(0).getFirstName()).isEqualTo("Mark");
					assertThat(customer.get(0).getLastName()).isEqualTo("Wood");
				});

	}

	@Test
	void given_graphql_endpoint_create_customer_should_return_new_customer() {
		String document = """
    			mutation newCustomer($input: CreateCustomerInput){
    				createCustomer(input: $input){
    					customerId
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
					assertThat(customer.getCustomerId()).isNotNull();
					assertThat(customer.getFirstName()).isNull();
					assertThat(customer.getLastName()).isNull();

				});

	}
}
