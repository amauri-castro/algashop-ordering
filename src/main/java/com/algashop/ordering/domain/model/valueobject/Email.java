package com.algashop.ordering.domain.model.valueobject;

import com.algashop.ordering.domain.model.validator.FieldValidations;

public record Email(String value) {

    public Email {
        FieldValidations.requiresValidEmail(value);
    }

    @Override
    public String toString() {
        return this.value();
    }
}
