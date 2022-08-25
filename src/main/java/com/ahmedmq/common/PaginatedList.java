package com.ahmedmq.common;

import java.util.List;

public interface PaginatedList<T extends Node> {
	List<T> getItems();
	void setItems(List<T> items);
	Integer getTotalItems();
	void setTotalItems(Integer totalItems);
}
