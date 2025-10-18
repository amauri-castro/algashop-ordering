package com.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class BirthDateTest {

    @Test
    void shouldGenerate() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1993, 8, 28));
        Assertions.assertThat(birthDate).isEqualTo(new BirthDate(LocalDate.of(1993, 8, 28)));

    }

    @Test
    void givenInvalidBirthDateShouldGenerateException() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BirthDate(null));
    }

    @Test
    void givenLaterBirthDateShouldGenerateExeception() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new BirthDate(LocalDate.of(2026, 10, 19)));
    }

    @Test
    void shouldCalculateAge() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1993, 8, 28));
        Assertions.assertThat(birthDate.age()).isEqualTo(32);
    }

}