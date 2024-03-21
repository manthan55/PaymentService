package org.example.paymentservice.dto.api;

import lombok.Getter;

@Getter
public class APIResponse {
    private String traceId;

    public APIResponse() {
        this.traceId = Long.valueOf(System.currentTimeMillis()).toString();
    }
}
