import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Random;

public class ProtectedClient
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{
		DataOutputStream out = new DataOutputStream(outStream);

		// IMPLEMENT THIS FUNCTION.
		java.util.Date today = new java.util.Date();
	    java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
	    long t1 = ts.getTime();
	    long t2 = ts.getTime();
	    
	    Random random = new Random();
	    double q1 = random.nextDouble();
	    double q2 = random.nextDouble();
	    
	    out.writeDouble(q1);
	    out.writeLong(t1);
	    out.writeUTF(user);
	    out.writeLong(t2);
	    out.writeDouble(q2);
	    
	    byte[] digest1=Protection.makeDigest(user, password, t1, q1);//first layer
	    byte[] digest2=Protection.makeDigest(digest1,t2,q2);//second layer
	    out.write(digest2);
	    
		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		
		String host = "localhost";
		int port = 7999;
		String user = "George";
		String password = "abc";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}