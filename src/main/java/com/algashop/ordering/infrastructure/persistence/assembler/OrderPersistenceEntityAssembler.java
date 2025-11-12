package com.algashop.ordering.infrastructure.persistence.assembler;

import com.algashop.ordering.domain.model.entity.Order;
import com.algashop.ordering.domain.model.valueobject.Address;
import com.algashop.ordering.domain.model.valueobject.Billing;
import com.algashop.ordering.domain.model.valueobject.Recipient;
import com.algashop.ordering.domain.model.valueobject.Shipping;
import com.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceEntityAssembler {

    public OrderPersistenceEntity fromDomain(Order order) {
        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        orderPersistenceEntity.setId(order.id().value().toLong());
        orderPersistenceEntity.setCustomerId(order.customerId().value());
        orderPersistenceEntity.setTotalAmount(order.totalAmount().value());
        orderPersistenceEntity.setTotalItems(order.totalItems().value());
        orderPersistenceEntity.setStatus(order.status().name());
        orderPersistenceEntity.setPaymentMethod(order.paymentMethod().name());
        orderPersistenceEntity.setPlacedAt(order.placedAt());
        orderPersistenceEntity.setPaidAt(order.paidAt());
        orderPersistenceEntity.setReadyAt(order.readyAt());
        orderPersistenceEntity.setVersion(order.version());
        orderPersistenceEntity.setBilling(billingEmbeddable(order.billing()));
        orderPersistenceEntity.setShipping(shippingEmbeddable(order.shipping()));
        return orderPersistenceEntity;
    }

    private BillingEmbeddable billingEmbeddable(Billing billing) {
        if (billing == null) return null;

        return BillingEmbeddable
                .builder()
                .firstName(billing.fullName().firstName())
                .lastName(billing.fullName().lastName())
                .document(billing.document().value())
                .phone(billing.phone().value())
                .address(addressEmbeddable(billing.address()))
                .build();
    }

    private ShippingEmbeddable shippingEmbeddable(Shipping shipping) {
        if (shipping == null) return null;

        return ShippingEmbeddable
                .builder()
                .cost(shipping.cost().value())
                .expectedDate(shipping.expectedDate())
                .address(addressEmbeddable(shipping.address()))
                .recipient(recipientEmbeddable(shipping.recipient()))
                .build();
    }

    private AddressEmbeddable addressEmbeddable(Address address) {
        if (address == null) return null;

        return AddressEmbeddable
                .builder()
                .street(address.street())
                .number(address.number())
                .complement(address.complement())
                .neighborhood(address.neighborhood())
                .city(address.city())
                .state(address.state())
                .zipCode(address.zipCode().value())
                .build();
    }

    private RecipientEmbeddable recipientEmbeddable(Recipient recipient) {
        if (recipient == null) return null;

        return RecipientEmbeddable
                .builder()
                .firstName(recipient.fullName().firstName())
                .lastName(recipient.fullName().lastName())
                .document(recipient.document().value())
                .phone(recipient.phone().value())
                .build();
    }


}
