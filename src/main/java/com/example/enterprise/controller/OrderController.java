package com.example.enterprise.controller;

import com.example.enterprise.dto.CreateOrderRequest;
import com.example.enterprise.dto.UpdateOrderStatusRequest;
import com.example.enterprise.entity.Order;
import com.example.enterprise.security.CustomUserDetails;
import com.example.enterprise.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(userDetails.getId(), request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Order>> myOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(orderService.getUserOrders(userDetails.getId()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Order>> allOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
    }
}

