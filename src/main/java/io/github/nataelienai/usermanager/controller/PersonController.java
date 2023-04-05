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

import io.github.nataelienai.usermanager.dto.PersonRequest;
import io.github.nataelienai.usermanager.dto.PersonResponse;
import io.github.nataelienai.usermanager.service.PersonService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/people")
public class PersonController {
  private final PersonService personService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PersonResponse create(@Valid @RequestBody PersonRequest personRequest) {
    return personService.create(personRequest);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<PersonResponse> findAll() {
    return personService.findAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public PersonResponse findById(@PathVariable Long id) {
    return personService.findById(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public PersonResponse update(@PathVariable Long id, @Valid @RequestBody PersonRequest personRequest) {
    return personService.update(id, personRequest);
  }
}
