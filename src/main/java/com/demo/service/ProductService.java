package com.demo.service;

import com.demo.dto.ProductRequest;
import com.demo.dto.ProductResponse;
import com.demo.entity.Product;
import com.demo.exception.ResourceNotFoundException;
import com.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final String PRODUCT = "Product";

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, id));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName().trim());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        product = productRepository.save(product);
        return toResponse(product);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, id));
        product.setName(request.getName().trim());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        product = productRepository.save(product);
        return toResponse(product);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(PRODUCT, id);
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null || maxPrice == null || minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Invalid price range: min must be <= max");
        }
        return productRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse r = new ProductResponse();
        r.setId(product.getId());
        r.setName(product.getName());
        r.setPrice(product.getPrice());
        r.setDescription(product.getDescription());
        r.setCreatedAt(product.getCreatedAt());
        return r;
    }
}
