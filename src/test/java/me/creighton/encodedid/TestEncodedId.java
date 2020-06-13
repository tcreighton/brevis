package me.creighton.encodedid;

import org.junit.jupiter.api.*;

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
    encoder2 = IEncodedId.EncodedIdFactory(IEncodedId.BIG_ALPHABET);
    encoder2.setUseSeparator(false);
    encoder2.setPadWidth(0);
    encoder3 = IEncodedId.TightlyEncodedIdFactory();
  }

  @Test
  public void encodingTests () {
    String e_id0x00 = encoder1.encodeId(id0x00);
    String e_id1xff = encoder1.encodeId(id1xff);

    System.out.println(id0x00 + " encodes as " + e_id0x00);
    System.out.println(id1xff + " encodes as " + e_id1xff);

    System.out.printf("Big Alphabet, no pad: %d encodes as %s\n", id2x80000, encoder2.encodeId(id2x80000));
    System.out.printf("Big Alphabet, no pad: %d encodes as %s\n", id3xfffff, encoder2.encodeId(id3xfffff));

    long d;
    System.out.println("Using TightlyEncodedId:");
    d = 0;
    System.out.printf("%d encodes as %s\n", d, encoder3.encodeId(d));
    d = 100;
    System.out.printf("%d encodes as %s\n", d, encoder3.encodeId(d));
    d = 1000;
    System.out.printf("%d encodes as %s\n", d, encoder3.encodeId(d));
    d = 10000;
    System.out.printf("%d encodes as %s\n", d, encoder3.encodeId(d));
    d = 100000;
    System.out.printf("%d encodes as %s\n", d, encoder3.encodeId(d));
    d = 1 * 1000 * 1000;  // 1 million
    System.out.printf("1 million encodes as %s\n", encoder3.encodeId(d));
    d *= 1000; // 1 billion
    System.out.printf("1 billion encodes as %s\n", encoder3.encodeId(d));
    d *= 1000; // 1 trillion
    System.out.printf("1 trillion encodes as %s\n", encoder3.encodeId(d));
  }

}
