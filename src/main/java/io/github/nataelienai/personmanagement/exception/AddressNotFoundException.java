package io.github.nataelienai.personmanagement.exception;

public class AddressNotFoundException extends RuntimeException {
  public AddressNotFoundException(Long addressId, Long personId) {
    super("Could not find address " + addressId + " in person " + personId);
  }
}
