package me.creighton.encodedid.profiles;

import me.creighton.encodedid.EncodedIdException;
import me.creighton.encodedid.IEncodedId;
import me.creighton.encodedid.ILongEncoder;
import me.creighton.encodedid.IVerificationEncoder;


import java.time.LocalDate;

import static me.creighton.encodedid.EncodedIdException.throwInvalidId;
import static me.creighton.encodedid.EncodedIdException.throwInvalidTargetDate;
import static me.creighton.encodedid.EncodingConstants.BASE_DEFAULT_CHARACTER_SET;
import static me.creighton.encodedid.EncodingConstants.DEFAULT_ALPHABET;
import static me.creighton.encodedid.IEncodedId.getEncodedIdBuilder;
import static me.creighton.encodedid.Utilities.getRandomLong;

/**
 * The purpose of the VerificationCode class is to make it easy to use Brevis to produce
 * verification codes such as what one sees during account verification. These can also
 * be used as one-time passwords, although a bit more infrastructure would be needed to
 * ensure that it is used at most one time.
 *
 * Among the options for using this class is that you can specify how many days from 'today'
 * you would like the verification code to be valid. A validation method on this class
 * lets you validate an encoding. This usage includes giving a base identifier as an integer
 * and a day count as an integer. The day count must be in the range of 0-255 and the base
 * identifier must be in the range 0-16777215 (0xFFFFFF). The rational is that we want to
 * have a relatively small number to encode so we get a short encoding even with a small
 * encoding base (alphabet).
 *
 * Over time we may want to change the algorithm or the alphabet used. To facilitate this,
 * we implement a 'flag' character as the last (right-most) character of the encoded
 * string. Strictly speaking, this is outside of the encoding since it is not in any
 * way based on the base id or the valid range. The flag character is an encoded number
 * in the range 0-29 encoded using the DEFAULT_ALPHABET. The numbers 0-14 are reserved
 * for future use by this code. The numbers 15-29 may be used by 'outside' derivations
 * of VerificationCode. Basically we use this flag to determine how to decode the rest
 * of the encoded id and how to interpret the decoded data.
 */
public class VerificationEncoder implements IVerificationEncoder {

  ILongEncoder longEncoder;
  int daysValid;

  VerificationEncoder (IBuilder builder) {

    setDaysValid(builder.getDaysValid());
    IEncodedId.Builder encodedIdBuilder = getEncodedIdBuilder(builder.getAlphabet(),
                                                              builder.getCharacterSet())
                                          .separator(builder.useSeparator())
                                          .padWidth(builder.getPadWidth())
                                          .setMinId(builder.getMinId())
                                          .setMaxId(builder.getMaxId());

    this.longEncoder = ILongEncoder.build(encodedIdBuilder);
  }

  protected int getDaysValid () {
    return this.daysValid;
  }

  protected void setDaysValid (int daysValid) {
    this.daysValid = daysValid;
  }

  protected ILongEncoder getEmbeddedEncoder () {
    return this.longEncoder;
  }

  @Override
  public long getRandomId () {
    long max = Long.MAX_VALUE > getMaxId() ? getMaxId() + 1 : Long.MAX_VALUE;
    long id = getRandomLong(getMinId(), max);

    return id;
  }

  @Override
  public String encode (long id) {

    return encode(id, getDaysValid());
  }

  @Override
  public String encode (long id, int daysValid) {
    IVerifier verifier = new Verifier()
                              .setValidationDate(daysValid)
                              .setId(id);

    return encode(verifier);
  }

  @Override
  public String encode (long id, LocalDate validUntilDate) throws EncodedIdException {
    IVerifier verifier = new Verifier()
        .setValidationDate(validUntilDate)
        .setId(id);

    return encode(verifier);
  }


  @Override
  public String encode (IVerifier verifier) throws EncodedIdException {
    long code = new Packer().pack(verifier);

    return getEmbeddedEncoder().encodeId(code);
  }

  @Override
  public IVerifier decode(String verificationCode) throws EncodedIdException {
    IVerifier verifier = new Packer().unpack(getEmbeddedEncoder().decodeId(verificationCode));

    return verifier;
  }

  @Override
  public String getAlphabet () {
    return getEmbeddedEncoder().alphabet();
  }

  @Override
  public String getCharacterSet () {
    return getEmbeddedEncoder().characterSet();
  }

  @Override
  public long getMinId () {
    return getEmbeddedEncoder().getMinId();
  }

  @Override
  public long getMaxId () {
    return getEmbeddedEncoder().getMaxId();
  }

  @Override
  public IVerificationEncoder setMinId (long minId) {
    if (minId < 0) {
      throwInvalidId(minId);  // DOESN'T RETURN!!!
    }

    getEmbeddedEncoder().setMinId(minId);

    return this;
  }

  @Override
  public IVerificationEncoder setMaxId (long maxId) {
    if (maxId > ABSOLUTE_LARGEST_ID) {
      throwInvalidId(maxId);  // DOESN'T RETURN!!!
    }

    getEmbeddedEncoder().setMaxId(maxId);

    return this;
  }


  //============================================

  public static IVerifier getVerifier () { return new Verifier(); }
  static class Verifier implements IVerifier {

    private long id = 0;
    private LocalDate validationDate = LocalDate.now();

    public Verifier () {

    }

    @Override
    public long getId () {
      return this.id;
    }

    @Override
    public IVerifier setId (long id) {

      if (id > ABSOLUTE_LARGEST_ID || id < 0) {
        throwInvalidId(id);           // DOESN'T RETURN!!!
      }
      this.id = id;

      return this;
    }

    @Override
    public LocalDate getValidationDate () {
      return this.validationDate;
    }

    @Override
    public IVerifier setValidationDate (LocalDate validationDate) {
      if (validationDate.isAfter(ABSOLUTE_LAST_VALID_DATE)) {
        throwInvalidTargetDate(validationDate);   // DOESN'T RETURN!!!
      }
      this.validationDate = validationDate;

      return this;
    }

    @Override
    public IVerifier setValidationDate (int daysFromToday) {
      this.validationDate = LocalDate.now().plusDays(daysFromToday);

      return this;
    }

    @Override
    public boolean isValid () {
      return isValid(LocalDate.now());
    }

    @Override
    public boolean isValid (LocalDate then) {
      return then.compareTo(getValidationDate()) <= 0;
    }

    @Override
    public boolean equals (Object o) {

      if (o == this)
        return true;
      if (! (o instanceof IVerificationEncoder.IVerifier))
        return false;

      IVerificationEncoder.IVerifier other = (IVerificationEncoder.IVerifier) o;
      boolean retVal =  getValidationDate().isEqual(other.getValidationDate()) &&
                        getId() == other.getId();

      return retVal;
    }

  }


  /**
   * Packer is responsible for packing and unpacking the contents of a IVerifier
   * instance into/out of a long.
   *
   * The id is encoded in the 48 high-order bits
   * The year is encoded in 7 bits
   * The month (0-11) is encoded in 4 bits
   * The day (0-30) is encoded in 5 bits
   * Total: 16 bits
   *
   * |YYYYYYYMMMMDDDDD|
   *
   */
  protected static final long ID_SHIFT = 16;   // ID gets upper 48 bits.
  protected static final long YEAR_SHIFT = 9;
  protected static final long MONTH_SHIFT = 5;
  protected static final long DAY_SHIFT = 0;   // Just for symmetry

  protected static final long ID_MASK = 0x7FFFFFFFFFFFL; // 47 bits for the id
  protected static final long YEAR_MASK = 0x7F;  // 7 bits for the year 0-99
  protected static final long MONTH_MASK = 0x0F; // 4 bits for the month 1-12
  protected static final long DAY_MASK = 0x1F;   // 5 bits for the day 1-31

  //============================================

  public static IPacker getPacker () { return new Packer(); }
  static class Packer implements IPacker {

    @Override
    public long pack (IVerifier verifier) {
      long pack = 0;

      // Pack the id.

      pack |= (((verifier.getId()) & ID_MASK) << ID_SHIFT);

      // Pack the year, month, day.
      pack |= (((verifier.getValidationDate().getYear() - 2000) & YEAR_MASK) << YEAR_SHIFT);
      pack |= (((verifier.getValidationDate().getMonthValue()) & MONTH_MASK) << MONTH_SHIFT);
      pack |= (((verifier.getValidationDate().getDayOfMonth())  & DAY_MASK) << DAY_SHIFT);

      return pack;
    }

    @Override
    public IVerifier unpack (long packedVerifier) {
      int year, month, day;
      long id = 0;

      id = (packedVerifier >>> ID_SHIFT) & ID_MASK;
      year = (int)((packedVerifier >>> YEAR_SHIFT) & YEAR_MASK) + 2000;
      month = (int)((packedVerifier >>> MONTH_SHIFT) & MONTH_MASK);
      day = (int)((packedVerifier >>> DAY_SHIFT) & DAY_MASK);

      IVerifier verifier = new Verifier ()
                            .setId(id)
                            .setValidationDate(LocalDate.of(year, month, day));

      return verifier;
    }

  }

  public static IBuilder getBuilder () { return new Builder(); }

  static class Builder implements IVerificationEncoder.IBuilder {
    String characterSet = BASE_DEFAULT_CHARACTER_SET;
    String alphabet = DEFAULT_ALPHABET;
    int daysValid = DEFAULT_PERIOD;
    boolean useSeparator;
    int padWidth = 0;
    long minId = Long.MIN_VALUE;
    long maxId = Long.MAX_VALUE;

    @Override
    public String getAlphabet () {
      return this.alphabet;
    }

    @Override
    public IBuilder setAlphabet (String alphabet) {
      this.alphabet = (null != alphabet) ? alphabet : DEFAULT_ALPHABET;

      return this;
    }

    @Override
    public String getCharacterSet () {
      return this.characterSet;
    }

    @Override
    public IBuilder setCharacterSet (String characterSet) {
      this.characterSet = (null != characterSet) ? characterSet : BASE_DEFAULT_CHARACTER_SET;

      return this;
    }

    @Override
    public int getDaysValid () {
      return this.daysValid;
    }

    @Override
    public IBuilder setDaysValid(int daysValid) {

      this.daysValid = (daysValid >= 0) ? daysValid : DEFAULT_PERIOD;

      return this;
    }

    @Override
    public boolean useSeparator() {
      return this.useSeparator;
    }

    @Override
    public IBuilder separator (boolean useSeparator) {
      this.useSeparator = useSeparator;

      return this;
    }

    @Override
    public int getPadWidth () {
      return this.padWidth;
    }

    @Override
    public IBuilder setPadWidth (int padWidth) {
      this.padWidth = padWidth;

      return this;
    }

    @Override
    public VerificationEncoder build () {
      return new VerificationEncoder(this);
    }

    @Override
    public long getMinId () {
      return this.minId;
    }

    @Override
    public long getMaxId () {
      return this.maxId;
    }

    @Override
    public IBuilder setMinId (long minId) {
      this.minId = minId;

      return this;
    }

    @Override
    public IBuilder setMaxId (long maxId) {
      this.maxId = maxId;

      return this;
    }
  }

}
