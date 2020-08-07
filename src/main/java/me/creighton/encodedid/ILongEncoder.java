package me.creighton.encodedid;

import me.creighton.encodedid.impl.LongEncoder;

public interface ILongEncoder extends  IEncodedId {
  String encodeId (long id) throws EncodedIdException;
  String encodeIdWithoutSeparator (long id) throws EncodedIdException;
  long decodeId (String encodedId) throws EncodedIdException;

  static ILongEncoder build (IEncodedId.Builder builder) {
    return new LongEncoder(builder);
  }


}
