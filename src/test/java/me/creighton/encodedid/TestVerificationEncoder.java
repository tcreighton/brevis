package me.creighton.encodedid;

import me.creighton.encodedid.profiles.IVerificationEncoder;
import me.creighton.encodedid.profiles.VerificationEncoder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static me.creighton.encodedid.IAlphabet.BASE_BIG_CHARACTER_SET;
import static me.creighton.encodedid.IAlphabet.BIG_ALPHABET;
import static me.creighton.encodedid.profiles.IVerificationEncoder.ABSOLUTE_LARGEST_ID;
import static me.creighton.encodedid.profiles.IVerificationEncoder.ABSOLUTE_LAST_VALID_DATE;
import static org.junit.jupiter.api.Assertions.*;

public class TestVerificationEncoder {

  static VerificationEncoder encoder1;
  static VerificationEncoder encoder2;
  static VerificationEncoder encoder3;

  @BeforeAll
  static void init () {

    encoder1  = IVerificationEncoder.getBuilder().build(); // All defaults
    encoder2  = IVerificationEncoder.getBuilder()
        .setAlphabet("5BP6H9RXYL2VFDK8GJCM1ZNW73QTS4")
        .separator(true)
        .setMinId(0)
        .setMaxId(150*1000*1000) // Random space of 150 million
        .build();
    encoder3  = IVerificationEncoder.getBuilder()
        .setAlphabet(BIG_ALPHABET)
        .setCharacterSet(BASE_BIG_CHARACTER_SET)
        .separator(true)
        .setMinId(0)
        .setMaxId(150*1000*1000) // Random space of 150 million
        .build();

  }

  @AfterAll
  static void cleanup () {

  }

  @Test
  public void encoderTests () {
    long id = 10000;
    int daysValid = 15;
    LocalDate today = LocalDate.now();
    String encoding1;
    IVerificationEncoder.IVerifier verifier;

    encoding1 = encoder1.encode(id, daysValid);

    verifier = encoder1.decode(encoding1);

    assertEquals(id, verifier.getId());
    assertTrue(verifier.isValid());
    assertTrue(verifier.isValid(today.plusDays(15)));
    assertFalse(verifier.isValid(today.plusDays(16)));

    assertThrows(EncodedIdException.class, () -> {
      encoder1.encode(-1L);
    });

    assertThrows(EncodedIdException.class, () -> {
      encoder1.encode(ABSOLUTE_LARGEST_ID + 1);
    });

    assertThrows(EncodedIdException.class, () -> {
      encoder1.encode(12, ABSOLUTE_LAST_VALID_DATE.plusDays(1));
    });

    id = ABSOLUTE_LARGEST_ID;
    daysValid = (int) DAYS.between(LocalDate.now(), ABSOLUTE_LAST_VALID_DATE); // < 30000 days

    encoding1 = encoder1.encode(id, daysValid);
    verifier = encoder1.decode(encoding1);

    System.out.printf("Using alphabet: %s.\n", encoder1.getAlphabet());
    System.out.printf("Largest id: 0x%X, validation date: %s; Encoding1: %s\n",
        verifier.getId(), verifier.getValidationDate().toString(), encoding1);
    assertEquals(id, verifier.getId());
    assertTrue(verifier.isValid());

    System.out.printf("\nThe next three are using the following alphabet: %s\n", encoder2.getAlphabet());
    String encoding2;
    id = encoder2.getMinId();
    encoding2 = encoder2.encode(id, 3); // 3 days validity
    verifier = encoder2.decode(encoding2);

    System.out.printf("minId: 0x%X, validation date: %s; Encoding2: %s\n",
        verifier.getId(), verifier.getValidationDate().toString(), encoding2);
    assertEquals(id, verifier.getId());
    assertTrue(verifier.isValid());

    id = encoder2.getMaxId();
    encoding2 = encoder2.encode(id, 3); // 3 days validity
    verifier = encoder2.decode(encoding2);

    System.out.printf("maxId: 0x%X, validation date: %s; Encoding2: %s\n",
        verifier.getId(), verifier.getValidationDate().toString(), encoding2);
    assertEquals(id, verifier.getId());
    assertTrue(verifier.isValid());

    id = encoder2.getRandomId();
    encoding2 = encoder2.encode(id, 3); // 3 days validity
    verifier = encoder2.decode(encoding2);

    System.out.printf("random id: 0x%X, validation date: %s; Encoding2: %s\n",
        verifier.getId(), verifier.getValidationDate().toString(), encoding2);
    assertEquals(id, verifier.getId());
    assertTrue(verifier.isValid());

    String encoding3;
    System.out.printf("\nNow using Alphabet: %s.\n", encoder3.getAlphabet());

    id = encoder3.getMaxId();
    encoding3 = encoder3.encode(id, 3); // 3 days validity
    verifier = encoder3.decode(encoding3);

    System.out.printf("maxId: 0x%X, validation date: %s; Encoding3: %s\n",
        verifier.getId(), verifier.getValidationDate().toString(), encoding3);
    assertEquals(id, verifier.getId());
    assertTrue(verifier.isValid());

    id = encoder3.getRandomId();
    encoding3 = encoder3.encode(id, 3); // 3 days validity
    verifier = encoder3.decode(encoding3);

    System.out.printf("random id: 0x%X, validation date: %s; Encoding3: %s\n",
        verifier.getId(), verifier.getValidationDate().toString(), encoding3);
    assertEquals(id, verifier.getId());
    assertTrue(verifier.isValid());


  }

  @Test
  public void packingTests() {
    long packed;
    long id = 0x7F7F;
    IVerificationEncoder.IPacker packer = IVerificationEncoder.IPacker.getPacker();
    IVerificationEncoder.IVerifier verifier = IVerificationEncoder.IVerifier.getVerifier();// today
    IVerificationEncoder.IVerifier verifier2;

    verifier.setValidationDate(10)
            .setId(id);

    packed = packer.pack(verifier);
    verifier2 = packer.unpack(packed);

    assertTrue(verifier.equals(verifier2));
  }

  @Test
  public void verifierTests () {
    IVerificationEncoder.IVerifier verifier = IVerificationEncoder.IVerifier.getVerifier();

    assertThrows(EncodedIdException.class, () -> {
      verifier.setValidationDate(ABSOLUTE_LAST_VALID_DATE.plusDays(3));
    });

    assertThrows(EncodedIdException.class, () -> {
      verifier.setId(-1);
    });

    assertThrows(EncodedIdException.class, () -> {
      verifier.setId(ABSOLUTE_LARGEST_ID+1);
    });


    verifier.setId(10000)
            .setValidationDate(15);


    assertTrue(verifier.getValidationDate().isEqual(LocalDate.now().plusDays(15)));

    verifier.setValidationDate(-5);

    assertTrue(verifier.getValidationDate().isEqual(LocalDate.now().plusDays(-5)));

    verifier.setValidationDate(LocalDate.now().plusDays(3));

    assertTrue(verifier.isValid());                             // Today is valid against 3 days from now
    assertTrue(verifier.isValid(LocalDate.now().plusDays(-33)));// 33 days ago is valid against 3 days from now
    assertTrue(verifier.isValid(LocalDate.now().plusDays(3)));  // 3 days from now is valid against 3 days from now
    assertFalse(verifier.isValid(LocalDate.now().plusDays(4))); // 4 days from now is not valid against 3 days from now

    verifier.setValidationDate(ABSOLUTE_LAST_VALID_DATE);
    assertTrue(verifier.isValid(ABSOLUTE_LAST_VALID_DATE));
    assertFalse(verifier.isValid(ABSOLUTE_LAST_VALID_DATE.plusDays(1)));
  }
}
