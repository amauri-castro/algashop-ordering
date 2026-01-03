package com.algashop.ordering.domain.model.commons;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class MoneyTest {

    @Test
    void shouldGenerate() {
        Money money = new Money("50.25");
        Assertions.assertThat(money).isEqualTo(new Money("50.25"));
    }

    @Test
    void givenInvalidShouldGenerateExpection() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Money(""));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Money("-1"));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Money(new BigDecimal(-1)));
    }

}