package io.github.nataelienai.usermanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import io.github.nataelienai.usermanager.dto.AddressDto;
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

  public Address create(Long personId, AddressDto addressDto) {
    Person person = personRepository.findById(personId)
        .orElseThrow(() -> new PersonNotFoundException(personId));

    Address address = new Address(addressDto.getCep(), addressDto.getCity(), addressDto.getStreet(),
        addressDto.getNumber(), false, person);

    return addressRepository.save(address);
  }

  public List<Address> findAllByPersonId(Long personId) {
    Person person = personRepository.findById(personId)
        .orElseThrow(() -> new PersonNotFoundException(personId));

    Set<Address> addresses = person.getAddresses();

    return new ArrayList<>(addresses);
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
      throw new AddressNotFoundException(addressId);
    }

    addressRepository.saveAll(addresses);
  }
}
