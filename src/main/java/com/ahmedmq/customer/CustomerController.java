package com.ahmedmq.customer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@QueryMapping
	public Collection<Customer> customers(){
		return customerService.customers();
	}


	@MutationMapping
	public Customer createCustomer(@Argument CreateCustomerInput input){
		return customerService.createCustomer(input);
	}
}