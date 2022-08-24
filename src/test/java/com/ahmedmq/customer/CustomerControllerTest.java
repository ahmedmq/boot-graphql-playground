package com.ahmedmq.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ahmedmq.config.GraphQLConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@GraphQlTest(CustomerController.class)
@Import(GraphQLConfig.class)
public class CustomerControllerTest {

	@Autowired
	GraphQlTester graphQLTester;

	@MockBean
	CustomerService customerService;

	Customer TEST_CUSTOMER = new Customer(1, "Mark", "Wood");

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
}
