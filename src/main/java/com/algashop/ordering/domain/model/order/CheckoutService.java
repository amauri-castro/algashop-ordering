package com.algashop.ordering.domain.model.order;

import com.algashop.ordering.domain.model.DomainService;
import com.algashop.ordering.domain.model.commons.Money;
import com.algashop.ordering.domain.model.customer.Customer;
import com.algashop.ordering.domain.model.product.Product;
import com.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algashop.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@DomainService
@RequiredArgsConstructor
public class CheckoutService {

    private final CustomerHaveFreeShippingSpecification haveFreeShippingSpecification;

    public Order checkout(Customer customer,
                          ShoppingCart shoppingCart,
                          Billing billing,
                          Shipping shipping,
                          PaymentMethod paymentMethod) {

        if (shoppingCart.isEmpty()) {
            throw new ShoppingCartCantProceedToCheckoutException(shoppingCart.id());
        }

        if (shoppingCart.containsUnavailableItems()) {
            throw new ShoppingCartCantProceedToCheckoutException(shoppingCart.id());
        }

        Set<ShoppingCartItem> items = shoppingCart.items();

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);

        if (haveFreeShipping(customer)) {
            Shipping freeShipping = shipping.toBuilder().cost(Money.ZERO).build();
            order.changeShipping(freeShipping);
        } else {
            order.changeShipping(shipping);
        }

        order.changePaymentMethod(paymentMethod);

        for (ShoppingCartItem item : items) {
            order.addItem(new Product(item.productId(), item.name(),
                    item.price(), item.isAvailable()), item.quantity());
        }

        order.place();
        shoppingCart.empty();

        return order;
    }

    private boolean haveFreeShipping(Customer customer) {
        return haveFreeShippingSpecification.isSatisfiedBy(customer);
    }
}
