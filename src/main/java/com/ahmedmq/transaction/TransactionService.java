package com.ahmedmq.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.criteria.Predicate;

import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;

	public List<Transaction> transactions(TransactionSearchInput transactionSearchInput) {
		return transactionRepository.findAll(getSpecificationFromTransactionInput(transactionSearchInput));
	}

	private Specification<Transaction> getSpecificationFromTransactionInput(TransactionSearchInput transactionSearchInput){
		return (root, query, criteriaBuilder ) -> {
			if (transactionSearchInput.getCustomerId() != null && transactionSearchInput.getAccountId() != null){
				return criteriaBuilder.or(
						criteriaBuilder.equal(
								root.get("accountId"),
								transactionSearchInput.getAccountId()
						),
						criteriaBuilder.equal(
								root.get("customerId"),
								transactionSearchInput.getCustomerId()
						)
				);
			} else if (transactionSearchInput.getAccountId() != null){
				return criteriaBuilder.equal(root.get("accountId"),transactionSearchInput.getAccountId());
			} else if (transactionSearchInput.getCustomerId() != null){
				return criteriaBuilder.equal(root.get("customerId"),transactionSearchInput.getCustomerId());
			}
			return criteriaBuilder.conjunction();
		};
	}
}
