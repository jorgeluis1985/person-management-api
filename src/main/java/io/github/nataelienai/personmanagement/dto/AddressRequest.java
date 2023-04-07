package io.github.nataelienai.personmanagement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

  @NotBlank(message = "CEP is required")
  @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "CEP has an invalid format, a valid format would be 00000-000")
  private String cep;

  @NotBlank(message = "City is required")
  private String city;

  @NotBlank(message = "Street is required")
  private String street;

  @NotNull(message = "Number is required")
  @Positive(message = "Number must be a positive integer")
  private Integer number;
}
