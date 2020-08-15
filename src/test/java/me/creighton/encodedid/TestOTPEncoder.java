package me.creighton.encodedid;

import me.creighton.encodedid.profiles.IOTPEncoder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestOTPEncoder {

  static IOTPEncoder encoder1; // All defaults
  static IOTPEncoder encoder2;

  @BeforeAll
  static void init () {
    encoder1 = IOTPEncoder.getBuilder().build();  // Take the defaults
    encoder2 = IOTPEncoder.getBuilder()
                            .setAlphabet("1730945862")
                            .setMinOtpId(0L)
                            .setMaxOtpId(9999L)
                            .build();
  }

  @AfterAll
  static void cleanup () {

  }

  @Test
  public void encodingTests () {
    String enc1 = encoder1.encode();
    String enc2 = encoder1.encode();
    String enc3 = encoder1.encode();
    String enc4 = encoder1.encode(575L);
    String enc5;

    assertNotNull(enc1);
    assertNotNull(enc2);
    assertNotNull(enc3);
    assertNotNull(enc4);

    System.out.printf("enc1 %s decoded as %d.\n", enc1, encoder1.decodeId(enc1));
    System.out.printf("enc2 %s decoded as %d.\n", enc2, encoder1.decodeId(enc2));
    System.out.printf("enc3 %s decoded as %d.\n", enc3, encoder1.decodeId(enc3));
    System.out.printf("enc4 %s decoded as %d.\n", enc4, encoder1.decodeId(enc4));

    assertEquals(575L, encoder1.decodeId(enc4));
/*
  Pattern for testing assertion:
  Assertions.assertThrows(NumberFormatException.class, () -> {
    Integer.parseInt("One");
  });
 */
    assertThrows(EncodedIdException.class, () -> {
      encoder1.encode(-1L);
    });

    assertThrows(EncodedIdException.class, () -> {
      encoder1.encode(encoder1.getLargestOtpId() + 1);
    });

    enc5 = encoder2.encode();
    long tmp = encoder2.decodeId(enc5);

    assertTrue(tmp >= encoder2.getSmallestOtpId() &&
        tmp <= encoder2.getLargestOtpId());

    enc5 = encoder2.encode(1L);
    tmp = encoder2.decodeId(enc5);
    assertEquals(1L, tmp);
    System.out.printf("enc5 %s decoded as %d.\n", enc5, tmp);

    enc5 = encoder2.encode(encoder2.getSmallestOtpId());
    tmp = encoder2.decodeId(enc5);
    assertEquals(encoder2.getSmallestOtpId(), tmp);
    System.out.printf("enc5 %s decoded as %d.\n", enc5, tmp);

    enc5 = encoder2.encode(encoder2.getLargestOtpId());
    tmp = encoder2.decodeId(enc5);
    assertEquals(encoder2.getLargestOtpId(), tmp);
    System.out.printf("enc5 %s decoded as %d.\n", enc5, tmp);

  }
}
