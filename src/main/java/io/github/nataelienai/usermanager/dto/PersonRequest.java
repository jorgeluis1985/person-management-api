package io.github.nataelienai.usermanager.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

  @NotNull(message = "Date of birth is required")
  private LocalDate dateOfBirth;
}
