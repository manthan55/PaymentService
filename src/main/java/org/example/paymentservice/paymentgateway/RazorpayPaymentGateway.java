package org.example.paymentservice.paymentgateway;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.example.paymentservice.exceptions.PaymentLinkGenerationException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class RazorpayPaymentGateway implements IPaymentGateway {

    private RazorpayClient razorpayClient;

    public RazorpayPaymentGateway(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Override
    public String generatePaymentLink(String orderId, Long amount) throws PaymentLinkGenerationException {
        String paymentLink = null;
        // https://stackoverflow.com/a/59802621
        // https://stackoverflow.com/a/23438360
        Long expireBy = LocalDateTime.now().plusDays(1).atZone(ZoneId.of("Asia/Kolkata")).toInstant().toEpochMilli();

        try {
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("expire_by", expireBy);
            paymentLinkRequest.put("reference_id", orderId);
            paymentLinkRequest.put("description", "Payment service requesting payment for orderId : " + orderId);

            paymentLinkRequest.put("callback_url", "http://localhost:8080");
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
            paymentLink = payment.get("short_url");
        } catch (RazorpayException ex) {
            throw new PaymentLinkGenerationException(ex);
        }
        return paymentLink;
    }
}
