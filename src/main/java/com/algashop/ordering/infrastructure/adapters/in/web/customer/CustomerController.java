package com.algashop.ordering.infrastructure.adapters.in.web.customer;

import com.algashop.ordering.core.ports.in.customer.*;
import com.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.algashop.ordering.infrastructure.adapters.in.web.PageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanReadCustomers;
import static com.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanReadShoppingCarts;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final ForManagingCustomers forManagingCustomers;
    private final ForQueryingCustomers forQueryingCustomers;
    private final ForQueryingShoppingCarts forQueryingShoppingCarts;

    @GetMapping
    @CanReadCustomers
    public PageModel<CustomerSummaryOutput> findAll(CustomerFilter customerFilter) {
        return PageModel.of(forQueryingCustomers.filter(customerFilter));
    }

    @GetMapping("/{customerId}")
    @CanReadCustomers
    public CustomerOutput findById(@PathVariable UUID customerId) {
        return forQueryingCustomers.findById(customerId);
    }

    @GetMapping("/{customerId}/shopping-cart")
    @CanReadShoppingCarts
    public ShoppingCartOutput findShoppingCartByCustomerId(@PathVariable UUID customerId) {
        return forQueryingShoppingCarts.findByCustomerId(customerId);
    }

}
