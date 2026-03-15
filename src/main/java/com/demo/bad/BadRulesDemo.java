package com.demo.bad;

import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BAD CODE - Vi phạm Top 20 rules SonarQube phổ biến cho Java/Spring Boot
 * Mỗi method vi phạm 1 hoặc nhiều rule để demo trên SonarQube
 */
@Service
public class BadRulesDemo {

    private static final Logger log = LoggerFactory.getLogger(BadRulesDemo.class);

    // BAD Rule 13: @Autowired field injection thay vì constructor
    @Autowired
    private ProductRepository productRepository;

    // BAD Rule 11: S2274 - Thiếu volatile cho multi-thread
    private boolean running = true;

    public boolean isRunning() { return running; }
    public void setRunning(boolean running) { this.running = running; }

    // ========== Rule 1: S2259 - Null pointer dereference ==========
    public String getNameOrNPE(Long id) {
        Product p = productRepository.findById(id).orElse(null);
        return p.getName();  // NPE khi p == null
    }

    // ========== Rule 2: S2095 - Resource không đóng ==========
    public String readFileUnsafe(String path) throws Exception {
        FileInputStream fis = new FileInputStream(path);
        byte[] buf = new byte[1024];
        int n = fis.read(buf);
        return n > 0 ? new String(buf, 0, n) : "";
    }

    // ========== Rule 2b: S2095 - Connection/Statement không đóng ==========
    public int countFromDb() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:demo", "sa", "");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products");
        return rs.next() ? rs.getInt(1) : 0;
    }

    // ========== Rule 3: S1166 - Exception bị nuốt (empty catch) ==========
    public void saveAndIgnoreError(Product p) {
        try {
            productRepository.save(p);
        } catch (Exception e) {
            // bỏ qua
        }
    }

    // ========== Rule 4: S106 - System.out thay vì logger ==========
    public void printStatus(String msg) {
        System.out.println("Status: " + msg);
        System.err.println("Error output");
    }

    // ========== Rule 5: S2077 - SQL Injection (đã có ở BadUserService) ==========
    public List<Object[]> sqlInjection(String id) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:demo", "sa", "");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE id = " + id);
        return Collections.emptyList();
    }

    // ========== Rule 6: S2068 - Hardcoded credentials (đã có ở BadUserService) ==========
    private static final String PWD = "secret123";

    // ========== Rule 9: S1155 - list.size() == 0 thay vì isEmpty() ==========
    public boolean hasNoItems(List<String> list) {
        return list.size() == 0;
    }

    // ========== Rule 10: S4973 - String so sánh bằng == ==========
    public boolean isAdmin(String name) {
        return name == "admin";
    }

    // ========== Rule 12: @Transactional trên private method - Spring không apply proxy ==========
    @Transactional
    private void savePrivate(Product p) {
        productRepository.save(p);
    }

    // ========== Rule 15: Trả về Entity trực tiếp thay vì DTO ==========
    public Product getProductEntity(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // ========== Rule 17: Magic numbers ==========
    public String getStatus(int code) {
        if (code == 200) return "OK";
        if (code == 404) return "Not Found";
        if (code == 500) return "Error";
        return "Unknown";
    }

    // ========== Rule 19: Return null thay vì empty list ==========
    public List<Product> findByName(String name) {
        List<Product> list = productRepository.findAll().stream()
                .filter(p -> name.equals(p.getName()))
                .toList();
        if (list.isEmpty()) {
            return null;
        }
        return new ArrayList<>(list);
    }

    // ========== Rule 20: Logger dùng string concat thay vì placeholder ==========
    public void logUser(String userName) {
        log.info("User logged in: " + userName);
        log.debug("Processing user " + userName + " with id " + System.currentTimeMillis());
    }
}
