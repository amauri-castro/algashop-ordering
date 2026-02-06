package com.algashop.ordering.domain.model.customer;

import com.algashop.ordering.domain.model.commons.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerTestDataBuilder {

    public static final CustomerId DEFAULT_CUSTOMER_ID = new CustomerId();

    private CustomerTestDataBuilder() {
    }

    public static Customer.BrandNewCustomerBuild brandNewCustomer() {
        return Customer.brandNew()
                .fullName(new FullName("Jhon", "Doe"))
                .birthDate(new BirthDate(LocalDate.of(1992, 8, 12)))
                .email(new Email("john.doe_" + UUID.randomUUID() + "@gmail.com"))
                .phone(new Phone("478-256-2504"))
                .document(new Document("478-256-2504"))
                .promotionNotificationsAllowed(false)
                .address(Address.builder()
                        .street("Bourbon Street")
                        .number("1140")
                        .neighborhood("North Ville")
                        .city("Springfield")
                        .state("Oregon")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 204")
                        .build()
                );
    }

    public static Customer.ExistingCustomerBuild existingCustomer() {
        return Customer.existing()
                .id(DEFAULT_CUSTOMER_ID)
                .registeredAt(OffsetDateTime.now())
                .archived(false)
                .archivedAt(null)
                .fullName(new FullName("Jhon", "Doe"))
                .birthDate(new BirthDate(LocalDate.of(1992, 8, 12)))
                .email(new Email("john.doe_" + UUID.randomUUID() + "@gmail.com"))
                .phone(new Phone("478-256-2504"))
                .document(new Document("478-256-2504"))
                .promotionNotificationsAllowed(false)
                .loyaltyPoints(LoyaltyPoints.ZERO)
                .address(Address.builder()
                        .street("Bourbon Street")
                        .number("1140")
                        .neighborhood("North Ville")
                        .city("Springfield")
                        .state("Oregon")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 204")
                        .build()
                );
    }

    public static Customer.ExistingCustomerBuild existingAnonymizedCustomer() {
        return Customer.existing()
                .id(new CustomerId())
                .fullName(new FullName("Anonymous", "Anonymous"))
                .birthDate(null)
                .email(new Email("anonymous@anonymous.com"))
                .phone(new Phone("478-256-2504"))
                .document(new Document("478-256-2504"))
                .promotionNotificationsAllowed(false)
                .archived(true)
                .registeredAt(OffsetDateTime.now())
                .archivedAt(OffsetDateTime.now())
                .loyaltyPoints(LoyaltyPoints.ZERO)
                .address(Address.builder()
                        .street("Bourbon Street")
                        .number("1140")
                        .neighborhood("North Ville")
                        .city("Springfield")
                        .state("Oregon")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 204")
                        .build()
                );
    }
}
