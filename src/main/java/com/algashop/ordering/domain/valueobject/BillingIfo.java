package com.algashop.ordering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

@Builder
public record BillingIfo(FullName fullName,
                         Document document,
                         Phone phone,
                         Address address) {

    public BillingIfo {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(address);
    }
}
