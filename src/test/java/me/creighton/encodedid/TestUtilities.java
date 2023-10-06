package me.creighton.encodedid;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static me.creighton.encodedid.EncodingConstants.*;
import static me.creighton.encodedid.Utilities.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class TestUtilities {

    static UUID uuid1;


    @BeforeAll
    public static void init () {
        uuid1 = UUID.randomUUID(); // type 4 UUID

    }

    @AfterAll
    public static void cleanup () {

    }

    @Test
    public void testRandom () { // Trivial tests since getRandom is just a wrapper.

        // We are only testing longs but the same limits exist for int.

        testRandomLimit(0L, 100L);

        testRandomLimit(Long.MIN_VALUE, Long.MAX_VALUE);

        testRandomLimit(0L, 1L);

        assertThrows(IllegalArgumentException.class, () -> testRandomLimit(1L, 1L),
                "Error! Exception test expected to throw on testRandom where min == max.");

        assertThrows(IllegalArgumentException.class, () -> testRandomLimit(10L, 1L),
                "Error! Exception test expected to throw on testRandom where min < max.");


    }

    private void testRandomLimit (long min, long max) {
        long lValue;

        lValue = getRandomLong(min, max);
        assertTrue(String.format("%d is not >= %d or < %d!", lValue, min, max),
                lValue >= min && lValue < max);
    }

    @Test
    public void testIsUrlCharacter () {
        char c;

        c = 'A';
        assertTrue(String.format("%c is a valid character!", c), isUrlCharacter(c));
    }

    @Test
    public void testIsValidSeparator () {
        char c;

        c = '\\';
        assertTrue(String.format("%c is an invalid separator!", c), isValidSeparator(c, DEFAULT_ALPHABET));

        c = ':';
        assertTrue(String.format("%c is an invalid separator!", c), isValidSeparator(c, DEFAULT_ALPHABET));
        assertFalse(String.format("%c is an invalid separator!", c), isValidSeparator(c, BIG_ALPHABET));

    }

    @Test
    public void testStringToCharacterSet () {
        String s = "IsThisanaardvark?";
        Set<Character> cs1 = new HashSet<>();
        Set<Character> cs2;

        cs1.add('s');
        cs1.add('d');
        cs1.add('i');
        cs1.add('T');
        cs1.add('a');
        cs1.add('I');
        cs1.add('r'); // duplicate
        cs1.add('v');
        cs1.add('r'); // duplicate
        cs1.add('k');
        cs1.add('n');
        cs1.add('?');
        cs1.add('h');

        cs2 = stringToCharacterSet(s);

        assertEquals(cs1, cs2);
    }

    @Test
    public void testIsValidAlphabet () {

        assertTrue("BIG_ALPHABET is a valid alphabet of BASE_BIG_CHARACTER_SET!",
                isValidAlphabet(BIG_ALPHABET, BASE_BIG_CHARACTER_SET));

        assertTrue("DEFAULT_ALPHABET is a valid alphabet of BASE_DEFAULT_CHARACTER_SET",
                isValidAlphabet(DEFAULT_ALPHABET, BASE_DEFAULT_CHARACTER_SET));

        assertTrue("Scrambled DEFAULT_ALPHABET is a valid alphabet of BASE_DEFAULT_CHARACTER_SET",
                isValidAlphabet(scramble(DEFAULT_ALPHABET), BASE_DEFAULT_CHARACTER_SET));

        assertFalse("DEFAULT_ALPHABET + ':' is not valid of BASE_DEFAULT_CHARACTER_SET!",
                isValidAlphabet(DEFAULT_ALPHABET + ':', BASE_DEFAULT_CHARACTER_SET));

    }

    @Test
    public void testIsValidUriAlphabet () {
        boolean tf = isValidUriAlphabet(null);

        assertTrue("Null should be handled as a valid character!", isValidUriAlphabet(null));

        assertTrue("LEGAL_URI_CHARACTER_SET is safe for URL Paths.",
                isValidUriAlphabet(LEGAL_URI_CHARACTER_SET));

        assertFalse("SPECIAL_CHARACTERS are not always safe in paths!",
                isValidUriAlphabet(SPECIAL_CHARACTERS));

        assertFalse("BIG_ALPHABET includes characters that might not be safe in all URLs!",
                isValidUriAlphabet(BIG_ALPHABET));
    }

    @Test
    public void testByteArrayToLong () {
        long l1 = Integer.MAX_VALUE * 3L;
        long l2;
        byte [] b = longToByteArray(l1);

        l2 = byteArrayToLong(b);

        assertEquals(l1, l2);
    }

    @Test
    public void testLongToByteArray () {
        long l1 = Integer.MAX_VALUE * 2L;
        long l2;
        byte[] b = longToByteArray(l1);

        l2 = byteArrayToLong(b);

        assertEquals(l1, l2);
    }

    @Test
    public void testUuidToBigInteger () {

        BigInteger big = uuidToBigInteger(uuid1);
        long msb = big.shiftRight(64).longValue();
        long lsb = big.longValue();
        UUID uuid2 = new UUID(msb, lsb);

        assertEquals(0, uuid1.compareTo(uuid2));
    }

    @Test
    public void testBigIntegerToUuid () {
        BigInteger big = uuidToBigInteger(uuid1);
        UUID uuid2 = bigIntegerToUuid(big);

        assertEquals(0, uuid1.compareTo(uuid2));
    }
}
