package com.algashop.ordering.domain.model.customer;

import com.algashop.ordering.domain.model.commons.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class CustomerRegistrationServiceIT {

    @Autowired
    private CustomerRegistrationService customerRegistrationService;

    @Test
    public void shouldRegister() {

        Customer customer = customerRegistrationService.register(
                new FullName("Jhon", "Doe"),
                new BirthDate(LocalDate.of(1992, 8, 12)),
                new Email("john.doe@gmail.com"),
                new Phone("478-256-2504"),
                new Document("478-256-2504"),
                true,
                Address.builder()
                        .street("Bourbon Street")
                        .number("1140")
                        .neighborhood("North Ville")
                        .city("Springfield")
                        .state("Oregon")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 204")
                        .build()
        );

        Assertions.assertThat(customer.fullName()).isEqualTo(new FullName("Jhon", "Doe"));
        Assertions.assertThat(customer.email()).isEqualTo(new Email("john.doe@gmail.com"));
    }
}