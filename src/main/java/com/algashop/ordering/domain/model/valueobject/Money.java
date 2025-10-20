package com.algashop.ordering.domain.model.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode roudingMode = RoundingMode.HALF_EVEN;

    public static Money ZERO = new Money(BigDecimal.ZERO);

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money(BigDecimal value) {
        Objects.requireNonNull(value);
        this.value = value.setScale(2, roudingMode);
        if (value.signum() == -1) {
            throw new IllegalArgumentException();
        }
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);
        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }
        BigDecimal multiplied = this.value.multiply(new BigDecimal(quantity.value()));
        return new Money(multiplied);
    }

    public Money add(Money other) {
        Objects.requireNonNull(other);
        return new Money(this.value.add(other.value));
    }

    public Money divide(Money other) {
        return new Money(this.value.divide(other.value, roudingMode));
    }

    @Override
    public String toString() {
        return value().toString();
    }

    @Override
    public int compareTo(Money other) {
        return this.value().compareTo(other.value());
    }
}
