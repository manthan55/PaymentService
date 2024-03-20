package org.example.paymentservice.exceptions;

public class PaymentLinkGenerationException extends Exception {
    public PaymentLinkGenerationException(Exception ex) {
        super(ex);
    }
}
