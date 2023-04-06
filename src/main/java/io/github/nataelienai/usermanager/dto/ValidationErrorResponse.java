package io.github.nataelienai.usermanager.dto;

import java.util.Map;

import lombok.Getter;

@Getter
public class ValidationErrorResponse extends ErrorResponse {
  private Map<String, String> fieldErrors;

  public ValidationErrorResponse(int statusCode, Map<String, String> fieldErrors) {
    super(statusCode, "Validation failed");
    this.fieldErrors = fieldErrors;
  }
}
