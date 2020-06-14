package me.creighton.encodedid;

import static me.creighton.encodedid.IEncodedId.*;
import static me.creighton.encodedid.Utilities.*;

public class App {
  public static void stats() {
    System.out.printf("Base Default Alphabet (len is %d): %s\n", BASE_DEFAULT_ALPHABET.length(), BASE_DEFAULT_ALPHABET);
    System.out.printf("Default Alphabet (len is %d): %s\n", DEFAULT_ALPHABET.length(), DEFAULT_ALPHABET);
    System.out.printf("Base Big Alphabet (len is %d): %s\n", BASE_BIG_ALPHABET.length(), BASE_BIG_ALPHABET);
    System.out.printf("Big Alphabet (len is %d): %s\n", BIG_ALPHABET.length(), BIG_ALPHABET);

  }

  // Main method
  public static void main(String[] args) {

    long foo;
    String s;
    IEncodedId encodedId = IEncodedId.EncodedIdFactory();
    int i = 0;
    for (String arg : args) {
      if (arg.equals("-stats"))
        stats();

      if (arg.equals("-scramble") && i + 1 < args.length)
        System.out.printf("Scrambled %s is %s\n", args[i+1], scramble(args[i + 1]));

      if (arg.equals("-unscramble") && i + 1 < args.length)
        System.out.printf("Unscrambled %s is %s\n", args[i+1], unscramble(args[i + 1]));

      if (arg.equals("-encode") && i + 1 < args.length) {
        foo = Long.valueOf(args[i+1]);
        System.out.printf("%d encodes to %s\n", foo, s = encodedId.encodeId(foo));
        System.out.printf("%s decodes to %d\n", s, foo = encodedId.decodeId(s));
      }
      i++;
    }

  }

}
