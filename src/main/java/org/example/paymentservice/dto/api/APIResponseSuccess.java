package org.example.paymentservice.dto.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class APIResponseSuccess<T> extends APIResponse {
    private T response;
}
