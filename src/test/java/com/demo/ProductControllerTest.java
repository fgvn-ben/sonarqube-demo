package com.demo;

import com.demo.dto.ProductRequest;
import com.demo.dto.ProductResponse;
import com.demo.exception.ResourceNotFoundException;
import com.demo.service.ProductService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void getAll_returnsOkAndEmptyList() throws Exception {
        when(productService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
        verify(productService).findAll();
    }

    @Test
    void getById_returns200AndBody_whenFound() throws Exception {
        ProductResponse resp = new ProductResponse(1L, "Laptop", BigDecimal.valueOf(99.99), null, null);
        when(productService.findById(1L)).thenReturn(resp);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(99.99));
        verify(productService).findById(1L);
    }

    @Test
    void getById_returns404_whenNotFound() throws Exception {
        when(productService.findById(999L)).thenThrow(new ResourceNotFoundException("Product", 999L));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
        verify(productService).findById(999L);
    }

    @Test
    void create_returns201AndBody_whenValid() throws Exception {
        ProductRequest req = new ProductRequest("Phone", BigDecimal.valueOf(199.99), "Desc");
        ProductResponse created = new ProductResponse(2L, "Phone", BigDecimal.valueOf(199.99), "Desc", null);
        when(productService.create(any(ProductRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Phone"));
        verify(productService).create(any(ProductRequest.class));
    }

    @Test
    void create_returns400_whenValidationFails() throws Exception {
        ProductRequest invalid = new ProductRequest("", BigDecimal.valueOf(-1), null);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
        verify(productService, never()).create(any());
    }

    @Test
    void delete_returns204_whenExists() throws Exception {
        doNothing().when(productService).deleteById(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
        verify(productService).deleteById(1L);
    }

    @Test
    void searchByPrice_returns200AndList() throws Exception {
        when(productService.findByPriceRange(eq(BigDecimal.valueOf(10)), eq(BigDecimal.valueOf(100))))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/products/search")
                        .param("minPrice", "10")
                        .param("maxPrice", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        verify(productService).findByPriceRange(BigDecimal.valueOf(10), BigDecimal.valueOf(100));
    }
}
