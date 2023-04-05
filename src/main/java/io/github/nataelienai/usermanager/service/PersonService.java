package io.github.nataelienai.usermanager.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.nataelienai.usermanager.dto.PersonRequest;
import io.github.nataelienai.usermanager.dto.PersonResponse;
import io.github.nataelienai.usermanager.dto.mapper.PersonMapper;
import io.github.nataelienai.usermanager.entity.Person;
import io.github.nataelienai.usermanager.exception.DateOfBirthParseException;
import io.github.nataelienai.usermanager.exception.PersonNotFoundException;
import io.github.nataelienai.usermanager.repository.PersonRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonService {
  private static final String DATE_PATTERN = "yyyy-MM-dd";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

  private final PersonRepository personRepository;

  public PersonResponse create(PersonRequest personRequest) {
    Person person = new Person(personRequest.getName(), parseDateOfBirth(personRequest.getDateOfBirth()));
    Person savedPerson = personRepository.save(person);
    return PersonMapper.mapToResponse(savedPerson, DATE_FORMATTER);
  }

  public List<PersonResponse> findAll() {
    return PersonMapper.mapToResponseList(personRepository.findAll(), DATE_FORMATTER);
  }

  public PersonResponse findById(Long id) {
    Person person = personRepository.findById(id)
        .orElseThrow(() -> new PersonNotFoundException(id));

    return PersonMapper.mapToResponse(person, DATE_FORMATTER);
  }

  public PersonResponse update(Long id, PersonRequest personRequest) {
    Person person = personRepository.findById(id)
        .orElseThrow(() -> new PersonNotFoundException(id));

    person.setName(personRequest.getName());
    person.setDateOfBirth(parseDateOfBirth(personRequest.getDateOfBirth()));

    Person savedPerson = personRepository.save(person);
    return PersonMapper.mapToResponse(savedPerson, DATE_FORMATTER);
  }

  private LocalDate parseDateOfBirth(String dateOfBirth) {
    try {
      return LocalDate.parse(dateOfBirth, DATE_FORMATTER);
    } catch (DateTimeParseException exception) {
      throw new DateOfBirthParseException(DATE_PATTERN);
    }
  }
}
