package com.ankit.order.dto;

import java.math.BigDecimal;

public record OrderDTO(Long id, String orderNumber, String skuCode,
                       BigDecimal price, Integer quantity) {
}
