package com.algashop.ordering.core.ports.in.order;

import com.algashop.ordering.core.ports.in.commons.AddressData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingData {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String document;
    @NotBlank
    private String phone;
    @NotBlank
    private String email;
    @Valid
    @NotNull
    private AddressData address;
}
