package me.creighton.encodedid;


import me.creighton.encodedid.profiles.VerificationEncoder;
import java.time.LocalDate;

public interface IVerificationEncoder {

  /**
   * This encoder will pack the id and a target date into a long and then use
   * the long encoder to do its work. As such, there must be a change in the
   * maximum allowable value for the id. The packing looks like this:
   *
   * <id><year of 21st century><month of year><day of month>
   *
   * The year is encoded in 7 bits
   * The month (1-12) is encoded in 4 bits
   * The day (1-31) is encoded in 5 bits
   * Total: 16 bits
   *
   * This means that the id can be 64 - 16 = 48 bits
   */

  int DEFAULT_PERIOD = 10; // default number of days to set for validity period
  LocalDate ABSOLUTE_LAST_VALID_DATE = LocalDate.of(2099, 12, 31);
  long ABSOLUTE_LARGEST_ID = 0x7FFFFFFFFFFFL; // 47 bits

  long getRandomId ();  // In the range minId to maxId, inclusive.
  String encode (long id) throws EncodedIdException;
  String encode (long id, int daysValid) throws EncodedIdException;
  String encode (long id, LocalDate validUntilDate) throws EncodedIdException;
  String encode (IVerifier verifier) throws EncodedIdException;

  IVerifier decode(String verificationCode) throws EncodedIdException;

  String getAlphabet ();
  String getCharacterSet ();

  long getMinId ();
  long getMaxId ();
  IVerificationEncoder setMinId (long minId);
  IVerificationEncoder setMaxId (long maxId);


  static IVerifier getVerifier () {return VerificationEncoder.getVerifier();}

  interface IVerifier {
    long getId ();
    IVerifier setId (long id);
    LocalDate getValidationDate ();
    IVerifier setValidationDate (LocalDate validationDate);
    IVerifier setValidationDate (int daysFromToday);
    boolean isValid (); // Tests if the validation date is good now.
    boolean isValid (LocalDate asOfDate); // Tests validation against some other date than now


  }

  static IPacker getPacker () {return VerificationEncoder.getPacker(); }

  interface IPacker {
    long pack (IVerifier verifier);
    IVerifier unpack (long packedVerifier);

  };

  static IBuilder getBuilder () {return VerificationEncoder.getBuilder();}

  interface IBuilder {

    String getAlphabet ();
    IBuilder setAlphabet (String alphabet);
    String getCharacterSet ();
    IBuilder setCharacterSet (String characterSet);
    int getDaysValid();
    IBuilder setDaysValid(int valididtyPeriod);
    boolean useSeparator();
    IBuilder separator (boolean useSeparator);
    int getPadWidth ();
    IBuilder setPadWidth (int pathWidth);
    long getMinId ();
    long getMaxId ();
    IBuilder setMinId (long minId);
    IBuilder setMaxId (long maxId);

    VerificationEncoder build ();
  }

}

