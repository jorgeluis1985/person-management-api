package io.github.nataelienai.usermanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.nataelienai.usermanager.dto.ErrorResponse;
import io.github.nataelienai.usermanager.dto.PersonRequest;
import io.github.nataelienai.usermanager.dto.PersonResponse;
import io.github.nataelienai.usermanager.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Person", description = "Person resource API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/people")
public class PersonController {
  private final PersonService personService;

  @Operation(summary = "Create a person", responses = {
      @ApiResponse(responseCode = "201", description = "Person created"),
      @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PersonResponse create(@Valid @RequestBody PersonRequest personRequest) {
    return personService.create(personRequest);
  }

  @Operation(summary = "Get all people", responses = {
      @ApiResponse(responseCode = "200", description = "People retrieved"),
  })
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<PersonResponse> findAll() {
    return personService.findAll();
  }

  @Operation(summary = "Get a person by their id", responses = {
      @ApiResponse(responseCode = "200", description = "Person retrieved"),
      @ApiResponse(responseCode = "404", description = "Person id not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/{personId}")
  @ResponseStatus(HttpStatus.OK)
  public PersonResponse findById(@PathVariable Long personId) {
    return personService.findById(personId);
  }

  @Operation(summary = "Update a person by their id", responses = {
      @ApiResponse(responseCode = "200", description = "Person updated"),
      @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Person id not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PutMapping("/{personId}")
  @ResponseStatus(HttpStatus.OK)
  public PersonResponse update(@PathVariable Long personId, @Valid @RequestBody PersonRequest personRequest) {
    return personService.update(personId, personRequest);
  }
}
