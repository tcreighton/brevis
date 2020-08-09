package me.creighton.encodedid.profiles;

public class SplitOTPEncoder implements ISplitOTPEncoder {
  private String alphabet1;
  private String alphabet2;
  private IntStruct length;
  private IntStruct minOtpId;
  private IntStruct maxOtpId;
  private IOTPEncoder encoder1;
  private IOTPEncoder encoder2;

  SplitOTPEncoder (IBuilder builder) {
    this.length = builder.getLength();
    this.alphabet1 = builder.getAlphabet1();
    this.alphabet2 = builder.getAlphabet2();
    this.maxOtpId = builder.getMaxOtpId();
    this.minOtpId = builder.getMinOtpId();

    this.encoder1 = IOTPEncoder.getBuilder()
        .setAlphabet(getAlphabet1())
        .setMinOtpId(getSmallestOtpValues().value1)
        .setMaxOtpId(getLargestOtpValues().value1)
        .build();

    this.encoder2 = IOTPEncoder.getBuilder()
        .setAlphabet(getAlphabet2())
        .setMinOtpId(getSmallestOtpValues().value2)
        .setMaxOtpId(getLargestOtpValues().value2)
        .build();
  }

  public String getAlphabet1 () {
    return this.alphabet1;
  }

  public String getAlphabet2 () {
    return this.alphabet2;
  }

  public IntStruct getOtpLength () {
    return this.length;
  }

  public IntStruct getSmallestOtpValues () {
    return this.minOtpId;
  }

  public IntStruct getLargestOtpValues () {
    return this.maxOtpId;
  }


  // Get random values and encode them.
  public String encode () {
    return this.encoder1.encode() + this.encoder2.encode();
  }

  // Encode these and concatenate result
  public String encode (int id1, int id2) {
    return this.encoder1.encode(id1) + this.encoder2.encode(id2);
  }

  // Same as above, just a convenience method
  public String encode (IntStruct id) {
    return this.encode(id.value1, id.value2);
  }

  public IntStruct decode (String encodedId) {
    IntStruct foo = new IntStruct();
    IntStruct len = getOtpLength();
    String s1, s2;

    s1 = encodedId.substring(0, len.value1);
    s2 = encodedId.substring(len.value1);

    foo.value1 = (int) this.encoder1.decodeId(s1);
    foo.value2 = (int) this.encoder2.decodeId(s2);
    return foo;
  }

  static class Builder implements ISplitOTPEncoder.IBuilder {
    private String alphabet1;
    private String alphabet2;
    private IntStruct minOtpId;
    private IntStruct maxOtpId;
    private IntStruct length;

    public Builder () {

      setAlphabet(DEFAULT_ALPHABET_1, DEFAULT_ALPHABET_2);
      setMinOtpId(new IntStruct(0, 0));
      setMaxOtpId(new IntStruct(999, 999));
    }

    public String getAlphabet1 () {
      return this.alphabet1;
    }

    public String getAlphabet2 () {
      return this.alphabet2;
    }

    public ISplitOTPEncoder.IBuilder setAlphabet (String alphabet1, String alphabet2) {
      this.alphabet1 = alphabet1;
      this.alphabet2 = alphabet2;

      return this;
    }

    public IntStruct getMinOtpId () {
      return this.minOtpId;
    }

    private ISplitOTPEncoder.IBuilder setMinOtpId (IntStruct minOtpId) {
      this.minOtpId = minOtpId;

      return this;
    }

    public IntStruct getMaxOtpId () {
      return this.maxOtpId;
    }

    private ISplitOTPEncoder.IBuilder setMaxOtpId (IntStruct maxOtpId) {

      this.length = new IntStruct(Integer.valueOf(maxOtpId.value1).toString().length(),
                                  Integer.valueOf(maxOtpId.value2).toString().length());
      this.maxOtpId = maxOtpId;

      return this;
    }

    public IntStruct getLength () {
      return this.length;
    }

    public ISplitOTPEncoder build () {
      return new SplitOTPEncoder(this);
    }

  }
}
