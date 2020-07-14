package me.creighton.encodedid;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

import static me.creighton.encodedid.IAlphabet.LEGAL_URI_CHARACTER_SET;

public class Utilities {

  // Utility methods

  public static boolean isUrlCharacter (char c) {
    return LEGAL_URI_CHARACTER_SET.indexOf(c) > -1;
  }

  public static boolean isValidSeparator (char separator, String alphabet) {
    // The separator MUST NOT be in the alphabet.

    return alphabet.indexOf(separator) == -1;
  }

  public static Set<Character> stringToCharacterSet(String s) {
    Set<Character> set = new HashSet<>();
    for (char c : s.toCharArray()) {
      set.add(c);
    }
    return set;
  }


  public static boolean isValidAlphabet (String alphabet, String characterSet) {
    // Every character in alphabet must be in characterSet.

    if (null != alphabet && null != characterSet) {
      return stringToCharacterSet(characterSet).containsAll
          (stringToCharacterSet(alphabet));
    } else
      return false;
  }

  public static boolean isValidUriAlphabet (String alphabet) {
    // The alphabet MUST contain only legal URL characters.

    if (null != alphabet) {
      return stringToCharacterSet(LEGAL_URI_CHARACTER_SET).containsAll
          (stringToCharacterSet(alphabet));
    } else
      return false;
  }

  // Convenience method to let you easily scramble a character string for a new alphabet.
  // This is slightly modified from technojeeves:
  // http://technojeeves.com/index.php/58-scramble-a-string-in-java
  //
  public static String scramble (String s) {
    if (null == s)
      s = "";
    String [] scram = s.split("");
    List<String> letters = Arrays.asList(scram);
    Collections.shuffle(letters);
    StringBuilder sb = new StringBuilder(s.length());
    for (String c : letters) {
      sb.append(c);
    }
    return sb.toString();
  }

  /**
   * Unscramble does not restore the String that produced the scambled String.
   * This simply sorts the String
   * 
   * @param s is some String that was perhaps scrambled by hand or by scramble().
   * @return The sorted values of s. This does not really 'unscramble' the input,
   *          it simply sorts the input.
   */
  public static String unscramble (String s) {
    if (null == s)
      s = "";

    String [] unScram = s.split("");
    List<String> letters = Arrays.asList(unScram);
    Collections.sort(letters);
    StringBuilder sb = new StringBuilder(s.length());
    for (String c : letters) {
      sb.append(c);
    }
    return sb.toString();
  }

  public static long byteArrayToLong (byte [] b) {
    long l =
            ((long) b[7]  << 56)
          | ((long) b[6]  & 0xff) << 48
          | ((long) b[5]  & 0xff) << 40
          | ((long) b[4]  & 0xff) << 32
          | ((long) b[3]  & 0xff) << 24
          | ((long) b[2]  & 0xff) << 16
          | ((long) b[1]  & 0xff) << 8
          | ((long) b[0]  & 0xff);

    return l;
  }

  public static long longToByteArray (long l) {
    byte b[] = new byte[] {
        (byte)  l,
        (byte)  (l >> 8),
        (byte)  (l >> 16),
        (byte)  (l >> 24),
        (byte)  (l >> 32),
        (byte)  (l >> 40),
        (byte)  (l >> 48),
        (byte)  (l >> 56)
    };

    return l;
  }

  public static BigInteger uuidToBigInteger (UUID id) {
    BigInteger bId = BigInteger.ZERO;
    long msb, lsb;  // most significant bits, least significant bits.

    msb = id.getMostSignificantBits();
    lsb = id.getLeastSignificantBits();

    ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);  // buffer for the BigInteger msb/lsb

    byteBuffer.putLong(msb);
    byteBuffer.putLong(lsb);

    bId = new BigInteger(1, byteBuffer.array()); // treat msb as unsigned long (sign is positive)

    return bId;
  }

  public static UUID bigIntegerToUuid (BigInteger bId) {
    long msb, lsb;
// intermediate values for debug only.
    lsb = bId.longValue();
    msb = bId.shiftRight(64).longValue();

    UUID uuid =
        new UUID(msb, lsb);

    return uuid;
  }

}
