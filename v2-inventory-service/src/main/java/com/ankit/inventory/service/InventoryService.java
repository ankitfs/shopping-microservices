package com.ankit.inventory.service;

import com.ankit.inventory.dto.InventoryStatusDTO;
import com.ankit.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public InventoryStatusDTO isInStock(String skuCode, Integer quantity) {
        return new InventoryStatusDTO(inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity));
    }
}
