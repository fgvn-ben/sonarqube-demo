package com.demo.bad;

import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BadUserServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private BadUserService badUserService;

    @Test
    void findByNameUnsafe_returnsList() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        List<Object[]> result = badUserService.findByNameUnsafe("test");

        assertThat(result).isEmpty();
        verify(entityManager).createNativeQuery("SELECT * FROM products WHERE name = 'test'");
    }

    @Test
    void getProductNameOrNull_returnsName_whenFound() {
        Product p = new Product();
        p.setName("Laptop");
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        String result = badUserService.getProductNameOrNull(1L);

        assertThat(result).isEqualTo("Laptop");
    }

    @Test
    void getProductNameOrNull_returnsNull_whenNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        String result = badUserService.getProductNameOrNull(999L);

        assertThat(result).isNull();
    }

    @Test
    void getPriceLength_returnsLength_whenFound() {
        Product p = new Product();
        p.setPrice(new BigDecimal("99.99"));
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        int result = badUserService.getPriceLength(1L);

        assertThat(result).isEqualTo(5);
    }

    @Test
    void badExceptionHandling_doesNotThrow() {
        badUserService.badExceptionHandling();
    }
}
