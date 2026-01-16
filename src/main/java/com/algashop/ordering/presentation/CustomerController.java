package com.algashop.ordering.presentation;

import com.algashop.ordering.application.customer.management.CustomerInput;
import com.algashop.ordering.application.customer.query.CustomerOutput;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOutput create(@RequestBody CustomerInput input) {
        return CustomerOutput.builder()
                .id(UUID.randomUUID())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .phone(input.getPhone())
                .email(input.getEmail())
                .birthDate(input.getBirthDate())
                .document(input.getDocument())
                .promotionNotificationsAllowed(input.getPromotionNotificationsAllowed())
                .registeredAt(OffsetDateTime.now())
                .archived(false)
                .loyaltyPoints(0)
                .address(input.getAddress())
                .build();
    }
}
