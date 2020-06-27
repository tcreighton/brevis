package me.creighton.encodedid;

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

}
