package com.algashop.ordering.application.service.customer.management;

import com.algashop.ordering.application.commons.AddressData;
import com.algashop.ordering.application.customer.management.CustomerUpdateInput;

public class CustomerUpdateInputTestDataBuilder {

    public static CustomerUpdateInput.CustomerUpdateInputBuilder aCustomerUpdate() {
        return CustomerUpdateInput.builder()
                .firstName("Matt")
                .lastName("Damon")
                .phone("123-321-1112")
                .promotionNotificationsAllowed(true)
                .address(AddressData.builder()
                        .street("Amphitheatre Parkway")
                        .number("1600")
                        .complement("")
                        .neighborhood("Mountain View")
                        .city("Mountain View")
                        .state("California")
                        .zipCode("94043")
                        .build());
    }
}
