package io.github.nataelienai.usermanager.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.nataelienai.usermanager.dto.AddressRequest;
import io.github.nataelienai.usermanager.dto.AddressResponse;
import io.github.nataelienai.usermanager.dto.ErrorResponse;
import io.github.nataelienai.usermanager.dto.ValidationErrorResponse;
import io.github.nataelienai.usermanager.exception.AddressNotFoundException;
import io.github.nataelienai.usermanager.exception.PersonNotFoundException;
import io.github.nataelienai.usermanager.service.AddressService;

@WebMvcTest(AddressController.class)
class AddressControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  AddressService addressService;

  @Test
  @DisplayName("POST /people/{personId}/addresses should return 201 and address when given valid person id and address")
  void create_shouldReturn201AndAddress_whenGivenValidPersonIdAndAddress() throws Exception {
    // given
    Long personId = 1L;
    AddressRequest addressRequest = new AddressRequest("12345-123", "City", "Street", 10);
    AddressResponse addressResponse = new AddressResponse(1L, "12345-123", "City", "Street", 10, false);

    given(addressService.create(personId, addressRequest)).willReturn(addressResponse);

    String addressRequestJson = objectMapper.writeValueAsString(addressRequest);
    String addressResponseJson = objectMapper.writeValueAsString(addressResponse);

    // when
    // then
    mockMvc.perform(post("/people/{personId}/addresses", personId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(addressRequestJson))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(addressResponseJson));
  }

  @Test
  @DisplayName("POST /people/{personId}/addresses should return 400 when given invalid address")
  void create_shouldReturn400_whenGivenInvalidAddress() throws Exception {
    // given
    Long personId = 1L;
    AddressRequest addressRequest = new AddressRequest(null, null, null, null);

    Map<String, String> fieldErrors = Map.of(
        "cep", "CEP is required",
        "city", "City is required",
        "street", "Street is required",
        "number", "Number is required");
    ValidationErrorResponse errorResponse = new ValidationErrorResponse(400, fieldErrors);

    String addressRequestJson = objectMapper.writeValueAsString(addressRequest);
    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(post("/people/{personId}/addresses", personId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(addressRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }

  @Test
  @DisplayName("POST /people/{personId}/addresses should return 404 when person id does not exist")
  void create_shouldReturn404_whenPersonIdDoesNotExist() throws Exception {
    // given
    Long personId = 1L;
    AddressRequest addressRequest = new AddressRequest("12345-123", "City", "Street", 10);
    PersonNotFoundException exception = new PersonNotFoundException(personId);
    ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());

    given(addressService.create(personId, addressRequest)).willThrow(exception);

    String addressRequestJson = objectMapper.writeValueAsString(addressRequest);
    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(post("/people/{personId}/addresses", personId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(addressRequestJson))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }

  @Test
  @DisplayName("GET /people/{personId}/addresses should return 200 and addresses when given valid person id")
  void findAllByPersonId_shouldReturn200AndAddresses_whenGivenValidPersonId() throws Exception {
    // given
    Long personId = 1L;
    List<AddressResponse> addressResponses = List.of(
        new AddressResponse(1L, "12345-123", "City", "Street", 10, false),
        new AddressResponse(2L, "12345-124", "City 2", "Street 2", 11, false));

    given(addressService.findAllByPersonId(personId)).willReturn(addressResponses);

    String addressResponseJson = objectMapper.writeValueAsString(addressResponses);

    // when
    // then
    mockMvc.perform(get("/people/{personId}/addresses", personId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(addressResponseJson));
  }

  @Test
  @DisplayName("GET /people/{personId}/addresses should return 404 when person id does not exist")
  void findAllByPersonId_shouldReturn404_whenPersonIdDoesNotExist() throws Exception {
    // given
    Long personId = 1L;
    PersonNotFoundException exception = new PersonNotFoundException(personId);
    ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());

    given(addressService.findAllByPersonId(personId)).willThrow(exception);

    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(get("/people/{personId}/addresses", personId))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }

  @Test
  @DisplayName("PATCH /people/{personId}/addresses/{addressId}/main should return 204 when given valid person and address ids")
  void setPersonAddressAsMain_shouldReturn204_whenGivenValidPersonAndAddressIds() throws Exception {
    // given
    Long personId = 1L;
    Long addressId = 1L;
    willDoNothing().given(addressService).setPersonAddressAsMain(personId, addressId);

    // when
    // then
    mockMvc.perform(patch("/people/{personId}/addresses/{addressId}/main", personId, addressId))
        .andExpect(status().isNoContent());

    then(addressService).should().setPersonAddressAsMain(personId, addressId);
  }

  @Test
  @DisplayName("PATCH /people/{personId}/addresses/{addressId}/main should return 404 when person id does not exist")
  void setPersonAddressAsMain_shouldReturn404_whenPersonIdDoesNotExist() throws Exception {
    // given
    Long personId = 1L;
    Long addressId = 1L;
    PersonNotFoundException exception = new PersonNotFoundException(personId);
    ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());

    willThrow(exception).given(addressService).setPersonAddressAsMain(personId, addressId);

    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(patch("/people/{personId}/addresses/{addressId}/main", personId, addressId))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }

  @Test
  @DisplayName("PATCH /people/{personId}/addresses/{addressId}/main should return 404 when address id does not exist")
  void setPersonAddressAsMain_shouldReturn404_whenAddressIdDoesNotExist() throws Exception {
    // given
    Long personId = 1L;
    Long addressId = 1L;
    AddressNotFoundException exception = new AddressNotFoundException(personId, addressId);
    ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());

    willThrow(exception).given(addressService).setPersonAddressAsMain(personId, addressId);

    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(patch("/people/{personId}/addresses/{addressId}/main", personId, addressId))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }
}
