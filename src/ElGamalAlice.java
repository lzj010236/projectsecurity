import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalAlice {//sender
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d) {
		// y = g^x mod p
		return g.modPow(d, p);
	}

	private static BigInteger computeK(BigInteger p) {
		// //Choose a random k such that 1 < k < p - 1 and gcd(k, p - 1) = 1

		BigInteger K = BigInteger.ZERO;
		BigInteger temp = BigInteger.ZERO;
		while (!temp.equals(BigInteger.ONE)) {
			SecureRandom mSecureRandom = new SecureRandom();
			K = new BigInteger(p.bitLength() - 1, mSecureRandom);
			temp = K.gcd(p.subtract(BigInteger.ONE));
		}
		return K;

	}

	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k) {
		return g.modPow(k, p);
	}

	private static BigInteger computeB(String message, BigInteger d,
			BigInteger a, BigInteger k, BigInteger p) throws Exception {
		// b=(H(m)-da) * (k^-1 mod (p-1))
		MessageDigest messageDigest = MessageDigest.getInstance("SHA");
		messageDigest.update(message.getBytes());
		BigInteger messageHash = new BigInteger(messageDigest.digest());
		return messageHash.subtract(d.multiply(a)).multiply(
				k.modInverse(p.subtract(BigInteger.ONE)));
	}

	public static void main(String[] args) throws Exception {
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find
		// out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically
															// strong
															// pseudo-random
															// number

		// Create a BigInterger with mStrength bit length that is highly likely
		// to be prime.
		// (The '16' determines the probability that p is prime. Refer to
		// BigInteger documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);

		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength - 1, mSecureRandom);
		d = new BigInteger(mStrength - 1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now
		// compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);

		// send signature
		os.writeObject(a);
		os.writeObject(b);

		s.close();
	}
}