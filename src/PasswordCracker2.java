import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class PasswordCracker2 {
	private static boolean type1(String passwordHash) throws Exception {
		String line = null;
		FileReader fileReader = new FileReader("dictionary.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((line = bufferedReader.readLine()) != null) {
			// get hash
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(line.getBytes());
			byte[] dictionarydHash = md.digest();
			BigInteger bigInt = new BigInteger(1, dictionarydHash);
			String hashtext = bigInt.toString(16);

			if (hashtext.equals(passwordHash)) {
				System.out.println("Password found:" + line);
				return true;
			}
		}
		System.out.println("could not find password with type1");
		return false;
	}
	


	private static boolean type2(String passwordHash, int k) throws Exception {
		char[] numChar = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '@', '#', '$', '%', '&' };
		String line = null;
		FileReader fileReader = new FileReader("dictionary.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int counter =0;
		int total=45402;
		while ((line = bufferedReader.readLine()) != null) {
			if (counter%5000==0)
				System.out.print(Integer.valueOf((int) (counter*1.0/total*100))+"% ");//print progress
			line=line.trim();
			counter+=1;
			if(line.length()==0)
				continue;
			if (k==-1)
				if (validate(line,passwordHash))
					return true;
			Stack<Element> st = new Stack<Element>();
			st.add(new Element(line,-1,numChar,0,k));
			while (true){
				Element e=st.peek().GenerateFirstSon();
				if (e!=null)
					st.add(e);
				else
					break;
			}
			while (!st.empty()){
				Element top=st.peek();
				if (top.depth==k)
					if (validate(top.current_job,passwordHash) )
						return true;
				if (top.GenerateSibling()){
					while (true){
						Element e=st.peek().GenerateFirstSon();
						if (e!=null)
							st.add(e);
						else
							break;
					}
				}else
					st.pop();
				
			}
		}
		System.out.println("\n");
		return false;
	}
	
	private static boolean validate(String password, String passwordHash)
			throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] dictionarydHash = md.digest();
		BigInteger bigInt = new BigInteger(1, dictionarydHash);
		String hashtext = bigInt.toString(16);

		if (hashtext.equals(passwordHash)) {
			System.out.println("Password found:" + password);
			return true;
		} else
			return false;
	}

	public static void main(String[] args) throws Exception {
		String passwordHash = "";
		Scanner scanIn = new Scanner(System.in);
		System.out.println("Please enter user name");
		String user = scanIn.nextLine();
		scanIn.close();

		// get hash code of user's password
		String line = null;
		FileReader fileReader = new FileReader("password.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((line = bufferedReader.readLine()) != null) {
			String[] getInfo = line.split("\t");
			if (user.equals(getInfo[0])) {
				passwordHash = getInfo[1];
				System.out.println("passwordHash: " + passwordHash);
				break;
			}
		}

		 // crack
		 //type 1
		 long startTime = System.currentTimeMillis();
		 type1(passwordHash);
		 long endTime = System.currentTimeMillis();
		 System.out.println("type 1 time taken: " + (endTime - startTime));
		
		 //type 2
		 startTime = System.currentTimeMillis();
		 for (int k=0;k<7;k++){
			 System.out.println("k="+k);
			 if (type2(passwordHash,k-1)){
				break; 
			 }
		 }
		 endTime = System.currentTimeMillis();
		 System.out.println("type 2 time taken: " + (endTime - startTime));
	}

}
