package com.demo.bad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * BAD CODE - For SonarQube demo: Magic numbers, code duplication, cognitive complexity
 */
public class BadUtil {

    // BAD: Magic numbers - SonarQube S109
    public BigDecimal calculateDiscount(BigDecimal price) {
        if (price.compareTo(new BigDecimal("100")) > 0) {
            return price.multiply(new BigDecimal("0.2"));
        }
        if (price.compareTo(new BigDecimal("50")) > 0) {
            return price.multiply(new BigDecimal("0.1"));
        }
        return price.multiply(new BigDecimal("0.05"));
    }

    // BAD: Code duplication
    public String formatPrice1(BigDecimal p) {
        return "Price: " + p.setScale(2, java.math.RoundingMode.HALF_UP) + " USD";
    }

    public String formatPrice2(BigDecimal p) {
        return "Price: " + p.setScale(2, java.math.RoundingMode.HALF_UP) + " USD";
    }

    public String formatPrice3(BigDecimal p) {
        return "Price: " + p.setScale(2, java.math.RoundingMode.HALF_UP) + " USD";
    }

    // BAD: High cognitive complexity - too many nested conditions
    public String getGrade(int score) {
        if (score >= 90) {
            return "A";
        } else {
            if (score >= 80) {
                return "B";
            } else {
                if (score >= 70) {
                    return "C";
                } else {
                    if (score >= 60) {
                        return "D";
                    } else {
                        return "F";
                    }
                }
            }
        }
    }

    // BAD: Unused variable - SonarQube S1481
    public List<String> processItems(List<String> items) {
        List<String> result = new ArrayList<>();
        String unusedVariable = "never used";
        for (String item : items) {
            result.add(item.toUpperCase());
        }
        return result;
    }

    // BAD: String concatenation in loop - SonarQube S1643
    public String concatenateAll(List<String> list) {
        String s = "";
        for (String item : list) {
            s = s + item + ",";
        }
        return s;
    }
}
