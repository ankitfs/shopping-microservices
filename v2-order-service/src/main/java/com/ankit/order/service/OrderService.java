package com.ankit.order.service;

import com.ankit.order.dto.InventoryClientDTO;
import com.ankit.order.dto.OrderDTO;
import com.ankit.order.event.OrderPlacedEvent;
import com.ankit.order.model.Order;
import com.ankit.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private WebClient.Builder webClient;

    private final String inventoryURL = "http://localhost:8082";
    private final String userEmail = "ankitagarwal.net@gmail.com";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

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

            //Send the message to Kafka Topic
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber(), userEmail);
            log.info("Start - Sending OrderPlacedEvent {} to Kafka Topic order-placed", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("End - Sending OrderPlacedEvent {} to Kafka Topic order-placed", orderPlacedEvent);
        }
        else
            throw new RuntimeException("Order cannot placed with " + orderDTO.skuCode() +" skuCode");
    }
}
