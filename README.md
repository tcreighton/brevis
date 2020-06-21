# Brevis
This project enables encoding of long integers in various ways that result in shorter strings than decimal or hexadecimal.

The name is from the latin word for small. As in small representations of integers.

The main motivation for the project was not to find a compact representation for integer IDs as
much as it was to have a means of representing integers so that humans could readily remember
and type them. This was to allow people to have item keys displayed that they could readily
share with others for collaboration purposes. To that end, we created an alphabet that would
be less likely to allow for mistakes such as substituting zero for the letter 'o'. This is
what the default alphabet provides. The separator concept enables people to better remember 
them and make approximate comparisons between two different IDs with little trouble.

An interesting benefit that we've seen is that since the sequence of the alphabet is obscured,
that is it is found only in code, someone trying to mine data that is stored by these IDs
will have to work hard to figure out the sequence in order to mine all of the data.

The more highly compact representations, such as BIG_ALPHABET, which end up being a base
68 representation are useful for cases where we want the smallest possible representation
of an ID. One such use case is to create a compact URL.

_