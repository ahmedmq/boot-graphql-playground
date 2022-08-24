package com.ahmedmq.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue
	Integer transactionId;

	Integer accountId;

	Integer customerId;

	@Enumerated(EnumType.STRING)
	TransactionType type;

	BigDecimal amount;

	BigDecimal balance;

	String description;

	LocalDateTime transactionDateTime;

}
