package io.github.nataelienai.usermanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
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
  public PersonResponse create(@RequestBody PersonRequest personRequest) {
    return personService.create(personRequest);
  }
}
