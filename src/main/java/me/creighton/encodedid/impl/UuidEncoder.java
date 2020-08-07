package me.creighton.encodedid.impl;

import me.creighton.encodedid.EncodedIdException;
import me.creighton.encodedid.IEncodedId;
import me.creighton.encodedid.IUuidEncoder;

import java.math.BigInteger;
import java.util.UUID;

import static me.creighton.encodedid.Utilities.bigIntegerToUuid;
import static me.creighton.encodedid.Utilities.uuidToBigInteger;

/*

  This encoder simply wraps a BigInteger encoder so that the encoding
  is a matter of extracting the 2 64-bit integers from the UUID and
  creating a BigInteger from them and then encoding that BigInteger.

  Decoding is the reverse process. Decode to a BigInteger, then extract
  the two long integers and compose a UUID.

  Watch out for sign extension!!!

 */
public class UuidEncoder extends EncodedId implements IUuidEncoder {

  private BigIntegerEncoder bigIntegerEncoder;

  // Public Constructors

  public UuidEncoder (IEncodedId.Builder builder) {
    super(builder);
    this.bigIntegerEncoder = new BigIntegerEncoder(builder);
  }

  // Public Work Methods

  @Override
  public String encodeId (UUID id) throws EncodedIdException {

    String s = encodeIdWithoutSeparator(id);

    return addSeparators(s);
  }

  @Override
  public String encodeIdWithoutSeparator (UUID id) throws EncodedIdException {

    BigInteger bigInteger = uuidToBigInteger(id);
    String s = this.bigIntegerEncoder.encodeIdWithoutSeparator(bigInteger);

    return s;

  }

  @Override
  public UUID decodeId(String encodedId) throws EncodedIdException {
    UUID id;
    BigInteger bigInteger = this.bigIntegerEncoder.decodeId(encodedId);

    id = bigIntegerToUuid(bigInteger);

    return id;
  }


}
