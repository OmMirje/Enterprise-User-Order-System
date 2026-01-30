package com.example.enterprise.service;

import com.example.enterprise.dto.CreateOrderRequest;
import com.example.enterprise.dto.UpdateOrderStatusRequest;
import com.example.enterprise.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Long userId, CreateOrderRequest request);

    List<Order> getUserOrders(Long userId);

    List<Order> getAllOrders();

    Order updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);
}

