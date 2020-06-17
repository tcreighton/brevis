package me.creighton.encodedid;

import org.junit.jupiter.api.*;

import static me.creighton.encodedid.IAlphabet.*;
import static me.creighton.encodedid.Utilities.unscramble;
import static org.junit.jupiter.api.Assertions.*;

public class TestEncodedId {

  static IEncodedId encoder1; // All defaults
  static IEncodedId encoder2; // Big alphabet & no separators & no pad
  static IEncodedId encoder3; // TightlyEncodedId (same as encoder2)

  static final long id0x00 = 0x00;
  static final long id1xff = 0xff;
  static final long id2x80000 = 0x80000;
  static final long id3xfffff = 0xfffff;

  @BeforeAll
  static void init () {

    encoder1 = IEncodedId.EncodedIdFactory();
    encoder2 = IEncodedId.EncodedIdFactory(IAlphabet.BIG_ALPHABET);
    encoder2.setUseSeparator(false);
    encoder2.setPadWidth(0);
    encoder3 = IEncodedId.TightlyEncodedIdFactory();
  }

  @Test
  public void constantsValidation () {
    String s1, s2;

    // Check that defined alphabets have all the same characters as its character set.

    s1 = unscramble(BASE_DEFAULT_CHARACTER_SET);
    s2 = unscramble(DEFAULT_ALPHABET);

    assertTrue(s1.equals(s2));

    s1 = unscramble(BASE_BIG_CHARACTER_SET);
    s2 = unscramble(BIG_ALPHABET);

    assertTrue(s1.equals(s2));

  }

  @Test
  public void encodingTests () {
    String e_id0x00 = encoder1.encodeId(id0x00);
    String e_id1xff = encoder1.encodeId(id1xff);
    String s;

    System.out.println(id0x00 + " encodes as " + e_id0x00);
    System.out.println(id1xff + " encodes as " + e_id1xff);

    System.out.printf("Big Alphabet, no pad: %d encodes as %s\n", id2x80000, encoder2.encodeId(id2x80000));
    System.out.printf("Big Alphabet, no pad: %d encodes as %s\n", id3xfffff, encoder2.encodeId(id3xfffff));

    long d, e;
    System.out.println("\nUsing Default Alphabet");
    d = 0;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d = 100;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d = 1000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d = 10000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d = 100000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d = 1 * 1000 * 1000;  // 1 million
    System.out.printf("1 million encodes as %s; decodes to %d\n", s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d *= 1000; // 1 billion
    System.out.printf("1 billion encodes as %s; decodes to %d\n", s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d *= 1000; // 1 trillion
    System.out.printf("1 trillion encodes as %s; decodes to %d\n", s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);

    System.out.println("\nDefault Alphabet with check digit.");

    encoder1.setCheckedEncoder(true);

    d = 0;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d = 100;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d = 1000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d = 10000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d = 100000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d = 1 * 1000 * 1000;  // 1 million
    System.out.printf("1 million encodes as %s; decodes to %d\n", s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d *= 1000; // 1 billion
    System.out.printf("1 billion encodes as %s; decodes to %d\n", s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);
    d *= 1000; // 1 trillion
    System.out.printf("1 trillion encodes as %s; decodes to %d\n", s = encoder1.encodeId(d), e = encoder1.decodeId(s));
    assertEquals(d,e);


    System.out.println("\nUsing TightlyEncodedId:");
    assertEquals(d,e);
    d = 0;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d = 100;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d = 1000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d = 10000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d = 100000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d = 1 * 1000 * 1000;  // 1 million
    System.out.printf("1 million encodes as %s; decodes to %d\n", s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d *= 1000; // 1 billion
    System.out.printf("1 billion encodes as %s; decodes to %d\n", s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d *= 1000; // 1 trillion
    System.out.printf("1 trillion encodes as %s; decodes to %d\n", s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);

    encoder3.setCheckedEncoder(true);
    System.out.println("\nUsing TightlyEncodedId with check character:");
    assertEquals(d,e);
    d = 0;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d = 100;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d = 1000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d = 10000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d = 100000;
    System.out.printf("%d encodes as %s; decodes to %d\n", d, s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d = 1 * 1000 * 1000;  // 1 million
    System.out.printf("1 million encodes as %s; decodes to %d\n", s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d *= 1000; // 1 billion
    System.out.printf("1 billion encodes as %s; decodes to %d\n", s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    d *= 1000; // 1 trillion
    System.out.printf("1 trillion encodes as %s; decodes to %d\n", s = encoder3.encodeId(d), e = encoder3.decodeId(s));
    assertEquals(d,e);
    try {
      System.out.printf("1 trillion with bad check character encodes as %s; decodes to %d\n", s = encoder3.encodeId(d), e = encoder3.decodeId("whakQUvW"));
    } catch (EncodedIdException ex) {
      System.out.printf("\n!!! Caught exception: %s", ex.getMessage());
    }
    try {
      System.out.printf("1 trillion with bad check character encodes as %s; decodes to %d\n", s = encoder3.encodeId(d), e = encoder3.decodeId("hwakQUvq"));
    } catch (EncodedIdException ex) {
      System.out.printf("\n!!! Caught exception: %s", ex.getMessage());
    }

  }

}
