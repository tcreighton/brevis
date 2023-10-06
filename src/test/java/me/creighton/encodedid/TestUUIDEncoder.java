package me.creighton.encodedid;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.UUID;

import static me.creighton.encodedid.Utilities.bigIntegerToUuid;
import static me.creighton.encodedid.Utilities.uuidToBigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUUIDEncoder {

  static final boolean SHOW = false;
  static IUuidEncoder encoder1;
  static IUuidEncoder encoder2;
  static IUuidEncoder encoder3;
  static IUuidEncoder encoder4;


  @BeforeAll
  public static void init () {
    encoder1 =
        IUuidEncoder.build(
            IEncodedId.getEncodedIdBuilder()
        );

    encoder2 =
        IUuidEncoder.build(
            IEncodedId.getTightlyEncodedIdBuilder()
            .checkedEncoder(true)
        );

    encoder3 =
        IUuidEncoder.build(
            IEncodedId.getEncodedIdBuilder()
            .separator(true)
            .segmentLength(8)
        );

    encoder4 =
        IUuidEncoder.build(
            IEncodedId.getTightlyEncodedIdBuilder()
        );
  }

  @AfterAll
  public static void cleanup () {

  }

  @Test
  public void conversionValidations () {

    UUID uuid1 = UUID.randomUUID();
    UUID uuid2;
    UUID uuid3;
    long umsb, ulsb, bmsb, blsb, t1, t2;

    BigInteger b;

    // Intermediate values for debug only.

    umsb = uuid1.getMostSignificantBits();
    ulsb = uuid1.getLeastSignificantBits();

    uuid2 = new UUID(umsb, ulsb);

    assertEquals(0, uuid1.compareTo(uuid2));

    b = uuidToBigInteger(uuid1);

    bmsb = b.shiftRight(64).longValue();
    blsb = b.longValue();

    uuid2 = bigIntegerToUuid(b);

    assertEquals(0, uuid1.compareTo(uuid2));


    uuid3 = UUID.randomUUID();

    String s = encoder3.encodeId(uuid3);

    showMessage("Using standard encoding:");
    showMessage(String.format("\tUUID %s (len: %d)", uuid3.toString(), uuid3.toString().length()));
    showMessage(String.format("\tEncodes as %s (len: %d)", s, s.length()));
  }

  @Test
  public void encodingTests () {
    String s, sc;
    UUID c,d,e;

//    System.out.println("\nUsing Tightly Encoded");
    d = UUID.randomUUID();

    s = encoder4.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder4.decodeId(s);
    c = encoder2.decodeId(sc);

    showMessage("Using tight encoding:");
    showMessage(String.format("\t%s (len: %d) \n\t\tencodes as \t\t\t%s (len %d)",
            d.toString(), d.toString().length(), s, s.length()));
    showMessage(String.format("\t\twith check char as \t%s (len %d) ",
            sc, sc.length()));
    showMessage(String.format("\t\tdecodes to \n\t%s", e.toString()));

    assertEquals(0, d.compareTo(e));
    assertEquals(0, d.compareTo(c));

  }

  private void showMessage (String msg) {
    if (SHOW) {
      System.out.printf("%s\n", msg);
    }
  }

}
