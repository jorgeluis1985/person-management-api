package io.github.nataelienai.usermanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  private int statusCode;
  private String message;
}
