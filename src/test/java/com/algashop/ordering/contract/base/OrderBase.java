package com.algashop.ordering.contract.base;

import com.algashop.ordering.core.application.checkout.BuyNowApplicationService;
import com.algashop.ordering.core.application.checkout.CheckoutApplicationService;
import com.algashop.ordering.core.application.order.OrderDetailOutputTestDataBuilder;
import com.algashop.ordering.core.application.order.OrderSummaryOutputTestDataBuilder;
import com.algashop.ordering.core.application.security.SecurityChecks;
import com.algashop.ordering.core.domain.model.order.OrderNotFoundException;
import com.algashop.ordering.core.ports.in.checkout.BuyNowInput;
import com.algashop.ordering.core.ports.in.checkout.CheckoutInput;
import com.algashop.ordering.core.ports.in.order.ForQueryingOrders;
import com.algashop.ordering.core.ports.in.order.OrderFilter;
import com.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.algashop.ordering.infrastructure.adapters.in.web.order.MyOrdersController;
import com.algashop.ordering.infrastructure.adapters.in.web.order.OrderController;
import com.algashop.ordering.infrastructure.adapters.out.persistence.order.ForObtainingOrdersJpaRepositoryImpl;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = {OrderController.class, MyOrdersController.class})
public class OrderBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ForQueryingOrders orderQueryService;

    @MockitoBean
    private BuyNowApplicationService buyNowApplicationService;

    @MockitoBean
    private CheckoutApplicationService checkoutApplicationService;

    @MockitoBean
    private ForQueryingShoppingCarts forQueryingShoppingCarts;

    @MockitoBean
    private SecurityChecks securityChecks;

    public static final String validOrderId = "01226N0640J7Q";
    public static final String notFoundOrderId = "01226N0693HDH";
    public static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");
    public static final UUID validShoppingCartId = UUID.fromString("4f31582a-66e6-4601-a9d3-ff608c2d4461");


    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context)
                        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                        .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        Mockito.when(buyNowApplicationService.buyNow(Mockito.any(BuyNowInput.class)))
                .thenReturn(validOrderId);

        Mockito.when(checkoutApplicationService.checkout(Mockito.any(CheckoutInput.class)))
                .thenReturn(validOrderId);

        Mockito.when(securityChecks.getAuthenticatedUserId())
                        .thenReturn(validCustomerId);

        Mockito.when(forQueryingShoppingCarts.findByCustomerId(validCustomerId))
                        .thenReturn(ShoppingCartOutput.builder()
                                .id(validShoppingCartId)
                                .customerId(validCustomerId)
                                .build());

        Mockito.when(orderQueryService.findById(validOrderId))
                .thenReturn(OrderDetailOutputTestDataBuilder.placedOrder(validOrderId).build());

        Mockito.when(orderQueryService.findByIdAndCustomerId(validOrderId, validCustomerId))
                        .thenReturn(OrderDetailOutputTestDataBuilder
                                .placedOrder(validOrderId)
                                .build());

        Mockito.when(orderQueryService.findById(notFoundOrderId))
                .thenThrow(new OrderNotFoundException());

        Mockito.when(orderQueryService.filter(Mockito.any(OrderFilter.class)))
                .thenReturn(new PageImpl<>(
                        List.of(OrderSummaryOutputTestDataBuilder.placedOrder().id(validOrderId).build())
                ));
    }
}
