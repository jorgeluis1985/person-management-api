package io.github.nataelienai.usermanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.nataelienai.usermanager.exception.AddressNotFoundException;
import io.github.nataelienai.usermanager.exception.PersonNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @Getter
  @AllArgsConstructor
  public class ErrorResponse {
    private int statusCode;
    private String message;
  }

  @ExceptionHandler({
      PersonNotFoundException.class,
      AddressNotFoundException.class
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleResourceNotFoundException(RuntimeException exception) {
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
  }
}
