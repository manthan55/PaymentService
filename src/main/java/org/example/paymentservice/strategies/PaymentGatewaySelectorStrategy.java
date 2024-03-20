package org.example.paymentservice.strategies;

import org.example.paymentservice.exceptions.InvalidPaymentGatewayException;
import org.example.paymentservice.models.PGVendor;
import org.example.paymentservice.paymentgateway.IPaymentGateway;
import org.example.paymentservice.paymentgateway.RazorpayPaymentGateway;
import org.example.paymentservice.paymentgateway.StripePaymentGateway;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewaySelectorStrategy {
    private final RazorpayPaymentGateway razorpayPaymentGateway;
    private final StripePaymentGateway stripePaymentGateway;

    public PaymentGatewaySelectorStrategy(RazorpayPaymentGateway razorpayPaymentGateway, StripePaymentGateway stripePaymentGateway) {
        this.razorpayPaymentGateway = razorpayPaymentGateway;
        this.stripePaymentGateway = stripePaymentGateway;
    }

    public IPaymentGateway getPaymentGateway(PGVendor pgVendor) throws InvalidPaymentGatewayException {
        if (pgVendor == PGVendor.RAZORPAY) {
            return razorpayPaymentGateway;
        } else if (pgVendor == PGVendor.STRIPE) {
            return stripePaymentGateway;
        }
        throw new InvalidPaymentGatewayException("Unable to generate object of payment gateway of type : "+ pgVendor);
    }
}
