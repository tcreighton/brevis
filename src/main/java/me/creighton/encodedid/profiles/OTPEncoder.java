package me.creighton.encodedid.profiles;

import me.creighton.encodedid.ILongEncoder;

import static me.creighton.encodedid.EncodedIdException.throwInvalidId;
import static me.creighton.encodedid.IEncodedId.getEncodedIdBuilder;

/*
  One Time Password
 */
public class OTPEncoder implements IOTPEncoder {


  /***
   * The actual limits on length of an OTP and value of an OTP code are somewhat
   * arbitrary. The typical use case is a random number in the range 1-999999, but
   * it could vary for specific uses. In any case, we are limiting it to the range
   * of positive, non-zero Long values.
   */
  private ILongEncoder longEncoder;

  // These attributes get values strictly from the builder.
  private int otpLength;
  private long minOtpCode;
  private long maxOtpCode;

  public OTPEncoder(Builder builder) {
    this.otpLength = builder.getOtpLength();
    this.minOtpCode = builder.getMinOtpCode();
    this.maxOtpCode = builder.getMaxOtpCode();

    this.longEncoder = ILongEncoder.build(
        getEncodedIdBuilder(builder.getAlphabet())
            .padWidth(this.otpLength)
            .separator(false)
    );
  }

  public String getAlphabet () {
    return this.longEncoder.alphabet();
  }

  public int getOptLength () {
    return this.otpLength;
  }

  public long getSmallestOtpCode () {
    return this.minOtpCode;
  }

  public long getLargestOtpCode () {
    return this.maxOtpCode;
  }


  /**
   * Encode a random number in the range 1:maxOtpCode
   * @return String
   */
  @Override
  public String encode () {
    long min = this.minOtpCode;
    long max = (this.maxOtpCode < Long.MAX_VALUE) ? this.maxOtpCode + 1 : this.maxOtpCode;

    long code = (long)(Math.random()*(max-min)+min);

    return this.encode(code);
  }

  @Override
  public String encode (long code) {
    if (code < this.minOtpCode || code > this.maxOtpCode) { // Can't be <= 0 for an OTP
      throwInvalidId(code);
    }

    return this.longEncoder.encodeId(code);
  }

  @Override
  public long decodeId (String encodedId) {
    return this.longEncoder.decodeId(encodedId);
  }

  static class Builder implements IBuilder {
    private int otpLength;
    private long minOtpCode;
    private long maxOtpCode;
    private String alphabet;

    public Builder () {
      setAlphabet(DEFAULT_ALPHABET);
      setMinOtpCode(SMALLEST_OTP_CODE);
      setMaxOtpCode(DEFAULT_OTP_MAX_CODE);
    }

    public int getOtpLength () {
      return this.otpLength;
    }

    // Note that this method is not part of the public interface IBuilder
    private Builder setOtpLength (int length) {
      this.otpLength = length;
      return this;
    }

    public String getAlphabet () {
      return this.alphabet;
    }

    public IBuilder setAlphabet (String alphabet) {
      this.alphabet =
          (alphabet != null && alphabet.length() > 0) ? alphabet : DEFAULT_ALPHABET;

      return this;
    }

    public long getMinOtpCode () {
      return this.minOtpCode;
    }

    public IBuilder setMinOtpCode (long minOtpCode) {
      this.minOtpCode = (minOtpCode > 0 && minOtpCode <= LARGEST_OTP_CODE) ?
                          minOtpCode : SMALLEST_OTP_CODE;

      return this;
    }

    public long getMaxOtpCode () {
      return this.maxOtpCode;
    }

    public IBuilder setMaxOtpCode (long maxOtpCode) {
      // The maxOtpCode determines the actual max otp length
      int len = Long.valueOf(maxOtpCode).toString().length();

      if (maxOtpCode < 9 || maxOtpCode > LARGEST_OTP_CODE)
        throwInvalidId(maxOtpCode);

      this.maxOtpCode = maxOtpCode;
      setOtpLength(len);
      return this;
    }

    public IOTPEncoder build () {
      return new OTPEncoder(this);
    }
  }
}
