package me.creighton.encodedid.impl;

import me.creighton.encodedid.*;

import static me.creighton.encodedid.EncodedIdException.*;
import static me.creighton.encodedid.Utilities.*;

public abstract class EncodedId implements IAlphabet, IEncodedId {


  private char separator = DEFAULT_SEPARATOR;
  private String alphabet = DEFAULT_ALPHABET;
//  private String characterSet = BASE_DEFAULT_CHARACTER_SET;
  private boolean useSeparator = false;
  private int padWidth = 0;
  private int segmentLength = DEFAULT_SEGMENT_LENGTH;

  private int numberBase = 0;
  private boolean checkedEncoder = false; // Default is to not use check characters.

  // Constructors

  public EncodedId (Builder builder) throws EncodedIdException {
    this.alphabet(builder.alphabet, builder.characterSet);
    this.separator(builder.separator);
    this.useSeparator(builder.useSeparator);
    this.segmentLength(builder.segmentLength);
    this.checkedEncoder(builder.checkedEncoder);
    this.padWidth(builder.padWidth);
  }

  // Public Getters/Setters

  @Override
  public char separator() {
    return this.separator;
  }

  @Override
  public void separator(char separator) {
    if (! isValidSeparator(separator, this.alphabet)) {
      throwInvalidSeparator(separator, this.alphabet);
    }

    this.separator = separator;
  }

  @Override
  public String alphabet() {
    return this.alphabet;
  }

  @Override
  public void alphabet(String alphabet, String characterSet) throws EncodedIdException {
    // Must check that the alphabet is legal for the characterSet
    // We must check that the separator is legal for this alphabet if
    // useSeparator returns true.

    if (! isValidAlphabet(alphabet, characterSet)) {
      throwInvalidAlphabet(alphabet, characterSet);
    }
    if (useSeparator() && ! isValidSeparator(this.separator(), alphabet)) {
      throwInvalidSeparator(this.separator, alphabet);
    }
    this.alphabet = alphabet;
    numberBase(alphabet.length()); // String.length() returns int, so we can depend on it being <= Integer.MAX_VALUE.
  }

  @Override
  public boolean useSeparator() {
    return this.useSeparator;
  }

  @Override
  public void useSeparator(boolean separator) {

    if (separator && !isValidSeparator(this.separator(), alphabet())) {
      throwInvalidSeparator(this.separator, this.alphabet);
    }

    this.useSeparator = separator;
  }

  protected int numberBase() {
    return this.numberBase;
  }

  private void numberBase(int numberBase) {
    this.numberBase = numberBase;
  }

  @Override
  public int padWidth() {
    return this.padWidth;
  }

  @Override
  public void padWidth(int padWidth) throws EncodedIdException {
    if (padWidth < 0 || padWidth > MAX_PAD_LENGTH)
      throwInvalidPadWidth(padWidth);

    this.padWidth = padWidth;
  }

  @Override
  public int segmentLength() {
    return this.segmentLength;
  }

  @Override
  public void segmentLength(int segmentLength) throws EncodedIdException {
    if (segmentLength < MIN_SEGMENT_LENGTH || segmentLength > MAX_SEGMENT_LENGTH) {
      throwBadSegmentLength(segmentLength);
      // Never returns to here.
    }

    this.segmentLength = segmentLength;
  }

  @Override
  public boolean checkedEncoder() {
    return this.checkedEncoder;
  }

  @Override
  public void checkedEncoder(boolean checkedEncoder) {
    this.checkedEncoder = checkedEncoder;
  }

  // Non-public methods

  /**
   * This method encodes one 'digit' of data. The value parameter contains the number
   * to encode and must be within a legal range based on the number of characters
   * in the alphabet. That is essentially the number base for this encoding.
   * If the value is in legal limits, the appropriate character is looked up in the alphabet.
   */
  protected char encodeValue(int value) {
    char ch;
    if (value < 0 || value >= numberBase()) { // number base is the length of the alphabet
      throwInvalidEncodingValue(value);
    }

    ch = alphabet().charAt(value);
    return ch;
  }

  protected int decodeChar (char encodedChar) throws EncodedIdException {
    int value;

    value = alphabet().indexOf(encodedChar);
    if (-1 == value) {
      // This means encodedId has a character not in the alphabet - bad!
      throwInvalidCharacter(encodedChar);
      // Never returns to here.
    }
    return value;
  }

  /**
   * This method will add separators to the encoded string.
   * Whether it does anything at all depends on isUseSeparator being true.
   * Separators are inserted every this.segmentSize characters.
   * Separators are entirely syntactic sugar. There is no semantic at all.
   */
  protected String addSeparators (String s) {
    StringBuilder sb = new StringBuilder(s);

    if (useSeparator()) {
      for (int i = segmentLength(); i < sb.length(); i += segmentLength() + 1) {
        sb.insert(i, this.separator());
      }
    }

    return sb.toString();
  }

  /*
   * These next methods implement a checksum algorithm. The checksum is computed across
   * the characters of an encoding and then is itself encoded and appended as the last
   * (least significant) character of the overall encoding.
   *
   * The algorithm used is the Luhn mod N algorithm as described in Wikipedia at
   * https://en.wikipedia.org/wiki/Luhn_mod_N_algorithm.
   * I've attemped to keep the implementation as close as reasonable to the java implementation
   * given on the Wikipedia page. I have made some changes to accommodate integration into
   * the EncodedId class. We could probably make some efficiency gains by computing the checksum
   * while encoding or decoding, but the logic would be a bit more obscure.
   */

  /**
   * This function generates a check character given an encoding. Note that it assumes there
   * are no separator characters. These MUST all be stripped prior to calling the generate
   * or validate functions.
   *
   */
  protected char generateCheckCharacter (String input) throws EncodedIdException {
    int factor = 2;
    int sum = 0;
    int n = numberOfValidInputCharacters();

    // Starting from the right and working leftwards is easier since
    // the initial "factor" will always be "2".
    for (int i = input.length() - 1; i >= 0; i--) {
      int codePoint = codePointFromCharacter(input.charAt(i));
      int addend = factor * codePoint;

      // Alternate the "factor" that each "codePoint" is multiplied by
      factor = (factor == 2) ? 1 : 2;

      // Sum the digits of the "addend" as expressed in base "n"
      addend = (addend / n) + (addend % n);
      sum += addend;
    }

    // Calculate the number that must be added to the "sum"
    // to make it divisible by "n".
    int remainder = sum % n;
    int checkCodePoint = (n - remainder) % n;

    return characterFromCodePoint(checkCodePoint);
  }

  boolean validateCheckCharacter(String input) {
    int factor = 1;
    int sum = 0;
    int n = numberOfValidInputCharacters();

    // Starting from the right, work leftwards
    // Now, the initial "factor" will always be "1"
    // since the last character is the check character.
    for (int i = input.length() - 1; i >= 0; i--) {
      int codePoint = codePointFromCharacter(input.charAt(i));
      int addend = factor * codePoint;

      // Alternate the "factor" that each "codePoint" is multiplied by
      factor = (factor == 2) ? 1 : 2;

      // Sum the digits of the "addend" as expressed in base "n"
      addend = (addend / n) + (addend % n);
      sum += addend;
    }

    int remainder = sum % n;

    return (remainder == 0);
  }

  /**
   * Helper methods for the check digit stuff - aids in mapping to the canonical implementations.
   */

  protected int numberOfValidInputCharacters () {
    return numberBase();
  }

  protected char characterFromCodePoint (int value) {
    return encodeValue(value);
  }

  protected int codePointFromCharacter (char encodedChar) throws EncodedIdException {
    return alphabet().indexOf(encodedChar);
  }

  /**
   * Builder class
   */

  public static class Builder implements IEncodedId.Builder {
    private String alphabet;
    private String characterSet;
    private boolean checkedEncoder = false;
    private char separator = '-';
    private boolean useSeparator = false;
    private int segmentLength = MIN_SEGMENT_LENGTH;
    private int padWidth = 0;

    public Builder () {
      this(DEFAULT_ALPHABET, BASE_DEFAULT_CHARACTER_SET);
    }

    public Builder (String alphabet, String characterSet) {
      this.alphabet(alphabet, characterSet);
    }

    public IEncodedId.Builder alphabet(String alphabet, String characterSet) {
      this.alphabet = alphabet;
      this.characterSet = characterSet;

      if (! isValidAlphabet(alphabet, characterSet)) {
        throwInvalidAlphabet(alphabet, characterSet);
      }
      return this;
    }

    public IEncodedId.Builder separator (char separator) {
      this.separator = separator;

      if (! isValidSeparator(separator, this.alphabet)) {
        throwInvalidSeparator(separator, this.alphabet);
      }
      return this;
    }

    public IEncodedId.Builder separator(boolean separator) {
      this.useSeparator = separator;

      if (separator && ! isValidSeparator(this.separator, this.alphabet)) {
        throwInvalidSeparator(this.separator, this.alphabet);
      }
      return this;
    }

    public IEncodedId.Builder segmentLength(int segmentLength) {
      this.segmentLength = segmentLength;

      if (segmentLength < MIN_SEGMENT_LENGTH || segmentLength > MAX_SEGMENT_LENGTH) {
        throwBadSegmentLength(segmentLength);
        // Never returns to here.
      }
      return this;
    }

    public IEncodedId.Builder checkedEncoder(boolean checkedEncoder) {
      this.checkedEncoder = checkedEncoder;
      return this;
    }

    public IEncodedId.Builder padWidth (int padWidth) {
      this.padWidth = padWidth;
      return this;
    }

    public ILongEncoder buildLongEncoder () {
      return new LongEncoder(this);
    }

    public IBigIntegerEncoder buildBigIntegerEncoder() {
      return new BigIntegerEncoder(this);
    }

    public IUuidEncoder buildUuidEncoder () {
      return new UuidEncoder(this);
    }

  }


}
