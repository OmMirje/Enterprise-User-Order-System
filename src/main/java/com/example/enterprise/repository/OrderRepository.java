package com.example.enterprise.repository;

import com.example.enterprise.entity.Order;
import com.example.enterprise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}

