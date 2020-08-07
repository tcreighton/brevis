package me.creighton.encodedid.impl;


import static me.creighton.encodedid.EncodedIdException.*;
import me.creighton.encodedid.EncodedIdException;
import me.creighton.encodedid.IBigIntegerEncoder;
import me.creighton.encodedid.IEncodedId;

import java.math.BigInteger;

public class BigIntegerEncoder extends EncodedId implements IBigIntegerEncoder {


  // Public Constructors

  public BigIntegerEncoder (IEncodedId.Builder builder) {
    super(builder);
  }

  // Getter overrides

  protected BigInteger bigNumberBase () {
    return BigInteger.valueOf(numberBase());
  }

  // Public Work Methods

  @Override
  public String encodeId (BigInteger id) throws EncodedIdException {

    String s = encodeIdWithoutSeparator(id);

    return addSeparators(s);
  }

  @Override
  public String encodeIdWithoutSeparator (BigInteger id) throws EncodedIdException {

    StringBuilder eId = new StringBuilder();
    int nextVal = 0;
    int countPads = 0;
    boolean isNegative = (id.compareTo(BigInteger.ZERO) < 0);

    if (isNegative) {
      id = id.abs(); // equivalent in this case to id *= -1
    }

    if (id.equals(BigInteger.ZERO)) {
      eId.append(encodeValue(0));       // encode that value
    }
    while (id.compareTo(BigInteger.ZERO) > 0) {
      nextVal = id.mod(bigNumberBase()).intValue(); // grab next value to encode - always int
      id = id.divide(bigNumberBase());              // reduce the source by one value size
      eId.append(encodeValue(nextVal));             // encode that value
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
  public BigInteger decodeId(String encodedId) throws EncodedIdException {
    BigInteger id = ( null == encodedId || encodedId.length() == 0) ?
                      BigInteger.valueOf(-1) :
                      BigInteger.ZERO;
    int nextVal;
    boolean isNegative = false; // assume positive id

    String s = encodedId;

    if (id.compareTo(BigInteger.ZERO) < 0) {
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
      id = id.multiply(bigNumberBase());        // Shift current value in the accumulator
      nextVal = decodeChar(s.charAt(i));
      id = id.add(BigInteger.valueOf(nextVal)); // Add in the value of the current character.
    }
    return id.multiply(isNegative ? BigInteger.valueOf(-1) : BigInteger.ONE);
  }

}
