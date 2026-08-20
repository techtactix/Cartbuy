package com.techtactix.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techtactix.app.model.Order;
import com.techtactix.app.model.OrderItem;
import com.techtactix.app.model.Product;
import com.techtactix.app.model.dto.OrderItemRequest;
import com.techtactix.app.model.dto.OrderItemResponse;
import com.techtactix.app.model.dto.OrderRequest;
import com.techtactix.app.model.dto.OrderResponse;
import com.techtactix.app.repo.OrderRepo;
import com.techtactix.app.repo.ProductRepo;

@Service
public class OrderService {

	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private OrderRepo orderRepo;
	
	
	public OrderResponse placeOrder(OrderRequest request) {
		Order order=new Order();
		String orderId=UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		order.setOrderId(orderId);
		order.setCustomerName(request.customerName());
		order.setEmail(request.email());
		order.setStatus("PLACED");
		order.setOrderDate(LocalDate.now());
		
		List<OrderItem> orderItems=new ArrayList<>();
		for(OrderItemRequest itemReq : request.items()) {
			Product product=productRepo.findById(itemReq.productId()).orElseThrow(() -> new RuntimeException("Product Not Found"));
			product.setStockQuantity(product.getStockQuantity()-itemReq.quantity());
			productRepo.save(product);
			OrderItem orderItem=OrderItem.builder()
					.product(product)
					.quantity(itemReq.quantity())
					.totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
					.order(order)
					.build();
			orderItems.add(orderItem);
		}
		
		order.setOrderItems(orderItems);
		Order savedOrder=orderRepo.save(order);
		
		List<OrderItemResponse> itemResponses = new ArrayList<OrderItemResponse>();
		for(OrderItem item: order.getOrderItems()) {
			OrderItemResponse orderItemResponse=new OrderItemResponse(item.getProduct().getName(),
					item.getQuantity(),
					item.getTotalPrice());
			itemResponses.add(orderItemResponse);
		}
		
		OrderResponse orderResponse=new OrderResponse(savedOrder.getOrderId(),savedOrder.getCustomerName(),
				savedOrder.getEmail(),savedOrder.getStatus(),savedOrder.getOrderDate(),
				itemResponses);
				
		return orderResponse;
	}

	public List<OrderResponse> getAllOrderResponses() {
		List<Order> orders=orderRepo.findAll();
		List<OrderResponse> orderResponses=new ArrayList<OrderResponse>();
		
		
		for(Order order : orders) {
			
			List<OrderItemResponse> orderItemResponses=new ArrayList<OrderItemResponse>();
			for(OrderItem item:order.getOrderItems()) {
				OrderItemResponse orderItemResponse=new OrderItemResponse(
						item.getProduct().getName(),
						item.getQuantity(),
						item.getTotalPrice());
				orderItemResponses.add(orderItemResponse);
			}
			
			OrderResponse or=new OrderResponse(order.getOrderId(),
					order.getCustomerName(),
					order.getEmail(),
					order.getStatus(),
					order.getOrderDate(),
					orderItemResponses);
			orderResponses.add(or);
		}
		
		return orderResponses;
	}

}
