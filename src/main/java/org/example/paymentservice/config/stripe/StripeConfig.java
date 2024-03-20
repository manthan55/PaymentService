package org.example.paymentservice.config.stripe;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class StripeConfig {
    @Value("${stripe.authentication.key}")
    private String stripeKey;

    @Value("${stripe.authentication.secret}")
    private String stripeSecret;

    @Value("${stripe.productId}")
    private String productId;

    @Value("${stripe.priceId}")
    private String priceId;
}
