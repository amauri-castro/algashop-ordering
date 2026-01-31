package com.algashop.ordering.presentation.shoppingcart;

import com.algashop.ordering.application.shoppingcart.management.ShoppingCartItemInput;
import com.algashop.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import com.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartManagementApplicationService shoppingCartManagementApplicationService;
    private final ShoppingCartQueryService shoppingCartQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartOutput create(@RequestBody @Valid ShoppingCartInput input) {
        UUID shoppingCartId = shoppingCartManagementApplicationService.createNew(input.getCustomerId());
        return shoppingCartQueryService.findById(shoppingCartId);
    }

    @GetMapping("/{shoppingCartId}")
    public ShoppingCartOutput findById(@PathVariable UUID shoppingCartId) {
        return shoppingCartQueryService.findById(shoppingCartId);
    }

    @GetMapping("/{shoppingCartId}/items")
    public ShoppingCartItemListModel findItems(@PathVariable UUID shoppingCartId) {
        ShoppingCartOutput shoppingCartOutput = shoppingCartQueryService.findById(shoppingCartId);
        ShoppingCartItemListModel shoppingCartItemListModel = new ShoppingCartItemListModel();
        shoppingCartItemListModel.setItems(shoppingCartOutput.getItems());
        return shoppingCartItemListModel;
    }

    @DeleteMapping("/{shoppingCartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID shoppingCartId) {
        shoppingCartManagementApplicationService.delete(shoppingCartId);
    }

    @DeleteMapping("/{shoppingCartId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItems(@PathVariable UUID shoppingCartId) {
        shoppingCartManagementApplicationService.empty(shoppingCartId);
    }

    @PostMapping("/{shoppingCartId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addItem(@PathVariable UUID shoppingCartId,
                        @RequestBody @Valid ShoppingCartItemInput itemInput) {
        shoppingCartManagementApplicationService.addItem(itemInput);
    }

    @DeleteMapping("/{shoppingCartId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable UUID shoppingCartId, @PathVariable UUID itemId) {
        shoppingCartManagementApplicationService.removeItem(shoppingCartId, itemId);
    }
}
