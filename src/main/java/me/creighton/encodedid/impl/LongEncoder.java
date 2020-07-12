package me.creighton.encodedid.impl;

import me.creighton.encodedid.EncodedIdException;
import me.creighton.encodedid.IEncodedId;
import me.creighton.encodedid.ILongEncoder;

import static me.creighton.encodedid.EncodedIdException.*;

public class LongEncoder extends EncodedId implements ILongEncoder {

  // Public Constructors

  public LongEncoder (EncodedId.Builder builder) {
    super(builder);
  }


  // Public Work Methods

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
      nextVal = (int) (id % numberBase()); // grab next value to encode
      id /= numberBase();                  // reduce the source by one value size
      eId.append(encodeValue(nextVal));       // encode that value
    }

    // Next we need to pad to the min size. Remember that we are building this in reverse.

    countPads = padWidth() - eId.length() - (checkedEncoder() ? 1 : 0);
    while (countPads-- > 0) {
      eId.append(encodeValue(0));         // Pad with 0 representation.
    }

    // After optional padding, reverse the string to make it the right way.
    eId.reverse();

    // Now append check character if enabled

    if (checkedEncoder()) {
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
    if (useSeparator()) {
      // If this is true - scan for and eliminate all separator characters.
      s = "" + this.separator();
      s = encodedId.replaceAll(s, "");
    }
    // At this point we have an encoded string with no separators
    // Note that 0 == id at this point

    // If isCheckedEncoder then the right-most character is the check character.
    // First validate that check character is correct, then decode a shorter length.

    if (checkedEncoder()) {
      encodedLength = s.length() - 1;
      if (! validateCheckCharacter(s)) {
        throwInvalidCheckCharacter(s);
        // !!! Never returns here !!!
      }
    } else {
      encodedLength = s.length();
    }

    for (int i = 0; i < encodedLength; i++) {
      id *= numberBase();  // Shift current value in the accumulator
      nextVal = decodeChar(s.charAt(i));
      id += nextVal;          // Add in the value of the current character.
    }
    return id;
  }


}
