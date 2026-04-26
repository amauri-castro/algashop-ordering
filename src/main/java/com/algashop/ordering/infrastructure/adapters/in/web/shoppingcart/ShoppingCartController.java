package com.algashop.ordering.infrastructure.adapters.in.web.shoppingcart;

import com.algashop.ordering.core.ports.in.shoppingcart.ForManagingShoppingCarts;
import com.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartItemInput;
import com.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.UnprocessableEntityException;
import com.algashop.ordering.infrastructure.config.config.SecurityAnnotations;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.algashop.ordering.infrastructure.config.config.SecurityAnnotations.*;

@RestController
@RequestMapping("/api/v1/shopping-carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ForManagingShoppingCarts forManagingShoppingCarts;
    private final ForQueryingShoppingCarts forQueryingShoppingCarts;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CanWriteShoppingCarts
    public ShoppingCartOutput create(@RequestBody @Valid ShoppingCartInput input) {
        UUID shoppingCartId;
        try {
            shoppingCartId = forManagingShoppingCarts.createNew(input.getCustomerId());
        } catch (CustomerNotFoundException e) {
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
        return forQueryingShoppingCarts.findById(shoppingCartId);
    }

    @GetMapping("/{shoppingCartId}")
    @CanReadShoppingCarts
    public ShoppingCartOutput findById(@PathVariable UUID shoppingCartId) {
        return forQueryingShoppingCarts.findById(shoppingCartId);
    }

    @GetMapping("/{shoppingCartId}/items")
    @CanReadShoppingCarts
    public ShoppingCartItemListModel findItems(@PathVariable UUID shoppingCartId) {
        var items= forQueryingShoppingCarts.findById(shoppingCartId).getItems();
        return new ShoppingCartItemListModel(items);
    }

    @DeleteMapping("/{shoppingCartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CanWriteShoppingCarts
    public void delete(@PathVariable UUID shoppingCartId) {
        forManagingShoppingCarts.delete(shoppingCartId);
    }

    @DeleteMapping("/{shoppingCartId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CanWriteShoppingCarts
    public void deleteItems(@PathVariable UUID shoppingCartId) {
        forManagingShoppingCarts.empty(shoppingCartId);
    }

    @PostMapping("/{shoppingCartId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CanWriteShoppingCarts
    public void addItem(@PathVariable UUID shoppingCartId,
                        @RequestBody @Valid ShoppingCartItemInput itemInput) {
        itemInput.setShoppingCartId(shoppingCartId);
        try {
            forManagingShoppingCarts.addItem(itemInput);
        } catch (ProductNotFoundException e) {
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{shoppingCartId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CanWriteShoppingCarts
    public void deleteItem(@PathVariable UUID shoppingCartId, @PathVariable UUID itemId) {
        forManagingShoppingCarts.removeItem(shoppingCartId, itemId);
    }
}
