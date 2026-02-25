package com.algashop.ordering.core.application.customer.management;

import com.algashop.ordering.core.application.AbstractApplicationIT;
import com.algashop.ordering.core.application.customer.CustomersManagementApplicationService;
import com.algashop.ordering.core.ports.out.customer.ForNotifyingCustomers;
import com.algashop.ordering.core.ports.in.customer.CustomerOutput;
import com.algashop.ordering.core.ports.in.customer.ForQueryingCustomers;
import com.algashop.ordering.core.domain.model.customer.*;
import com.algashop.ordering.core.ports.in.customer.CustomerInput;
import com.algashop.ordering.core.ports.in.customer.CustomerUpdateInput;
import com.algashop.ordering.infrastructure.adapters.in.listener.customer.CustomerEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDate;
import java.util.UUID;


class CustomerManagementApplicationServiceIT extends AbstractApplicationIT {

    @Autowired
    private CustomersManagementApplicationService customerManagementApplicationService;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoSpyBean
    private ForNotifyingCustomers customerNotificationApplicationService;

    @Autowired
    private ForQueryingCustomers queryService;

    @Test
    public void shouldRegister() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = queryService.findById(customerId);

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getEmail,
                        CustomerOutput::getBirthDate
                ).containsExactly(
                        customerId,
                        "John",
                        "Doe",
                        "johndoe@gmail.com",
                        LocalDate.of(1991, 7, 5)
                );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();

        Mockito.verify(customerEventListener)
                .listen(Mockito.any(CustomerRegisteredEvent.class));

        Mockito.verify(customerEventListener, Mockito.never())
                .listen(Mockito.any(CustomerArchivedEvent.class));

        Mockito.verify(customerNotificationApplicationService)
                .notifyNewRegistration(Mockito.any(ForNotifyingCustomers.NotifyNewRegistrationInput.class));
    }

    @Test
    public void shouldUpdate() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        CustomerUpdateInput updateInput = CustomerUpdateInputTestDataBuilder.aCustomerUpdate().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId, updateInput);

        CustomerOutput customerOutput = queryService.findById(customerId);

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getEmail,
                        CustomerOutput::getBirthDate
                ).containsExactly(
                        customerId,
                        "Matt",
                        "Damon",
                        "johndoe@gmail.com",
                        LocalDate.of(1991, 7, 5)
                );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

    @Test
    public void shouldArchive() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        CustomerOutput archivedCustomer = queryService.findById(customerId);

        Assertions.assertThat(archivedCustomer)
                .isNotNull()
                .extracting(
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getPhone,
                        CustomerOutput::getDocument,
                        CustomerOutput::getBirthDate,
                        CustomerOutput::getPromotionNotificationsAllowed
                ).containsExactly(
                        "Anonymous",
                        "Anonymous",
                        "000-000-0000",
                        "000-00-0000",
                        null,
                        false
                );

        Assertions.assertThat(archivedCustomer.getEmail()).endsWith("@anonymous.com");
        Assertions.assertThat(archivedCustomer.getArchived()).isTrue();
        Assertions.assertThat(archivedCustomer.getArchivedAt()).isNotNull();

        Assertions.assertThat(archivedCustomer.getAddress()).isNotNull();
        Assertions.assertThat(archivedCustomer.getAddress().getNumber()).isNotNull().isEqualTo("Anonymized");
        Assertions.assertThat(archivedCustomer.getAddress().getComplement()).isNull();
    }

    @Test
    public void shouldThrowCustomerNotFoundExceptionWhenArchivingNonExistingCustomer() {
        UUID nonExistingId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerManagementApplicationService.archive(nonExistingId));
    }

    @Test
    public void shouldThrowCustomerArchivedExceptionWhenArchivingAlreadyArchivedCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customerManagementApplicationService.archive(customerId));
    }


    @Test
    public void shouldChangeEmail() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.changeEmail(customerId, "testemail@gmail.com");

        CustomerOutput changedCustomer = queryService.findById(customerId);

        Assertions.assertThat(changedCustomer.getEmail()).isEqualTo("testemail@gmail.com");
    }

    @Test
    public void shouldThrowCustomerNotFoundExceptionWhenChangeEmailOfNonExistingCustomer() {
        UUID nonExistingId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerManagementApplicationService
                        .changeEmail(nonExistingId, "testemail@gmail.com"));
    }

    @Test
    public void shouldThrowCustomerArchivedExceptionWhenChangeEmailOfArchivedCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customerManagementApplicationService
                        .changeEmail(customerId, "testemail@gmail.com"));
    }

    @Test
    public void shouldTrownIllegalArgumentExceptionWhenInvalidEmail() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> customerManagementApplicationService
                                .changeEmail(customerId, "testemail.com")
                        );

    }

    @Test
    public void shouldThrowCustomerEmailIsInUseExceptionWhenReuseEmail() {
        CustomerInput input1 = CustomerInputTestDataBuilder.aCustomer().build();
        CustomerInput input2 = CustomerInputTestDataBuilder.aCustomer()
                .email("jonhcena@gmail.com")
                .build();

        UUID customerId1 = customerManagementApplicationService.create(input1);
        UUID customerId2 = customerManagementApplicationService.create(input2);

        Assertions.assertThat(customerId1).isNotNull();
        Assertions.assertThat(customerId2).isNotNull();

        Assertions.assertThatExceptionOfType(CustomerEmailIsInUseException.class)
                        .isThrownBy(() -> customerManagementApplicationService
                                .changeEmail(customerId2, "johndoe@gmail.com")
                        );

    }


}