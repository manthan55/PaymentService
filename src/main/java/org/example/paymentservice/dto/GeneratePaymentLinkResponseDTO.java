package org.example.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GeneratePaymentLinkResponseDTO {
    private String orderId;
    private String paymentLink;
}
