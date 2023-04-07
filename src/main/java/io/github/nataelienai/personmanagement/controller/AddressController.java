package io.github.nataelienai.personmanagement.controller;

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

import io.github.nataelienai.personmanagement.dto.AddressRequest;
import io.github.nataelienai.personmanagement.dto.AddressResponse;
import io.github.nataelienai.personmanagement.dto.ErrorResponse;
import io.github.nataelienai.personmanagement.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Address", description = "Address resource API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/people/{personId}/addresses")
public class AddressController {
  private final AddressService addressService;

  @Operation(summary = "Create an address for a person", responses = {
      @ApiResponse(responseCode = "201", description = "Address created"),
      @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Person id not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AddressResponse create(@PathVariable Long personId, @Valid @RequestBody AddressRequest addressRequest) {
    return addressService.create(personId, addressRequest);
  }

  @Operation(summary = "Get all addresses for a person", responses = {
      @ApiResponse(responseCode = "200", description = "Addresses retrieved"),
      @ApiResponse(responseCode = "404", description = "Person id not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<AddressResponse> findAllByPersonId(@PathVariable Long personId) {
    return addressService.findAllByPersonId(personId);
  }

  @Operation(summary = "Set a person's address as their main one", responses = {
      @ApiResponse(responseCode = "204", description = "Address set as main"),
      @ApiResponse(responseCode = "404", description = "Person id or address id not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PatchMapping("/{addressId}/main")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setPersonAddressAsMain(@PathVariable Long personId, @PathVariable Long addressId) {
    addressService.setPersonAddressAsMain(personId, addressId);
  }
}
