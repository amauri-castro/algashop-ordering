package com.algashop.ordering.infrastructure.persistence.disassembler;

import com.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algashop.ordering.domain.model.valueobject.Money;
import com.algashop.ordering.domain.model.valueobject.ProductName;
import com.algashop.ordering.domain.model.valueobject.Quantity;
import com.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import com.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartPersistenceEntityDisassembler {

    public ShoppingCart toDomainEntity(ShoppingCartPersistenceEntity persistenceEntity) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(persistenceEntity.getId()))
                .customerId(new CustomerId(persistenceEntity.getCustomerId()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(persistenceEntity.getTotalItems()))
                .createdAt(persistenceEntity.getCreatedAt())
                .items(toDomainEntityItems(persistenceEntity.getItems()))
                .version(persistenceEntity.getVersion())
                .build();
    }

    private Set<ShoppingCartItem> toDomainEntityItems(Set<ShoppingCartItemPersistenceEntity> source) {
        return source.stream()
                .map(this::toDomainEntityItem)
                .collect(Collectors.toSet());
    }

    private ShoppingCartItem toDomainEntityItem(ShoppingCartItemPersistenceEntity source) {

        return ShoppingCartItem.existing()
                .id(new ShoppingCartItemId(source.getId()))
                .shoppingCartId(new ShoppingCartId(source.getShoppingCartId()))
                .productId(new ProductId(source.getProductId()))
                .productName(new ProductName(source.getName()))
                .price(new Money(source.getPrice()))
                .quantity(new Quantity(source.getQuantity()))
                .available(source.getAvailable())
                .totalAmount(new Money(source.getTotalAmount()))
                .build();
    }
}
