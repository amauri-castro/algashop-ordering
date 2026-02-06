package com.algashop.ordering.infrastructure.persistence.order;

import com.algashop.ordering.domain.model.commons.*;
import com.algashop.ordering.domain.model.order.*;
import com.algashop.ordering.domain.model.product.ProductName;
import com.algashop.ordering.domain.model.customer.CustomerId;
import com.algashop.ordering.domain.model.order.OrderId;
import com.algashop.ordering.domain.model.order.OrderItemId;
import com.algashop.ordering.domain.model.product.ProductId;
import com.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainEntity(OrderPersistenceEntity persistenceEntity) {
        return Order.existing()
                .id(new OrderId(persistenceEntity.getId()))
                .customerId(new CustomerId(persistenceEntity.getCustomerId()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(persistenceEntity.getTotalItems()))
                .status(OrderStatus.valueOf(persistenceEntity.getStatus()))
                .paymentMethod(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()))
                .placedAt(persistenceEntity.getPlacedAt())
                .paidAt(persistenceEntity.getPaidAt())
                .canceledAt(persistenceEntity.getCanceledAt())
                .readyAt(persistenceEntity.getReadyAt())
                .items(new HashSet<>())
                .version(persistenceEntity.getVersion())
                //.billing(toBillingValueObject(persistenceEntity.getBilling()))
                //.shipping(toShippingValueObject(persistenceEntity.getShipping()))
                .items(toOrderItemSet(persistenceEntity.getItems()))
                .build();
    }

    private Set<OrderItem> toOrderItemSet(Set<OrderItemPersistenceEntity> orderItemPersistenceEntities) {
        return orderItemPersistenceEntities.stream()
                .map(orderItem -> OrderItem.existing()
                        .id(new OrderItemId(orderItem.getId()))
                        .orderId(new OrderId(orderItem.getOrderId()))
                        .productId(new ProductId(orderItem.getProductId()))
                        .productName(new ProductName(orderItem.getProductName()))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(new Quantity(orderItem.getQuantity()))
                        .totalAmount(new Money(orderItem.getTotalAmount()))
                        .build()
                )
                .collect(Collectors.toSet());
    }

    private Billing toBillingValueObject(BillingEmbeddable billingEmbeddable) {
        if (billingEmbeddable == null) return null;

        return Billing.builder()
                .fullName(new FullName(billingEmbeddable.getFirstName(), billingEmbeddable.getLastName()))
                .document(new Document(billingEmbeddable.getDocument()))
                .phone(new Phone(billingEmbeddable.getPhone()))
                .address(toAddressValueObject(billingEmbeddable.getAddress()))
                .build();
    }

    private Shipping toShippingValueObject(ShippingEmbeddable shippingEmbeddable) {
        if (shippingEmbeddable == null) return null;

        return Shipping.builder()
                .cost(new Money(shippingEmbeddable.getCost()))
                .expectedDate(shippingEmbeddable.getExpectedDate())
                .recipient(toRecipientValueObject(shippingEmbeddable.getRecipient()))
                .address(toAddressValueObject(shippingEmbeddable.getAddress()))
                .build();
    }

    private Address toAddressValueObject(AddressEmbeddable addressEmbeddable) {
        if (addressEmbeddable == null) return null;

        return Address.builder()
                .street(addressEmbeddable.getStreet())
                .number(addressEmbeddable.getNumber())
                .complement(addressEmbeddable.getComplement())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .city(addressEmbeddable.getCity())
                .state(addressEmbeddable.getState())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .build();
    }

    private Recipient toRecipientValueObject(RecipientEmbeddable recipientEmbeddable) {
        if (recipientEmbeddable == null) return null;

        return Recipient.builder()
                .fullName(new FullName(
                        recipientEmbeddable.getFirstName(),
                        recipientEmbeddable.getLastName()
                ))
                .document(new Document(recipientEmbeddable.getDocument()))
                .phone(new Phone(recipientEmbeddable.getPhone()))
                .build();
    }
}
