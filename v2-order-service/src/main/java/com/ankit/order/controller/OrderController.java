package com.ankit.order.controller;

import com.ankit.order.dto.OrderDTO;
import com.ankit.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody OrderDTO orderDTO) {
       orderService.placeOrder(orderDTO);
       return "Order Placed Successfully";
    }
}
