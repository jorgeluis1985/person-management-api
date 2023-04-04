package io.github.nataelienai.usermanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.nataelienai.usermanager.dto.PersonDto;
import io.github.nataelienai.usermanager.entity.Person;
import io.github.nataelienai.usermanager.exception.PersonNotFoundException;
import io.github.nataelienai.usermanager.repository.PersonRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonService {
  private final PersonRepository personRepository;

  public Person create(PersonDto personDto) {
    Person person = new Person(personDto.getName(), personDto.getDateOfBirth());
    return personRepository.save(person);
  }

  public List<Person> findAll() {
    return personRepository.findAll();
  }

  public Person findById(Long id) {
    return personRepository.findById(id)
        .orElseThrow(() -> new PersonNotFoundException(id));
  }
}
