package com.demo.bad;

import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BAD CODE - File mới thêm để demo New Code trên SonarQube.
 * Sau khi push và chạy Jenkins scan, các issue ở đây sẽ hiện trong phần "New Code".
 */
@Service
public class BadNewCode {

    private final ProductRepository productRepository;

    public BadNewCode(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // BAD S2259: NPE - không check null
    public String getProductName(Long id) {
        Product p = productRepository.findById(id).orElse(null);
        return p.getName();
    }

    // BAD S106: System.out thay vì logger
    public void logMessage(String msg) {
        System.out.println("New code message: " + msg);
    }

    // BAD S4973: So sánh String bằng ==
    public boolean isActive(String status) {
        return status == "ACTIVE";
    }

    // BAD S1155: list.size() == 0 thay vì isEmpty()
    public boolean isEmpty(List<String> list) {
        return list.size() == 0;
    }

    // BAD: Magic numbers S109
    public String getLevel(int score) {
        if (score > 100) return "S";
        if (score > 80) return "A";
        if (score > 60) return "B";
        return "C";
    }
}
