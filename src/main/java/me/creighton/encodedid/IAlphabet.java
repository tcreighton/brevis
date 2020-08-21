package me.creighton.encodedid;

/**
 * We use two different terms to describe the character strings that form the basis for encoding and decoding.
 *
 * CHARACTER_SET is used to mean the list of characters that can be used in a given ALPHABET. There is no concept
 * of order within a CHARACTER_SET. Obviously there is an ordering because we create a String constant to contain
 * the characters within the set. However, we don't care about the ordering.
 *
 * ALPHABET is a term that indicates a particular ordering of a CHARACTER_SET. The encoding and decoding of an ID
 * is completely dependent on an ALPHABET. And so once there are encodings in the wild based on a particular ALPHABET
 * that ALPHABET CANNOT change. For
 */

public interface IAlphabet {

  String LOWER_ALPHA = "abcdefghijklmnopqrstuvwxyz";
  String UPPER_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  String ALPHA = LOWER_ALPHA + UPPER_ALPHA;
  String DIGIT = "0123456789";
  String SPECIAL = ":._~!;"; // '-' is excluded as default separator
  String LEGAL_URI_CHARACTER_SET = ALPHA + DIGIT + SPECIAL;

  // The alphabets are given as constants here rather than calls to static Utilities methods
  // So they are easy to see the values and so that people can easily create new alphabets from them.
  // BIG_ALPHABET is just one of any number of scrambles. Don't replace the constant string with
  // anything other than another constant string. IOW, don't replace it with a call to Utilities.scramble.
  // In order to maintain compatibility with encodings in the wild, BIG_ALPHABET and DEFAULT_ALPHABET
  // MUST NOT change. You can create a new alphabet anytime you want, but be sure to use it for
  // all decoding after that. It is not too hard to annotate an encoding to indicate which alphabet
  // to use, but that's beyond the scope of this basic implementation.
  //
  // These Alphabets are made to work in a path element of a URL.
  // If you create your own alphabets and want them to work in a URL,
  // check them with isUrlAlphabet.
  //

  String NUMBERS = "0123456789";
  String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
  String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  String SPECIAL_CHARACTERS = "!$&'()*.:;=_";
  String BASE_DEFAULT_CHARACTER_SET = "123456789BCDFGHJKLMNPQRSTVWXYZ";
  String DEFAULT_ALPHABET = "PDGM4ZSCV8QRW3TYNK5FXB216H79LJ";
  String BASE_BIG_CHARACTER_SET = LOWER_CASE + UPPER_CASE + NUMBERS + SPECIAL_CHARACTERS;
  String BIG_ALPHABET = "TVWboQg4pGnE9w0rhSqFLKmdxZceNk5RBtUDf3iPvMX12OAslIu6yJCa8HYz7j.!;_:&$'()*="; // scrambled LEGAL_URI_ALPHABET

  /** Reserved characters **/
  char NEGATIVE_SIGN = '~';
  char DEFAULT_SEPARATOR = '-';

}
