package io.github.nataelienai.personmanagement.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import io.github.nataelienai.personmanagement.dto.AddressResponse;
import io.github.nataelienai.personmanagement.dto.ErrorResponse;
import io.github.nataelienai.personmanagement.dto.PersonRequest;
import io.github.nataelienai.personmanagement.dto.PersonResponse;
import io.github.nataelienai.personmanagement.dto.ValidationErrorResponse;
import io.github.nataelienai.personmanagement.exception.DateOfBirthParseException;
import io.github.nataelienai.personmanagement.exception.PersonNotFoundException;
import io.github.nataelienai.personmanagement.service.PersonService;

@WebMvcTest(PersonController.class)
class PersonControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PersonService personService;

  @Test
  @DisplayName("POST /people should return 201 and person when given valid person")
  void create_shouldReturn201AndPerson_whenGivenValidPerson() throws Exception {
    // given
    PersonRequest personRequest = new PersonRequest("John Doe", "2000-01-01");
    PersonResponse personResponse = new PersonResponse(1L, "John Doe", "2000-01-01", List.of());

    given(personService.create(personRequest)).willReturn(personResponse);

    String personRequestJson = objectMapper.writeValueAsString(personRequest);
    String personResponseJson = objectMapper.writeValueAsString(personResponse);

    // when
    // then
    mockMvc.perform(post("/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(personRequestJson))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(personResponseJson));
  }

  @Test
  @DisplayName("POST /people should return 400 when given invalid person")
  void create_shouldReturn400_whenGivenInvalidPerson() throws Exception {
    // given
    PersonRequest personRequest = new PersonRequest(null, null);

    Map<String, String> fieldErrors = Map.of(
        "name", "Name is required",
        "dateOfBirth", "Date of birth is required");
    ValidationErrorResponse errorResponse = new ValidationErrorResponse(400, fieldErrors);

    String personRequestJson = objectMapper.writeValueAsString(personRequest);
    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(post("/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(personRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }

  @Test
  @DisplayName("POST /people should return 400 when given invalid date of birth")
  void create_shouldReturn400_whenGivenInvalidDateOfBirth() throws Exception {
    // given
    PersonRequest personRequest = new PersonRequest("John Doe", "2000-01-32");
    DateOfBirthParseException exception = new DateOfBirthParseException("yyyy-MM-dd");
    ErrorResponse errorResponse = new ErrorResponse(400, exception.getMessage());

    given(personService.create(personRequest)).willThrow(exception);

    String personRequestJson = objectMapper.writeValueAsString(personRequest);
    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(post("/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(personRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }

  @Test
  @DisplayName("GET /people should return 200 and all people")
  void findAll_shouldReturn200AndAllPeople() throws Exception {
    // given
    List<AddressResponse> addressResponses = List.of(
        new AddressResponse(1L, "12325-123", "city", "street", 10, false));
    List<PersonResponse> personResponses = List.of(
        new PersonResponse(1L, "John Doe", "2000-01-01", addressResponses));

    given(personService.findAll()).willReturn(personResponses);

    String personResponsesJson = objectMapper.writeValueAsString(personResponses);

    // when
    // then
    mockMvc.perform(get("/people"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(personResponsesJson));
  }

  @Test
  @DisplayName("GET /people/{personId} should return 200 and person when person id exists")
  void findById_shouldReturn200AndPerson_whenPersonIdExists() throws Exception {
    // given
    List<AddressResponse> addressResponses = List.of(
        new AddressResponse(1L, "12325-123", "city", "street", 10, false));
    Long personId = 1L;
    PersonResponse personResponse = new PersonResponse(personId, "John Doe", "2000-01-01", addressResponses);

    given(personService.findById(personId)).willReturn(personResponse);

    String personResponseJson = objectMapper.writeValueAsString(personResponse);

    // when
    // then
    mockMvc.perform(get("/people/{personId}", personId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(personResponseJson));
  }

  @Test
  @DisplayName("GET /people/{personId} should return 404 when person id does not exist")
  void findById_shouldReturn404_whenPersonIdDoesNotExist() throws Exception {
    // given
    Long personId = 1L;
    PersonNotFoundException exception = new PersonNotFoundException(personId);
    ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());

    given(personService.findById(personId)).willThrow(exception);

    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(get("/people/{personId}", personId))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }

  @Test
  @DisplayName("PUT /people/{personId} should return 200 and person when given valid person and id")
  void update_shouldReturn200AndPerson_whenGivenValidPersonAndId() throws Exception {
    // given
    Long personId = 1L;
    PersonRequest personRequest = new PersonRequest("John Doe", "2000-01-01");
    PersonResponse personResponse = new PersonResponse(personId, "John Doe", "2000-01-01", List.of());

    given(personService.update(personId, personRequest)).willReturn(personResponse);

    String personRequestJson = objectMapper.writeValueAsString(personRequest);
    String personResponseJson = objectMapper.writeValueAsString(personResponse);

    // when
    // then
    mockMvc.perform(put("/people/{personId}", personId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(personRequestJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(personResponseJson));
  }

  @Test
  @DisplayName("PUT /people/{personId} should return 400 when given invalid person")
  void update_shouldReturn400_whenGivenInvalidPerson() throws Exception {
    // given
    PersonRequest personRequest = new PersonRequest(null, null);

    Map<String, String> fieldErrors = Map.of(
        "name", "Name is required",
        "dateOfBirth", "Date of birth is required");
    ValidationErrorResponse errorResponse = new ValidationErrorResponse(400, fieldErrors);

    String personRequestJson = objectMapper.writeValueAsString(personRequest);
    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(put("/people/{personId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(personRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }

  @Test
  @DisplayName("PUT /people/{personId} should return 400 when given invalid date of birth")
  void update_shouldReturn400_whenGivenInvalidDateOfBirth() throws Exception {
    // given
    Long personId = 1L;
    PersonRequest personRequest = new PersonRequest("John Doe", "2000-01-32");
    DateOfBirthParseException exception = new DateOfBirthParseException("yyyy-MM-dd");
    ErrorResponse errorResponse = new ErrorResponse(400, exception.getMessage());

    given(personService.update(personId, personRequest)).willThrow(exception);

    String personRequestJson = objectMapper.writeValueAsString(personRequest);
    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(put("/people/{personId}", personId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(personRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }

  @Test
  @DisplayName("PUT /people/{personId} should return 404 when person id does not exist")
  void update_shouldReturn404_whenPersonIdDoesNotExist() throws Exception {
    // given
    Long personId = 1L;
    PersonRequest personRequest = new PersonRequest("John Doe", "2000-01-01");
    PersonNotFoundException exception = new PersonNotFoundException(personId);
    ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());

    given(personService.update(personId, personRequest)).willThrow(exception);

    String personRequestJson = objectMapper.writeValueAsString(personRequest);
    String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

    // when
    // then
    mockMvc.perform(put("/people/{personId}", personId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(personRequestJson))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(errorResponseJson));
  }
}
