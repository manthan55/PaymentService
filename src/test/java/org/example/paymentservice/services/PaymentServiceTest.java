package org.example.paymentservice.services;

import org.example.paymentservice.exceptions.InvalidPaymentGatewayException;
import org.example.paymentservice.exceptions.PaymentLinkGenerationException;
import org.example.paymentservice.models.PGVendor;
import org.example.paymentservice.paymentgateway.IPaymentGateway;
import org.example.paymentservice.services.PaymentService;
import org.example.paymentservice.strategies.PaymentGatewaySelectorStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;
    @MockBean
    private PaymentGatewaySelectorStrategy paymentGatewaySelectorStrategy;

    // as we are mocking an interface, we must use @Mock and not @MockBean
    // https://www.baeldung.com/java-spring-mockito-mock-mockbean
    @Mock
    private IPaymentGateway paymentGateway;

    @Test
    @DisplayName("Happy - generate payment link")
    public void Test_GivenValidDetails_GeneratePaymentLink() throws InvalidPaymentGatewayException, PaymentLinkGenerationException {
        // Arrange
        String orderId = "123";
        PGVendor pgVendor = PGVendor.RAZORPAY;
        String expectedPaymentLink = "some_payment_link";
        when(paymentGatewaySelectorStrategy.getPaymentGateway(any(PGVendor.class))).thenReturn(paymentGateway);
        when(paymentGateway.generatePaymentLink(any(String.class), any(Long.class))).thenReturn(expectedPaymentLink);

        // Act
        String generatedPaymentLink = paymentService.generatePaymentLink(orderId, pgVendor);

        // Assert
        assertEquals(expectedPaymentLink, generatedPaymentLink);
    }
}
