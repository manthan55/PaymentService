package org.example.paymentservice.dto.api;

import lombok.Data;

import java.util.Arrays;

@Data
public class APIResponseFailure extends APIResponse {
    private String message;
    private String stackTrace;

    public APIResponseFailure(Exception ex) {
        this.message = ex.getMessage();
        this.stackTrace = Arrays.toString(ex.getStackTrace());
    }
}
