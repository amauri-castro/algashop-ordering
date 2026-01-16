package com.algashop.ordering.application.customer.query;

import com.algashop.ordering.application.commons.AddressData;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerOutputTestDataBuilder {

 public static CustomerOutput.CustomerOutputBuilder existing() {
  return CustomerOutput.builder()
    .id(UUID.randomUUID())
    .registeredAt(OffsetDateTime.now())
    .phone("11211244545")
    .email("johndoe@email.com")
    .firstName("John")
    .lastName("Doe")
    .birthDate(LocalDate.of(1991, 7, 5))
    .document("12345")
    .promotionNotificationsAllowed(false)
    .loyaltyPoints(0)
    .archived(false)
    .address(AddressData.builder()
      .street("Bourbon Street")
      .number("2500")
      .complement("apt 201")
      .neighborhood("North Ville")
      .city("Yostfort")
      .state("South Carolina")
      .zipCode("13232")
      .build());
 }

}