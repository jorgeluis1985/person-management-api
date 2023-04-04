package io.github.nataelienai.usermanager.exception;

public class PersonNotFoundException extends RuntimeException {
  public PersonNotFoundException(Long id) {
    super("Could not find person " + id);
  }
}
