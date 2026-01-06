package com.algashop.ordering.application.service.customer.management;

import com.algashop.ordering.application.commons.AddressData;
import com.algashop.ordering.application.customer.management.CustomerInput;

import java.time.LocalDate;

public class CustomerInputTestDataBuilder {

    public static CustomerInput.CustomerInputBuilder aCustomer() {
        return CustomerInput.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1991, 7, 5))
                .document("255-08-0578")
                .phone("478-256-2604")
                .email("johndoe@gmail.com")
                .promotionNotificationsAllowed(false)
                .address(AddressData.builder()
                        .street("Bourbon Street")
                        .number("1200")
                        .complement("Apt. 901")
                        .neighborhood("North Ville")
                        .city("Yostfort")
                        .state("South Carolina")
                        .zipCode("70283")
                        .build());
    }
}
