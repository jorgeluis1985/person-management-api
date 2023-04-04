package io.github.nataelienai.usermanager.service;

import org.springframework.stereotype.Service;

import io.github.nataelienai.usermanager.dto.AddressDto;
import io.github.nataelienai.usermanager.entity.Address;
import io.github.nataelienai.usermanager.entity.Person;
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
}
