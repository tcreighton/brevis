package me.creighton.encodedid.impl;

import me.creighton.encodedid.EncodedIdException;
import me.creighton.encodedid.IEncodedId;
import me.creighton.encodedid.ILongEncoder;

import static me.creighton.encodedid.EncodedIdException.*;

public class LongEncoder extends EncodedId implements ILongEncoder {


  public LongEncoder(IEncodedId.Builder builder) {
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
    boolean isNegative = (id < 0);

    if (isNegative) {
      id *= -1; // encode the positive & track the sign; ie. signed magnitude
    }

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

    // Finally, we add the sign if the original string was negative.

    if (isNegative) {
      eId.append(NEGATIVE_SIGN);
    }

    // After optional padding, reverse the string to make it the right way.
    eId.reverse();

    // Now append check character if enabled

    if (checkedEncoder()) {
      String s = eId.toString();
      if (isNegative) {
        s = s.substring(1); // skip the negative sign
      }
      char checkCharacter = generateCheckCharacter(s);

      eId.append(checkCharacter);
    }

    return eId.toString();
  }

  @Override
  public long decodeId(String encodedId) throws EncodedIdException {
    long id = (null == encodedId || encodedId.length() == 0) ? -1 : 0;
    int nextVal;
    boolean isNegative = false; // assume positive id

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

    // Now check for the negative sign.
    if(s.charAt(0) == NEGATIVE_SIGN) {
      isNegative = true;
      s = s.substring(1); // trim the negative sign
    }


    // If isCheckedEncoder then the right-most character is the check character.
    // First validate that check character is correct, then decode a shorter length.

    if (checkedEncoder()) {
      if (! validateCheckCharacter(s)) {
        throwInvalidCheckCharacter(s);
        // !!! Never returns here !!!
      }
      s = s.substring(0, s.length() - 1);
    }

    for (int i = 0; i < s.length(); i++) {
      id *= numberBase();  // Shift current value in the accumulator
      nextVal = decodeChar(s.charAt(i));
      id += nextVal;          // Add in the value of the current character.
    }
    return id * (isNegative ? -1 : 1);
  }


}
