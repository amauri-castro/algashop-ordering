package com.algashop.ordering.infrastructure.persistence.disassembler;

import com.algashop.ordering.domain.model.entity.Order;
import com.algashop.ordering.domain.model.entity.OrderStatus;
import com.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algashop.ordering.domain.model.valueobject.*;
import com.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

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
                .billing(billing(persistenceEntity.getBilling()))
                .shipping(shipping(persistenceEntity.getShipping()))
                .build();
    }

    private Billing billing(BillingEmbeddable billingEmbeddable) {
        if (billingEmbeddable == null) return null;

        return Billing.builder()
                .fullName(new FullName(billingEmbeddable.getFirstName(), billingEmbeddable.getLastName()))
                .document(new Document(billingEmbeddable.getDocument()))
                .phone(new Phone(billingEmbeddable.getPhone()))
                .address(address(billingEmbeddable.getAddress()))
                .build();
    }

    private Shipping shipping(ShippingEmbeddable shippingEmbeddable) {
        if (shippingEmbeddable == null) return null;

        return Shipping.builder()
                .cost(new Money(shippingEmbeddable.getCost()))
                .expectedDate(shippingEmbeddable.getExpectedDate())
                .recipient(recipient(shippingEmbeddable.getRecipient()))
                .address(address(shippingEmbeddable.getAddress()))
                .build();
    }

    private Address address(AddressEmbeddable addressEmbeddable) {
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

    private Recipient recipient(RecipientEmbeddable recipientEmbeddable) {
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
