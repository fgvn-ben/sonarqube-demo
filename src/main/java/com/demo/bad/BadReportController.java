package com.demo.bad;

import com.demo.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * BAD CODE - For SonarQube demo: No input validation, raw types, security issues
 */
@RestController
@RequestMapping("/api/bad/reports")
public class BadReportController {

    private final ProductRepository productRepository;
    private final BadUserService badUserService;

    public BadReportController(ProductRepository productRepository, BadUserService badUserService) {
        this.productRepository = productRepository;
        this.badUserService = badUserService;
    }

    // BAD: SQL injection via request param - user input directly in query
    @GetMapping("/search")
    public Object searchByName(@RequestParam String name) {
        return badUserService.findByNameUnsafe(name);
    }

    // BAD: No validation - can throw NPE
    @GetMapping("/price-length/{id}")
    public Map<String, Object> getPriceLength(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("length", badUserService.getPriceLength(id));
        return result;
    }

    // BAD: Raw use of parameterized class - SonarQube S3740
    @GetMapping("/raw")
    public Map getRawData() {
        Map raw = new HashMap();
        raw.put("data", productRepository.findAll());
        return raw;
    }

    // BAD: Equals on enum without null check - SonarQube S4551 (if we had enum)
    // BAD: System.out - SonarQube S106
    @GetMapping("/debug")
    public String debug() {
        System.out.println("Debug endpoint called");
        return "ok";
    }
}
