package me.creighton.encodedid;

import me.creighton.encodedid.impl.EncodedId;
import me.creighton.encodedid.impl.TightlyEncodedId;

public interface IEncodedId {

  // Default configuration constants
  char DEFAULT_SEPARATOR = '-';
  int DEFAULT_SEGMENT_LENGTH = 4; // somewhat arbitrary
  int MIN_SEGMENT_LENGTH = 2; // somewhat arbitrary
  int MAX_SEGMENT_LENGTH = 8; // somewhat arbitrary
  int DEFAULT_PAD_LENGTH = 7; // By default we show XXXX-YYY
  int MAX_PAD_LENGTH = 30;  // max number of encoded characters to pad with 0.

  // Attribute getters/setters - factory constructors will handle most cases.
  char getSeparator();
  void setSeparator(char separator);
  String getAlphabet ();
  void setAlphabet (String alphabet) throws EncodedIdException;
  boolean isUseSeparator ();
  void setUseSeparator (boolean useSeparator);
  int getPadWidth ();
  void setPadWidth (int padWidth) throws EncodedIdException;
  int getSegmentLength ();
  void setSegmentLength (int segmentLength) throws EncodedIdException;
  boolean isCheckedEncoder (); // Indicates if this instance of IEncodedId requires check characters.
  void setCheckedEncoder (boolean isCheckedEncoder);

  // Primary work methods
  String encodeId (long id) throws EncodedIdException;
  String encodeIdWithoutSeparator (long id) throws EncodedIdException;
  long decodeId (String encodedId) throws EncodedIdException;

  // Public constructors for various implementations

  static IEncodedId EncodedIdFactory () {
    return new EncodedId();
  }

  static IEncodedId EncodedIdFactory (String alphabet) {
    return new EncodedId(alphabet);
  }

  static IEncodedId EncodedIdFactory (char separator) {
    return new EncodedId(separator);
  }

  static IEncodedId EncodedIdFactory (char separator, String alphabet) {
    return new EncodedId(separator, alphabet);
  }

  static IEncodedId TightlyEncodedIdFactory () {
    return new TightlyEncodedId();
  }

}
