package me.creighton.encodedid.profiles;

import me.creighton.encodedid.ILongEncoder;

import static me.creighton.encodedid.EncodedIdException.throwInvalidId;
import static me.creighton.encodedid.IEncodedId.getEncodedIdBuilder;
import static me.creighton.encodedid.Utilities.getRandomLong;

/*
  One Time Password
 */
public class OTPEncoder implements IOTPEncoder {


  /***
   * The actual limits on length of an OTP and value of an OTP code are somewhat
   * arbitrary. The typical use case is a random number in the range 0-999999, but
   * it could vary for specific uses. In any case, we are limiting it to the range
   * of positive, non-zero Long values.
   */
  private ILongEncoder longEncoder;

  // These attributes get values strictly from the builder.
  private int otpLength;
  private long minOtpId;
  private long maxOtpId;

  public OTPEncoder(Builder builder) {
    this.otpLength = builder.getOtpLength();
    this.minOtpId = builder.getMinOtpId();
    this.maxOtpId = builder.getMaxOtpId();

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

  public long getSmallestOtpId() {
    return this.minOtpId;
  }

  public long getLargestOtpId() {
    return this.maxOtpId;
  }


  /**
   * Encode a random number in the range 1:maxOtpId
   * @return String
   */
  @Override
  public String encode () {
    long min = this.minOtpId;
    long max = (this.maxOtpId < Long.MAX_VALUE) ? this.maxOtpId : this.maxOtpId;

    long id = getRandomLong(max, min);  //

    return this.encode(id);
  }

  @Override
  public String encode (long id) {
    if (id < this.minOtpId || id > this.maxOtpId) { // Can't be <= 0 for an OTP
      throwInvalidId(id);
    }

    return this.longEncoder.encodeId(id);
  }

  @Override
  public long decodeId (String encodedId) {
    return this.longEncoder.decodeId(encodedId);
  }

  static class Builder implements IOTPEncoder.IBuilder {
    private int otpLength;
    private long minOtpId;
    private long maxOtpId;
    private String alphabet;

    public Builder () {
      setAlphabet(DEFAULT_ALPHABET);
      setMinOtpId(SMALLEST_OTP_ID);
      setMaxOtpId(DEFAULT_OTP_MAX_ID);
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

    public long getMinOtpId() {
      return this.minOtpId;
    }

    public IBuilder setMinOtpId(long minOtpId) {
      this.minOtpId = (minOtpId >= 0 && minOtpId <= LARGEST_OTP_ID) ?
                          minOtpId : SMALLEST_OTP_ID;

      return this;
    }

    public long getMaxOtpId() {
      return this.maxOtpId;
    }

    public IBuilder setMaxOtpId(long maxOtpId) {
      // The maxOtpId determines the actual max otp length
      int len = Long.valueOf(maxOtpId).toString().length();

      if (maxOtpId < 9 || maxOtpId > LARGEST_OTP_ID)
        throwInvalidId(maxOtpId);

      this.maxOtpId = maxOtpId;
      setOtpLength(len);
      return this;
    }

    public IOTPEncoder build () {
      return new OTPEncoder(this);
    }
  }
}
