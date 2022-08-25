package com.ahmedmq.customer;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import com.ahmedmq.account.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "accounts")
@EqualsAndHashCode(exclude = "accounts")
public class Customer {

	@Id
	@GeneratedValue(generator = "customer_sequence")
	@SequenceGenerator(name = "customer_sequence", sequenceName = "customer_seq_id")
	Integer customerId;
	String firstName;
	String lastName;

	@ManyToMany(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(
			name = "customer_accounts",
			joinColumns = @JoinColumn(name = "cust_id"),
			inverseJoinColumns = @JoinColumn(name = "acc_id")
	)
	Set<Account> accounts = new LinkedHashSet<>();

	public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public void addAccount(Account account){
		this.accounts.add(account);
		account.getCustomers().add(this);
	}

}
