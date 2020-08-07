package me.creighton.encodedid;

import me.creighton.encodedid.impl.BigIntegerEncoder;

import java.math.BigInteger;

public interface IBigIntegerEncoder extends IEncodedId {
  String encodeId (BigInteger id) throws EncodedIdException;
  String encodeIdWithoutSeparator (BigInteger id) throws EncodedIdException;
  BigInteger decodeId (String encodedId) throws EncodedIdException;

  static IBigIntegerEncoder build (IEncodedId.Builder builder) {
    return new BigIntegerEncoder(builder);
  }
}
