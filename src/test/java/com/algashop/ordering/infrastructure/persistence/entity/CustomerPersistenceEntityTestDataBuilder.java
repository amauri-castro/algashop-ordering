package com.algashop.ordering.infrastructure.persistence.entity;

import com.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static com.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class CustomerPersistenceEntityTestDataBuilder {

    private CustomerPersistenceEntityTestDataBuilder() {
    }

    public static CustomerPersistenceEntity.CustomerPersistenceEntityBuilder aCustomer() {
        return CustomerPersistenceEntity.builder()
                .id(DEFAULT_CUSTOMER_ID.value())
                .firstName("Jhon")
                .lastName("Doe")
                .birthDate(LocalDate.now().minusYears(20))
                .email("jhon.doe@gmail.com")
                .phone("78445457744")
                .document("5545445454")
                .promotionNotificationsAllowed(false)
                .archived(false)
                .registeredAt(OffsetDateTime.now())
                .archivedAt(null)
                .loyaltyPoints(0)
                .address(AddressEmbeddable.builder()
                        .street("Manchester avenue")
                        .number("420")
                        .complement("abc")
                        .neighborhood("Bronks")
                        .city("New York")
                        .state("New York")
                        .zipCode("64545454")
                        .build()
                );


    }
}
