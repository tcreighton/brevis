package me.creighton.encodedid;

import java.math.BigInteger;

public interface IBigIntegerEncoder extends IEncodedId {
  String encodeId (BigInteger id) throws EncodedIdException;
  String encodeIdWithoutSeparator (BigInteger id) throws EncodedIdException;
  BigInteger decodeId (String encodedId) throws EncodedIdException;
}
