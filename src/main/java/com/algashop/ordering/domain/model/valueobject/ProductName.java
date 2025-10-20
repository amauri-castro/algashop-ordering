package com.algashop.ordering.domain.model.valueobject;

import com.algashop.ordering.domain.model.validator.FieldValidations;

public record ProductName(String value) {

    public ProductName {
        FieldValidations.requiresNonBlank(value);
    }

    @Override
    public String toString() {
        return this.value();
    }
}
