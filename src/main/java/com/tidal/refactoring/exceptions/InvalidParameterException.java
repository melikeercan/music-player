package com.tidal.refactoring.exceptions;

public class InvalidParameterException extends BaseException {

  public InvalidParameterException(String stringValue) {
    super(String.format("Invalid parameter name: %s", stringValue));
  }

  public InvalidParameterException(String param, String secondParam) {
    super(String.format("Invalid parameter name: %s %2d", param, secondParam));
  }
}
