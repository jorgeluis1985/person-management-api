package io.github.nataelienai.usermanager.controller;

import static org.mockito.BDDMockito.given;
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

import io.github.nataelienai.usermanager.dto.ErrorResponse;
import io.github.nataelienai.usermanager.dto.PersonRequest;
import io.github.nataelienai.usermanager.dto.PersonResponse;
import io.github.nataelienai.usermanager.dto.ValidationErrorResponse;
import io.github.nataelienai.usermanager.exception.DateOfBirthParseException;
import io.github.nataelienai.usermanager.service.PersonService;

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
}
