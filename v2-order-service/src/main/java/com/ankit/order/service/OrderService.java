package com.ankit.order.service;

import com.ankit.order.dto.InventoryClientDTO;
import com.ankit.order.dto.OrderDTO;
import com.ankit.order.model.Order;
import com.ankit.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private WebClient.Builder webClient;

    private final String inventoryURL = "http://localhost:8082";

    @Autowired
    private OrderRepository orderRepository;

    public void placeOrder(OrderDTO orderDTO) {

        InventoryClientDTO inventoryDTO = webClient.build()
                .get()
                .uri(inventoryURL + "/api/inventory?skuCode="+orderDTO.skuCode()
                        +"&quantity="+orderDTO.quantity())
                .retrieve()
                .bodyToMono(InventoryClientDTO.class)
                .block();


        if(inventoryDTO!= null && inventoryDTO.inventoryStatus()) {
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .skuCode(orderDTO.skuCode())
                    .price(orderDTO.price())
                    .quantity(orderDTO.quantity())
                    .build();

            orderRepository.save(order);
        }
        else
            throw new RuntimeException("Order cannot placed with " + orderDTO.skuCode() +" skuCode");
    }
}
