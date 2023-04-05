package io.github.nataelienai.usermanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
  public AddressResponse create(@PathVariable Long personId, @Valid @RequestBody AddressRequest addressRequest) {
    return addressService.create(personId, addressRequest);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<AddressResponse> findAllByPersonId(@PathVariable Long personId) {
    return addressService.findAllByPersonId(personId);
  }

  @PatchMapping("/{addressId}/main")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setPersonAddressAsMain(@PathVariable Long personId, @PathVariable Long addressId) {
    addressService.setPersonAddressAsMain(personId, addressId);
  }
}
