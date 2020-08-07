package me.creighton.encodedid.profiles;

import me.creighton.encodedid.EncodedIdException;
import me.creighton.encodedid.IEncodedId;

public interface IVerificationCode extends IEncodedId {
  String encodeId (int id) throws EncodedIdException;
  String encodeId (int id, int daysValid) throws EncodedIdException;
  String encodeIdWithoutSeparator (int id) throws EncodedIdException;
  String encodeIdWithoutSeparator (int id, int daysValid) throws EncodedIdException;
  int decodeId (String verificationCode) throws EncodedIdException;
  int decodeDaysValid (String verificationCode) throws EncodedIdException;
  boolean isValid (String verificationCode);
  boolean isValid (int daysValid);


}

