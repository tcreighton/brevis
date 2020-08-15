package me.creighton.encodedid;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.UUID;

import static me.creighton.encodedid.IAlphabet.*;
import static me.creighton.encodedid.Utilities.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUUIDEncoder {

  static IUuidEncoder encoder1;
  static IUuidEncoder encoder2;
  static IUuidEncoder encoder3;

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

    System.out.printf("Using standard encoding, UUID %s (len: %d)\nencodes as %s (len: %d)\n", uuid3.toString(), uuid3.toString().length(), s, s.length());
  }

  @Test
  public void encodingTests () {
    String s, sc;
    UUID c,d,e;

    System.out.println("\nUsing Tightly Encoded");
    d = UUID.randomUUID();

    s = encoder1.encodeId(d);
    sc = encoder2.encodeId(d);
    e = encoder1.decodeId(s);
    c = encoder2.decodeId(sc);
    System.out.printf("%s (len: %d) encodes as %s (len %d); \nwith check char as %s (len %d); decodes to \n%s\n",
                      d.toString(), d.toString().length(), s, s.length(), sc, sc.length(), e.toString());

    assertEquals(0, d.compareTo(e));
    assertEquals(0, d.compareTo(c));

  }
}
