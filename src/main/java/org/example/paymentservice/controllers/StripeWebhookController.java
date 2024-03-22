package org.example.paymentservice.controllers;

import com.stripe.model.Event;
import org.example.paymentservice.dto.GeneratePaymentLinkRequestDTO;
import org.example.paymentservice.dto.api.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stripeWebhook")
public class StripeWebhookController {

    @PostMapping
    public void receiveWebhook(@RequestBody Event event){
        System.out.println("event received : "+event.getType());
    }

}
