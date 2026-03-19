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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BadNewCodeTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private BadNewCode badNewCode;

    @Test
    void getProductName_returnsName_whenFound() {
        Product p = new Product();
        p.setId(1L);
        p.setName("Laptop");
        p.setPrice(BigDecimal.TEN);
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        String result = badNewCode.getProductName(1L);

        assertThat(result).isEqualTo("Laptop");
    }

    @Test
    void logMessage_doesNotThrow() {
        badNewCode.logMessage("test");
    }

    @Test
    void isActive_returnsFalse_forNonLiteral() {
        boolean result = badNewCode.isActive("ACTIVE");
        assertThat(result).isFalse();
    }

    @Test
    void isEmpty_returnsTrue_whenEmpty() {
        assertThat(badNewCode.isEmpty(List.of())).isTrue();
    }

    @Test
    void isEmpty_returnsFalse_whenNotEmpty() {
        assertThat(badNewCode.isEmpty(List.of("a"))).isFalse();
    }

    @Test
    void getLevel_returnsS_whenOver100() {
        assertThat(badNewCode.getLevel(101)).isEqualTo("S");
    }

    @Test
    void getLevel_returnsA_whenOver80() {
        assertThat(badNewCode.getLevel(85)).isEqualTo("A");
    }

    @Test
    void getLevel_returnsB_whenOver60() {
        assertThat(badNewCode.getLevel(70)).isEqualTo("B");
    }

    @Test
    void getLevel_returnsC_when60OrBelow() {
        assertThat(badNewCode.getLevel(50)).isEqualTo("C");
    }

    @Test
    void buildQueryByName_concatenatesInput() {
        String result = badNewCode.buildQueryByName("test");
        assertThat(result).isEqualTo("SELECT * FROM products WHERE name = 'test'");
    }

    @Test
    void hashPasswordWeak_returnsBase64Hash() throws Exception {
        String result = badNewCode.hashPasswordWeak("password");
        assertThat(result).isNotEmpty();
        assertThat(result).matches("^[A-Za-z0-9+/=]+$");
    }

    @Test
    void generateResetToken_returnsTokenFormat() {
        String result = badNewCode.generateResetToken();
        assertThat(result).startsWith("RST-");
        assertThat(result.length()).isGreaterThan(4);
    }
}
