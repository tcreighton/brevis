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


  /**
   * Exception wrappers
   */

  public static void throwInvalidSeparator (char separator, String alphabet) throws EncodedIdException {
    throw new EncodedIdException("Invalid Separator: " + separator + " for alphabet: " + alphabet);
  }

  public static void throwInvalidAlphabet (String alphabet, String characterSet) throws EncodedIdException {
    throw new EncodedIdException("Invalid alphabet: " + alphabet + " for character set: " + characterSet);
  }

  public static void throwInvalidId (long id) throws EncodedIdException {
    throw new EncodedIdException("id is less than 0");
  }

  public static void throwInvalidEncodingValue (int i) throws EncodedIdException {
    throw new EncodedIdException("Invalid value for encoding: " + i);
  }

  public static void throwInvalidPadWidth (int padWidth) throws EncodedIdException {
    throw new EncodedIdException("Invalid padWidth: " + padWidth);
  }

  public static void throwInvalidCharacter (char c) throws EncodedIdException {
    throw new EncodedIdException("Invalid character for alphabet: " + c);
  }

  public static void throwBadSegmentLength (int segmentLength) throws EncodedIdException {
    throw new EncodedIdException("segmentLength out of range: " + segmentLength);
  }

  public static void throwInvalidCheckCharacter (String s) throws EncodedIdException {
    throw new EncodedIdException("Invalid check character in encoding: " + s);
  }

}
