package me.creighton.encodedid;

public interface ILongEncoder extends  IEncodedId {
  String encodeId (long id) throws EncodedIdException;
  String encodeIdWithoutSeparator (long id) throws EncodedIdException;
  long decodeId (String encodedId) throws EncodedIdException;
}
