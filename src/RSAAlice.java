import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Scanner;

import javax.crypto.Cipher;

public class RSAAlice {
	public static void main(String[] args) throws Exception {
		String message =  "The quick brown fox is jumping over a lazy dog";
		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);
		
		//generate key.
		int mStrength = 1024; 
		SecureRandom mSecureRandom = new SecureRandom();
		KeyPairGenerator keyGenerate = KeyPairGenerator.getInstance("RSA");
		keyGenerate.initialize(mStrength, mSecureRandom);
		KeyPair kpg = keyGenerate.generateKeyPair();
		Key publicKey = (Key) kpg.getPublic();
		Key privateKey = (Key) kpg.getPrivate();

		//store keys in file
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("RSAAlicePublicKey.txt"));
        out.writeObject(publicKey);
        out.close();
        
        //wait till all keys are stored
        System.out.println("key stored");
		System.out.print("press enter to continue");
        Scanner scanIn = new Scanner(System.in);
        scanIn.nextLine();
        scanIn.close();
        
		//get Bob's public key
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("RSABobPublicKey.txt"));
		Key BobPublicKey = (Key) in.readObject();
		in.close();
		
		//encrypt message with Bob's public key
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, BobPublicKey);
		byte[] encryptedMessage = cipher.doFinal(message.getBytes());
		
		//generate signature with Alice's private key
		Signature signature = Signature.getInstance("MD5WithRSA");
		signature.initSign((PrivateKey)privateKey);
		signature.update(encryptedMessage);
		byte[] signatureBytes = signature.sign();
		

		//send the encrypted message and the signature to the bob
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		os.writeObject(encryptedMessage);
		os.writeObject(signatureBytes);
		os.flush();
		s.close();

		System.out.println(message);
	}
}