package me.creighton.encodedid.impl;


import static me.creighton.encodedid.IAlphabet.BIG_ALPHABET;

// This class is an extension of the base EncodedId.
// Its purpose is to make it easy to get a compact encoding that also adheres to the rules
// for URL characters. This way you can safely use the encodings anywhere in a URL.
//
//
public class TightlyEncodedId extends EncodedId {

  // Constructors

  public TightlyEncodedId() {
    super(BIG_ALPHABET);
    this.setUseSeparator(false);
    this.setPadWidth(0);
  }

}
