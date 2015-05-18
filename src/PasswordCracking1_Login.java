import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Scanner;

public class PasswordCracking1_Login {

	public static void main(String[] args) throws Exception {
		Scanner scanIn = new Scanner(System.in);
		System.out.println("Please enter user name");
		String user = scanIn.nextLine();
		System.out.println("Please enter password");
		String password = scanIn.nextLine();
		scanIn.close();

		String line = null;
		FileReader fileReader = new FileReader("password.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((line = bufferedReader.readLine()) != null) {
			String[] getInfo = line.split("\t");
			if (user.equals(getInfo[0])) {
				// get hash
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(password.getBytes());
				byte[] passwordHash = md.digest();
				BigInteger bigInt = new BigInteger(1, passwordHash);
				String hashtext = bigInt.toString(16);

				if (hashtext.equals(getInfo[1])) {
					System.out.println("password matched");
				} else {
					System.out.println("password not matched");
				}
				break;
			}
			if (bufferedReader.read() == -1) {// reached end of file, not found
				System.out.println("user not found");
			}
		}
		bufferedReader.close();
	}

}
