package com.techtactix.app.model.dto;

public record OrderItemRequest(
	int productId,
	int quantity
)
{}
