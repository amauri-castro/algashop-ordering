package com.algashop.ordering.domain.model.service;

import com.algashop.ordering.domain.model.entity.Order;
import com.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algashop.ordering.domain.model.exception.ShoppingCartCantProceedToCheckoutException;
import com.algashop.ordering.domain.model.utility.DomainService;
import com.algashop.ordering.domain.model.valueobject.Billing;
import com.algashop.ordering.domain.model.valueobject.Product;
import com.algashop.ordering.domain.model.valueobject.Shipping;

import java.util.Set;

@DomainService
public class CheckoutService {

    public Order checkout(ShoppingCart shoppingCart,
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
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);

        for (ShoppingCartItem item : items) {
            order.addItem(new Product(item.productId(), item.name(),
                    item.price(), item.isAvailable()), item.quantity());
        }

        order.place();
        shoppingCart.empty();

        return order;
    }
}
