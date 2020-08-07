package me.creighton.encodedid.profiles;

public interface IOTPEncoder {

  static final String DEFAULT_ALPHABET = "4827105396";
  static final int DEFAULT_OTP_LENGTH = 6;
  static final int LONGEST_OTP_LENGTH = 19; // longest OTP length; length of largest long
  static final int DEFAULT_OTP_MAX_CODE = 999999;
  static final long SMALLEST_OTP_CODE = 1L;
  static final long LARGEST_OTP_CODE  = Long.MAX_VALUE; // must match LONGEST_OTP_LENGTH

  public String getAlphabet ();
  public int getOptLength ();
  public long getSmallestOtpCode ();
  public long getLargestOtpCode ();

  public String encode ();
  public String encode (long code);
  public long decodeId (String encodedId);

  public static IBuilder getBuilder () {
    return new OTPEncoder.Builder();
  }

  interface IBuilder {
    int getOtpLength ();
    String getAlphabet ();
    IBuilder setAlphabet (String alphabet);
    long getMinOtpCode ();
    IBuilder setMinOtpCode (long minOtpCode);
    long getMaxOtpCode ();
    IBuilder setMaxOtpCode (long maxOtpCode);
    IOTPEncoder build ();
  }
}
