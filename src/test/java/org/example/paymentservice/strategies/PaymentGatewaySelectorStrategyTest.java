package org.example.paymentservice.strategies;

import org.example.paymentservice.exceptions.InvalidPaymentGatewayException;
import org.example.paymentservice.models.PGVendor;
import org.example.paymentservice.paymentgateway.IPaymentGateway;
import org.example.paymentservice.paymentgateway.RazorpayPaymentGateway;
import org.example.paymentservice.paymentgateway.StripePaymentGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentGatewaySelectorStrategyTest {

    @Mock
    RazorpayPaymentGateway razorpayPaymentGateway;
    @Mock
    StripePaymentGateway stripePaymentGateway;

    @Test
    @DisplayName("Should return valid payment gateway")
    public void Test_ShouldReturnValidIPaymentGateway() throws InvalidPaymentGatewayException {
        // Arrange
        PaymentGatewaySelectorStrategy paymentGatewaySelectorStrategy = new PaymentGatewaySelectorStrategy(razorpayPaymentGateway,stripePaymentGateway);

        // Act
        IPaymentGateway razorpayPG = paymentGatewaySelectorStrategy.getPaymentGateway(PGVendor.RAZORPAY);
        IPaymentGateway stripePG = paymentGatewaySelectorStrategy.getPaymentGateway(PGVendor.STRIPE);


        // Assert
        assertEquals(razorpayPaymentGateway, razorpayPG);
        assertEquals(stripePaymentGateway, stripePG);
    }

    @Test
    @DisplayName("Should throw error for invalid PGVendor value")
    public void Test_ShouldThrowInvalidPaymentGatewayException_GivenInvalidPGVendor() {
        // Arrange
//        https://stackoverflow.com/a/7233572
//        https://stackoverflow.com/a/63920995
        PGVendor INVALID_PG_VENDOR = mock(PGVendor.class);
        when(INVALID_PG_VENDOR.ordinal()).thenReturn(2);
        mockStatic(PGVendor.class);
        when(PGVendor.values()).thenReturn(new PGVendor[]{PGVendor.RAZORPAY, PGVendor.STRIPE, INVALID_PG_VENDOR});
        String expectedExceptionMessage = "Unable to generate object of payment gateway of type : "+INVALID_PG_VENDOR;

        PaymentGatewaySelectorStrategy paymentGatewaySelectorStrategy = new PaymentGatewaySelectorStrategy(razorpayPaymentGateway,stripePaymentGateway);

        // Act & Assert
        Exception ex = assertThrows(InvalidPaymentGatewayException.class,()-> paymentGatewaySelectorStrategy.getPaymentGateway(INVALID_PG_VENDOR));
        assertEquals(ex.getMessage(), expectedExceptionMessage);
    }
}
