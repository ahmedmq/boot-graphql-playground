package com.ahmedmq.account;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import com.ahmedmq.customer.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "customers")
@EqualsAndHashCode(exclude = "customers")
public class Account {

	@Id
	@GeneratedValue(generator = "account_sequence")
	@SequenceGenerator(name = "account_sequence", sequenceName = "account_seq_id")
	Integer accountId;

	@Enumerated(value = EnumType.STRING)
	AccountType type;

	BigDecimal balance;

	@ManyToMany(mappedBy = "accounts")
	Set<Customer> customers= new LinkedHashSet<>();

	public Account(AccountType type, BigDecimal balance){
		this.type = type;
		this.balance = balance;
	}

	public void addCustomer(Customer customer){
		this.customers.add(customer);
		customer.getAccounts().add(this);
	}

}
