package me.creighton.encodedid;


import java.util.UUID;

public interface IUuidEncoder extends IEncodedId {
  String encodeId (UUID id) throws EncodedIdException;
  String encodeIdWithoutSeparator (UUID id) throws EncodedIdException;
  UUID decodeId (String encodedId) throws EncodedIdException;
}
