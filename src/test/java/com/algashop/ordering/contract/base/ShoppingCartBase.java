package com.algashop.ordering.contract.base;

import com.algashop.ordering.core.application.security.SecurityChecks;
import com.algashop.ordering.core.application.shoppingcart.ShoppingCartManagementApplicationService;
import com.algashop.ordering.core.application.shoppingcart.ShoppingCartOutputTestDataBuilder;
import com.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algashop.ordering.infrastructure.adapters.in.web.shoppingcart.MyShoppingCartController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebMvcTest(controllers = MyShoppingCartController.class)
public class ShoppingCartBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ShoppingCartManagementApplicationService managementService;
    @MockitoBean
    private ForQueryingShoppingCarts queryService;
    @MockitoBean
    private SecurityChecks securityChecks;

    public static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");
    public static final UUID validShoppingCartId = UUID.fromString("ad265aa3-c77d-46e9-9782-b70c487c1e17");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context)
                        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                        .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        Mockito.when(securityChecks.getAuthenticatedUserId())
                        .thenReturn(validCustomerId);

        Mockito.when(queryService.findByCustomerId(validCustomerId))
                        .thenReturn(ShoppingCartOutputTestDataBuilder.aShoppingCart()
                                .id(validShoppingCartId)
                                .customerId(validCustomerId)
                                .build());

        Mockito.when(managementService.createNew(Mockito.any(UUID.class)))
                .thenReturn(validShoppingCartId);

    }
}