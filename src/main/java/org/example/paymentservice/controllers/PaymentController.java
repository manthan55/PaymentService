package org.example.paymentservice.controllers;

import org.example.paymentservice.dto.GeneratePaymentLinkRequestDTO;
import org.example.paymentservice.dto.GeneratePaymentLinkResponseDTO;
import org.example.paymentservice.dto.api.APIResponse;
import org.example.paymentservice.dto.api.APIResponseFailure;
import org.example.paymentservice.dto.api.APIResponseSuccess;
import org.example.paymentservice.exceptions.InvalidPaymentGatewayException;
import org.example.paymentservice.exceptions.PaymentLinkGenerationException;
import org.example.paymentservice.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/generatePaymentLink")
    public ResponseEntity<APIResponse> generatePaymentLink(@RequestBody GeneratePaymentLinkRequestDTO requestDTO){
        APIResponse response;
        HttpStatus httpStatus = HttpStatus.CREATED;
        try {
            String paymentLink = paymentService.generatePaymentLink(requestDTO.getOrderId(), requestDTO.getPgVendor());
            response = new APIResponseSuccess<GeneratePaymentLinkResponseDTO>(new GeneratePaymentLinkResponseDTO(requestDTO.getOrderId(), paymentLink));
        } catch (PaymentLinkGenerationException | InvalidPaymentGatewayException e) {
            response = new APIResponseFailure(e);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(response);
    }
}
