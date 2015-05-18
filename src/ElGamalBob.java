import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalBob{//receiver
	private static boolean verifySignature(	BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message) throws NoSuchAlgorithmException
	{
		//g^H(m)(mod P) = y^r * r^s(mod p)
		byte[] message_byte= message.getBytes();
		MessageDigest message_digest = MessageDigest.getInstance("SHA");
		message_digest.update(message_byte);
		BigInteger message_hash = new BigInteger(message_digest.digest());
		
		BigInteger left = y.modPow(a, p).multiply(a.modPow(b, p));
		left = left.mod(p);
		BigInteger right = g.modPow(message_hash, p);
		
		if(left.equals(right))
			return true;
		else
			return false;
		
	}


	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());

		// read public key
		BigInteger y = (BigInteger)is.readObject();
		BigInteger g = (BigInteger)is.readObject();
		BigInteger p = (BigInteger)is.readObject();

		// read message
		String message = (String)is.readObject();

		// read signature
		BigInteger a = (BigInteger)is.readObject();
		BigInteger b = (BigInteger)is.readObject();

		boolean result = verifySignature(y, g, p, a, b, message);

		System.out.println(message);

		if (result == true)
			System.out.println("Signature verified.");
		else
			System.out.println("Signature verification failed.");

		s.close();
	}
}