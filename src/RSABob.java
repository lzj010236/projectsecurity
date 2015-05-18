import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.Scanner;

import javax.crypto.Cipher;

public class RSABob {

	public static void main(String[] args) throws Exception {
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		// generate key
		int mStrength = 1024;
		SecureRandom mSecureRandom = new SecureRandom();
		KeyPairGenerator keyGenerate = KeyPairGenerator.getInstance("RSA");
		keyGenerate.initialize(mStrength, mSecureRandom);
		KeyPair kpg = keyGenerate.generateKeyPair();
		Key publicKey = (Key) kpg.getPublic();
		Key privateKey = (Key) kpg.getPrivate();

		// store keys in file
		FileOutputStream fileOut = new FileOutputStream("RSABobPublicKey.txt");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(publicKey);
		out.close();

		// wait till all keys are stored
		System.out.println("key stored");
		System.out.print("press Enter to continue");
		Scanner scanIn = new Scanner(System.in);
		scanIn.nextLine();
		scanIn.close();

		//get Alice's public key
		FileInputStream fileIn = new FileInputStream("RSAAlicePublicKey.txt");
		ObjectInputStream in = new ObjectInputStream(fileIn);
		PublicKey AlicePublicKey = (PublicKey) in.readObject();
		in.close();

		//get encrypted message and the signature
		byte[] encryptedMessage, signatureBytes;
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());
		encryptedMessage = (byte[]) is.readObject();
		signatureBytes = (byte[]) is.readObject();

		//decrypt message with bob's private key
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

		//verify signature
		Signature signature = Signature.getInstance("MD5WithRSA");
		signature.initVerify(AlicePublicKey);
		signature.update(encryptedMessage);
		if (signature.verify(signatureBytes) == true) {
			System.out.println("signature verified");
		} else
			System.out.println("verify failed");

		String message = new String(decryptedMessage, "UTF-8");
		System.out.println(message);
		s.close();
	}
}