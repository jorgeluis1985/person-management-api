package io.github.nataelienai.personmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.github.nataelienai.personmanagement.entity.Address;
import io.github.nataelienai.personmanagement.entity.Person;

@DataJpaTest
class AddressRepositoryTest {
  @Autowired
  PersonRepository personRepository;

  @Autowired
  AddressRepository addressRepository;

  @Test
  @DisplayName("save() should save an address")
  void save_shouldSaveOneAddress() {
    // given
    Person person = new Person("John Doe", LocalDate.parse("2001-01-01"));
    Person savedPerson = personRepository.save(person);

    Address address = new Address("12325-123", "City", "Street", 10, true, savedPerson);

    // when
    Address savedAddress = addressRepository.save(address);

    // then
    assertThat(savedAddress.getId()).isPositive();
    assertThat(savedAddress)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(address);
  }

  @Test
  @DisplayName("saveAll() should save multiple addresses")
  void saveAll_shouldSaveMultipleAddresses() {
    // given
    Person person = new Person("John Doe", LocalDate.parse("2001-01-01"));
    Person savedPerson = personRepository.save(person);

    List<Address> addresses = List.of(
        new Address("12325-123", "City", "Street", 10, true, savedPerson),
        new Address("12325-124", "City", "Street 2", 10, false, savedPerson),
        new Address("12325-125", "City", "Street 3", 10, false, savedPerson));

    // when
    List<Address> savedAddresses = addressRepository.saveAll(addresses);

    // then
    assertThat(savedAddresses).allMatch(address -> address.getId() > 0);
    assertThat(savedAddresses)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(addresses);
  }

  @Test
  @DisplayName("findById() should retrieve an address")
  void findById_shouldRetrieveAnAddress() {
    // given
    Person person = new Person("John Doe", LocalDate.parse("2001-01-01"));
    Person savedPerson = personRepository.save(person);

    Address address = new Address("12325-123", "City", "Street", 10, true, savedPerson);
    Address savedAddress = addressRepository.save(address);

    // when
    // when
    Optional<Address> optionalAddress = addressRepository.findById(savedAddress.getId());

    // then
    assertThat(optionalAddress).isPresent();
    assertThat(optionalAddress.get())
        .usingRecursiveComparison()
        .isEqualTo(savedAddress);
  }

  @Test
  @DisplayName("findAll() should retrieve multiple addresses")
  void findAll_shouldRetrieveMultipleAddresses() {
    // given
    Person person = new Person("John Doe", LocalDate.parse("2001-01-01"));
    Person savedPerson = personRepository.save(person);

    List<Address> addresses = List.of(
        new Address("12325-123", "City", "Street", 10, true, savedPerson),
        new Address("12325-124", "City", "Street 2", 10, false, savedPerson),
        new Address("12325-125", "City", "Street 3", 10, false, savedPerson));
    List<Address> savedAddresses = addressRepository.saveAll(addresses);

    // when
    List<Address> foundAddresses = addressRepository.findAll();

    // then
    assertThat(foundAddresses)
        .usingRecursiveComparison()
        .isEqualTo(savedAddresses);
  }
}
