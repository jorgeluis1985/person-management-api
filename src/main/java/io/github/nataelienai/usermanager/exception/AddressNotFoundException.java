package io.github.nataelienai.usermanager.exception;

public class AddressNotFoundException extends RuntimeException {
  public AddressNotFoundException(Long id) {
    super("Could not find address " + id);
  }
}
