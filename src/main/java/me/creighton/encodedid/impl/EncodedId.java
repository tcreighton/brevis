package me.creighton.encodedid.impl;

import me.creighton.encodedid.EncodedIdException;
import me.creighton.encodedid.IEncodedId;

import static me.creighton.encodedid.IEncodedId.*;
import static me.creighton.encodedid.Utilities.*;

public class EncodedId implements IEncodedId {


  private char separator = DEFAULT_SEPARATOR;
  private String alphabet = DEFAULT_ALPHABET;
  private boolean useSeparator = true;
  private int padWidth = 0;
  private int segmentLength = DEFAULT_SEGMENT_LENGTH;

  private int numberBase = 0;

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
    setNumberBase(getAlphabet().length());
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
  }

  @Override
  public boolean isUseSeparator () {
    return this.useSeparator;
  }

  @Override
  public void setUseSeparator (boolean useSeparator) {
    this.useSeparator = useSeparator;
  }

  public int getNumberBase () {
    return this.numberBase;
  }

  public void setNumberBase (int numberBase) {
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

  // Public Methods

  @Override
  public String encodeId (long id) throws EncodedIdException {

    String s = encodeIdWithoutSeparator(id);

    return addSeparators(s);
  }

  @Override
  public String encodeIdWithoutSeparator (long id) throws EncodedIdException {

    StringBuilder eId = new StringBuilder();
    int nextVal;

    if (id < 0)
      throwInvalidId(id);

    while (id > 0) {
      nextVal = (int) (id % getNumberBase()); // grab next value to encode
      eId.append(encodeValue(nextVal));       // encode that value
      id /= getNumberBase();                  // reduce the source by one value size
    }

    // Next we need to pad to the min size. Remember that we are building this in reverse.

    while (eId.length() < getPadWidth()) {
      eId.append(encodeValue(0));         // Pad with 0 representation.
    }

    return eId.reverse().toString();
  }

  @Override
  public long decodeId(String encodedId) throws EncodedIdException {
    long id = (null == encodedId || encodedId.length() == 0) ? -1 : 0;
    int nextVal;
    char c;
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
    for (int i = 0; i < s.length(); i++) {
      c = s.charAt(i);
      nextVal = getAlphabet().indexOf(c);
      if (-1 == nextVal) {
        // This means encodedId has a character not in the alphabet - bad!
        throwInvalidCharacter(c);
        // Never returns to here.
      }
      id *= getNumberBase();  // Shift current value in the accumulator
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
   * @param value
   * @return encoded character for value
   */
  protected char encodeValue(int value) {
    char ch;
    if (value < 0 || value >= getNumberBase()) { // number base is the length of the alphabet
      throwInvalidEncodingValue(value);
    }

    ch = getAlphabet().charAt(value);
    return ch;
  }

  /**
   * This method will add separators to the encoded string.
   * Whether it does anything at all depends on isUseSeparator being true.
   * Separators are inserted every this.segmentSize characters.
   * Separators are entirely syntactic sugar. There is no semantic at all.
   * @param s
   * @return encoded string with separators
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

}
