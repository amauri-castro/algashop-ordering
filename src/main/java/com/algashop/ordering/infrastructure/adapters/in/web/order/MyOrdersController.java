package com.algashop.ordering.infrastructure.adapters.in.web.order;

import com.algashop.ordering.core.application.checkout.BuyNowApplicationService;
import com.algashop.ordering.core.application.checkout.CheckoutApplicationService;
import com.algashop.ordering.core.application.security.SecurityChecks;
import com.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algashop.ordering.core.ports.in.checkout.BuyNowInput;
import com.algashop.ordering.core.ports.in.checkout.CheckoutInput;
import com.algashop.ordering.core.ports.in.order.ForQueryingOrders;
import com.algashop.ordering.core.ports.in.order.OrderFilter;
import com.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.algashop.ordering.core.ports.out.order.OrderSummaryOutput;
import com.algashop.ordering.infrastructure.adapters.in.web.PageModel;
import com.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.UnprocessableEntityException;
import com.algashop.ordering.infrastructure.config.security.SecurityAnnotations;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/customers/me/orders")
@RequiredArgsConstructor
public class MyOrdersController {

    private final ForQueryingOrders forQueryingOrders;
    private final CheckoutApplicationService checkoutApplicationService;
    private final BuyNowApplicationService buyNowApplicationService;
    private final SecurityChecks securityChecks;

    @SecurityAnnotations.CanReadMyOrders
    @GetMapping("/{orderId}")
    public OrderDetailOutput findById(@PathVariable String orderId) {
        return forQueryingOrders.findByIdAndCustomerId(orderId, securityChecks.getAuthenticatedUserId());
    }

    @SecurityAnnotations.CanReadMyOrders
    @GetMapping
    public PageModel<OrderSummaryOutput> filter(OrderFilter filter) {
        filter.setCustomerId(securityChecks.getAuthenticatedUserId());
        return PageModel.of(forQueryingOrders.filter(filter));
    }

    @SecurityAnnotations.CanWriteMyOrders
    @PostMapping(consumes = "application/vnd.order-with-product.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailOutput createWithProduct(@RequestBody @Valid BuyNowInput input) {
        input.setCustomerId(securityChecks.getAuthenticatedUserId());
        String orderId;
        try {
            orderId = buyNowApplicationService.buyNow(input);
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
        return forQueryingOrders.findById(orderId);
    }

    @SecurityAnnotations.CanWriteMyOrders
    @PostMapping(consumes = "application/vnd.order-with-shopping-cart.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailOutput createWithShoppingCart(@RequestBody @Valid CheckoutInput input) {
        input.setCustomerId(securityChecks.getAuthenticatedUserId());
        String orderId;
        try {
            orderId = checkoutApplicationService.checkout(input);
        } catch (CustomerNotFoundException | ShoppingCartNotFoundException e) {
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
        return forQueryingOrders.findById(orderId);
    }
}
