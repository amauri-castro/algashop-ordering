package com.algashop.ordering.domain.model.service;

import com.algashop.ordering.domain.model.entity.Order;
import com.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algashop.ordering.domain.model.utility.DomainService;
import com.algashop.ordering.domain.model.valueobject.Billing;
import com.algashop.ordering.domain.model.valueobject.Product;
import com.algashop.ordering.domain.model.valueobject.Quantity;
import com.algashop.ordering.domain.model.valueobject.Shipping;
import com.algashop.ordering.domain.model.valueobject.id.CustomerId;

@DomainService
public class BuyNowService {

    public Order buyNow(Product product,
                        CustomerId customerId,
                        Billing billing,
                        Shipping shipping,
                        Quantity quantity,
                        PaymentMethod paymentMethod) {

        product.checkOutOfStock();

        Order order = Order.draft(customerId);
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);

        order.addItem(product, quantity);
        order.place();

        return order;
    }
}
