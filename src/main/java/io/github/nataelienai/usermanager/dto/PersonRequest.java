package io.github.nataelienai.usermanager.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {
  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Date of birth is required")
  private String dateOfBirth;
}
