package me.creighton.encodedid;

import org.jetbrains.annotations.Contract;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.*;

import static me.creighton.encodedid.EncodingConstants.LEGAL_URI_CHARACTER_SET;

public class Utilities {

  static final SecureRandom SECURE_RANDOM = new SecureRandom();


  // Utility methods

  public static long getRandomLong (long min, long max) {
    long l = SECURE_RANDOM.nextLong(min, max);  // min is inclusive, max is exclusive!
    return l;
  }

  public static int getRandomInt (int min, int max) {
    int i = SECURE_RANDOM.nextInt(min, max);  // min is inclusive, max is exclusive!
    return i;
  }

  /*
    This tests whether a char value is valid in an alphabet based on LEGAL_URI_CHARACTER_SET.
    It does not determine validity based on RFC 3986. The purpose is to limit characters that
    will work well in an encoding that is to be used within a URL.
   */
  public static boolean isUrlCharacter (char c) {
    return (LEGAL_URI_CHARACTER_SET.indexOf(c) > -1);
  }

  @Contract(pure = true)
  public static boolean isValidSeparator (char separator, String alphabet) {
    if (null == alphabet) {
      alphabet = "";    // Note that this means any separator value is valid.
    }
    // The separator MUST NOT be in the alphabet.

    return alphabet.indexOf(separator) == -1;
  }

  public static Set<Character> stringToCharacterSet(String s) {
    if (null == s) {
      s = "";
    }
    Set<Character> set = new HashSet<>();
    for (char c : s.toCharArray()) {
      set.add(c);
    }
    return set;
  }


  public static boolean isValidAlphabet (String alphabet, String characterSet) {
    // Every character in alphabet must be in characterSet.

    if (null == alphabet) {
      alphabet = "";
    }

    if (null == characterSet) {
      characterSet = "";
    }
    return stringToCharacterSet(characterSet).containsAll
            (stringToCharacterSet(alphabet));
  }

  public static boolean isValidUriAlphabet (String alphabet) {
    // The alphabet MUST contain only legal URL characters.

    if (null == alphabet) {
      alphabet = "";
    }

    return stringToCharacterSet(LEGAL_URI_CHARACTER_SET).containsAll
          (stringToCharacterSet(alphabet));
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
   * Unscramble does not restore the String that produced the scrambled String.
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

  public static byte [] longToByteArray (long l) {
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

    return b;
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
