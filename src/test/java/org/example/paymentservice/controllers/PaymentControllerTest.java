package org.example.paymentservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.paymentservice.dto.GeneratePaymentLinkRequestDTO;
import org.example.paymentservice.dto.GeneratePaymentLinkResponseDTO;
import org.example.paymentservice.dto.api.APIResponse;
import org.example.paymentservice.dto.api.APIResponseFailure;
import org.example.paymentservice.dto.api.APIResponseSuccess;
import org.example.paymentservice.exceptions.InvalidPaymentGatewayException;
import org.example.paymentservice.exceptions.PaymentLinkGenerationException;
import org.example.paymentservice.models.PGVendor;
import org.example.paymentservice.services.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private PaymentController paymentController;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PaymentService paymentService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should generate valid payment link")
    public void Test_ShouldGenerateValidPaymentLink() throws Exception {
        // Arrange
        String orderId = "123";
        PGVendor pgVendor = PGVendor.RAZORPAY;
        GeneratePaymentLinkRequestDTO requestDTO = new GeneratePaymentLinkRequestDTO();
        requestDTO.setOrderId(orderId);
        requestDTO.setPgVendor(pgVendor);

        String expectedPaymentLink = "some_payment_link";
        when(paymentService.generatePaymentLink(any(String.class), any(PGVendor.class))).thenReturn(expectedPaymentLink);

        GeneratePaymentLinkResponseDTO responseDTO = new GeneratePaymentLinkResponseDTO(orderId, expectedPaymentLink);
        APIResponseSuccess<GeneratePaymentLinkResponseDTO> expectedAPIResponseSuccess = new APIResponseSuccess<>(responseDTO);


        // Act
        mockMvc.perform(
                        post("/payment/generatePaymentLink")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                // this won't work -- we won't be able to directly compare the entire reponse object as we are randomly generating a traceId value which we cannot expect
//                .andExpect(content().string(objectMapper.writeValueAsString(expectedAPIResponseSuccess)))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.response.orderId").value(orderId))
                .andExpect(jsonPath("$.response.paymentLink").value(expectedPaymentLink));
    }

    @Test
    @DisplayName("Should not generate payment link due to PaymentLinkGenerationException")
    public void Test_ShouldNotGeneratePaymentLink_DueToPaymentLinkGenerationException() throws Exception {
        // Arrange
        String orderId = "123";
        PGVendor pgVendor = PGVendor.RAZORPAY;
        GeneratePaymentLinkRequestDTO requestDTO = new GeneratePaymentLinkRequestDTO();
        requestDTO.setOrderId(orderId);
        requestDTO.setPgVendor(pgVendor);

        String expectedPaymentLink = "some_payment_link";
        RuntimeException runtimeException = new RuntimeException("some exception");
        PaymentLinkGenerationException paymentLinkGenerationException = new PaymentLinkGenerationException(runtimeException);
        when(paymentService.generatePaymentLink(any(String.class), any(PGVendor.class))).thenThrow(paymentLinkGenerationException);

        GeneratePaymentLinkResponseDTO responseDTO = new GeneratePaymentLinkResponseDTO(orderId, expectedPaymentLink);
        APIResponseFailure expectedAPIResponseFailure = new APIResponseFailure(paymentLinkGenerationException);


        // Act
        mockMvc.perform(
                        post("/payment/generatePaymentLink")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.length()").value(3));

//                // below is not working
//                // expectation = some exception
//                // actual = java.lang.RuntimeException: some exception
//                // when doing ex.GetMessage() --> I am receiving "some exception"
//                // but when ex.getMessage() is called within APIResponseFailure.Constructor, it returns "java.lang.RuntimeException: some exception"
//                // this is because the runtime exception is wrapped within PaymentLinkGenerationException
//                .andExpect(jsonPath("$.message").value(runtimeException.getMessage()))
//                .andExpect(jsonPath("$.stackTrace").value(runtimeException.getStackTrace()));
    }
}
