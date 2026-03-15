package com.demo.bad;

import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BAD CODE - For SonarQube demo: SQL injection, empty catch, hardcoded credentials
 */
@Service
public class BadUserService {

    // BAD: Hardcoded credentials - SonarQube S2068
    private static final String DB_PASSWORD = "admin123";
    private static final String API_KEY = "sk-12345-secret-key";

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public BadUserService(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    // BAD: SQL Injection vulnerability - SonarQube S2077
    @SuppressWarnings("unchecked")
    public List<Object[]> findByNameUnsafe(String userName) {
        String sql = "SELECT * FROM products WHERE name = '" + userName + "'";
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    // BAD: Empty catch block - SonarQube S108
    public String getProductNameOrNull(Long id) {
        try {
            return productRepository.findById(id).get().getName();
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }

    // BAD: Possible NPE - SonarQube S2259
    public int getPriceLength(Long id) {
        Product p = productRepository.findById(id).orElse(null);
        return p.getPrice().toString().length();
    }

    // BAD: Using deprecated method / printStackTrace
    public void badExceptionHandling() {
        try {
            int x = 1 / 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
