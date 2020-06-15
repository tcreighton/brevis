package me.creighton.encodedid.impl;

import me.creighton.encodedid.EncodedIdException;
import me.creighton.encodedid.IEncodedId;

import static me.creighton.encodedid.Utilities.*;

public class EncodedId implements IEncodedId {


  private char separator = DEFAULT_SEPARATOR;
  private String alphabet = DEFAULT_ALPHABET;
  private boolean useSeparator = true;
  private int padWidth = 0;
  private int segmentLength = DEFAULT_SEGMENT_LENGTH;

  private int numberBase = 0;
  private boolean isCheckedEncoder = false; // Default is to not use check characters.

  // Constructors

  public EncodedId () throws EncodedIdException {
    this(DEFAULT_SEPARATOR, DEFAULT_ALPHABET);
  }

  public EncodedId (String alphabet) throws EncodedIdException {
    this(DEFAULT_SEPARATOR, alphabet);
  }

  public EncodedId (char separator) throws EncodedIdException {
    this(separator, DEFAULT_ALPHABET);
  }

  public EncodedId (char separator, String alphabet) throws EncodedIdException {
    this(separator, true, alphabet);
  }

  public EncodedId (char separator, boolean useSeparator, String alphabet) throws EncodedIdException {

    setSeparator(separator);
    setUseSeparator(useSeparator);
    setAlphabet(alphabet);
    setPadWidth(DEFAULT_PAD_LENGTH);
    setSegmentLength(DEFAULT_SEGMENT_LENGTH);
  }

  // Public Getters/Setters

  @Override
  public char getSeparator() {
    return this.separator;
  }

  @Override
  public void setSeparator(char separator) {
    if (! isValidSeparator(separator, getAlphabet())) {
      throwInvalidSeparator(separator);
    }

    this.separator = separator;
  }

  @Override
  public String getAlphabet () {
    return this.alphabet;
  }

  @Override
  public void setAlphabet (String alphabet) throws EncodedIdException {
    if (! isValidAlphabet(alphabet)) {
      throwInvalidAlphabet(alphabet);
    }

    this.alphabet = alphabet;
    setNumberBase(alphabet.length());
  }

  @Override
  public boolean isUseSeparator () {
    return this.useSeparator;
  }

  @Override
  public void setUseSeparator (boolean useSeparator) {
    this.useSeparator = useSeparator;
  }

  protected int getNumberBase () {
    return this.numberBase;
  }

  private void setNumberBase (int numberBase) {
    this.numberBase = numberBase;
  }

  @Override
  public int getPadWidth () {
    return this.padWidth;
  }

  @Override
  public void setPadWidth (int padWidth) throws EncodedIdException {
    if (padWidth < 0 || padWidth > MAX_PAD_LENGTH)
      throwInvalidPadWidth(padWidth);

    this.padWidth = padWidth;
  }

  @Override
  public int getSegmentLength () {
    return this.segmentLength;
  }

  @Override
  public void setSegmentLength (int segmentLength) throws EncodedIdException {
    if (segmentLength < MIN_SEGMENT_LENGTH || segmentLength > MAX_SEGMENT_LENGTH) {
      throwBadSegmentLength(segmentLength);
      // Never returns to here.
    }

    this.segmentLength = segmentLength;
  }

  @Override
  public boolean isCheckedEncoder () {
    return this.isCheckedEncoder;
  }

  @Override
  public void setCheckedEncoder (boolean isCheckedEncoder) {
    this.isCheckedEncoder = isCheckedEncoder;
  }

  // Public Methods

  @Override
  public String encodeId (long id) throws EncodedIdException {

    String s = encodeIdWithoutSeparator(id);

    return addSeparators(s);
  }

  @Override
  public String encodeIdWithoutSeparator (long id) throws EncodedIdException {

    StringBuilder eId = new StringBuilder();
    int nextVal = 0;
    int countPads = 0;

    if (id < 0)
      throwInvalidId(id);

    if (0 == id) {
      eId.append(encodeValue(0));       // encode that value
    }
    while (id > 0) {
      nextVal = (int) (id % getNumberBase()); // grab next value to encode
      id /= getNumberBase();                  // reduce the source by one value size
      eId.append(encodeValue(nextVal));       // encode that value
    }

    // Next we need to pad to the min size. Remember that we are building this in reverse.

    countPads = getPadWidth() - eId.length() - (isCheckedEncoder() ? 1 : 0);
    while (countPads-- > 0) {
      eId.append(encodeValue(0));         // Pad with 0 representation.
    }

    // After optional padding, reverse the string to make it the right way.
    eId.reverse();

    // Now append check character if enabled

    if (isCheckedEncoder()) {
      char checkCharacter = generateCheckCharacter(eId.toString());

      eId.append(checkCharacter);
    }

    return eId.toString();
  }

  @Override
  public long decodeId(String encodedId) throws EncodedIdException {
    long id = (null == encodedId || encodedId.length() == 0) ? -1 : 0;
    int nextVal;
    int encodedLength;  // This is used to determine how much of encodedId to decode (see isCheckedEncoder)

    String s = encodedId;

    if (-1 == id) {
      // Invalid encodedId
      throwInvalidCharacter(' '); // Not really a space - null length or null
      // Never returns to here
    }
    if (useSeparator) {
      // If this is true - scan for and eliminate all separator characters.
      s = "" + getSeparator();
      s = encodedId.replaceAll(s, "");
    }
    // At this point we have an encoded string with no separators
    // Note that 0 == id at this point

    // If isCheckedEncoder then the right-most character is the check character.
    // First validate that check character is correct, then decode a shorter length.

    if (isCheckedEncoder()) {
      encodedLength = s.length() - 1;
      if (! validateCheckCharacter(s)) {
        throwInvalidCheckCharacter(s);
        // !!! Never returns here !!!
      }
    } else {
      encodedLength = s.length();
    }

    for (int i = 0; i < encodedLength; i++) {
      id *= getNumberBase();  // Shift current value in the accumulator
      nextVal = decodeChar(s.charAt(i));
      id += nextVal;          // Add in the value of the current character.
    }
    return id;
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
    if (value < 0 || value >= getNumberBase()) { // number base is the length of the alphabet
      throwInvalidEncodingValue(value);
    }

    ch = getAlphabet().charAt(value);
    return ch;
  }

  protected int decodeChar (char encodedChar) throws EncodedIdException {
    int value;

    value = getAlphabet().indexOf(encodedChar);
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

    if (isUseSeparator()) {
      for (int i = getSegmentLength(); i < sb.length(); i += getSegmentLength() + 1) {
        sb.insert(i, getSeparator());
      }
    }

    return sb.toString();
  }

  /**
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
   * are no separator characters. These MUST all be stripped prior to calling the generate function.
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
    return getNumberBase();
  }

  protected char characterFromCodePoint (int value) {
    return encodeValue(value);
  }

  protected int codePointFromCharacter (char encodedChar) throws EncodedIdException {

    return getAlphabet().indexOf(encodedChar);
  }

  /**
   * Exception wrappers
   */

  private void throwInvalidSeparator (char separator) throws EncodedIdException {
    throw new EncodedIdException("Invalid Separator: " + separator);
  }

  private void throwInvalidAlphabet (String alphabet) throws EncodedIdException {
    throw new EncodedIdException("Invalid alphabet: " + alphabet);
  }

  private void throwInvalidId (long id) throws EncodedIdException {
    throw new EncodedIdException("id is less than 0");
  }

  private void throwInvalidEncodingValue (int i) throws EncodedIdException {
    throw new EncodedIdException("Invalid value for encoding: " + i);
  }

  private void throwInvalidPadWidth (int padWidth) throws EncodedIdException {
    throw new EncodedIdException("Invalid padWidth: " + padWidth);
  }

  private void throwInvalidCharacter (char c) throws EncodedIdException {
    throw new EncodedIdException("Invalid character for alphabet: " + c);
  }

  private void throwBadSegmentLength (int segmentLength) throws EncodedIdException {
    throw new EncodedIdException("segmentLength out of range: " + segmentLength);
  }

  private void throwInvalidCheckCharacter (String s) throws EncodedIdException {
    throw new EncodedIdException("Invalid check character in encoding: " + s);
  }
}
