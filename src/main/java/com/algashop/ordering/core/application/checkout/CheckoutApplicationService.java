package com.algashop.ordering.core.application.checkout;

import com.algashop.ordering.core.application.order.BillingInputDisassembler;
import com.algashop.ordering.core.application.order.ShippingInputDisassembler;
import com.algashop.ordering.core.application.security.SecurityCheckApplicationService;
import com.algashop.ordering.core.domain.model.DomainException;
import com.algashop.ordering.core.domain.model.commons.ZipCode;
import com.algashop.ordering.core.domain.model.customer.Customer;
import com.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.algashop.ordering.core.domain.model.customer.Customers;
import com.algashop.ordering.core.domain.model.order.*;
import com.algashop.ordering.core.domain.model.order.shipping.OriginAddressService;
import com.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import com.algashop.ordering.core.domain.model.product.ProductCatalogService;
import com.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartId;
import com.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algashop.ordering.core.domain.model.shoppingcart.ShoppingCarts;
import com.algashop.ordering.core.ports.in.checkout.CheckoutInput;
import com.algashop.ordering.core.ports.in.checkout.ForBuyingWithShoppingCart;
import com.algashop.ordering.core.ports.in.order.ShippingInput;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutApplicationService implements ForBuyingWithShoppingCart {

    private final Orders orders;
    private final ShoppingCarts shoppingCarts;
    private final Customers customers;
    private final CheckoutService checkoutService;

    private final BillingInputDisassembler billingInputDisassembler;
    private final ShippingInputDisassembler shippingInputDisassembler;

    private final ShippingCostService shippingCostService;
    private final OriginAddressService originAddressService;
    private final ProductCatalogService productCatalogService;

    private final SecurityCheckApplicationService securityCheck;

    @Transactional
    @Override
    public String checkout(CheckoutInput input) {
        Objects.requireNonNull(input);
        PaymentMethod paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());
        CreditCardId creditCardId = null;

        if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            if (input.getCreditCardId() == null) {
                throw new DomainException("Credit card id is required");
            }
            creditCardId = new CreditCardId(input.getCreditCardId());
        }
        ShoppingCartId shoppingCartId = new ShoppingCartId(input.getShoppingCartId());
        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId.value()));

        verifyCanOrderFor(shoppingCart.customerId().value());

        Customer customer = customers.ofId(shoppingCart.customerId()).orElseThrow(() -> new CustomerNotFoundException());

        var calculationResult = calculateShippingCost(input.getShipping());

        Billing billing = billingInputDisassembler.toDomainModel(input.getBilling());
        Shipping shipping = shippingInputDisassembler.toDomainModel(input.getShipping(), calculationResult);

        Order order = checkoutService.checkout(
                customer,
                shoppingCart,
                billing,
                shipping,
                paymentMethod,
                creditCardId
        );

        orders.add(order);
        shoppingCarts.add(shoppingCart);

        return order.id().toString();
    }

    private ShippingCostService.CalculationResult calculateShippingCost(ShippingInput shipping) {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode(shipping.getAddress().getZipCode());

        return shippingCostService.calculate(
                new ShippingCostService.CalculationRequest(origin, destination)
        );
    }

    private void verifyCanOrderFor(UUID customerId) {
        if (!(securityCheck.isCustomer() && securityCheck.getAuthenticatedUserId().equals(customerId))) {
            throw new AccessDeniedException("Cannot order for customer " + customerId);
        }
    }
}
