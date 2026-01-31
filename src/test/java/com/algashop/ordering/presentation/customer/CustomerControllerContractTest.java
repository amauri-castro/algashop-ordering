package com.algashop.ordering.presentation.customer;

import com.algashop.ordering.application.commons.AddressData;
import com.algashop.ordering.application.customer.management.CustomerInput;
import com.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.algashop.ordering.application.customer.management.CustomerUpdateInput;
import com.algashop.ordering.application.customer.query.*;
import com.algashop.ordering.domain.model.DomainException;
import com.algashop.ordering.domain.model.customer.CustomerEmailIsInUseException;
import com.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerContractTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoBean
    private CustomerQueryService customerQueryService;

    @BeforeEach
    public void setupAll() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void createCustomerContract() {
        CustomerOutput customerOutput = CustomerOutputTestDataBuilder.existing().build();

        UUID customerId = UUID.randomUUID();

        Mockito.when(customerManagementApplicationService.create(Mockito.any(CustomerInput.class)))
                .thenReturn(customerId);
        Mockito.when(customerQueryService.findById(Mockito.any(UUID.class)))
                .thenReturn(customerOutput);
        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "johndoe@email.com",
                  "document": "12345",
                  "phone": "11211244545",
                  "birthDate": "1991-07-05",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2500",
                    "complement": "apt 201",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "13232"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.containsString("/api/v1/customers/" + customerId))
                .body(
                        "id", Matchers.notNullValue(),
                        "registeredAt", Matchers.notNullValue(),
                        "firstName", Matchers.is("John"),
                        "lastName", Matchers.is("Doe"),
                        "email", Matchers.is("johndoe@email.com"),
                        "document", Matchers.is("12345"),
                        "phone", Matchers.is("11211244545"),
                        "birthDate", Matchers.is("1991-07-05"),
                        "promotionNotificationsAllowed", Matchers.is(false),
                        "loyaltyPoints", Matchers.is(0),
                        "address.street", Matchers.is("Bourbon Street"),
                        "address.number", Matchers.is("2500"),
                        "address.complement", Matchers.is("apt 201"),
                        "address.neighborhood", Matchers.is("North Ville"),
                        "address.city", Matchers.is("Yostfort"),
                        "address.state", Matchers.is("South Carolina"),
                        "address.zipCode", Matchers.is("13232")
                );
    }

    @Test
    public void createCustomerError400Contract() {

        String jsonInput = """
                {
                  "firstName": "",
                  "lastName": "",
                  "email": "johndoe@email.com",
                  "document": "12345",
                  "phone": "11211244545",
                  "birthDate": "1991-07-05",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2500",
                    "complement": "apt 201",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "13232"
                  }
                }
                """;

        RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "status", Matchers.is(HttpStatus.BAD_REQUEST.value()),
                        "type", Matchers.is("/errors/invalid-fields"),
                        "title", Matchers.notNullValue(),
                        "detail", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue(),
                        "fields", Matchers.notNullValue()
                );

    }

    @Test
    public void findCustomersContract() {
        int sizeLimit = 5;
        int pageNumber = 0;

        CustomerSummaryOutput customer1 = CustomerSummaryOutputTestDataBuilder.existing().build();
        CustomerSummaryOutput customer2 = CustomerSummaryOutputTestDataBuilder.existingAlt1().build();

        Mockito.when(customerQueryService.filter(Mockito.any(CustomerFilter.class)))
                .thenReturn(new PageImpl<>(List.of(customer1, customer2)));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("size", sizeLimit)
                .queryParam("page", pageNumber)
                .when()
                .get("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.OK.value())
                .body(
                        "number", Matchers.equalTo(pageNumber),
                        "size", Matchers.equalTo(2),
                        "totalPages", Matchers.equalTo(1),
                        "totalElements", Matchers.equalTo(2),

                        "content[0].id", Matchers.equalTo(customer1.getId().toString()),
                        "content[0].firstName", Matchers.equalTo(customer1.getFirstName()),
                        "content[0].lastName", Matchers.equalTo(customer1.getLastName()),
                        "content[0].email", Matchers.equalTo(customer1.getEmail()),
                        "content[0].document", Matchers.equalTo(customer1.getDocument()),
                        "content[0].phone", Matchers.equalTo(customer1.getPhone()),
                        "content[0].birthDate", Matchers.equalTo(customer1.getBirthDate().toString()),
                        "content[0].loyaltyPoints", Matchers.equalTo(customer1.getLoyaltyPoints()),
                        "content[0].promotionNotificationsAllowed", Matchers.equalTo(customer1.getPromotionNotificationsAllowed()),
                        "content[0].archived", Matchers.is(customer1.getArchived()),
                        "content[0].registeredAt", Matchers.is(formatter.format(customer1.getRegisteredAt())),


                        "content[1].id", Matchers.equalTo(customer2.getId().toString()),
                        "content[1].firstName", Matchers.equalTo(customer2.getFirstName()),
                        "content[1].lastName", Matchers.equalTo(customer2.getLastName()),
                        "content[1].email", Matchers.equalTo(customer2.getEmail()),
                        "content[1].document", Matchers.equalTo(customer2.getDocument()),
                        "content[1].phone", Matchers.equalTo(customer2.getPhone()),
                        "content[1].birthDate", Matchers.equalTo(customer2.getBirthDate().toString()),
                        "content[1].loyaltyPoints", Matchers.equalTo(customer2.getLoyaltyPoints()),
                        "content[1].promotionNotificationsAllowed", Matchers.equalTo(customer2.getPromotionNotificationsAllowed()),
                        "content[1].archived", Matchers.is(customer2.getArchived()),
                        "content[1].registeredAt", Matchers.is(formatter.format(customer2.getRegisteredAt()))

                );

    }

    @Test
    public void findByIdContract() {
        CustomerOutput customer = CustomerOutputTestDataBuilder.existing().build();

        Mockito.when(customerQueryService.findById(customer.getId())).thenReturn(customer);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        AddressData address = customer.getAddress();

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/v1/customers/{customers}", customer.getId())
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.OK.value())
                .body(
                        "id", Matchers.equalTo(customer.getId().toString()),
                        "firstName", Matchers.equalTo(customer.getFirstName()),
                        "lastName", Matchers.is(customer.getLastName()),
                        "email", Matchers.is(customer.getEmail()),
                        "document", Matchers.is(customer.getDocument()),
                        "phone", Matchers.is(customer.getPhone()),
                        "birthDate", Matchers.is(customer.getBirthDate().toString()),
                        "loyaltyPoints", Matchers.is(customer.getLoyaltyPoints()),
                        "promotionNotificationsAllowed", Matchers.is(customer.getPromotionNotificationsAllowed()),
                        "archived", Matchers.is(customer.getArchived()),
                        "registeredAt", Matchers.is(formatter.format(customer.getRegisteredAt())),
                        "address.street", Matchers.is(address.getStreet()),
                        "address.number", Matchers.is(address.getNumber()),
                        "address.complement", Matchers.is(address.getComplement()),
                        "address.neighborhood", Matchers.is(address.getNeighborhood()),
                        "address.city", Matchers.is(address.getCity()),
                        "address.state", Matchers.is(address.getState()),
                        "address.zipCode", Matchers.is(address.getZipCode())

                );
    }

    @Test
    public void findByIdError404Contract() {
        UUID invalidCustomerId = UUID.randomUUID();

        Mockito.when(customerQueryService.findById(invalidCustomerId))
                .thenThrow(CustomerNotFoundException.class);

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/v1/customers/{customers}", invalidCustomerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(
                        "status", Matchers.is(HttpStatus.NOT_FOUND.value()),
                        "type", Matchers.is("/errors/not-found"),
                        "title", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue()
                );


    }

    @Test
    public void createCustomerError409Contract() {
        Mockito.when(customerManagementApplicationService.create(Mockito.any(CustomerInput.class)))
                .thenThrow(CustomerEmailIsInUseException.class);

        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "johndoe@email.com",
                  "document": "12345",
                  "phone": "11211244545",
                  "birthDate": "1991-07-05",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2500",
                    "complement": "apt 201",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "13232"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.CONFLICT.value())
                .body(
                        "status", Matchers.is(HttpStatus.CONFLICT.value()),
                        "type", Matchers.is("/errors/conflict"),
                        "title", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue()
                );
    }

    @Test
    public void createCustomerError422Contract() {
        Mockito.when(customerManagementApplicationService.create(Mockito.any(CustomerInput.class)))
                .thenThrow(DomainException.class);

        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "johndoe@email.com",
                  "document": "12345",
                  "phone": "11211244545",
                  "birthDate": "1991-07-05",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2500",
                    "complement": "apt 201",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "13232"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(
                        "status", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                        "type", Matchers.is("/errors/unprocessable-entity"),
                        "title", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue()
                );

    }

    @Test
    public void createCustomerError500Contract() {
        Mockito.when(customerManagementApplicationService.create(Mockito.any(CustomerInput.class)))
                .thenThrow(RuntimeException.class);

        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "johndoe@email.com",
                  "document": "12345",
                  "phone": "11211244545",
                  "birthDate": "1991-07-05",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2500",
                    "complement": "apt 201",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "13232"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(
                        "status", Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                        "type", Matchers.is("/errors/internal"),
                        "title", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue()
                );

    }

    @Test
    public void updateCustomerContract() {
        CustomerOutput customer = CustomerOutputTestDataBuilder.existing().build();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        AddressData address = customer.getAddress();

        UUID customerId = UUID.randomUUID();
        Mockito.when(customerQueryService.findById(Mockito.any(UUID.class)))
                .thenReturn(customer);

        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "johndoe@email.com",
                  "document": "12345",
                  "phone": "1191234564",
                  "birthDate": "1991-07-05",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/v1/customers/{customerId}", customerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.OK.value())
                .body(
                        "id", Matchers.equalTo(customer.getId().toString()),
                        "firstName", Matchers.equalTo(customer.getFirstName()),
                        "lastName", Matchers.is(customer.getLastName()),
                        "email", Matchers.is(customer.getEmail()),
                        "document", Matchers.is(customer.getDocument()),
                        "phone", Matchers.is(customer.getPhone()),
                        "birthDate", Matchers.is(customer.getBirthDate().toString()),
                        "loyaltyPoints", Matchers.is(customer.getLoyaltyPoints()),
                        "promotionNotificationsAllowed", Matchers.is(customer.getPromotionNotificationsAllowed()),
                        "archived", Matchers.is(customer.getArchived()),
                        "registeredAt", Matchers.is(formatter.format(customer.getRegisteredAt())),
                        "address.street", Matchers.is(address.getStreet()),
                        "address.number", Matchers.is(address.getNumber()),
                        "address.complement", Matchers.is(address.getComplement()),
                        "address.neighborhood", Matchers.is(address.getNeighborhood()),
                        "address.city", Matchers.is(address.getCity()),
                        "address.state", Matchers.is(address.getState()),
                        "address.zipCode", Matchers.is(address.getZipCode())
                );
    }

    @Test
    public void updateCustomerError400Contract() {

        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "phone": "",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/v1/customers/{customerId}", UUID.randomUUID())
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "status", Matchers.is(HttpStatus.BAD_REQUEST.value()),
                        "type", Matchers.is("/errors/invalid-fields"),
                        "title", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue()
                );
    }

    @Test
    public void updateCustomerError404Contract() {

        UUID invalidCustomerId = UUID.randomUUID();

        Mockito.doThrow(CustomerNotFoundException.class)
                .when(customerManagementApplicationService)
                .update(Mockito.any(UUID.class), Mockito.any(CustomerUpdateInput.class));

        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "phone": "87989778787",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/v1/customers/{customerId}", invalidCustomerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(
                        "status", Matchers.is(HttpStatus.NOT_FOUND.value()),
                        "type", Matchers.is("/errors/not-found"),
                        "title", Matchers.is(HttpStatus.NOT_FOUND.getReasonPhrase()),
                        "instance", Matchers.notNullValue()
                );

        Mockito.verify(customerQueryService, Mockito.never())
                .findById(Mockito.any());
    }

    @Test
    public void updateCustomerError409Contract() {

        Mockito.doThrow(CustomerEmailIsInUseException.class)
                .when(customerManagementApplicationService)
                .update(Mockito.any(UUID.class), Mockito.any(CustomerUpdateInput.class));

        UUID customerId = UUID.randomUUID();
        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "phone": "87989778787",
                  "email": "jonhdoe@email.com",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/v1/customers/{customerId}", customerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.CONFLICT.value())
                .body(
                        "status", Matchers.is(HttpStatus.CONFLICT.value()),
                        "type", Matchers.is("/errors/conflict"),
                        "title", Matchers.is(HttpStatus.CONFLICT.getReasonPhrase()),
                        "instance", Matchers.notNullValue()
                );
    }

    @Test
    public void updateCustomerError422Contract() {

        Mockito.doThrow(DomainException.class)
                .when(customerManagementApplicationService)
                .update(Mockito.any(UUID.class), Mockito.any(CustomerUpdateInput.class));

        UUID customerId = UUID.randomUUID();
        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "phone": "87989778787",
                  "email": "jonhdoe@email.com",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/v1/customers/{customerId}", customerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(
                        "status", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                        "type", Matchers.is("/errors/unprocessable-entity"),
                        "title", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()),
                        "instance", Matchers.notNullValue()
                );
    }

    @Test
    public void deleteCustomerContract() {
        Mockito.doNothing().when(customerManagementApplicationService)
                .archive(Mockito.any(UUID.class));

        UUID customerId = UUID.randomUUID();
        RestAssuredMockMvc
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/v1/customers/{customerId}", customerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deleteCustomer404Contract() {
        UUID invalidCustomerId = UUID.randomUUID();

        Mockito.doThrow(CustomerNotFoundException.class)
                .when(customerManagementApplicationService)
                .archive(Mockito.any(UUID.class));

        RestAssuredMockMvc
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/v1/customers/{customerId}", invalidCustomerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(
                        "status", Matchers.is(HttpStatus.NOT_FOUND.value()),
                        "type", Matchers.is("/errors/not-found"),
                        "title", Matchers.is(HttpStatus.NOT_FOUND.getReasonPhrase()),
                        "instance", Matchers.notNullValue()
                );
    }

    @Test
    public void deleteCustomer422Contract() {
        UUID customerId = UUID.randomUUID();

        Mockito.doThrow(DomainException.class)
                .when(customerManagementApplicationService)
                .archive(Mockito.any(UUID.class));

        RestAssuredMockMvc
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/v1/customers/{customerId}", customerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(
                        "status", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                        "type", Matchers.is("/errors/unprocessable-entity"),
                        "title", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()),
                        "instance", Matchers.notNullValue()
                );
    }

    @Test
    public void deleteCustomer500Contract() {
        UUID customerId = UUID.randomUUID();

        Mockito.doThrow(RuntimeException.class)
                .when(customerManagementApplicationService)
                .archive(Mockito.any(UUID.class));

        RestAssuredMockMvc
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/v1/customers/{customerId}", customerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(
                        "status", Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                        "type", Matchers.is("/errors/internal"),
                        "title", Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),
                        "instance", Matchers.notNullValue()
                );
    }

}