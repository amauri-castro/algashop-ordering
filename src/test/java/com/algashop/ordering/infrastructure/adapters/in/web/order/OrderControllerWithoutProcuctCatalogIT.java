package com.algashop.ordering.infrastructure.adapters.in.web.order;

import com.algashop.ordering.infrastructure.adapters.in.web.AbstractPresentationIT;
import com.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityRepository;
import com.algashop.ordering.infrastructure.adapters.out.persistence.order.OrderPersistenceEntityRepository;
import com.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart.ShoppingCartPersistenceEntityRepository;
import com.algashop.ordering.utils.AlgaShopResourceUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

@TestPropertySource(properties = {
        "algashop.integrations.product-catalog.url=http://localhost:9999"
})
public class OrderControllerWithoutProcuctCatalogIT extends AbstractPresentationIT {

    private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");
    private static final UUID validProductId = UUID.fromString("019be330-5c35-7ef8-b59b-0cf73765a296");
    private static final UUID validShoppingCartId = UUID.fromString("4f31582a-66e6-4601-a9d3-ff608c2d4461");

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    @Autowired
    private OrderPersistenceEntityRepository orderRepository;

    @Autowired
    private ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository;

    @BeforeEach
    public void setup() {
        super.beforeEach();
    }

    @BeforeAll
    public static void setupBeforeAll() {
        AbstractPresentationIT.initWireMock();
    }

    @AfterAll
    public static void afterAll() {
        AbstractPresentationIT.stopMock();
    }

    @Test
    public void shouldNotCreateOrderUsingProductWhenProductAPIIsUnavailable() {

        String json = AlgaShopResourceUtils.readContent("json/create-order-with-product.json");
        givenAuthenticated()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType("application/vnd.order-with-product.v1+json")
                    .body(json)
                .when()
                    .post("/api/v1/orders")
                .then()
                .assertThat()
                    .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                    .statusCode(HttpStatus.GATEWAY_TIMEOUT.value());
    }
}
