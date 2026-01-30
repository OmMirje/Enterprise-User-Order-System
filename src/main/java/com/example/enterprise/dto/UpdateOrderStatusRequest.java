package com.example.enterprise.dto;

import com.example.enterprise.entity.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    private OrderStatus status;
}

