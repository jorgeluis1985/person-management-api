package io.github.nataelienai.usermanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.nataelienai.usermanager.dto.PersonRequest;
import io.github.nataelienai.usermanager.entity.Person;
import io.github.nataelienai.usermanager.exception.PersonNotFoundException;
import io.github.nataelienai.usermanager.repository.PersonRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonService {
  private final PersonRepository personRepository;

  public Person create(PersonRequest personRequest) {
    Person person = new Person(personRequest.getName(), personRequest.getDateOfBirth());
    return personRepository.save(person);
  }

  public List<Person> findAll() {
    return personRepository.findAll();
  }

  public Person findById(Long id) {
    return personRepository.findById(id)
        .orElseThrow(() -> new PersonNotFoundException(id));
  }

  public Person update(Long id, PersonRequest personRequest) {
    Person person = personRepository.findById(id)
        .orElseThrow(() -> new PersonNotFoundException(id));

    person.setName(personRequest.getName());
    person.setDateOfBirth(personRequest.getDateOfBirth());

    return personRepository.save(person);
  }
}
