package org.example.paymentservice.paymentgateway;

import com.stripe.StripeClient;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.service.PaymentLinkService;
import org.example.paymentservice.config.stripe.StripeConfig;
import org.example.paymentservice.exceptions.PaymentLinkGenerationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StripePaymentGatewayTest {
    @Autowired
    private StripePaymentGateway stripePaymentGateway;
    @MockBean
    private StripeClient stripeClient;
    // even though we haven't used StripeConfig in our tests
    // we must still include it as @MockBean
    // if we don't then spring will provide live object of StripeConfig which will have actual keys/data
    @MockBean
    private StripeConfig stripeConfig;

    @Test
    @DisplayName("Should generate payment link successfully")
    public void Test_ShouldGeneratePaymentLink() throws StripeException, PaymentLinkGenerationException {
        // Arrange
        String orderId = "123";
        Long amount = 1000L;
        String expectedPaymentLink = "some payment link";

        PaymentLinkService mockPaymentLinkService = mock(PaymentLinkService.class);
        when(stripeClient.paymentLinks()).thenReturn(mockPaymentLinkService);
        PaymentLink stripePaymentLinkObject = new PaymentLink();
        stripePaymentLinkObject.setUrl(expectedPaymentLink);
        when(mockPaymentLinkService.create(any(PaymentLinkCreateParams.class))).thenReturn(stripePaymentLinkObject);

        // Act
        String paymentLink = stripePaymentGateway.generatePaymentLink(orderId,amount);

        // Assert
        assertEquals(expectedPaymentLink, paymentLink);
    }

    @Test
    @DisplayName("Should throw PaymentLinkGenerationException due to error within StripeClient")
    public void Test_ShouldThrowPaymentLinkGenerationException_DueToErrorWithinStripeClient() throws StripeException {
        // Arrange
        String orderId = "123";
        Long amount = 1000L;

        PaymentLinkService mockPaymentLinkService = mock(PaymentLinkService.class);
        when(stripeClient.paymentLinks()).thenReturn(mockPaymentLinkService);


        StripeException stripeException = new AuthenticationException("message","requestId","code",1);
        when(mockPaymentLinkService.create(any(PaymentLinkCreateParams.class))).thenThrow(stripeException);

        // Act & Assert
        assertThrows(PaymentLinkGenerationException.class,()-> stripePaymentGateway.generatePaymentLink(orderId,amount));
    }
}
