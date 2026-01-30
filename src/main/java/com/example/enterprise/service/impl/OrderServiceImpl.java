package com.example.enterprise.service.impl;

import com.example.enterprise.dto.CreateOrderRequest;
import com.example.enterprise.dto.OrderItemRequest;
import com.example.enterprise.dto.UpdateOrderStatusRequest;
import com.example.enterprise.entity.*;
import com.example.enterprise.exception.BadRequestException;
import com.example.enterprise.exception.ResourceNotFoundException;
import com.example.enterprise.repository.OrderRepository;
import com.example.enterprise.repository.ProductRepository;
import com.example.enterprise.repository.UserRepository;
import com.example.enterprise.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (request.getItems().isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemReq.getProductId()));

            if (product.getStock() < itemReq.getQuantity()) {
                throw new BadRequestException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemReq.getQuantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));

            order.getItems().add(item);
        }

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return orderRepository.findByUser(user);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        order.setStatus(request.getStatus());
        return orderRepository.save(order);
    }
}

