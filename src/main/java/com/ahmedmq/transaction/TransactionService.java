package com.ahmedmq.transaction;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;

	public List<Transaction> transactions(TransactionSearchInput transactionSearchInput) {
		PageRequest pageRequest = PageRequest.of(transactionSearchInput.getPage(),
				transactionSearchInput.getPageSize());
		return transactionRepository
				.findAll(getSpecificationFromTransactionInput(transactionSearchInput), pageRequest)
				.stream().collect(Collectors.toList());
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
