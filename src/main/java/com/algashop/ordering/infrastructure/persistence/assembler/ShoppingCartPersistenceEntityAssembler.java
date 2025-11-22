package com.algashop.ordering.infrastructure.persistence.assembler;

import com.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceEntityAssembler {

    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
        return merge(new ShoppingCartPersistenceEntity(), shoppingCart);
    }

    public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity persistenceEntity,
                                               ShoppingCart shoppingCart) {
        persistenceEntity.setId(shoppingCart.id().value());
        var customerPersistence = customerPersistenceEntityRepository
                .getReferenceById(shoppingCart.customerId().value());
        persistenceEntity.setCustomer(customerPersistence);
        persistenceEntity.setTotalAmount(shoppingCart.totalAmount().value());
        persistenceEntity.setTotalItems(shoppingCart.totalItems().value());
        persistenceEntity.setCreatedAt(shoppingCart.createdAt());
        Set<ShoppingCartItemPersistenceEntity> mergedItems = mergeItems(shoppingCart, persistenceEntity);
        persistenceEntity.replaceItems(mergedItems);
        return persistenceEntity;
    }

    private Set<ShoppingCartItemPersistenceEntity> mergeItems(ShoppingCart shoppingCart,
                                                              ShoppingCartPersistenceEntity shoppingCartPersistence) {
        Set<ShoppingCartItem> newOrUpdatedItems = shoppingCart.items();
        if (newOrUpdatedItems == null || newOrUpdatedItems.isEmpty()) {
            return new HashSet<>();
        }

        Set<ShoppingCartItemPersistenceEntity> existingItems = shoppingCartPersistence.getItems();
        if (existingItems == null || existingItems.isEmpty()) {
            return newOrUpdatedItems.stream()
                    .map(this::fromDomain)
                    .collect(Collectors.toSet());
        }

        Map<UUID, ShoppingCartItemPersistenceEntity> existingItemMap = existingItems.stream()
                .collect(Collectors.toMap(ShoppingCartItemPersistenceEntity::getId, item -> item));

        return newOrUpdatedItems.stream()
                .map(shoppingCartItem -> {
                    ShoppingCartItemPersistenceEntity itemPersistence = existingItemMap.getOrDefault(
                            shoppingCartItem.id().value(),
                            new ShoppingCartItemPersistenceEntity()
                    );
                    return mergeItem(itemPersistence, shoppingCartItem);
                })
                .collect(Collectors.toSet());
    }

    public ShoppingCartItemPersistenceEntity fromDomain(ShoppingCartItem shoppingCartItem) {
        return mergeItem(new ShoppingCartItemPersistenceEntity(), shoppingCartItem);
    }

    private ShoppingCartItemPersistenceEntity mergeItem(ShoppingCartItemPersistenceEntity persistenceEntity,
                                                       ShoppingCartItem shoppingCartItem) {

        persistenceEntity.setId(shoppingCartItem.id().value());
        persistenceEntity.setProductId(shoppingCartItem.productId().value());
        persistenceEntity.setName(shoppingCartItem.name().value());
        persistenceEntity.setPrice(shoppingCartItem.price().value());
        persistenceEntity.setQuantity(shoppingCartItem.quantity().value());
        persistenceEntity.setAvailable(shoppingCartItem.isAvailable());
        persistenceEntity.setTotalAmount(shoppingCartItem.totalAmount().value());
        return persistenceEntity;
    }


}
