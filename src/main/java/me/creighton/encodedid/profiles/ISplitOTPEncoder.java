package me.creighton.encodedid.profiles;

/*
  The split OTP encoder works very much like the default
  OTP encoder. But with this, you don't have control over
  the size of the string - it's 6 characters wide. Also,
  the six characters really can be thought of as 2 3-character
  encodings, each with a different alphabet.

  You can override the default alphabets if you really want to.
  Also, you can provide your own two int codes. code1 is the
  most significant part of the encoding and code2 is the least.

  If you are asking why we should have this vs just use OTPEncoder
  and pass it a long id built from the two ints used here, this
  is the reason: This way it's harder to guess the value for 0
  that is used to left-pad the encoding. If you have two different
  alphabets they will look different from each other. For example,
  encoding the value 1L the default IOTPEncoder results in 444448.
  Encoding the values 0,1 using default ISplitOTPEncoder results in
  555774. Encoding values 1,1 results in 558774.

 */

public interface ISplitOTPEncoder {
  static final String DEFAULT_ALPHABET_1 = "5836942017";
  static final String DEFAULT_ALPHABET_2 = "7482195306";

  String getAlphabet1 ();
  String getAlphabet2 ();
  IntStruct getSmallestOtpValues ();
  IntStruct getLargestOtpValues ();

  String encode ();   // Get random values and encode them.
  String encode (int id1, int id2); // Encode these and concatenate result
  String encode (IntStruct id);    // Same as above, just a convenience method
  IntStruct decode (String encodedId);

  class IntStruct {
    public int value1 = 0;
    public int value2 = 0;

    public IntStruct () {

    }

    public IntStruct (int v1, int v2) {
      this.value1 = v1;
      this.value2 = v2;
    }

    @Override
    public boolean equals (Object o) {

      if (o == this)
        return true;
      if (! (o instanceof IntStruct))
        return false;

      IntStruct other = (IntStruct) o;
      boolean retVal = this.value1 == other.value1 && this.value2 == other.value2;

      return retVal;
    }
  }

  public static IBuilder getBuilder () {
    return new SplitOTPEncoder.Builder();
  }

  interface IBuilder {
    IntStruct getLength ();
    String getAlphabet1 ();
    String getAlphabet2 ();
    ISplitOTPEncoder.IBuilder setAlphabet (String alphabet1, String alphabet2);
    IntStruct getMinOtpId ();
    IntStruct getMaxOtpId ();
    ISplitOTPEncoder build ();
  }

}
