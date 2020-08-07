package me.creighton.encodedid;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static me.creighton.encodedid.IAlphabet.*;
import static me.creighton.encodedid.IEncodedId.getEncodedIdBuilder;
import static me.creighton.encodedid.IEncodedId.getTightlyEncodedIdBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBigIntegerEncoder {


  static IBigIntegerEncoder encoder1; // All defaults
  static IBigIntegerEncoder encoder2; // All defaults + check character
  static IBigIntegerEncoder encoder3; // Big alphabet & no separators & no pad
  static IBigIntegerEncoder encoder4; // TightlyEncodedId (same as encoder3)
  static IBigIntegerEncoder encoder5; // TightlyEncodedId (same as encoder3)

  static final BigInteger id0x00 = BigInteger.ONE;
  static final BigInteger id1xff = BigInteger.valueOf(0xff);
  static final BigInteger id2x80000 = BigInteger.valueOf(0x80000);
  static final BigInteger id3xfffff = BigInteger.valueOf(0xfffff);

  static final BigInteger idNegative5 = BigInteger.valueOf(-5);
  static final BigInteger idNegative5Million = BigInteger.valueOf(1000 * 1000 * -5);
  static final BigInteger idNegative1000MaxLong = BigInteger.valueOf(Long.MAX_VALUE * 1000);

  @BeforeAll
  static void init () {

    encoder1 = IBigIntegerEncoder.build(
                getEncodedIdBuilder());
    encoder2 = IBigIntegerEncoder.build(
                getEncodedIdBuilder()
                .checkedEncoder(true));
    encoder3 = IBigIntegerEncoder.build(
                getEncodedIdBuilder(IAlphabet.BIG_ALPHABET, BASE_BIG_CHARACTER_SET)
                .separator(false)
                .padWidth(0));
    encoder4 = IBigIntegerEncoder.build(
                getTightlyEncodedIdBuilder());
    encoder5 = IBigIntegerEncoder.build(
                getTightlyEncodedIdBuilder()
                .checkedEncoder(true));
  }

  @Test
  public void encodingTests () {
    String e_id0x00 = encoder1.encodeId(id0x00);
    String e_id1xff = encoder1.encodeId(id1xff);
    String e_neg_5 = encoder1.encodeId(idNegative5);
    String e_neg_5M = encoder1.encodeId(idNegative5Million);
    String e_checked_neg_5M = encoder2.encodeId(idNegative5Million);
    String e_neg_1000MaxLong = encoder1.encodeId(idNegative1000MaxLong);

    String s, sc;

    System.out.println(id0x00 + " encodes as " + e_id0x00);
    System.out.println(id1xff + " encodes as " + e_id1xff);
    System.out.println(idNegative5 + " encodes as " + e_neg_5);
    System.out.println((idNegative5Million + " encodes as " + e_neg_5M));
    System.out.println((idNegative5Million + " encodes checked as " + e_checked_neg_5M));
    System.out.println((idNegative1000MaxLong + " encodes as " + e_neg_1000MaxLong));

    assertEquals(idNegative5, encoder1.decodeId(e_neg_5));
    assertEquals(idNegative5Million, encoder1.decodeId(e_neg_5M));
    assertEquals(idNegative5Million, encoder2.decodeId(e_checked_neg_5M));
    assertEquals(idNegative1000MaxLong, encoder1.decodeId(e_neg_1000MaxLong));

    System.out.printf("Big Alphabet, no pad: %d encodes as %s\n", id2x80000, encoder3.encodeId(id2x80000));
    System.out.printf("Big Alphabet, no pad: %d encodes as %s\n", id3xfffff, encoder3.encodeId(id3xfffff));

    BigInteger c, d, e;
    System.out.println("\nUsing Default Alphabet");
    d = BigInteger.ZERO;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(100);
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(1000);
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(10000);
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(100000);
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(1 * 1000 * 1000);  // 1 million
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = d.multiply(BigInteger.valueOf(1000)); // 1 billion
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = d.multiply(BigInteger.valueOf(1000)); // 1 trillion
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(Long.MAX_VALUE);
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(1000));
    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);


    // Test Tight encoding


    System.out.println("\nUsing TightlyEncodedId:");
    d = BigInteger.ZERO;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(100);

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(1000);

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(10000);

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(100000);

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(1 * 1000 * 1000);  // 1 million

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = d.multiply(BigInteger.valueOf(1000)); // 1 billion

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d.multiply(BigInteger.valueOf(1000)); // 1 trillion

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(Long.MAX_VALUE);
    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(1000));
    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);


    try {
      d = BigInteger.valueOf(1000000000000L);
      s = encoder2.encodeId(d);
      e = encoder2.decodeId("DYB2-C9DM-QW");
      System.out.printf("1 trillion with bad check character encodes as %s; decodes to %d\n", s, e);
    } catch (EncodedIdException ex) {
      System.out.printf("\n!!! Caught exception: %s", ex.getMessage());
    }
    try {
      d = BigInteger.valueOf(1000L * 1000L * 1000L * 1000L);
      s = encoder5.encodeId(d);
      e = encoder5.decodeId("oxsrN185Mkz;W");
      System.out.printf("1 trillion with bad check character encodes as %s; decodes to %d\n", s, e);
    } catch (EncodedIdException ex) {
      System.out.printf("\n!!! Caught exception: %s", ex.getMessage());
    }

  }

}
