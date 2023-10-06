package me.creighton.encodedid;

// One purpose of this is to make it easy to run the main method with various
// command line arguments. This way the test environment (args) are not lost with
// the IDE config since we don't put the IDE config into the source repository.

// Another purpose is to demonstrate various usages of the encoding classes.
// In both cases the purpose is not so much to run a unit test as it is to show
// some aspect of the library. As such we have named this class and its methods
// with the prefix Show (or show) rather than the traditional Test (or test).

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static me.creighton.encodedid.EncodingConstants.BASE_BIG_CHARACTER_SET;
import static me.creighton.encodedid.EncodingConstants.BASE_DEFAULT_CHARACTER_SET;
import static me.creighton.encodedid.IEncodedId.getEncodedIdBuilder;

public class ShowApp {
  public static final String testAlphabet1 = "8GQJ36WHNBY2L7MVK9ZFXTPR51CS4D";
  public static final String testAlphabet2 = "C_QTX04yH31:B7zfPh=mLwR8$d!ct2(Ws&nYqN';bgVlx*FjvS9M.)D56JrKGkpZ";
  public static final String testAlphabet3 = "C_QTX04yHi31:B7zfAPh=mLwR8I$d!cto2(Ws&neYaEqN'u;bgVlx*FjvS9M.O)D56JrKGkpZU";

  @BeforeAll
  public static void init () {

  }

  @AfterAll
  public static void cleanup () {

  }

  @Test
  public void show1 () {
    String [] args = {
        "-stats",
        "-scramble",
        BASE_DEFAULT_CHARACTER_SET    // This is an easy way to get a randomized alphabet from the base
    };


    App.main(args);
  }

  @Test
  public void show2 () {
    String [] args = {
        "-stats",
        "-scramble",
        BASE_BIG_CHARACTER_SET

    };

    App.main(args);
  }

  /*
    showSimpleRandom demonstrates a simple encoding of random long integers.
    Obviously you can generate your own random numbers easily enough. This
    simply provides a helper to generate random integers within your specified range.

    We use the testAlphabet1, but you could use the default alphabet or generate your own.
   */
  @Test
  public void showSimpleRandom () {
    final long big = (730L * 1000L * 1000L) - 1L;  // 729,999,999
    ILongEncoder simpleRandom = ILongEncoder.build(getEncodedIdBuilder()
        .separator(true)
        .segmentLength(3)
        .padWidth(6)
        .setMaxId(big) // set big to 20 Million
        .setMinId(0)
        .alphabet(testAlphabet1, testAlphabet1));
    ILongEncoder mediumRandom = ILongEncoder.build(getEncodedIdBuilder()
        .separator(true)
        .segmentLength(3)
        .padWidth(6)
        .setMaxId(big)
        .setMinId(0)
        .alphabet(testAlphabet2, testAlphabet2));
    ILongEncoder compactRandom = ILongEncoder.build(getEncodedIdBuilder()
        .setMaxId(big)
        .setMinId(0)
        .alphabet(testAlphabet3, testAlphabet3));
    long id;
    String simpleCode, mediumCode, compactCode;

    System.out.printf("\n********** Start Random **********\n");

    System.out.printf("Simple Decoded %s to %d.\n", "DDD-DDD", simpleRandom.decodeId("DDD-DDD"));
    System.out.printf("Simple Decoded %s to %d.\n\n", "DDD-DDD-D", simpleRandom.decodeId("DDD-DDD-D"));


    simpleCode = simpleRandom.encodeId(big);
    mediumCode = mediumRandom.encodeId(big);
    compactCode = compactRandom.encodeId(big);

    System.out.printf("Encoded %d as Simple: %s Medium: %s Compact: %s\n", big, simpleCode, mediumCode, compactCode);


    for (int i = 0; i < 10; i++) {  // generate 10 ids

      simpleCode = simpleRandom.encodeId();
      id = simpleRandom.decodeId(simpleCode);
      mediumCode = mediumRandom.encodeId(id);
      compactCode = compactRandom.encodeId(id);

      System.out.printf("Encoded %d as Simple: %s Medium: %s Compact: %s\n", id, simpleCode, mediumCode, compactCode);
    }

    System.out.printf("\n********** End Random **********\n");

  }

  /*
    The purpose of this test method is to demonstrate how the verification encoding might be
    used. There are lots of choices, but this shows a few.
   */
  @Test
  public void showVerification() {

  }

}
