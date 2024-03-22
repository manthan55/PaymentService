package org.example.paymentservice.paymentgateway;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.example.paymentservice.config.stripe.StripeConfig;
import org.example.paymentservice.exceptions.PaymentLinkGenerationException;
import org.springframework.stereotype.Component;

@Component
public class StripePaymentGateway implements IPaymentGateway{
    private StripeClient stripeClient;
    private StripeConfig stripeConfig;

    public StripePaymentGateway(StripeClient stripeClient, StripeConfig stripeConfig) {
        this.stripeClient = stripeClient;
        this.stripeConfig = stripeConfig;
    }

    @Override
    public String generatePaymentLink(String orderId, Long amount) throws PaymentLinkGenerationException {
        String paymentLink = null;

        try{
            PaymentLinkCreateParams params =
                    PaymentLinkCreateParams.builder()
                            .addLineItem(
                                    PaymentLinkCreateParams.LineItem.builder()
                                            .setPrice(stripeConfig.getPriceId())
                                            .setQuantity(1L)
                                            .build()
                            )
                            .setAfterCompletion(
                                    PaymentLinkCreateParams.AfterCompletion.builder()
                                            .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                            .setRedirect(
                                                    PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                            .setUrl("https://scaler.com")
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();

            PaymentLink stripePaymentLink = stripeClient.paymentLinks().create(params);
            paymentLink = stripePaymentLink.getUrl();
        }
        catch (StripeException ex){
            throw new PaymentLinkGenerationException(ex);
        }

        return paymentLink;
    }
}
