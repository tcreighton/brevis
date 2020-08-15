package me.creighton.encodedid;

import me.creighton.encodedid.profiles.ISplitOTPEncoder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSplitOTPEncoder {

  static ISplitOTPEncoder encoder1; // All defaults

  @BeforeAll
  static void init() {

    encoder1 = ISplitOTPEncoder.getBuilder().build();
  }

  @AfterAll
  static void cleanup() {

  }

  @Test
  public void encodingTests() {

    String enc, enc1;

    ISplitOTPEncoder.IntStruct id, id1;

    enc = encoder1.encode();
    assertNotNull(enc);
    id = encoder1.decode(enc);
    assertNotNull(id);
    System.out.printf("Random encoding %s decoded as (%d, %d).\n", enc, id.value1, id.value2);
    enc1 = encoder1.encode(id.value1, id.value2);
    assertNotNull(enc1);
    assertEquals(enc, enc1);

    testEncodeDecode(encoder1, new ISplitOTPEncoder.IntStruct(345,14));
    testEncodeDecode(encoder1, new ISplitOTPEncoder.IntStruct(999,0));
    testEncodeDecode(encoder1, new ISplitOTPEncoder.IntStruct(666,666));
    testEncodeDecode(encoder1, new ISplitOTPEncoder.IntStruct(0,666));

  }

  private void testEncodeDecode (ISplitOTPEncoder encoder, ISplitOTPEncoder.IntStruct id) {
    String enc;
    ISplitOTPEncoder.IntStruct id1;

    assertNotNull(id);
    enc = encoder.encode(id);
    assertNotNull(enc);
    System.out.printf("(%d, %d) encoded as %s;", id.value1, id.value2, enc);
    id1 = encoder1.decode(enc);
    assertNotNull(id1);
    System.out.printf(" %s decoded as (%d, %d).\n", enc, id1.value1, id1.value2);
    assertTrue(id.equals(id1));

  }

}