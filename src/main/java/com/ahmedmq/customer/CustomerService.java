package com.ahmedmq.customer;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}


	public Collection<Customer> customers() {
		return customerRepository.findAll();
	}

	public Customer createCustomer(CreateCustomerInput input) {
		return customerRepository.save(new Customer(null, input.getFirstName(), input.getLastName()));
	}
}
