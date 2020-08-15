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

import static me.creighton.encodedid.IAlphabet.BASE_DEFAULT_CHARACTER_SET;

public class ShowApp {
  public static final String testAlphabet = "8GQJ36WHNBY2L7MVK9ZFXTPR51CS4D";

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

  /*
    The purpose of this test method is to demonstrate how the verification encoding might be
    used. There are lots of choices, but this shows a few.
   */
  @Test
  public void showVerification() {

  }

}
