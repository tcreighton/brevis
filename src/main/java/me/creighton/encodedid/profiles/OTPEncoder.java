package me.creighton.encodedid.profiles;

import me.creighton.encodedid.IEncodedId;
import me.creighton.encodedid.ILongEncoder;
import me.creighton.encodedid.IOTPEncoder;

import static me.creighton.encodedid.EncodedIdException.throwInvalidId;
import static me.creighton.encodedid.EncodedIdException.throwInvalidRange;
import static me.creighton.encodedid.Utilities.getRandomLong;
import static me.creighton.encodedid.profiles.OTPConstants.*;

/*
  One Time Password
 */



public class OTPEncoder implements IOTPEncoder {


  /***
   * The actual limits on length of an OTP and value of an OTP code are somewhat
   * arbitrary. The typical use case is a random number in the range 0-999999, but
   * it could vary for specific uses. While this seems to be the most common length
   * used, there are certainly cases where 8 characters are used. And so the default
   * is 6 characters, but you can override that if you need to.
   *
   * In any case, we are limiting it to the range
   * of positive, non-zero Long values.
   */


  private ILongEncoder longEncoder;

  // These attributes get values strictly from the builder.
  private int otpLength;    // Length in characters of the encoded OTP
  private long minOtpId;    // minimum value of the OTP range: 0 <= minOtpId <= maxOtpId <= MAX_LONG
  private long maxOtpId;

  private OTPEncoder(OTPEncoderBuilder builder) {
    this.otpLength = builder.getOtpLength();
    this.minOtpId = builder.getMinOtpId();
    this.maxOtpId = builder.getMaxOtpId();

    this.longEncoder = ILongEncoder.build(
            IEncodedId.getEncodedIdBuilder(builder.getAlphabet())  // returns EncodedIdBuilder
//              .padWidth(this.otpLength)
              .separator(false)
    );
  }

  public String getAlphabet () {
    return this.longEncoder.alphabet();
  }

  public int getOtpLength() {
    return this.otpLength;
  }

  public long getMinOtpId() {
    return this.minOtpId;
  }

  public long getMaxOtpId() {
    return this.maxOtpId;
  }


  /**
   * Encode a random number in the range 1:maxOtpId
   * @return String
   */
  @Override
  public String encode () {
    long min = this.minOtpId;
    long max = this.maxOtpId;

    long id = getRandomLong(min, max);  //

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


  /**
   * OTPEncoder builder implementation
   */
  public static class OTPEncoderBuilder implements IOTPEncoder.IOTPEncoderBuilder {
    private int otpLength;
    private long minOtpId;
    private long maxOtpId;
    private String alphabet;

    public OTPEncoderBuilder () {
      // All attributes have default values so no required parameters in the constructor.
      // This can be overridden by the actual builder code.

      withAlphabet(DEFAULT_OTP_ALPHABET);
      withLimits(DEFAULT_OTP_MIN_ID, DEFAULT_OTP_MAX_ID);
    }

    @Override
    public int getOtpLength () {
      return this.otpLength;
    }

    // Note that this method is not part of the public interface IBuilder
    private OTPEncoderBuilder setOtpLength (int length) {
      this.otpLength = length;
      return this;
    }

    public String getAlphabet () {
      return this.alphabet;
    }

    @Override
    public OTPEncoderBuilder withAlphabet (String alphabet) {
      this.alphabet =
          (alphabet != null && alphabet.length() > 0) ? alphabet : DEFAULT_OTP_ALPHABET;

      return this;
    }

    @Override
    public long getMinOtpId() {
      return this.minOtpId;
    }

    private void setMinOtpId(long minOtpId) {
      this.minOtpId = (minOtpId >= 0 && minOtpId <= MAX_OTP_ID) ?
                          minOtpId : MIN_OTP_ID;

    }

    @Override
    public long getMaxOtpId() {
      return this.maxOtpId;
    }

    private void setMaxOtpId(long maxOtpId) {
      // The maxOtpId determines the actual max otp length
      int len = Long.valueOf(maxOtpId).toString().length();

      if (maxOtpId < 9 || maxOtpId > MAX_OTP_ID)
        throwInvalidId(maxOtpId);

      this.maxOtpId = maxOtpId;
      setOtpLength(len);
    }

    /**
     *
     * @return IOTPEncoderBuilder
     *
     * withLimits is how you control the min and max of your id space.
     * It has a side effect of setting the length of the encoded id.
     * The length is just the String.length() of the generated ID.
     *
     * Note that setLimits will throw EncodedIDException if the limits
     * don't make sense.
     */

    @Override
    public IOTPEncoderBuilder withLimits (long minId, long maxId) {

      if (minId < maxId - 10) { // We want to allow at least one digit!
        setMaxOtpId(maxId);
        setMinOtpId(minId);
      } else {
        throwInvalidRange(minId, maxId);
      }

      return this;
    }

    @Override
    public IOTPEncoder build () {
      return new OTPEncoder(this);
    }
  }
}
