package com.algashop.ordering.application.checkout;

import com.algashop.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingInput {

    private RecipientData recipient;
    private AddressData address;
}
