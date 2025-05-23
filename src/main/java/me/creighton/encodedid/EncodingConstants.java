package me.creighton.encodedid;


/**
 * We use two different terms to describe the character strings that form the basis for encoding and decoding.
 *
 * CHARACTER_SET is used to mean the list of characters that can be used in a given ALPHABET. There is no concept
 * of order within a CHARACTER_SET. Obviously there is an ordering because we create a String constant to contain
 * the characters within the set. However, we don't care about the ordering.
 *
 * ALPHABET is a term that indicates a particular ordering of a CHARACTER_SET. The encoding and decoding of an ID
 * is completely dependent on an ALPHABET. And so once there are encodings in the wild based on a particular ALPHABET
 * that ALPHABET CANNOT change. For
 */

public final class EncodingConstants {
    private EncodingConstants () {

    }


    public static final String LOWER_ALPHA = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHA = LOWER_ALPHA + UPPER_ALPHA;
    public static final String DIGIT = "0123456789";

    public static final String UNRESERVED_3986 = ALPHA + DIGIT + "-._~";
    public static final String SPECIAL = ":._~!;"; // '-' is excluded as default separator
    public static final String NOT_SAFE_FOR_URL = "?/#[]@";
    public static final String LEGAL_URI_CHARACTER_SET = ALPHA + DIGIT + SPECIAL;

    // The alphabets are given as constants here rather than calls to static Utilities methods
    // So they are easy to see the values and so that people can easily create new alphabets from them.
    // BIG_ALPHABET is just one of any number of scrambles. Don't replace the constant string with
    // anything other than another constant string. IOW, don't replace it with a call to Utilities.scramble.
    // In order to maintain compatibility with encodings in the wild, BIG_ALPHABET and DEFAULT_ALPHABET
    // MUST NOT change. You can create a new alphabet anytime you want, but be sure to use it for
    // all decoding after that. It is not too hard to annotate an encoding to indicate which alphabet
    // to use, but that's beyond the scope of this basic implementation.
    //
    // These Alphabets are made to work in a path element of a URL.
    // If you create your own alphabets and want them to work in a URL,
    // check them with isUrlAlphabet.
    //
    // This work attempts to comply with RFC 3986.
    //

    public static final String NUMBERS = DIGIT; // "0123456789";
    public static final String LOWER_CASE = LOWER_ALPHA; // "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_CASE = UPPER_ALPHA; // "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String SPECIAL_CHARACTERS = "!$&'()*.:;=_"; // could cause problems in some URL paths
    public static final String BASE_DEFAULT_CHARACTER_SET = "123456789BCDFGHJKLMNPQRSTVWXYZ";
    public static final String DEFAULT_ALPHABET = "PDGM4ZSCV8QRW3TYNK5FXB216H79LJ";
    public static final String MEDIUM_CHARACTER_SET = LOWER_CASE + UPPER_CASE + NUMBERS;
    public static final String BASE_BIG_CHARACTER_SET = LOWER_CASE + UPPER_CASE + NUMBERS + SPECIAL_CHARACTERS;
    public static final String BIG_ALPHABET = "TVWboQg4pGnE9w0rhSqFLKmdxZceNk5RBtUDf3iPvMX12OAslIu6yJCa8HYz7j.!;_:&$'()*="; // scrambled LEGAL_URI_ALPHABET

    /** Reserved characters **/
    public static final char NEGATIVE_SIGN = '~';
    public static final char DEFAULT_SEPARATOR = '-';

}
