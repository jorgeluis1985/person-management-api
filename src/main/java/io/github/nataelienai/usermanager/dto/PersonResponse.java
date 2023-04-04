package io.github.nataelienai.usermanager.dto;

import java.time.LocalDate;
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
  private LocalDate dateOfBirth;
  private List<AddressResponse> addresses;
}
