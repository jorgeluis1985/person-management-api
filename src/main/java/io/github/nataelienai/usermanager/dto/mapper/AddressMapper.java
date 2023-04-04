package io.github.nataelienai.usermanager.dto.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import io.github.nataelienai.usermanager.dto.AddressResponse;
import io.github.nataelienai.usermanager.entity.Address;

public class AddressMapper {
  private AddressMapper() {
  }

  public static AddressResponse mapToResponse(Address address) {
    return new AddressResponse(
        address.getId(),
        address.getCep(),
        address.getCity(),
        address.getStreet(),
        address.getNumber(),
        address.getMain());
  }

  public static List<AddressResponse> mapToResponseList(Collection<Address> addresses) {
    return addresses.stream()
        .map(AddressMapper::mapToResponse)
        .collect(Collectors.toList());
  }
}
