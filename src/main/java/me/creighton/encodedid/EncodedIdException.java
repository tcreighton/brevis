package me.creighton.encodedid;

import java.math.BigInteger;
import java.time.LocalDate;

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
    throw new EncodedIdException("Exception!! Invalid Separator: " + separator + " for alphabet: " + alphabet);
  }

  public static void throwInvalidAlphabet (String alphabet, String characterSet) throws EncodedIdException {
    throw new EncodedIdException("Exception!! Invalid alphabet: " + alphabet + " for character set: " + characterSet);
  }

  public static void throwInvalidId (long id) throws EncodedIdException {
    throw new EncodedIdException("Exception!! Invalid id for encoding: " + id);
  }

  public static void throwInvalidId (BigInteger id) throws EncodedIdException {
    throw new EncodedIdException("Exception!! Invalid id for encoding: " + id);
  }

  public static void throwInvalidRange (long minOtpId, long maxOtpId) {
    throw new EncodedIdException("Exception!! Invalid range for OTP. minOtpId must be <= maxOtpId - 10: "
            + minOtpId + "," + maxOtpId);
  }

  public static void throwInvalidEncodingValue (int i) throws EncodedIdException {
    throw new EncodedIdException("Exception!! Invalid value for encoding: " + i);
  }

  public static void throwInvalidPadWidth (int padWidth) throws EncodedIdException {
    throw new EncodedIdException("Exception!! Invalid padWidth: " + padWidth);
  }

  public static void throwInvalidCharacter (char c) throws EncodedIdException {
    throw new EncodedIdException("Exception!! Invalid character for alphabet: " + c);
  }

  public static void throwBadSegmentLength (int segmentLength) throws EncodedIdException {
    throw new EncodedIdException("Exception!! segmentLength out of range: " + segmentLength);
  }

  public static void throwInvalidCheckCharacter (String s) throws EncodedIdException {
    throw new EncodedIdException("Exception!! Invalid check character in encoding: " + s);
  }

  public static void throwInvalidTargetDate (LocalDate date) throws  EncodedIdException {
    throw new EncodedIdException("Exception!! Invalid target date for validity check: " + date);
  }

}
