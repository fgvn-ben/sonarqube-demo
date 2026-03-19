package com.demo.bad;

import com.demo.entity.Product;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BadRulesDemoTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private BadRulesDemo badRulesDemo;

    @Test
    void isRunning_returnsTrueByDefault() {
        assertThat(badRulesDemo.isRunning()).isTrue();
    }

    @Test
    void setRunning_updatesValue() {
        badRulesDemo.setRunning(false);
        assertThat(badRulesDemo.isRunning()).isFalse();
    }

    @Test
    void getNameOrNPE_returnsName_whenFound() {
        Product p = new Product();
        p.setName("Laptop");
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        String result = badRulesDemo.getNameOrNPE(1L);

        assertThat(result).isEqualTo("Laptop");
    }

    @Test
    void saveAndIgnoreError_doesNotThrow() {
        Product p = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(p);

        badRulesDemo.saveAndIgnoreError(p);

        verify(productRepository).save(p);
    }

    @Test
    void hasNoItems_returnsTrue_whenEmpty() {
        assertThat(badRulesDemo.hasNoItems(List.of())).isTrue();
    }

    @Test
    void hasNoItems_returnsFalse_whenNotEmpty() {
        assertThat(badRulesDemo.hasNoItems(List.of("a"))).isFalse();
    }

    @Test
    void isAdmin_returnsFalse_forNonLiteral() {
        assertThat(badRulesDemo.isAdmin("admin")).isFalse();
    }

    @Test
    void getProductEntity_returnsProduct_whenFound() {
        Product p = new Product();
        p.setId(1L);
        p.setName("Phone");
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        Product result = badRulesDemo.getProductEntity(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Phone");
    }

    @Test
    void getProductEntity_returnsNull_whenNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Product result = badRulesDemo.getProductEntity(999L);

        assertThat(result).isNull();
    }

    @Test
    void getStatus_returnsCorrectForEachCode() {
        assertThat(badRulesDemo.getStatus(200)).isEqualTo("OK");
        assertThat(badRulesDemo.getStatus(404)).isEqualTo("Not Found");
        assertThat(badRulesDemo.getStatus(500)).isEqualTo("Error");
        assertThat(badRulesDemo.getStatus(999)).isEqualTo("Unknown");
    }

    @Test
    void findByName_returnsList_whenMatchFound() {
        Product p = new Product();
        p.setId(1L);
        p.setName("Laptop");
        when(productRepository.findAll()).thenReturn(List.of(p));

        List<Product> result = badRulesDemo.findByName("Laptop");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Laptop");
    }

    @Test
    void findByName_returnsNull_whenNoMatch() {
        Product p = new Product();
        p.setName("Phone");
        when(productRepository.findAll()).thenReturn(List.of(p));

        List<Product> result = badRulesDemo.findByName("Laptop");

        assertThat(result).isNull();
    }

    @Test
    void logUser_doesNotThrow() {
        badRulesDemo.logUser("john");
    }

    @Test
    void printStatus_doesNotThrow() {
        badRulesDemo.printStatus("test");
    }
}
