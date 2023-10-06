package me.creighton.encodedid;

import me.creighton.encodedid.impl.EncodedId;

import static me.creighton.encodedid.EncodingConstants.BASE_BIG_CHARACTER_SET;
import static me.creighton.encodedid.EncodingConstants.BIG_ALPHABET;

public interface IEncodedId {

  // Default configuration constants
  int DEFAULT_SEGMENT_LENGTH = 4; // somewhat arbitrary
  int MIN_SEGMENT_LENGTH = 2; // somewhat arbitrary
  int MAX_SEGMENT_LENGTH = 8; // somewhat arbitrary
  int DEFAULT_PAD_LENGTH = 7; // By default we show XXXX-YYY
  int MAX_PAD_LENGTH = 30;  // max number of encoded characters to pad with 0.

  // Attribute getters/setters
  char separator();
  void separator(char separator);
  String alphabet();
  void alphabet(String alphabet, String characterSet) throws EncodedIdException;
  String characterSet ();
  boolean useSeparator();
  void useSeparator(boolean useSeparator);
  int padWidth();
  void padWidth(int padWidth) throws EncodedIdException;
  int segmentLength();
  void segmentLength(int segmentLength) throws EncodedIdException;
  boolean checkedEncoder(); // Indicates if this instance of IEncodedId requires check characters.
  void checkedEncoder(boolean checkedEncoder);
  long getMinId ();
  long getMaxId ();
  IEncodedId setMinId (long minId);
  IEncodedId setMaxId (long maxId);

  // Public builders for building various types of IEncodedId.

  static Builder getEncodedIdBuilder() {
    return new EncodedId.Builder();
  }

  static Builder getEncodedIdBuilder (String alphabet) {
    return new EncodedId.Builder(alphabet);
  }

  static Builder getEncodedIdBuilder(String alphabet, String characterSet) {
    return new EncodedId.Builder(alphabet, characterSet);
  }

  // TightlyEncodedId can only have alphabets that are derived from BASE_BIG_CHARACTER_SET.

  static Builder getTightlyEncodedIdBuilder() {
    return new EncodedId.Builder(BIG_ALPHABET, BASE_BIG_CHARACTER_SET);
  }

  static Builder getTightlyEncodedIdBuilder(String alphabet) {
    return new EncodedId.Builder(alphabet, BASE_BIG_CHARACTER_SET);
  }

  interface Builder {

    Builder alphabet(String alphabet, String characterSet);
    String getAlphabet();
    String getCharacterSet();
    Builder checkedEncoder(boolean checkedEncoder);
    boolean isCheckedEncoder();
    Builder padWidth(int padWidth);
    int getPadWidth();
    Builder separator(char separator);
    char getSeparator();
    Builder separator(boolean separator);
    boolean useSeparator();
    Builder segmentLength(int segmentLength);
    int getSegmentLength();
    long getMinId ();
    Builder setMinId (long minId);
    long getMaxId ();
    Builder setMaxId (long maxId);
  }


}
