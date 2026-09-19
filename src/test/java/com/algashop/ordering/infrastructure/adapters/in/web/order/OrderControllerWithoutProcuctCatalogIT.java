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
                    .post("/api/v1/customers/me/orders")
                .then()
                .assertThat()
                    .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                    .statusCode(HttpStatus.GATEWAY_TIMEOUT.value());
    }
}
