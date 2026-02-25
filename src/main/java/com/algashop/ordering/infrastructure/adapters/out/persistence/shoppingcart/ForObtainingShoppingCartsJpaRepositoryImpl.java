package com.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.algashop.ordering.core.application.utility.Mapper;
import com.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algashop.ordering.core.ports.out.shoppingcart.ForObtainingShoppingCarts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ForObtainingShoppingCartsJpaRepositoryImpl implements ForObtainingShoppingCarts {

    private final ShoppingCartPersistenceEntityRepository repository;
    private final Mapper mapper;

    @Override
    public ShoppingCartOutput findById(UUID shoppingCartId) {
        return repository.findById(shoppingCartId)
                .map(s -> mapper.convert(s, ShoppingCartOutput.class))
                .orElseThrow(() -> new ShoppingCartNotFoundException());
    }

    @Override
    public ShoppingCartOutput findByCustomerId(UUID customerId) {
        return repository.findByCustomer_Id(customerId)
                .map(s -> mapper.convert(s, ShoppingCartOutput.class))
                .orElseThrow(() -> new ShoppingCartNotFoundException());
    }
}
