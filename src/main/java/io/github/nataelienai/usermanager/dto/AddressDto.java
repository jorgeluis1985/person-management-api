package io.github.nataelienai.usermanager.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
  private String cep;
  private String city;
  private String street;
  private Integer number;
}
