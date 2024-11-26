package com.ankit.order.service;

import com.ankit.order.dto.OrderDTO;
import com.ankit.order.model.Order;
import com.ankit.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void placeOrder(OrderDTO orderDTO) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .skuCode(orderDTO.skuCode())
                .price(orderDTO.price())
                .quantity(orderDTO.quantity())
                .build();

        orderRepository.save(order);

    }
}
