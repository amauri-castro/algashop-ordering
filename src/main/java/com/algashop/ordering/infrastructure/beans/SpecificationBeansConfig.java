package com.algashop.ordering.infrastructure.beans;

import com.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.algashop.ordering.domain.model.order.CustomerHaveFreeShippingSpecification;
import com.algashop.ordering.domain.model.order.Orders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpecificationBeansConfig {

    @Bean
    public CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification(Orders orders) {
        return new CustomerHaveFreeShippingSpecification(
                orders,
                new LoyaltyPoints(200),
                2L,
                new LoyaltyPoints(2000)
        );
    }
}
