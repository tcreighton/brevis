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
  String LOWER_ALPHA = "abcdefghijklmnopqrstuvwxyz";
  String UPPER_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  String ALPHA = LOWER_ALPHA + UPPER_ALPHA;
  String DIGIT = "0123456789";
  String SPECIAL = "._~"; // '-' is excluded as default separator
  String LEGAL_URI_ALPHABET = ALPHA + DIGIT + SPECIAL;

  // The alphabets are given as constants here rather than calls to static Utilities methods
  // So they are easy to see the values and so that people can easily create new alphabets from them.
  // BIG_ALPHABET is just one of any number of scrambles. Don't replace the constant string with
  // anything other than another constant string. IOW, don't replace it with a call to Utilities.scramble.
  // In order to maintain compatibility with encodings in the wild, BIG_ALPHABET and DEFAULT_ALPHABET
  // MUST NOT change. You can create a new alphabet anytime you want, but be sure to use it for
  // all decoding after that. It is not too hard to annotate an encoding to indicate which alphabet
  // to use, but that's beyond the scope of this basic implementation.
  //
  String BASE_DEFAULT_ALPHABET = "123456789BCDFGHJKLMNPQRSTVWXYZ";
  String DEFAULT_ALPHABET = "PDGM4ZSCV8QRW3TYNK5FXB216H79LJ";
  String BASE_BIG_ALPHABET = ".0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz~";
  String BIG_ALPHABET = "TVWboQg4pGnE9w0rhSqFLKmdxZceNk5RBtUDf3iPvMX12OAslIu6yJCa8HYz7j.~_"; // scrambled LEGAL_URI_ALPHABET

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
