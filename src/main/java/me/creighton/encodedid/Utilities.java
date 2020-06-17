package me.creighton.encodedid;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.creighton.encodedid.IAlphabet.LEGAL_URI_CHARACTER_SET;

public class Utilities {

  // Utility methods

  public static Boolean isValidSeparator (char separator, String alphabet) {
    // The separator MUST be a legal URL character.
    // The separator MUST NOT be in the alphabet.

    boolean retVal = (LEGAL_URI_CHARACTER_SET.indexOf(separator) > -1 || separator == '-') &&
        alphabet.indexOf(separator) == -1;
    return retVal;
  }

  public static Boolean isValidAlphabet (String alphabet) {
    // The alphabet MUST contain only legal URL characters.
    boolean retVal = true;

    char [] charArray = alphabet.toCharArray();

    for (char ch : charArray) {
      if (! (retVal = LEGAL_URI_CHARACTER_SET.indexOf(ch) > -1))
        break;
    }
    return retVal;
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

}
