package me.creighton.encodedid;

import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static me.creighton.encodedid.IAlphabet.*;
import static me.creighton.encodedid.IEncodedId.getEncodedIdBuilder;
import static me.creighton.encodedid.IEncodedId.getTightlyEncodedIdBuilder;
import static me.creighton.encodedid.Utilities.unscramble;
import static org.junit.jupiter.api.Assertions.*;

public class TestLongEncoder {

  static final boolean SHOW = false;  // change this to allow demonstration code to display messages on console.

  static ILongEncoder encoder1; // All defaults
  static ILongEncoder encoder2; // All defaults + check character
  static ILongEncoder encoder3; // Big alphabet & no separators & no pad
  static ILongEncoder encoder4; // TightlyEncodedId (same as encoder3)
  static ILongEncoder encoder5; // TightlyEncodedId (same as encoder3)
  static final String ENCODES_AS = "encodes as";

  static final long id0x00 = 0x00;
  static final long id1xff = 0xff;
  static final long id2x80000 = 0x80000;
  static final long id3xfffff = 0xfffff;

  static final long idNegative5 = -5;
  static final long idNegative5Million = 1000 * 1000 * -5;

  @BeforeAll
  static void init () {

    encoder1 = ILongEncoder.build(getEncodedIdBuilder());
    encoder2 = ILongEncoder.build(
                getEncodedIdBuilder()
                .checkedEncoder(true));
    encoder3 = ILongEncoder.build(
                getEncodedIdBuilder(IAlphabet.BIG_ALPHABET, BASE_BIG_CHARACTER_SET)
                .separator(false)
                .padWidth(0));
    encoder4 = ILongEncoder.build(
                getTightlyEncodedIdBuilder());
    encoder5 = ILongEncoder.build(
                getTightlyEncodedIdBuilder()
                .checkedEncoder(true));
  }

  @AfterAll
  public static void cleanup () {

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

    Map<Character, Integer> map = new HashMap<>();

    // Ensure that there are no repeated characters in the character sets or alphabets.

    map.clear();
    for(char c : BASE_DEFAULT_CHARACTER_SET.toCharArray()) {
      assertNull(map.putIfAbsent(c, 1));  // If it's already there assertion fails.
    }

    map.clear();
    for(char c : DEFAULT_ALPHABET.toCharArray()) {
      assertNull(map.putIfAbsent(c, 1));  // If it's already there assertion fails.
    }

    map.clear();
    for(char c : BASE_BIG_CHARACTER_SET.toCharArray()) {
      assertNull(map.putIfAbsent(c, 1));  // If it's already there assertion fails.
    }

    map.clear();
    for(char c : BIG_ALPHABET.toCharArray()) {
      assertNull(map.putIfAbsent(c, 1));  // If it's already there assertion fails.
    }

    // Ensure that nobody is using a reserved character

    assertEquals(-1, BASE_DEFAULT_CHARACTER_SET.indexOf(DEFAULT_SEPARATOR));
    assertEquals(-1, BASE_DEFAULT_CHARACTER_SET.indexOf(NEGATIVE_SIGN));
    assertEquals(-1, BASE_BIG_CHARACTER_SET.indexOf(DEFAULT_SEPARATOR));
    assertEquals(-1, BASE_BIG_CHARACTER_SET.indexOf(NEGATIVE_SIGN));
  }

  @Test
  public void encodingTests () {
    String e_id0x00 = encoder1.encodeId(id0x00);
    String e_id1xff = encoder1.encodeId(id1xff);
    String e_neg_5 = encoder1.encodeId(idNegative5);
    String e_neg_5M = encoder1.encodeId(idNegative5Million);
    String e_checked_neg_5M = encoder2.encodeId(idNegative5Million);
    String s, sc;


    showMessage(String.format("%d %s %s", id0x00, ENCODES_AS, e_id0x00));
    showMessage(String.format("%d %s %s", id1xff, ENCODES_AS, e_id1xff));
    showMessage(String.format("%d %s %s", idNegative5, ENCODES_AS, e_neg_5));
    showMessage(String.format("%d %s %s", idNegative5Million, ENCODES_AS, e_neg_5M));
    showMessage(String.format("%d encodes checked as %s", idNegative5Million, e_checked_neg_5M));

    assertEquals(idNegative5, encoder1.decodeId(e_neg_5));
    assertEquals(idNegative5Million, encoder1.decodeId(e_neg_5M));
    assertEquals(idNegative5Million, encoder2.decodeId(e_checked_neg_5M));

    showMessage(String.format("%s %d %s", "Big Alphabet, no pad:", id2x80000, encoder3.encodeId(id2x80000)));
    showMessage(String.format("%s %d %s", "Big Alphabet, no pad:", id3xfffff, encoder3.encodeId(id3xfffff)));

    long c, d, e;
    showMessage(String.format("%s", "Using Default Alphabet"));
    d = 0;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = 100;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = 1000;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = 10000;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = 100000;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = 1 * 1000 * 1000;  // 1 million
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d *= 1000; // 1 billion
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d *= 1000; // 1 trillion
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);

    d = Long.MAX_VALUE;
    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
    assertEquals(d,e);
    assertEquals(d,c);


    // Test Tight encoding

    showMessage(String.format("Using TightlyEncodedId"));

    d = 0;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
//    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 100;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
//    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 1000;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
//    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 10000;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
//    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 100000;

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
//    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = 1 * 1000 * 1000;  // 1 million

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
//    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d *= 1000; // 1 billion

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
//    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d *= 1000; // 1 trillion

    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d %s %s; with check char as %s; decodes to %d", d, ENCODES_AS, s, sc, e));
//    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    d = Long.MAX_VALUE;
    s = encoder4.encodeId(d);
    sc = encoder5.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder5.decodeId(sc);
    showMessage(String.format("%d %s %s with check char as %s decodes to %d", d, ENCODES_AS, s, sc, e));
//    System.out.printf("%d encodes as %s; with check char as %s; decodes to %d\n", d, s, sc, e);
    assertEquals(d,e);
    assertEquals(d,c);

    final String tmp1, tmp2;
    EncodedIdException thrown;

    s = encoder2.encodeId(d);
    tmp1 = "KQFHHRWDYWGPC7"; // wrong check char encoding! Correct is KQFHHRWDYWGPC3
    thrown =
            assertThrows(EncodedIdException.class, () -> {
              encoder2.decodeId(tmp1);  // Should be: KQFHHRWDYWGPC3
            }, "EncodedIdException error (invalid check digit) excpected!!!");
    showMessage(thrown.getMessage());
    e = encoder2.decodeId(s);
    assertEquals(d, e);
    showMessage(String.format("The encoded ID, including check digit: %s We gave it %s", s, tmp1));

    s = encoder5.encodeId(d);
    tmp2 = "V;OeKx09cJQW";  // Should be V;OeKx09cJQP
    thrown =
            assertThrows(EncodedIdException.class, () -> {
              encoder5.decodeId(tmp2);  // Should be: V;OeKx09cJQP
            }, "EncodedIdException error (invalid check digit) excpected!!!");
    showMessage(thrown.getMessage());
    e = encoder5.decodeId(s);
    assertEquals(d, e);
    showMessage(String.format("The encoded ID, including check digit: %s. We gave it %s.", s, tmp2));


  }
  private void showMessage (String msg) {
    if (SHOW)
      System.out.printf("%s\n", msg);
  }
}
