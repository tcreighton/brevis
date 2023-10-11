package me.creighton.encodedid;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestOTPEncoder {

  static final boolean SHOW = false;  // change this to allow demonstration code to display messages on console.
  static final long LOW_LIMIT = 0L;
  static final long HIGH_LIMIT_4_DIGITS = 9999L;
  static final long HIGH_LIMIT_8_DIGITS = 99999999L;
  static final long TEST_ID_1 = 892575L;
  static final long TEST_ID_2 = 48925735L;
  static final String ALPHABET = "1730945862";
  static final String ALPHABET2 = "4835201679PDGMZSCVQRTYNKFXBHLJ";

  static IOTPEncoder encoder1;
  static IOTPEncoder encoder2;
  static IOTPEncoder encoder3;
  static IOTPEncoder encoder4;

  @BeforeAll
  static void init () {
    encoder1 = IOTPEncoder.getOTPEncoder();  // Take the defaults
    encoder2 = IOTPEncoder.getOTPBuilder()
                            .withAlphabet(ALPHABET)
                            .withLimits(LOW_LIMIT, HIGH_LIMIT_4_DIGITS)
                            .build();
    encoder3 = IOTPEncoder.getOTPBuilder()
                            .withAlphabet(ALPHABET)
                            .withLimits(LOW_LIMIT, HIGH_LIMIT_8_DIGITS)
                            .build();
    encoder4 = IOTPEncoder.getOTPBuilder()
                            .withAlphabet(ALPHABET2)
                            .withLimits(LOW_LIMIT, HIGH_LIMIT_8_DIGITS)
                            .build();
  }

  @AfterAll
  static void cleanup () {

  }

  @Test
  public void encodingTests () {
    String msg = "";
    long id;

    String enc1 = encoder1.encode();
    String enc2 = encoder1.encode();
    String enc3 = encoder1.encode();
    String enc4 = encoder1.encode(TEST_ID_1);
    String enc6 = encoder3.encode(TEST_ID_2);
    String enc7 = encoder4.encode(TEST_ID_2);

    assertNotNull(enc1);
    assertNotNull(enc2);
    assertNotNull(enc3);
    assertNotNull(enc4);

    showMessage(String.format("enc1 %s decoded as %d.", enc1, encoder1.decodeId(enc1)));
    showMessage(String.format("enc2 %s decoded as %d.", enc2, encoder1.decodeId(enc2)));
    showMessage(String.format("enc3 %s decoded as %d.", enc3, encoder1.decodeId(enc3)));
    assertEquals(TEST_ID_1, encoder1.decodeId(enc4));
    showMessage(String.format("enc4 %s decoded as %d.", enc4, encoder1.decodeId(enc4)));
    assertEquals(TEST_ID_2, encoder3.decodeId(enc6));
    showMessage(String.format("enc6 %s decoded as %d.", enc6, encoder3.decodeId(enc6)));
    assertEquals(TEST_ID_2, encoder4.decodeId(enc7));
    showMessage(String.format("enc7 %s decoded as %d.", enc7, encoder4.decodeId(enc7)));


    assertEquals(TEST_ID_1, encoder1.decodeId(enc4));

    assertThrows(EncodedIdException.class, () -> {
      encoder1.encode(-1L);
    }, "EncodedIdException error (invalid check digit) expected!!!");

    assertThrows(EncodedIdException.class, () -> {
      encoder1.encode(encoder1.getMaxOtpId() + 1);
    }, "EncodedIdException error (id too large) expected!!!");

    enc6 = encoder2.encode();
    long tmp = encoder2.decodeId(enc6);

    assertTrue(tmp >= encoder2.getMinOtpId() &&
        tmp <= encoder2.getMaxOtpId());

    enc6 = encoder2.encode(1L);
    tmp = encoder2.decodeId(enc6);
    assertEquals(1L, tmp);
    showMessage(String.format("enc6 %s decoded as %d.", enc6, tmp));

    enc6 = encoder2.encode(encoder2.getMinOtpId());
    tmp = encoder2.decodeId(enc6);
    assertEquals(encoder2.getMinOtpId(), tmp);
    showMessage(String.format("enc6 %s decoded as %d.", enc6, tmp));

    enc6 = encoder2.encode(encoder2.getMaxOtpId());
    tmp = encoder2.decodeId(enc6);
    assertEquals(encoder2.getMaxOtpId(), tmp);
    showMessage(String.format("enc6 %s decoded as %d.", enc6, tmp));

    enc6 = encoder2.encode(); // get random number
    id = encoder2.decodeId(enc6);
    showMessage(String.format("Random number encoder2: %d encodes as %s", id, enc6));

  }

  private void showMessage (String msg) {
    if (SHOW) {
      System.out.printf("%s\n", msg);
    }
  }
}
