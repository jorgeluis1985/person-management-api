package io.github.nataelienai.usermanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.nataelienai.usermanager.dto.AddressRequest;
import io.github.nataelienai.usermanager.dto.AddressResponse;
import io.github.nataelienai.usermanager.entity.Address;
import io.github.nataelienai.usermanager.entity.Person;
import io.github.nataelienai.usermanager.exception.PersonNotFoundException;
import io.github.nataelienai.usermanager.repository.AddressRepository;
import io.github.nataelienai.usermanager.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
  AddressService addressService;

  @Mock
  AddressRepository addressRepository;

  @Mock
  PersonRepository personRepository;

  @BeforeEach
  void setUp() {
    addressService = new AddressService(addressRepository, personRepository);
  }

  @Test
  @DisplayName("create() should throw when person id does not exist")
  void create_shouldThrow_whenPersonIdDoesNotExist() {
    // given
    Long personId = 1L;
    AddressRequest addressRequest = new AddressRequest("12345-123", "City", "Street", 10);

    given(personRepository.findById(personId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> addressService.create(personId, addressRequest))
        .isInstanceOf(PersonNotFoundException.class);
  }

  @Test
  @DisplayName("create() should save address when given a valid address request")
  void create_shouldSaveAddress_whenGivenValidAddressRequest() {
    // given
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Long personId = 1L;
    Person person = new Person();
    person.setId(personId);
    person.setName("Kendrick Lamar");
    person.setDateOfBirth(LocalDate.parse("2000-01-01", formatter));
    AddressRequest addressRequest = new AddressRequest("12345-123", "City", "Street", 10);

    given(personRepository.findById(personId)).willReturn(Optional.of(person));
    given(addressRepository.save(any(Address.class))).will(returnsFirstArg());

    // when
    AddressResponse addressResponse = addressService.create(personId, addressRequest);

    // then
    ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
    then(addressRepository).should().save(addressArgumentCaptor.capture());
    Address capturedAddress = addressArgumentCaptor.getValue();

    assertThat(capturedAddress.getPerson()).isEqualTo(person);

    assertThat(addressResponse)
        .usingRecursiveComparison()
        .ignoringFields("id", "main")
        .isEqualTo(addressRequest);
    assertThat(addressResponse.getMain()).isFalse();
  }
}
