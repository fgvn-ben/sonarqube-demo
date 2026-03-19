package com.demo.bad;

import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Random;

/**
 * BAD CODE - File mới thêm để demo New Code trên SonarQube.
 * Sau khi push và chạy Jenkins scan, các issue ở đây sẽ hiện trong phần "New Code".
 */
@Service
public class BadNewCode {

    // BAD S2068: Hardcoded secret
    private static final String JWT_SECRET = "my-super-secret-key";

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

    // BAD S2077: SQL query nối chuỗi trực tiếp từ user input
    public String buildQueryByName(String name) {
        return "SELECT * FROM products WHERE name = '" + name + "'";
    }

    // BAD S4790: Dùng thuật toán hash yếu MD5
    public String hashPasswordWeak(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hash = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    // BAD S2245: Dùng Random cho token bảo mật
    public String generateResetToken() {
        return "RST-" + new Random().nextInt(999999);
    }

    // BAD S2115/S5042: Deserialize dữ liệu không tin cậy
    public Object unsafeDeserialize(byte[] payload) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(payload));
        return in.readObject();
    }

    // BAD S4721/S2076: Thực thi command từ input
    public Process runCommand(String command) throws Exception {
        return Runtime.getRuntime().exec(command);
    }
}
