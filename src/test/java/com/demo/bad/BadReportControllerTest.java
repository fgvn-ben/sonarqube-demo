package com.demo.bad;

import com.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BadReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private BadUserService badUserService;

    @Test
    void searchByName_returnsResult() throws Exception {
        when(badUserService.findByNameUnsafe("test")).thenReturn(List.of());

        mockMvc.perform(get("/api/bad/reports/search").param("name", "test"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void getPriceLength_returnsMap() throws Exception {
        when(badUserService.getPriceLength(1L)).thenReturn(5);

        mockMvc.perform(get("/api/bad/reports/price-length/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length").value(5));
    }

    @Test
    void getRawData_returnsMap() throws Exception {
        when(productRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/bad/reports/raw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void debug_returnsOk() throws Exception {
        mockMvc.perform(get("/api/bad/reports/debug"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}
