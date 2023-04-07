package io.github.nataelienai.personmanagement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {
  private Long id;
  private String name;
  private String dateOfBirth;
  private List<AddressResponse> addresses;
}
