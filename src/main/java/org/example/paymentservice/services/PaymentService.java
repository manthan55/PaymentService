package org.example.paymentservice.services;

import org.example.paymentservice.exceptions.InvalidPaymentGatewayException;
import org.example.paymentservice.exceptions.PaymentLinkGenerationException;
import org.example.paymentservice.models.PGVendor;
import org.example.paymentservice.paymentgateway.IPaymentGateway;
import org.example.paymentservice.strategies.PaymentGatewaySelectorStrategy;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private PaymentGatewaySelectorStrategy paymentGatewaySelectorStrategy;

    public PaymentService(PaymentGatewaySelectorStrategy paymentGatewaySelectorStrategy) {
        this.paymentGatewaySelectorStrategy = paymentGatewaySelectorStrategy;
    }

    public String generatePaymentLink(String orderId, PGVendor pgVendor) throws PaymentLinkGenerationException, InvalidPaymentGatewayException {
        // Ideally we will fetch the order details ike customer email, phone & amount from OrderService
        // but as we don't have any OrderService, we'll hardcode these values here
        // https://razorpay.com/docs/api/payments/payment-links/create-standard/
        //  Must be in the smallest unit of the currency. For example, if you want to receive a payment of ₹300.00, you must enter the value 30000.
        Long amount = 1000L;

        IPaymentGateway paymentGateway = paymentGatewaySelectorStrategy.getPaymentGateway(pgVendor);
        String paymentLink = paymentGateway.generatePaymentLink(orderId, amount);
        return paymentLink;
    }
}
