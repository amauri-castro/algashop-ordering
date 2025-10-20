package com.algashop.ordering.domain.model.entity;

import com.algashop.ordering.domain.model.exception.OrderCannotBeEditedException;
import com.algashop.ordering.domain.model.exception.OrderDoesNotContainOrderItemExeption;
import com.algashop.ordering.domain.model.valueobject.Money;
import com.algashop.ordering.domain.model.valueobject.Quantity;
import com.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderRemoveItemTest {

    @Test
    void givenDraftOrder_whenRemoveItem_shouldRecalculate() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProductAltIphone().build(),
                new Quantity(2)
        );

        OrderItem orderItem1 = order.items().iterator().next();

        order.addItem(
                ProductTestDataBuilder.aProductMacbook().build(),
                new Quantity(3)
        );

        order.removeItem(orderItem1.id());

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("3897.00")),
                (i) -> Assertions.assertThat(i.totalItems()).isEqualTo(new Quantity(3))
        );
    }

    @Test
    void givenDraftOrder_whenTryToRemoveNoExistingItem_shouldGenerateException() {
        Order order = OrderTestDataBuilder.anOrder().build();

        Assertions.assertThatExceptionOfType(OrderDoesNotContainOrderItemExeption.class)
                .isThrownBy(()-> order.removeItem(new OrderItemId()));

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("3407")),
                (i) -> Assertions.assertThat(i.totalItems()).isEqualTo(new Quantity(3))
        );
    }

    @Test
    void givenPlacedOrder_whenTryToRemoveItem_shouldGenerateException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(()->order.removeItem(new OrderItemId()));

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("3407.00")),
                (i) -> Assertions.assertThat(i.totalItems()).isEqualTo(new Quantity(3))
        );
    }
}
