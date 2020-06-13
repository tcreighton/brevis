package me.creighton.encodedid;

public class EncodedIdException extends RuntimeException {

  public EncodedIdException() { }

  public EncodedIdException(String message) {
    super(message);
  }

  public EncodedIdException(Throwable cause) {
    super(cause);
  }

  public EncodedIdException(String message, Throwable cause) {
    super(message, cause);
  }

}
