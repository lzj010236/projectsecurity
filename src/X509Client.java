import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.*;

import javax.crypto.Cipher;

public class X509Client {

	public static void main(String[] args) throws Exception {
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);

		//get certificate from file
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(
				"X509Certificate.txt"));
		X509Certificate certificate = (X509Certificate) in.readObject();
		in.close();

		//validate certificate
		try {
			certificate.checkValidity();
			System.out.println("certificate validated");
		} catch (CertificateExpiredException e) {
			System.out.println("expired");
		} catch (CertificateNotYetValidException e) {
			System.out.println("certificate not valide");
		}

		PublicKey ServerPublicKey = certificate.getPublicKey();
		certificate.verify(ServerPublicKey);
		System.out.println("Server piblic key validated");
		
		//print the content of the certificate
		System.out.println("Certificate:\n" + certificate.toString());
		
		//encrypt and send message
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, ServerPublicKey);
		byte[] encryptedMessage = cipher.doFinal(message.getBytes());
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		os.writeObject(encryptedMessage);
		
		s.close();
		System.out.println(message);
	}

}
