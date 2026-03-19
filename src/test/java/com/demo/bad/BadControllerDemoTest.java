package com.demo.bad;

import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BadControllerDemoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private BadRulesDemo badRulesDemo;

    @Test
    void createProduct_returns201() throws Exception {
        Product product = new Product();
        product.setName("Test");
        product.setPrice(BigDecimal.TEN);
        Product saved = new Product();
        saved.setId(1L);
        saved.setName("Test");
        saved.setPrice(BigDecimal.TEN);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/api/bad/demo/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void getProduct_returnsEntity_whenFound() throws Exception {
        Product p = new Product();
        p.setId(1L);
        p.setName("Laptop");
        p.setPrice(BigDecimal.valueOf(99.99));
        when(badRulesDemo.getProductEntity(1L)).thenReturn(p);

        mockMvc.perform(get("/api/bad/demo/entity/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void findByName_returnsList_whenFound() throws Exception {
        Product p = new Product();
        p.setId(1L);
        p.setName("Phone");
        when(badRulesDemo.findByName("Phone")).thenReturn(List.of(p));

        mockMvc.perform(get("/api/bad/demo/by-name").param("name", "Phone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Phone"));
    }

    @Test
    void findByName_returnsNull_whenEmpty() throws Exception {
        when(badRulesDemo.findByName("Nonexistent")).thenReturn(null);

        mockMvc.perform(get("/api/bad/demo/by-name").param("name", "Nonexistent"))
                .andExpect(status().isOk())
                .andExpect(content().string("null"));
    }

    @Test
    void isAdmin_returnsBoolean() throws Exception {
        mockMvc.perform(get("/api/bad/demo/check-admin").param("role", "user"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
