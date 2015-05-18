import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
/*
 * generate the certificate:
 * keytool -genkey -dname "CN=Jing Lin, OU=iSchool, O=PITT, L=Pittsburgh, S=Pennsylvania, C=US" -alias jinglin -keypass jinglin -storepass jinglin -keyalg RSA -keysize 1024 -keystore keystore.jks -validity 999
 */
public class X509Server {
	private static Key privateKey;

	public static void main(String[] args) throws Exception {
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		//get keystore
		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(new FileInputStream(new File("keystore.jks")),
				"jinglin".toCharArray());

		//get private key and certificate
		privateKey = keystore.getKey("jinglin", "jinglin".toCharArray());
		X509Certificate certificate = (X509Certificate) keystore
				.getCertificate("jinglin");

		//store certificate in file
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
				"X509Certificate.txt"));
		out.writeObject(certificate);
		out.close();

		//get and decrypt the message
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());
		byte[] encryptedMessage = (byte[]) is.readObject();
		byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

		String message = new String(decryptedMessage, "UTF-8");
		System.out.println(message);

		s.close();
	}

}
