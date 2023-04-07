package io.github.nataelienai.personmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.nataelienai.personmanagement.dto.PersonRequest;
import io.github.nataelienai.personmanagement.dto.PersonResponse;
import io.github.nataelienai.personmanagement.entity.Address;
import io.github.nataelienai.personmanagement.entity.Person;
import io.github.nataelienai.personmanagement.exception.DateOfBirthParseException;
import io.github.nataelienai.personmanagement.exception.PersonNotFoundException;
import io.github.nataelienai.personmanagement.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
  final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  PersonService personService;

  @Mock
  PersonRepository personRepository;

  @BeforeEach
  void setUp() {
    personService = new PersonService(personRepository);
  }

  @Test
  @DisplayName("create() should throw when given an invalid date of birth")
  void create_shouldThrow_whenGivenInvalidDateOfBirth() {
    // given
    PersonRequest personRequest = new PersonRequest("john doe", "2000-99-01");

    // when
    // then
    assertThatThrownBy(() -> personService.create(personRequest))
        .isInstanceOf(DateOfBirthParseException.class);

    then(personRepository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("create() should save a person when given a valid person request")
  void create_shouldSavePerson_whenGivenValidPersonRequest() {
    // given
    PersonRequest personRequest = new PersonRequest("john doe", "2000-01-01");
    given(personRepository.save(any(Person.class))).will(returnsFirstArg());

    // when
    PersonResponse personResponse = personService.create(personRequest);

    // then
    assertThat(personResponse.getName()).isEqualTo(personRequest.getName());
    assertThat(personResponse.getDateOfBirth()).isEqualTo(personRequest.getDateOfBirth());
    assertThat(personResponse.getAddresses()).isEmpty();
  }

  @Test
  @DisplayName("findAll() should retrieve all people")
  void findAll_shouldRetrieveAllPeople() {
    // given
    Person person = createPersonWithAddress();
    String dateOfBirth = person.getDateOfBirth().format(DATE_FORMATTER);

    given(personRepository.findAll()).willReturn(List.of(person));

    // when
    List<PersonResponse> personResponses = personService.findAll();

    // then
    assertThat(personResponses).isNotEmpty();
    assertThat(personResponses.get(0))
        .usingRecursiveComparison()
        .ignoringFields("dateOfBirth")
        .isEqualTo(person);
    assertThat(personResponses.get(0).getDateOfBirth()).isEqualTo(dateOfBirth);
  }

  @Test
  @DisplayName("findById() should throw when person id does not exist")
  void findById_shouldThrow_whenPersonIdDoesNotExist() {
    // given
    Long id = 1L;
    given(personRepository.findById(id)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> personService.findById(id))
        .isInstanceOf(PersonNotFoundException.class);
  }

  @Test
  @DisplayName("findById() should retrieve person when id exists")
  void findById_shouldRetrievePerson_whenIdExists() {
    // given
    Person person = createPersonWithAddress();
    Long id = person.getId();
    String dateOfBirth = person.getDateOfBirth().format(DATE_FORMATTER);

    given(personRepository.findById(id)).willReturn(Optional.of(person));

    // when
    PersonResponse personResponse = personService.findById(id);

    // then
    assertThat(personResponse)
        .usingRecursiveComparison()
        .ignoringFields("dateOfBirth")
        .isEqualTo(person);
    assertThat(personResponse.getDateOfBirth()).isEqualTo(dateOfBirth);
  }

  @Test
  @DisplayName("update() should throw when person id does not exist")
  void update_shouldThrow_whenPersonIdDoesNotExist() {
    // given
    Long id = 1L;
    PersonRequest personRequest = new PersonRequest("john doe", "2000-01-01");
    given(personRepository.findById(id)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> personService.update(id, personRequest))
        .isInstanceOf(PersonNotFoundException.class);
  }

  @Test
  @DisplayName("update() should throw when given an invalid date of birth")
  void update_shouldThrow_whenGivenInvalidDateOfBirth() {
    // given
    PersonRequest personRequest = new PersonRequest("john doe", "2000-99-01");
    Person person = createPersonWithAddress();
    Long id = person.getId();

    given(personRepository.findById(id)).willReturn(Optional.of(person));

    // when
    // then
    assertThatThrownBy(() -> personService.update(id, personRequest))
        .isInstanceOf(DateOfBirthParseException.class);
  }

  @Test
  @DisplayName("update() should update person when given an existent id and a valid person request")
  void update_shouldUpdatePerson_whenGivenExistentIdAndValidPersonRequest() {
    // given
    PersonRequest personRequest = new PersonRequest("new john doe", "2001-02-02");
    Person person = createPersonWithAddress();
    Long id = person.getId();

    given(personRepository.findById(id)).willReturn(Optional.of(person));
    given(personRepository.save(any(Person.class))).will(returnsFirstArg());

    // when
    PersonResponse personResponse = personService.update(id, personRequest);

    // then
    assertThat(personResponse)
        .usingRecursiveComparison()
        .ignoringFields("name", "dateOfBirth")
        .isEqualTo(person);
    assertThat(personResponse.getName()).isEqualTo(personRequest.getName());
    assertThat(personResponse.getDateOfBirth()).isEqualTo(personRequest.getDateOfBirth());
  }

  Person createPersonWithAddress() {
    Person person = new Person();
    person.setId(1L);
    person.setName("John Doe");
    person.setDateOfBirth(LocalDate.parse("2000-01-01", DATE_FORMATTER));

    Address address = new Address();
    address.setId(1L);
    address.setCep("12345-123");
    address.setCity("City");
    address.setStreet("Street");
    address.setNumber(10);
    address.setMain(true);

    person.setAddresses(Set.of(address));
    address.setPerson(person);

    return person;
  }
}
