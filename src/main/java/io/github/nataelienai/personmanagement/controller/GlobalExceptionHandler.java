package io.github.nataelienai.personmanagement.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.nataelienai.personmanagement.dto.ErrorResponse;
import io.github.nataelienai.personmanagement.dto.ValidationErrorResponse;
import io.github.nataelienai.personmanagement.exception.AddressNotFoundException;
import io.github.nataelienai.personmanagement.exception.DateOfBirthParseException;
import io.github.nataelienai.personmanagement.exception.PersonNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Hidden
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "JSON request body has an invalid format");
  }

  @Hidden
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException exception) {
    Map<String, String> fieldErrors = new HashMap<>();

    exception.getFieldErrors().forEach(fieldError -> {
      String fieldName = fieldError.getField();
      String errorMessage = fieldError.getDefaultMessage();
      fieldErrors.put(fieldName, errorMessage);
    });

    return new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), fieldErrors);
  }

  @Hidden
  @ExceptionHandler(DateOfBirthParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleDateOfBirthParseException(DateOfBirthParseException exception) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  @Hidden
  @ExceptionHandler({
      PersonNotFoundException.class,
      AddressNotFoundException.class
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleResourceNotFoundException(RuntimeException exception) {
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
  }

  @Hidden
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleUncaughtException(Exception exception) {
    logger.error(exception.getMessage(), exception);
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
  }
}
