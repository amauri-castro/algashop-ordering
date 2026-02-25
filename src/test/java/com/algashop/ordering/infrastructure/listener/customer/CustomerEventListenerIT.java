package com.algashop.ordering.infrastructure.listener.customer;

import com.algashop.ordering.core.application.AbstractApplicationIT;
import com.algashop.ordering.core.ports.in.customer.ForAddingLoyaltyPoints;
import com.algashop.ordering.core.ports.out.customer.ForNotifyingCustomers;
import com.algashop.ordering.core.domain.model.commons.Email;
import com.algashop.ordering.core.domain.model.commons.FullName;
import com.algashop.ordering.core.domain.model.customer.CustomerId;
import com.algashop.ordering.core.domain.model.customer.CustomerRegisteredEvent;
import com.algashop.ordering.core.domain.model.order.OrderId;
import com.algashop.ordering.core.domain.model.order.OrderReadyEvent;
import com.algashop.ordering.infrastructure.adapters.in.listener.customer.CustomerEventListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.OffsetDateTime;
import java.util.UUID;

@SpringBootTest
class CustomerEventListenerIT extends AbstractApplicationIT {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoBean
    private ForAddingLoyaltyPoints loyaltyPointsApplicationService;

    @MockitoBean
    private ForNotifyingCustomers notificationApplicationService;

    @Test
    public void shouldListenOrderReadyEvent() {
        applicationEventPublisher.publishEvent(
                new OrderReadyEvent(
                        new OrderId(),
                        new CustomerId(),
                        OffsetDateTime.now()
                )
        );

        Mockito.verify(customerEventListener).listen(Mockito.any(OrderReadyEvent.class));

        Mockito.verify(loyaltyPointsApplicationService).addLoyaltyPoints(
                Mockito.any(UUID.class),
                Mockito.any(String.class)
        );
    }

    @Test
    public void shouldListenCustomerRegisteredEvent() {
        applicationEventPublisher.publishEvent(
                new CustomerRegisteredEvent(
                        new CustomerId(),
                        OffsetDateTime.now(),
                        new FullName("John", "Doe"),
                        new Email("john.doe@email.com")
                )
        );

        Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));

        Mockito.verify(notificationApplicationService).notifyNewRegistration(
                Mockito.any(ForNotifyingCustomers.NotifyNewRegistrationInput.class)
        );
    }
}