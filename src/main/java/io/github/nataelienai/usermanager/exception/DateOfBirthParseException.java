package io.github.nataelienai.usermanager.exception;

public class DateOfBirthParseException extends RuntimeException {
  public DateOfBirthParseException(String datePattern) {
    super("Date of birth has an invalid format, a valid format would be " + datePattern);
  }
}
