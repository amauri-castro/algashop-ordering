package com.algashop.ordering.infrastructure.adapters.out.web.shipping.client.rapidex;

import com.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import com.algashop.ordering.core.domain.model.commons.Money;
import com.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.BadGatewayException;
import com.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.GatewayTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.net.SocketTimeoutException;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "algashop.integrations.shipping.provider", havingValue = "RAPIDEX")
public class ShippingCostServiceRapidexImpl implements ShippingCostService {

    private final RapidexAPIClient rapidexAPIClient;

    @Override
    public CalculationResult calculate(CalculationRequest request) {
        DeliveryCostResponse response;
        try {
            response = rapidexAPIClient.calculate(
                    new DeliveryCostRequest(
                            request.origin().value(),
                            request.destination().value()
                    )
            );
        } catch (ResourceAccessException e) {
            throw new GatewayTimeoutException("Rapidex API Timeout", e);
        } catch (RestClientException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                throw new GatewayTimeoutException("Rapidex API Timeout", e);
            }
            throw new BadGatewayException("Rapidex API Bad Gateway", e);
        }

        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(response.getEstimatedDaysToDeliver());

        return CalculationResult.builder()
                .cost(new Money(response.getDeliveryCost()))
                .expectedDate(expectedDeliveryDate)
                .build();
    }
}
