package io.github.nataelienai.usermanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.nataelienai.usermanager.dto.AddressRequest;
import io.github.nataelienai.usermanager.dto.AddressResponse;
import io.github.nataelienai.usermanager.service.AddressService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/people/{personId}/addresses")
public class AddressController {
  private final AddressService addressService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AddressResponse create(@PathVariable Long personId, @RequestBody AddressRequest addressRequest) {
    return addressService.create(personId, addressRequest);
  }
}
