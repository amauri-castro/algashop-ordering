package com.algashop.ordering.contract.base;

import com.algashop.ordering.core.application.checkout.BuyNowApplicationService;
import com.algashop.ordering.core.application.checkout.CheckoutApplicationService;
import com.algashop.ordering.core.application.order.OrderDetailOutputTestDataBuilder;
import com.algashop.ordering.core.application.order.OrderSummaryOutputTestDataBuilder;
import com.algashop.ordering.core.domain.model.order.OrderNotFoundException;
import com.algashop.ordering.core.ports.in.checkout.BuyNowInput;
import com.algashop.ordering.core.ports.in.checkout.CheckoutInput;
import com.algashop.ordering.core.ports.in.order.ForQueryingOrders;
import com.algashop.ordering.core.ports.in.order.OrderFilter;
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

@WebMvcTest(controllers = OrderController.class)
public class OrderBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ForQueryingOrders orderQueryService;

    @MockitoBean
    private BuyNowApplicationService buyNowApplicationService;

    @MockitoBean
    private CheckoutApplicationService checkoutApplicationService;

    public static final String validOrderId = "01226N0640J7Q";

    public static final String notFoundOrderId = "01226N0693HDH";

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

        Mockito.when(orderQueryService.findById(validOrderId))
                .thenReturn(OrderDetailOutputTestDataBuilder.placedOrder(validOrderId).build());

        Mockito.when(orderQueryService.findById(notFoundOrderId))
                .thenThrow(new OrderNotFoundException());

        Mockito.when(orderQueryService.filter(Mockito.any(OrderFilter.class)))
                .thenReturn(new PageImpl<>(
                        List.of(OrderSummaryOutputTestDataBuilder.placedOrder().id(validOrderId).build())
                ));
    }
}
