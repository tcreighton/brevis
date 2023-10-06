package me.creighton.encodedid;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static me.creighton.encodedid.EncodingConstants.*;
import static me.creighton.encodedid.IEncodedId.getEncodedIdBuilder;
import static me.creighton.encodedid.IEncodedId.getTightlyEncodedIdBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestBigIntegerEncoder {

  static final boolean SHOW = false;
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
                getEncodedIdBuilder(BIG_ALPHABET, BASE_BIG_CHARACTER_SET)
                .separator(false)
                .padWidth(0));
    encoder4 = IBigIntegerEncoder.build(
                getTightlyEncodedIdBuilder());
    encoder5 = IBigIntegerEncoder.build(
                getTightlyEncodedIdBuilder()
                .checkedEncoder(true));
  }

  @AfterAll
  public static void cleanup () {

  }

  @Test
  public void encodingTests () {
    String e_id0x00 = encoder1.encodeId(id0x00);
    String e_id1xff = encoder1.encodeId(id1xff);
    String e_neg_5 = encoder1.encodeId(idNegative5);
    String e_neg_5M = encoder1.encodeId(idNegative5Million);
    String e_checked_neg_5M = encoder2.encodeId(idNegative5Million);
    String e_neg_1000MaxLong = encoder1.encodeId(idNegative1000MaxLong);

    BigInteger bThousand        = BigInteger.valueOf(1000);
    BigInteger bTenThousand     = BigInteger.valueOf(10 * 1000);
    BigInteger bHundredThousand = BigInteger.valueOf(100 * 1000);
    BigInteger bMillion         = BigInteger.valueOf(1000L * 1000L);
    BigInteger bBillion         = BigInteger.valueOf(1000L * 1000L * 1000L);
    BigInteger bTrillion        = BigInteger.valueOf(1000L * 1000L * 1000L * 1000L);
    BigInteger longMaxTimes1000 = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(1000));


    String s, sc;

    showMessage(String.format("%d encodes as %s", id0x00, e_id0x00));
    showMessage(String.format("%d encodes as %s", id1xff, e_id1xff));
    showMessage(String.format("%d encodes as %s", idNegative5, e_neg_5));
    showMessage(String.format("%d encodes as %s", idNegative5Million, e_neg_5M));
    showMessage(String.format("%d encodes checked as %s", idNegative5Million, e_checked_neg_5M));
    showMessage(String.format("%d encodes as %s", idNegative1000MaxLong, e_neg_1000MaxLong));

    assertEquals(idNegative5, encoder1.decodeId(e_neg_5));
    assertEquals(idNegative5Million, encoder1.decodeId(e_neg_5M));
    assertEquals(idNegative5Million, encoder2.decodeId(e_checked_neg_5M));
    assertEquals(idNegative1000MaxLong, encoder1.decodeId(e_neg_1000MaxLong));

    showMessage("\n");
    showMessage(String.format("Big Alphabet, no pad: %d encodes as %s", id2x80000, encoder3.encodeId(id2x80000)));
    showMessage(String.format("Big Alphabet, no pad: %d encodes as %s", id3xfffff, encoder3.encodeId(id3xfffff)));
    showMessage("\n");

    BigInteger c, d, e;
    showMessage("Using Default Alphabet");
    showMessage("\n");

    d = BigInteger.ZERO;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(100);
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bThousand;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bTenThousand;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bHundredThousand;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bMillion;  // 1 million
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bBillion; // 1 billion
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bTrillion; // 1 trillion
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(Long.MAX_VALUE);
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = longMaxTimes1000;
    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);


    // Test Tight encoding

    showMessage("\n");
    showMessage("Using TightlyEncodedId:");
    d = BigInteger.ZERO;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(100);

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bThousand;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bTenThousand;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bHundredThousand;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bMillion;  // 1 million

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bBillion; // 1 billion

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bTrillion; // 1 trillion

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = BigInteger.valueOf(Long.MAX_VALUE);
    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = longMaxTimes1000;
    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d encodes as %s; with check char as %s; decodes to %d", d, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = bTrillion;  // test two encoders with 10^^12; test bad check char handling
    final String tmp1, tmp2;
    EncodedIdException thrown;

    showMessage("\n");
    s = encoder2.encodeId(d);
    showMessage(String.format("Encoded 1 trillion with check char: %s", s));
    tmp1 = "DYB2C9DMQX";  // Correct encoding is DYB2C9DMQG
    thrown =
            assertThrows(EncodedIdException.class, () -> {
              encoder2.decodeId(tmp1);  // Should be: DYB2C9DMQG
            }, "EncodedIdException error (invalid check digit) expected!!!");
    showMessage(thrown.getMessage());
    e = encoder2.decodeId(s);
    assertEquals(d, e);
    showMessage(String.format("The encoded ID, including check digit: %s We gave it %s", s, tmp1));

    showMessage("\n");
    s = encoder5.encodeId(d);
    showMessage(String.format("Encoded 1 trillion with check char: %s", s));
    tmp2 = "gglq&Di.";  // Correct encoding is gglq&Di;
    thrown =
            assertThrows(EncodedIdException.class, () -> {
              encoder2.decodeId(tmp2);  // Should be: gglq&Di;
            }, "EncodedIdException error (invalid check digit) expected!!!");
    showMessage(thrown.getMessage());
    e = encoder5.decodeId(s);
    assertEquals(d, e);
    showMessage(String.format("The encoded ID, including check digit: %s We gave it %s", s, tmp2));

  }

  private void showMessage (String msg) {
    if (SHOW)
      System.out.printf("%s\n", msg);
  }
}
