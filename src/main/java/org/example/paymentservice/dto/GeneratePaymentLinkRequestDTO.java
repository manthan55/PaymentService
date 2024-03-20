package org.example.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.paymentservice.models.PGVendor;

@Getter
@Setter
public class GeneratePaymentLinkRequestDTO {
    private String orderId;
    private PGVendor pgVendor;
}
