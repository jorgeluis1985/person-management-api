package io.github.nataelienai.usermanager.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.nataelienai.usermanager.dto.PersonRequest;
import io.github.nataelienai.usermanager.dto.PersonResponse;
import io.github.nataelienai.usermanager.dto.mapper.PersonMapper;
import io.github.nataelienai.usermanager.entity.Person;
import io.github.nataelienai.usermanager.exception.PersonNotFoundException;
import io.github.nataelienai.usermanager.repository.PersonRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonService {
  private final PersonRepository personRepository;

  public PersonResponse create(PersonRequest personRequest) {
    Person person = new Person(personRequest.getName(), parseDateOfBirth(personRequest.getDateOfBirth()));
    Person savedPerson = personRepository.save(person);
    return PersonMapper.mapToResponse(savedPerson);
  }

  public List<PersonResponse> findAll() {
    return PersonMapper.mapToResponseList(personRepository.findAll());
  }

  public PersonResponse findById(Long id) {
    Person person = personRepository.findById(id)
        .orElseThrow(() -> new PersonNotFoundException(id));

    return PersonMapper.mapToResponse(person);
  }

  public PersonResponse update(Long id, PersonRequest personRequest) {
    Person person = personRepository.findById(id)
        .orElseThrow(() -> new PersonNotFoundException(id));

    person.setName(personRequest.getName());
    person.setDateOfBirth(parseDateOfBirth(personRequest.getDateOfBirth()));

    Person savedPerson = personRepository.save(person);
    return PersonMapper.mapToResponse(savedPerson);
  }

  private LocalDate parseDateOfBirth(String dateOfBirth) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(dateOfBirth, formatter);
  }
}
