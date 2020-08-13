package me.creighton.encodedid.profiles;

public interface IOTPEncoder {

  String DEFAULT_ALPHABET = "4827105396";
  int DEFAULT_OTP_LENGTH = 6;
  int LONGEST_OTP_LENGTH = 19; // longest OTP length; length of largest long
  int DEFAULT_OTP_MAX_ID = 999999;
  long SMALLEST_OTP_ID = 0L;
  long LARGEST_OTP_ID = Long.MAX_VALUE; // must match LONGEST_OTP_LENGTH

  String getAlphabet ();
  int getOptLength ();
  long getSmallestOtpId();
  long getLargestOtpId();

  String encode ();
  String encode (long id);
  long decodeId(String encodedId);

  static IBuilder getBuilder() {
    return new OTPEncoder.Builder();
  }

  interface IBuilder {
    int getOtpLength ();
    String getAlphabet ();
    IBuilder setAlphabet (String alphabet);
    long getMinOtpId();
    IBuilder setMinOtpId(long minOtpCode);
    long getMaxOtpId();
    IBuilder setMaxOtpId(long maxOtpCode);
    IOTPEncoder build ();
  }
}
