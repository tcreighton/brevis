package me.creighton.encodedid;

import me.creighton.encodedid.impl.EncodedId;
import me.creighton.encodedid.impl.TightlyEncodedId;

import static me.creighton.encodedid.IAlphabet.BASE_BIG_CHARACTER_SET;

public interface IEncodedId {

  // Default configuration constants
  char DEFAULT_SEPARATOR = '-';
  int DEFAULT_SEGMENT_LENGTH = 4; // somewhat arbitrary
  int MIN_SEGMENT_LENGTH = 2; // somewhat arbitrary
  int MAX_SEGMENT_LENGTH = 8; // somewhat arbitrary
  int DEFAULT_PAD_LENGTH = 7; // By default we show XXXX-YYY
  int MAX_PAD_LENGTH = 30;  // max number of encoded characters to pad with 0.

  // Attribute getters/setters - factory constructors will handle most cases.
  char separator();
  void separator(char separator);
  String alphabet();
  void alphabet(String alphabet, String characterSet) throws EncodedIdException;
  boolean useSeparator();
  void useSeparator(boolean useSeparator);
  int padWidth();
  void padWidth(int padWidth) throws EncodedIdException;
  int segmentLength();
  void segmentLength(int segmentLength) throws EncodedIdException;
  boolean checkedEncoder(); // Indicates if this instance of IEncodedId requires check characters.
  void checkedEncoder(boolean checkedEncoder);

  // Primary work methods
  String encodeId (long id) throws EncodedIdException;
  String encodeIdWithoutSeparator (long id) throws EncodedIdException;
  long decodeId (String encodedId) throws EncodedIdException;

  // Public builders for building various types of IEncodedId.

  static IEncodedId.Builder getEncodedIdBuilder() {
    return new EncodedId.Builder();
  }

  static Builder getEncodedIdBuilder(String alphabet, String characterSet) {
    return new EncodedId.Builder(alphabet, characterSet);
  }

  // TightlyEncodedId can only have alphabets that are derived from BASE_BIG_CHARACTER_SET.
  static IEncodedId.Builder getTightlyEncodedIdBuilder() {
    return new TightlyEncodedId.Builder();
  }

  static Builder getTightlyEncodedIdBuilder(String alphabet) {
    return new TightlyEncodedId.Builder(alphabet, BASE_BIG_CHARACTER_SET);
  }


  interface Builder {

    Builder alphabet(String alphabet, String characterSet);
    Builder checkedEncoder(boolean checkedEncoder);
    Builder padWidth(int padWidth);
    Builder separator(char separator);
    Builder separator(boolean separator);
    Builder segmentLength(int segmentLength);

    IEncodedId build();
  }


}
