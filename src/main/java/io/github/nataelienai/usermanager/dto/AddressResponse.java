package io.github.nataelienai.usermanager.dto;

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
