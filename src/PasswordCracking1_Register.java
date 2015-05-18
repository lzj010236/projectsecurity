import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Scanner;

public class PasswordCracking1_Register {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// prompt to get user name and password
		Scanner scanIn = new Scanner(System.in);
		System.out.println("Please enter user name");
		String user = scanIn.nextLine();
		System.out.println("Please enter password");
		String password = scanIn.nextLine();
		scanIn.close();

		// get hash
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] passwordHash = md.digest();
		BigInteger bigInt = new BigInteger(1, passwordHash);
		String hashtext = bigInt.toString(16);

		// write to file
		FileWriter fileWritter = new FileWriter("password.txt", true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		bufferWritter.write(user);
		bufferWritter.write("\t");
		bufferWritter.write(hashtext);
		bufferWritter.write("\n");
		bufferWritter.flush();
		bufferWritter.close();
	}

}
