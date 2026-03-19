package com.demo.bad;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BadUtilTest {

    private final BadUtil badUtil = new BadUtil();

    @Test
    void calculateDiscount_returns20Percent_whenPriceOver100() {
        BigDecimal result = badUtil.calculateDiscount(new BigDecimal("150"));
        assertThat(result).isEqualByComparingTo("30");
    }

    @Test
    void calculateDiscount_returns10Percent_whenPriceOver50() {
        BigDecimal result = badUtil.calculateDiscount(new BigDecimal("75"));
        assertThat(result).isEqualByComparingTo("7.5");
    }

    @Test
    void calculateDiscount_returns5Percent_whenPriceUnder50() {
        BigDecimal result = badUtil.calculateDiscount(new BigDecimal("30"));
        assertThat(result).isEqualByComparingTo("1.5");
    }

    @Test
    void formatPrice1_formatsCorrectly() {
        String result = badUtil.formatPrice1(new BigDecimal("99.999"));
        assertThat(result).isEqualTo("Price: 100.00 USD");
    }

    @Test
    void formatPrice2_formatsCorrectly() {
        String result = badUtil.formatPrice2(new BigDecimal("50.5"));
        assertThat(result).isEqualTo("Price: 50.50 USD");
    }

    @Test
    void formatPrice3_formatsCorrectly() {
        String result = badUtil.formatPrice3(new BigDecimal("0.99"));
        assertThat(result).isEqualTo("Price: 0.99 USD");
    }

    @Test
    void getGrade_returnsA_whenScore90OrAbove() {
        assertThat(badUtil.getGrade(90)).isEqualTo("A");
        assertThat(badUtil.getGrade(95)).isEqualTo("A");
    }

    @Test
    void getGrade_returnsB_whenScore80To89() {
        assertThat(badUtil.getGrade(80)).isEqualTo("B");
        assertThat(badUtil.getGrade(85)).isEqualTo("B");
    }

    @Test
    void getGrade_returnsC_whenScore70To79() {
        assertThat(badUtil.getGrade(70)).isEqualTo("C");
        assertThat(badUtil.getGrade(75)).isEqualTo("C");
    }

    @Test
    void getGrade_returnsD_whenScore60To69() {
        assertThat(badUtil.getGrade(60)).isEqualTo("D");
        assertThat(badUtil.getGrade(65)).isEqualTo("D");
    }

    @Test
    void getGrade_returnsF_whenScoreBelow60() {
        assertThat(badUtil.getGrade(59)).isEqualTo("F");
        assertThat(badUtil.getGrade(0)).isEqualTo("F");
    }

    @Test
    void processItems_uppercasesAllItems() {
        List<String> result = badUtil.processItems(List.of("a", "b", "c"));
        assertThat(result).containsExactly("A", "B", "C");
    }

    @Test
    void processItems_returnsEmpty_whenEmptyInput() {
        List<String> result = badUtil.processItems(List.of());
        assertThat(result).isEmpty();
    }

    @Test
    void concatenateAll_joinsWithComma() {
        String result = badUtil.concatenateAll(List.of("x", "y", "z"));
        assertThat(result).isEqualTo("x,y,z,");
    }

    @Test
    void concatenateAll_returnsEmpty_whenEmptyList() {
        String result = badUtil.concatenateAll(List.of());
        assertThat(result).isEmpty();
    }
}
