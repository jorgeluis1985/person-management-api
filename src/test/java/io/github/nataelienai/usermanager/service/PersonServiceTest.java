package io.github.nataelienai.usermanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.nataelienai.usermanager.dto.PersonRequest;
import io.github.nataelienai.usermanager.dto.PersonResponse;
import io.github.nataelienai.usermanager.entity.Address;
import io.github.nataelienai.usermanager.entity.Person;
import io.github.nataelienai.usermanager.exception.DateOfBirthParseException;
import io.github.nataelienai.usermanager.repository.PersonRepository;

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
    ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
    then(personRepository).should().save(personArgumentCaptor.capture());
    Person capturedPerson = personArgumentCaptor.getValue();

    assertThat(capturedPerson.getName()).isEqualTo(personRequest.getName());
    assertThat(capturedPerson.getDateOfBirth().format(DATE_FORMATTER)).isEqualTo(personRequest.getDateOfBirth());
    assertThat(capturedPerson.getAddresses()).isEmpty();

    assertThat(personResponse.getName()).isEqualTo(personRequest.getName());
    assertThat(personResponse.getDateOfBirth()).isEqualTo(personRequest.getDateOfBirth());
    assertThat(personResponse.getAddresses()).isEmpty();
  }

  @Test
  @DisplayName("findAll() should retrieve all people")
  void findAll_shouldRetrieveAllPeople() {
    // given
    String dateOfBirth = "2000-01-01";
    Person person = new Person();
    Address address = new Address();

    person.setId(1L);
    person.setName("John Doe");
    person.setDateOfBirth(LocalDate.parse(dateOfBirth, DATE_FORMATTER));
    person.setAddresses(Set.of(address));

    address.setId(1L);
    address.setCep("12345-123");
    address.setCity("City");
    address.setStreet("Street");
    address.setNumber(10);
    address.setMain(true);
    address.setPerson(person);

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
}
