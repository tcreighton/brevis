package me.creighton.encodedid.impl;


import me.creighton.encodedid.IEncodedId;

import static me.creighton.encodedid.EncodedIdException.*;
import static me.creighton.encodedid.Utilities.isValidAlphabet;
import static me.creighton.encodedid.Utilities.isValidSeparator;

// This class is an extension of the base EncodedId.
// Its purpose is to make it easy to get a compact encoding that also adheres to the rules
// for URL characters. This way you can safely use the encodings anywhere in a URL.
//
//
public class TightlyEncodedId extends EncodedId {

  // Constructors

  public TightlyEncodedId() {
    super(BIG_ALPHABET, BASE_BIG_CHARACTER_SET);
    this.useSeparator(false);
    this.padWidth(0);
  }

  public TightlyEncodedId (Builder builder) {

    this.alphabet(builder.alphabet, builder.characterSet);
    this.separator(builder.separator);
    this.useSeparator(builder.useSeparator);
    this.segmentLength(builder.segmentLength);
    this.checkedEncoder(builder.checkedEncoder);
    this.padWidth(builder.padWidth);
  }

  /**
   * Builder class
   */

  public static class Builder implements IEncodedId.Builder {
    private String alphabet;
    private String characterSet;
    private char separator = '-';
    private boolean useSeparator = false;
    private int segmentLength = MIN_SEGMENT_LENGTH;
    private boolean checkedEncoder = false;
    private int padWidth = 0;

    public Builder () {
      this(BIG_ALPHABET);
    }

    public Builder (String alphabet) {
      this.alphabet(alphabet, BASE_BIG_CHARACTER_SET);
    }

    public Builder (String alphabet, String characterSet) {
      if (! characterSet.equals(BASE_BIG_CHARACTER_SET)) {
        throwInvalidAlphabet(alphabet, characterSet);
      }
      this.alphabet(alphabet, characterSet);
    }

    public IEncodedId.Builder alphabet (String alphabet, String characterSet) {
      if (! isValidAlphabet(alphabet, BASE_BIG_CHARACTER_SET) ||
          ! characterSet.equals(BASE_BIG_CHARACTER_SET)) {
        throwInvalidAlphabet(alphabet, BASE_BIG_CHARACTER_SET);
      }
      this.alphabet = alphabet;
      this.characterSet = characterSet;

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

    public IEncodedId build () {
      return new TightlyEncodedId(this);
    }
  }

}
