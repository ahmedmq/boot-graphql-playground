package com.ahmedmq.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.ahmedmq.transaction.TransactionType.DEPOSIT;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TransactionServiceTest {

	@Autowired
	TransactionRepository transactionRepository;

	@BeforeEach
	public void setup(){
		transactionRepository.deleteAll();
		List<Transaction> transactions = List.of(
				new Transaction(null, 1, 99, DEPOSIT, BigDecimal.TEN, BigDecimal.TEN, "Deposit", LocalDateTime.now()),
				new Transaction(null, 99, 1, DEPOSIT, BigDecimal.TEN, BigDecimal.TEN, "Deposit", LocalDateTime.now())
		);
		transactionRepository.saveAll(transactions);
	}

	@Test
	void shouldReturnTransactionsMatchingAccountIdWhenAccountIdIsOnlyProvided() {
		TransactionService transactionService = new TransactionService(transactionRepository);
		TransactionSearchInput transactionSearchInput = new TransactionSearchInput(99, 1, 0, 1);
		List<Transaction> transactionList = transactionService.transactions(transactionSearchInput);

		assertThat(transactionList.size()).isEqualTo(1);
		assertThat(transactionList.get(0).getCustomerId()).isEqualTo(99);
		assertThat(transactionList.get(0).getAccountId()).isEqualTo(1);

	}

	@Test
	void shouldReturnTransactionsMatchingCustomerIdWhenCustomerIdIsOnlyProvided() {
		TransactionService transactionService = new TransactionService(transactionRepository);
		TransactionSearchInput transactionSearchInput = new TransactionSearchInput(1, 99, 0, 2);
		List<Transaction> transactionList = transactionService.transactions(transactionSearchInput);

		assertThat(transactionList.size()).isEqualTo(1);
		assertThat(transactionList.get(0).getAccountId()).isEqualTo(99);
		assertThat(transactionList.get(0).getCustomerId()).isEqualTo(1);

	}

	@Test
	void shouldReturnAllTransactionsWhenCustomerIdAndAccountIdIsNotProvided() {
		TransactionService transactionService = new TransactionService(transactionRepository);
		TransactionSearchInput transactionSearchInput = new TransactionSearchInput(null, null, 0, 2);
		List<Transaction> transactionList = transactionService.transactions(transactionSearchInput);

		assertThat(transactionList.size()).isEqualTo(2);

	}

	@Test
	void shouldReturnMultiPagedTransactionsWhenCustomerIdAndAccountIdIsNotProvided() {
		TransactionService transactionService = new TransactionService(transactionRepository);
		TransactionSearchInput transactionSearchInput = new TransactionSearchInput(null, null, 0, 1);
		List<Transaction> transactionList = transactionService.transactions(transactionSearchInput);

		assertThat(transactionList.size()).isEqualTo(1);

	}


}
