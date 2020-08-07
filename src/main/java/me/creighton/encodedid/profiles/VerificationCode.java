package me.creighton.encodedid.profiles;

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
 * encoding base.
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
public class VerificationCode {
}
