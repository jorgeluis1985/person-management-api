package io.github.nataelienai.personmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
  private Long id;
  private String cep;
  private String city;
  private String street;
  private Integer number;
  private Boolean main;
}
