package me.creighton.encodedid;

import org.junit.jupiter.api.*;

import static me.creighton.encodedid.IAlphabet.*;
import static me.creighton.encodedid.Utilities.unscramble;
import static org.junit.jupiter.api.Assertions.*;

public class TestEncodedId {

  static IEncodedId encoder1; // All defaults
  static IEncodedId encoder2; // All defaults + check character
  static IEncodedId encoder3; // Big alphabet & no separators & no pad
  static IEncodedId encoder4; // TightlyEncodedId (same as encoder3)
  static IEncodedId encoder5; // TightlyEncodedId (same as encoder3)

  static final long id0x00 = 0x00;
  static final long id1xff = 0xff;
  static final long id2x80000 = 0x80000;
  static final long id3xfffff = 0xfffff;

  @BeforeAll
  static void init () {

    encoder1 = IEncodedId.EncodedIdFactory();
    encoder2 = IEncodedId.EncodedIdFactory();
    encoder2.setCheckedEncoder(true);
    encoder3 = IEncodedId.EncodedIdFactory(IAlphabet.BIG_ALPHABET);
    encoder3.setUseSeparator(false);
    encoder3.setPadWidth(0);
    encoder4 = IEncodedId.TightlyEncodedIdFactory();
    encoder5 = IEncodedId.TightlyEncodedIdFactory();
    encoder5.setCheckedEncoder(true);
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
    String s, sc;

    System.out.println(id0x00 + " encodes as " + e_id0x00);
    System.out.println(id1xff + " encodes as " + e_id1xff);

    System.out.printf("Big Alphabet, no pad: %d encodes as %s\n", id2x80000, encoder3.encodeId(id2x80000));
    System.out.printf("Big Alphabet, no pad: %d encodes as %s\n", id3xfffff, encoder3.encodeId(id3xfffff));

    long c, d, e;
    System.out.println("\nUsing Default Alphabet");
    d = 0;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 100;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 1000;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 10000;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 100000;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 1 * 1000 * 1000;  // 1 million
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d *= 1000; // 1 billion
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d *= 1000; // 1 trillion
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = Long.MAX_VALUE;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);


    // Test Tight encoding


    System.out.println("\nUsing TightlyEncodedId:");
    d = 0;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 100;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 1000;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 10000;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 100000;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 1 * 1000 * 1000;  // 1 million

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d *= 1000; // 1 billion

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d *= 1000; // 1 trillion

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = Long.MAX_VALUE;
    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);


    try {
      d = 1000000000000L;
      s = encoder2.encodeId(d);
      e = encoder2.decodeId("DYB2-C9DM-QW");
      System.out.printf("1 trillion with bad check character encodes as %s; decodes to %d\n", s, e);
    } catch (EncodedIdException ex) {
      System.out.printf("\n!!! Caught exception: %s", ex.getMessage());
    }
    try {
      d = 1000L * 1000L * 1000L * 1000L;
      s = encoder5.encodeId(d);
      e = encoder5.decodeId("oxsrN185Mkz;W");
      System.out.printf("1 trillion with bad check character encodes as %s; decodes to %d\n", s, e);
    } catch (EncodedIdException ex) {
      System.out.printf("\n!!! Caught exception: %s", ex.getMessage());
    }

  }

}
