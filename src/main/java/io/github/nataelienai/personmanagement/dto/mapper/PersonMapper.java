package io.github.nataelienai.personmanagement.dto.mapper;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import io.github.nataelienai.personmanagement.dto.PersonResponse;
import io.github.nataelienai.personmanagement.entity.Person;

public class PersonMapper {
  private PersonMapper() {
  }

  public static PersonResponse mapToResponse(Person person, DateTimeFormatter formatter) {
    return new PersonResponse(
        person.getId(),
        person.getName(),
        person.getDateOfBirth().format(formatter),
        AddressMapper.mapToResponseList(person.getAddresses()));
  }

  public static List<PersonResponse> mapToResponseList(Collection<Person> people, DateTimeFormatter formatter) {
    return people.stream()
        .map(person -> mapToResponse(person, formatter))
        .collect(Collectors.toList());
  }
}
