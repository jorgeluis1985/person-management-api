package io.github.nataelienai.usermanager.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import io.github.nataelienai.usermanager.dto.AddressRequest;
import io.github.nataelienai.usermanager.dto.AddressResponse;
import io.github.nataelienai.usermanager.dto.mapper.AddressMapper;
import io.github.nataelienai.usermanager.entity.Address;
import io.github.nataelienai.usermanager.entity.Person;
import io.github.nataelienai.usermanager.exception.AddressNotFoundException;
import io.github.nataelienai.usermanager.exception.PersonNotFoundException;
import io.github.nataelienai.usermanager.repository.AddressRepository;
import io.github.nataelienai.usermanager.repository.PersonRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AddressService {
  private final AddressRepository addressRepository;
  private final PersonRepository personRepository;

  public AddressResponse create(Long personId, AddressRequest addressRequest) {
    Person person = personRepository.findById(personId)
        .orElseThrow(() -> new PersonNotFoundException(personId));

    Address address = new Address(addressRequest.getCep(), addressRequest.getCity(), addressRequest.getStreet(),
        addressRequest.getNumber(), false, person);

    Address savedAddress = addressRepository.save(address);
    return AddressMapper.mapToResponse(savedAddress);
  }

  public List<AddressResponse> findAllByPersonId(Long personId) {
    Person person = personRepository.findById(personId)
        .orElseThrow(() -> new PersonNotFoundException(personId));

    Set<Address> addresses = person.getAddresses();
    return AddressMapper.mapToResponseList(addresses);
  }

  public void setPersonAddressAsMain(Long personId, Long addressId) {
    Person person = personRepository.findById(personId)
        .orElseThrow(() -> new PersonNotFoundException(personId));

    Set<Address> addresses = person.getAddresses();
    boolean addressFound = false;

    for (Address address : addresses) {
      if (addressId.equals(address.getId())) {
        addressFound = true;
        address.setMain(true);
      } else {
        address.setMain(false);
      }
    }

    if (!addressFound) {
      throw new AddressNotFoundException(addressId, personId);
    }

    addressRepository.saveAll(addresses);
  }
}
