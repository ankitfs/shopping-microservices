package com.ankit.product.service;

import com.ankit.product.dto.ProductDTO;
import com.ankit.product.model.Product;
import com.ankit.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .id(productDTO.id())
                .name(productDTO.name())
                .description(productDTO.description())
                .price(productDTO.price())
                .build();
        productRepository.save(product);
        log.info("Product Saved Successfully");
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(),
                product.getPrice());
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> new ProductDTO(product.getId(),
                                            product.getName(),
                                            product.getDescription(),
                                            product.getPrice()))
                .toList();
    }
}
