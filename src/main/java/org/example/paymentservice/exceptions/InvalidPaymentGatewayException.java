package org.example.paymentservice.exceptions;

public class InvalidPaymentGatewayException extends Exception {
    public InvalidPaymentGatewayException(String message) {
        super(message);
    }
}
