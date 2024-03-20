package org.example.paymentservice.paymentgateway;

import org.example.paymentservice.exceptions.PaymentLinkGenerationException;

public interface IPaymentGateway {
    String generatePaymentLink(String orderId, Long amount) throws PaymentLinkGenerationException;
}
