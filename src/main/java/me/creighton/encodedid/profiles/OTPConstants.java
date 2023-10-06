package me.creighton.encodedid.profiles;

public final class OTPConstants {

    private OTPConstants () {

    }
    // Constants

    static final String DEFAULT_OTP_ALPHABET = "4827105396";

    static final long DEFAULT_OTP_MIN_ID = 111L;
    static final long DEFAULT_OTP_MAX_ID = 999999L;
    static final long MIN_OTP_ID = 0L;
    static final long MAX_OTP_ID = Long.MAX_VALUE; // must match LONGEST_OTP_LENGTH


}
