package me.creighton.encodedid;

import me.creighton.encodedid.profiles.OTPEncoder;

public interface IOTPEncoder {


  String getAlphabet();

  int getOtpLength();

  long getMinOtpId();

  long getMaxOtpId();

  String encode();

  String encode(long id);

  long decodeId(String encodedId);

  static IOTPEncoder getOTPEncoder() {
    return new OTPEncoder.OTPEncoderBuilder().build();
  }

  static IOTPEncoder getOTPEncoder(String alphabet) {
    return new OTPEncoder.OTPEncoderBuilder()
            .withAlphabet(alphabet)
            .build();
  }

  static IOTPEncoderBuilder getOTPBuilder () {
    return new OTPEncoder.OTPEncoderBuilder();
  }

  interface IOTPEncoderBuilder {
    int getOtpLength();

    long getMinOtpId();

    long getMaxOtpId();

    IOTPEncoderBuilder withAlphabet(String alphabet);

    IOTPEncoderBuilder withLimits(long minId, long maxId);

    IOTPEncoder build();

  }

}

