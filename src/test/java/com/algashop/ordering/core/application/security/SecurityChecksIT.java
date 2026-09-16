package com.algashop.ordering.core.application.security;

import com.algashop.ordering.core.application.AbstractApplicationIT;
import com.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.algashop.ordering.utils.WithMockJwt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class SecurityChecksIT extends AbstractApplicationIT {

    @Autowired
    private SecurityChecks securityChecks;

    @Test
    @WithMockJwt
    void givenAuthenticadedCustomerShouldAllowOrderForHimself() {
        UUID customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID.value();
        boolean canOrderFor = securityChecks.canOrderFor(customerId);

        Assertions.assertThat(canOrderFor).isTrue();

    }

    @Test
    @WithMockJwt(role = "", audiences = "machine-client-id", subject = "machine-client-id")
    void givenAuthenticadedMachineShouldNotAllowOrder() {
        UUID customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID.value();
        boolean canOrderFor = securityChecks.canOrderFor(customerId);
        Assertions.assertThat(canOrderFor).isFalse();

    }

    @Test
    @WithMockJwt(role = "", audiences = "machine-client-id", subject = "machine-client-id")
    void givenAuthenticadedMachineShouldReturnTrue() {
        boolean isMachineAuthenticaded = securityChecks.isMachineAuthenticated();
        Assertions.assertThat(isMachineAuthenticaded).isTrue();
    }


}