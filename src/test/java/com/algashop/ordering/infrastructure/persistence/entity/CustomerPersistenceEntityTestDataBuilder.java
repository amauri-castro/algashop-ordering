package com.algashop.ordering.infrastructure.persistence.entity;

import com.algashop.ordering.domain.model.utility.IdGenerator;
import com.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerPersistenceEntityTestDataBuilder {

    private CustomerPersistenceEntityTestDataBuilder() {
    }

    public static CustomerPersistenceEntity.CustomerPersistenceEntityBuilder existing() {
        return CustomerPersistenceEntity.builder()
                .id(IdGenerator.generateTimeBasedUUID())
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
