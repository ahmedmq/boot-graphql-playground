package com.ahmedmq.customer;

import java.util.Collection;

import com.ahmedmq.account.Account;
import com.ahmedmq.account.AccountService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final AccountService accountService;

	public Collection<Customer> customers() {
		return customerRepository.findAll();
	}

	public Customer createCustomer(CreateCustomerInput input) {
		return customerRepository.save(new Customer(input.getFirstName(), input.getLastName()));
	}

	public Customer linkAccount(Long customerId, Long accountId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
		Account account = accountService.account(accountId);
		customer.addAccount(account);
		return customerRepository.save(customer);
	}
}
