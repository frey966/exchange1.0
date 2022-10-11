package com.financia.system.crypto.tron.common.exception;

public class BadItemException extends StoreException {

  public BadItemException() {
    super();
  }

  public BadItemException(String message) {
    super(message);
  }

  public BadItemException(String message, Throwable cause) {
    super(message, cause);
  }
}
