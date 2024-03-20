package org.example.paymentservice.dto.api;

import lombok.Data;

@Data
public class APIResponse {
    private String traceId;

    public APIResponse() {
        this.traceId = Long.valueOf(System.currentTimeMillis()).toString();
    }
}
