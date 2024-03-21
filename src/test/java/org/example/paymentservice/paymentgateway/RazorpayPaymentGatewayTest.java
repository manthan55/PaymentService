package org.example.paymentservice.paymentgateway;

import com.google.gson.JsonObject;
import com.razorpay.PaymentLink;
import com.razorpay.PaymentLinkClient;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.example.paymentservice.exceptions.InvalidPaymentGatewayException;
import org.example.paymentservice.exceptions.PaymentLinkGenerationException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RazorpayPaymentGatewayTest {
    @Autowired
    private RazorpayPaymentGateway razorpayPaymentGateway;
    @MockBean
    private RazorpayClient razorpayClient;
    @Captor
    private ArgumentCaptor<JSONObject> jsonObjectCaptor;

    @Test
    @DisplayName("Should generate payment link successfully")
    public void Test_ShouldGeneratePaymentLink() throws RazorpayException, PaymentLinkGenerationException {
        // Arrange
        String orderId = "123";
        Long amount = 1000L;
        String expectedPaymentLink = "some payment link";

        JSONObject paymentLinkJSONObject = new JSONObject();
        paymentLinkJSONObject.put("short_url", expectedPaymentLink);
        PaymentLink rpPaymentLinkObject = new PaymentLink(paymentLinkJSONObject);
        PaymentLinkClient mockPaymentLinkClient = mock(PaymentLinkClient.class);

        // this works but the below line does not
        razorpayClient.paymentLink = mockPaymentLinkClient;
//        when(razorpayClient.paymentLink).thenReturn(mockPaymentLinkClient);

        when(razorpayClient.paymentLink.create(any(JSONObject.class))).thenReturn(rpPaymentLinkObject);

        // Act
        String paymentLink = razorpayPaymentGateway.generatePaymentLink(orderId, amount);

        // Assert
        assertEquals(expectedPaymentLink, paymentLink);
        verify(razorpayClient.paymentLink).create(jsonObjectCaptor.capture());
        assertEquals(amount,jsonObjectCaptor.getValue().get("amount"));
        assertEquals(orderId,jsonObjectCaptor.getValue().get("reference_id"));
    }

    @Test
    @DisplayName("Should throw PaymentLinkGenerationException due to error within RazorpayClient")
    public void Test_ShouldThrowPaymentLinkGenerationException_DueToErrorWithinRazorpayClient() throws RazorpayException {
        // Arrange
        String orderId = "123";
        Long amount = 1000L;

        razorpayClient.paymentLink = mock(PaymentLinkClient.class);

        RazorpayException razorpayException = new RazorpayException("invalid secret key");
        when(razorpayClient.paymentLink.create(any(JSONObject.class))).thenThrow(razorpayException);

        // Act & Assert
        assertThrows(PaymentLinkGenerationException.class,()-> razorpayPaymentGateway.generatePaymentLink(orderId,amount));
    }
}
