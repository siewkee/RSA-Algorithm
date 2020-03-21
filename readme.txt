# RSA signature

Below are components that need to be implemented.
1. KeyGen: The RSA key generations function. 
2. Sign: The RSA signing function.
3. Verify: The RSA verification function.

= Key generation function (KeyGen) should take the bit-length (up to 32) of p and q as input, and output the public key (N, e) and the corresponding private key (N, p, q, d) into two separate files pk.txt and sk.txt respectively

= Check that 
> p, q are distinct primer numbers
> N = p ∗ q
> d ∗ e = 1mod((p−1)∗(q−1))

= The signing function (Sign) should take the private (secret) key from sk.txt and a message M (a positive integer smaller than N) from a file msg.txt as input, and output the corresponding signature S = Md (mod N) into another file sig.txt.

= The verification function (Verify) should take the public key from pk.txt and a signature S (a positive integer smaller than N) from sig.txt and the message M as input, and output (display) the verification result “True” or “False” on the screen (terminal). 
