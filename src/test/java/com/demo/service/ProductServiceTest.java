package com.demo.service;

import com.demo.dto.ProductRequest;
import com.demo.dto.ProductResponse;
import com.demo.entity.Product;
import com.demo.exception.ResourceNotFoundException;
import com.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void findAll_returnsEmptyList_whenNoProducts() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<ProductResponse> result = productService.findAll();

        assertThat(result).isEmpty();
        verify(productRepository).findAll();
    }

    @Test
    void findAll_returnsList_whenProductsExist() {
        Product p = product("Laptop", "99.99");
        p.setId(1L);
        when(productRepository.findAll()).thenReturn(List.of(p));

        List<ProductResponse> result = productService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Laptop");
        assertThat(result.get(0).getPrice()).isEqualByComparingTo("99.99");
    }

    @Test
    void findById_throws_whenNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
        verify(productRepository).findById(999L);
    }

    @Test
    void findById_returnsResponse_whenFound() {
        Product p = product("Phone", "299.00");
        p.setId(2L);
        when(productRepository.findById(2L)).thenReturn(Optional.of(p));

        ProductResponse result = productService.findById(2L);

        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Phone");
        assertThat(result.getPrice()).isEqualByComparingTo("299.00");
    }

    @Test
    void create_savesAndReturnsResponse() {
        ProductRequest req = new ProductRequest("Tablet", BigDecimal.valueOf(199.99), "Desc");
        Product saved = product("Tablet", "199.99");
        saved.setId(3L);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse result = productService.create(req);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Tablet");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteById_throws_whenNotFound() {
        when(productRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> productService.deleteById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void deleteById_deletes_whenExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteById(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void findByPriceRange_throws_whenInvalidRange() {
        assertThatThrownBy(() -> productService.findByPriceRange(
                BigDecimal.TEN, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid price range");
        verify(productRepository, never()).findByPriceRange(any(), any());
    }

    @Test
    void findByPriceRange_returnsFilteredList() {
        Product p = product("Mid", "75.00");
        p.setId(1L);
        when(productRepository.findByPriceRange(
                BigDecimal.valueOf(50), BigDecimal.valueOf(100)))
                .thenReturn(List.of(p));

        List<ProductResponse> result = productService.findByPriceRange(
                BigDecimal.valueOf(50), BigDecimal.valueOf(100));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPrice()).isEqualByComparingTo("75.00");
    }

    @Test
    void update_returnsResponse_whenFound() {
        ProductRequest req = new ProductRequest("Updated Tablet", BigDecimal.valueOf(249.99), "New desc");
        Product existing = product("Tablet", "199.99");
        existing.setId(3L);
        Product updated = product("Updated Tablet", "249.99");
        updated.setId(3L);
        when(productRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(updated);

        ProductResponse result = productService.update(3L, req);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Updated Tablet");
        assertThat(result.getPrice()).isEqualByComparingTo("249.99");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void update_throws_whenNotFound() {
        ProductRequest req = new ProductRequest("Product", BigDecimal.valueOf(99.99), null);
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.update(999L, req))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
        verify(productRepository, never()).save(any());
    }

    private static Product product(String name, String price) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(new BigDecimal(price));
        return p;
    }
}
