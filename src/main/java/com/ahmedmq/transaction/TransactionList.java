package com.ahmedmq.transaction;

import java.util.ArrayList;
import java.util.List;

import com.ahmedmq.common.PaginatedList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionList implements PaginatedList<Transaction> {
	List<Transaction> items = new ArrayList<>();
	Integer totalItems;
}
