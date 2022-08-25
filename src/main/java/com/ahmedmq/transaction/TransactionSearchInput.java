package com.ahmedmq.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionSearchInput {
	Long customerId;

	Long accountId;

	Integer currentPage;

	Integer pageSize;
}
