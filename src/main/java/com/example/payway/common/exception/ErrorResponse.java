package com.example.payway.common.exception;

import java.util.List;

public record ErrorResponse(
    String message,
    List<String> errors
) {
}
