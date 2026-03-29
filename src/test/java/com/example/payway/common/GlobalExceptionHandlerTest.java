package com.example.payway.common;

import com.example.payway.common.exception.BusinessException;
import com.example.payway.common.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void shouldHandleNotFoundException() {
    var exception = new NotFoundException("Account not found");
    var response = handler.handleNotFound(exception);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Account not found", response.getBody().message());
    assertTrue(response.getBody().errors().isEmpty());
  }

  @Test
  void shouldHandleBusinessException() {
    var exception = new BusinessException("Document already exists");
    var response = handler.handleBusiness(exception);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Document already exists", response.getBody().message());
    assertTrue(response.getBody().errors().isEmpty());
  }

  @Test
  void shouldHandleValidationException() {
    var bindingResult = mock(BindingResult.class);
    var fieldError1 = new FieldError("object", "field1", "must not be null");
    var fieldError2 = new FieldError("object", "field2", "invalid format");

    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

    var exception = new MethodArgumentNotValidException(null, bindingResult);
    var response = handler.handleValidation(exception);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Validation failed", response.getBody().message());
    assertEquals(2, response.getBody().errors().size());
    assertTrue(response.getBody().errors().contains("field1: must not be null"));
    assertTrue(response.getBody().errors().contains("field2: invalid format"));
  }

  @Test
  void shouldHandleValidationExceptionWithSingleError() {
    var bindingResult = mock(BindingResult.class);
    var fieldError = new FieldError("accountDTO", "documentNumber", "must not be blank");

    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

    var exception = new MethodArgumentNotValidException(null, bindingResult);

    var response = handler.handleValidation(exception);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Validation failed", response.getBody().message());
    assertEquals(1, response.getBody().errors().size());
    assertEquals("documentNumber: must not be blank", response.getBody().errors().get(0));
  }

  @Test
  void shouldHandleMalformedPayloadException() {
    var exception = mock(HttpMessageNotReadableException.class);
    var response = handler.handleMalformedPayload(exception);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Malformed request body", response.getBody().message());
    assertTrue(response.getBody().errors().isEmpty());
  }

  @Test
  void shouldHandleIllegalArgumentException() {
    var exception = new IllegalArgumentException("Invalid parameter");
    var response = handler.handleIllegalArgument(exception);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Invalid parameter", response.getBody().message());
    assertTrue(response.getBody().errors().isEmpty());
  }

  @Test
  void shouldHandleNotFoundExceptionWithDifferentMessage() {
    var exception = new NotFoundException("Document number not found!");
    var response = handler.handleNotFound(exception);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Document number not found!", response.getBody().message());
  }

  @Test
  void shouldHandleBusinessExceptionWithDifferentMessage() {
    var exception = new BusinessException("Invalid business rule");
    var response = handler.handleBusiness(exception);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertEquals("Invalid business rule", response.getBody().message());
  }
}
