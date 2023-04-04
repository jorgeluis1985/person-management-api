package io.github.nataelienai.usermanager.dto.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import io.github.nataelienai.usermanager.dto.PersonResponse;
import io.github.nataelienai.usermanager.entity.Person;

public class PersonMapper {
  private PersonMapper() {
  }

  public static PersonResponse mapToResponse(Person person) {
    return new PersonResponse(
        person.getId(),
        person.getName(),
        person.getDateOfBirth(),
        AddressMapper.mapToResponseList(person.getAddresses()));
  }

  public static List<PersonResponse> mapToResponseList(Collection<Person> people) {
    return people.stream()
        .map(PersonMapper::mapToResponse)
        .collect(Collectors.toList());
  }
}
