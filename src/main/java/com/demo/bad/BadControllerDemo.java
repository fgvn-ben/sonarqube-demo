package com.demo.bad;

import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BAD CODE - Rule 14 (no @Valid), Rule 15 (return Entity), Rule 19 (return null)
 */
@RestController
@RequestMapping("/api/bad/demo")
public class BadControllerDemo {

    private final ProductRepository productRepository;
    private final BadRulesDemo badRulesDemo;

    public BadControllerDemo(ProductRepository productRepository, BadRulesDemo badRulesDemo) {
        this.productRepository = productRepository;
        this.badRulesDemo = badRulesDemo;
    }

    // BAD Rule 14: Không có @Valid - user input không validate
    @PostMapping("/create")
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // BAD Rule 15: Trả về Entity trực tiếp thay vì DTO
    @GetMapping("/entity/{id}")
    public Product getProduct(@PathVariable Long id) {
        return badRulesDemo.getProductEntity(id);
    }

    // BAD Rule 19: Có thể return null
    @GetMapping("/by-name")
    public List<Product> findByName(@RequestParam String name) {
        return badRulesDemo.findByName(name);
    }

    // BAD Rule 10: S4973 - String == trong controller
    @GetMapping("/check-admin")
    public boolean isAdmin(@RequestParam String role) {
        return role == "admin";
    }
}
