package org.example.paymentservice.config.stripe;

import com.stripe.StripeClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
public class StripeBeanConfig {
    private StripeConfig stripeConfig;

    public StripeBeanConfig(StripeConfig stripeConfig) {
        this.stripeConfig = stripeConfig;
    }

    @Bean
    public StripeClient getStripeClient(){
        return new StripeClient(this.stripeConfig.getStripeSecret());
    }
}
