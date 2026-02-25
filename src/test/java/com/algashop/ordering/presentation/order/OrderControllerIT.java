package com.algashop.ordering.presentation.order;

import com.algashop.ordering.core.application.checkout.BuyNowInput;
import com.algashop.ordering.core.application.checkout.BuyNowInputTestDataBuilder;
import com.algashop.ordering.core.application.order.query.OrderDetailOutput;
import com.algashop.ordering.core.domain.model.order.OrderId;
import com.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityRepository;
import com.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityRepository;
import com.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart.ShoppingCartPersistenceEntityRepository;
import com.algashop.ordering.presentation.AbstractPresentationIT;
import com.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;


public class OrderControllerIT extends AbstractPresentationIT {

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
    public void shouldCreateOrderUsingProduct() {

        String json = AlgaShopResourceUtils.readContent("json/create-order-with-product.json");
        String createdOrderId = RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType("application/vnd.order-with-product.v1+json")
                    .body(json)
                .when()
                    .post("/api/v1/orders")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", Matchers.not(Matchers.emptyString()),
                            "customer.id", Matchers.is(validCustomerId.toString())
                    )
                .extract().jsonPath().getString("id");

        boolean orderExists = orderRepository.existsById(new OrderId(createdOrderId).value().toLong());

        Assertions.assertThat(orderExists).isTrue();
    }

    @Test
    public void shouldCreateOrderUsingProduct_DTO() {
        UUID creditCardId = UUID.randomUUID();
        BuyNowInput input = BuyNowInputTestDataBuilder.aBuyNowInput()
                .productId(validProductId)
                .customerId(validCustomerId)
                .creditCardId(creditCardId)
                .build();

        OrderDetailOutput orderDetailOutput = RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType("application/vnd.order-with-product.v1+json")
                    .body(input)
                .when()
                    .post("/api/v1/orders")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", Matchers.not(Matchers.emptyString()),
                            "customer.id", Matchers.is(validCustomerId.toString())
                    )
                .extract()
                    .body().as(OrderDetailOutput.class);

        Assertions.assertThat(orderDetailOutput.getCustomer().getId()).isEqualTo(validCustomerId);
        Assertions.assertThat(orderDetailOutput.getCreditCardId()).isEqualTo(creditCardId);

        boolean orderExists = orderRepository.existsById(new OrderId(orderDetailOutput.getId()).value().toLong());

        Assertions.assertThat(orderExists).isTrue();
    }

    @Test
    public void shouldNotCreateOrderUsingProductWhenProductNotExists() {
        String json = AlgaShopResourceUtils.readContent("json/create-order-with-invalid-product.json");

        RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType("application/vnd.order-with-product.v1+json")
                    .body(json)
                .when()
                    .post("/api/v1/orders")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void shouldNotCreateOrderUsingProductWhenCustomerWasNotFound() {

        String json = AlgaShopResourceUtils.readContent("json/create-order-with-product-and-invalid-customer.json");
        RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType("application/vnd.order-with-product.v1+json")
                    .body(json)
                .when()
                    .post("/api/v1/orders")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void shouldCreateOrderUsingShoppingCart() {

        String json = AlgaShopResourceUtils.readContent("json/create-order-with-shoppingcart.json");

        OrderDetailOutput orderDetailOutput = RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType("application/vnd.order-with-shopping-cart.v1+json")
                    .body(json)
                .when()
                    .post("/api/v1/orders")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", Matchers.not(Matchers.emptyString()),
                            "customer.id", Matchers.is(validCustomerId.toString())
                    )
                .extract().body().as(OrderDetailOutput.class);

        Assertions.assertThat(orderDetailOutput.getCustomer().getId()).isEqualTo(validCustomerId);

        boolean orderExists = orderRepository.existsById(new OrderId(orderDetailOutput.getId()).value().toLong());

        Assertions.assertThat(orderExists).isTrue();
    }

    @Test
    public void shouldNotCreateOrderUsingInexistentShoppingCart() {

        String json = AlgaShopResourceUtils
                .readContent("json/create-order-with-invalid-shoppingcart.json");

        RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType("application/vnd.order-with-shopping-cart.v1+json")
                    .body(json)
                .when()
                    .post("/api/v1/orders")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                    .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
