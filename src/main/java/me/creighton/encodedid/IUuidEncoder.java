package me.creighton.encodedid;


import me.creighton.encodedid.impl.UuidEncoder;

import java.util.UUID;

public interface IUuidEncoder extends IEncodedId {
  String encodeId (UUID id) throws EncodedIdException;
  String encodeIdWithoutSeparator (UUID id) throws EncodedIdException;
  UUID decodeId (String encodedId) throws EncodedIdException;

  static IUuidEncoder build (IEncodedId.Builder builder) {
    return new UuidEncoder(builder);
  }

}
