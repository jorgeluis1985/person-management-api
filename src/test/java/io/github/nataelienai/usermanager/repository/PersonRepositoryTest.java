package io.github.nataelienai.usermanager.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.github.nataelienai.usermanager.entity.Address;
import io.github.nataelienai.usermanager.entity.Person;

@DataJpaTest
class PersonRepositoryTest {
  @Autowired
  PersonRepository personRepository;

  @Autowired
  AddressRepository addressRepository;

  @Test
  @DisplayName("save() should save a person")
  void save_shouldSaveOnePerson() {
    // given
    Person person = new Person("John Doe", LocalDate.parse("2001-01-01"));

    // when
    Person savedPerson = personRepository.save(person);

    // then
    assertThat(savedPerson.getId()).isPositive();
    assertThat(savedPerson.getName()).isEqualTo(person.getName());
    assertThat(savedPerson.getDateOfBirth()).isEqualTo(person.getDateOfBirth());
  }

  @Test
  @DisplayName("saveAll() should save multiple people")
  void saveAll_shouldSaveMultiplePeople() {
    // given
    List<Person> people = List.of(
        new Person("John Doe", LocalDate.parse("2001-01-01")),
        new Person("Jane Doe", LocalDate.parse("2001-01-02")),
        new Person("Peter Doe", LocalDate.parse("2001-01-03")));

    // when
    List<Person> savedPeople = personRepository.saveAll(people);

    // then
    assertThat(savedPeople)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(people);
    assertThat(savedPeople).allMatch(person -> person.getId() > 0);
  }

  @Test
  @DisplayName("findById() should retrieve a person when they have no addresses")
  void findById_shouldRetrievePerson_whenTheyHaveNoAddresses() {
    // given
    Person person = new Person("John Doe", LocalDate.parse("2001-01-01"));
    Person savedPerson = personRepository.save(person);

    // when
    Optional<Person> optionalPerson = personRepository.findById(savedPerson.getId());

    // then
    assertThat(optionalPerson).isPresent();
    assertThat(optionalPerson.get())
        .usingRecursiveComparison()
        .isEqualTo(savedPerson);
  }

  @Test
  @DisplayName("findById() should retrieve a person when they have addresses")
  void findById_shouldRetrievePerson_whenTheyHaveAddresses() {
    // given
    Person person = new Person("John Doe", LocalDate.parse("2001-01-01"));
    Person savedPerson = personRepository.save(person);

    Address address = new Address("12325-123", "City", "Street", 10, true, savedPerson);
    Address savedAddress = addressRepository.save(address);

    savedPerson.getAddresses().add(savedAddress);

    // when
    Optional<Person> optionalPerson = personRepository.findById(savedPerson.getId());

    // then
    assertThat(optionalPerson).isPresent();
    assertThat(optionalPerson.get())
        .usingRecursiveComparison()
        .isEqualTo(savedPerson);
  }

  @Test
  @DisplayName("findAll() should retrieve multiple people when they have no addresses")
  void findAll_shouldRetrieveMultiplePeople_whenTheyHaveNoAddresses() {
    // given
    List<Person> people = List.of(
        new Person("John Doe", LocalDate.parse("2001-01-01")),
        new Person("Jane Doe", LocalDate.parse("2001-01-02")),
        new Person("Peter Doe", LocalDate.parse("2001-01-03")));
    List<Person> savedPeople = personRepository.saveAll(people);

    // when
    List<Person> foundPeople = personRepository.findAll();

    // then
    assertThat(foundPeople)
        .usingRecursiveComparison()
        .isEqualTo(savedPeople);
  }

  @Test
  @DisplayName("findAll() should retrieve multiple people when they have addresses")
  void findAll_shouldRetrieveMultiplePeople_whenTheyHaveAddresses() {
    // given
    List<Person> people = List.of(
        new Person("John Doe", LocalDate.parse("2001-01-01")),
        new Person("Jane Doe", LocalDate.parse("2001-01-02")),
        new Person("Peter Doe", LocalDate.parse("2001-01-03")));
    List<Person> savedPeople = personRepository.saveAll(people);

    List<Address> addresses = savedPeople.stream()
        .map(savedPerson -> {
          return new Address("12325-123", "City", "Street", 10, true, savedPerson);
        })
        .collect(Collectors.toList());
    List<Address> savedAddresses = addressRepository.saveAll(addresses);

    for (int i = 0; i < savedPeople.size(); i++) {
      Person savedPerson = savedPeople.get(i);
      Address savedAddress = savedAddresses.get(i);
      savedPerson.getAddresses().add(savedAddress);
    }

    // when
    List<Person> foundPeople = personRepository.findAll();

    // then
    assertThat(foundPeople)
        .usingRecursiveComparison()
        .isEqualTo(savedPeople);
  }
}
