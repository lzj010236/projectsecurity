import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		DataInputStream in = new DataInputStream(inStream);
		
		// IMPLEMENT THIS FUNCTION.
		double getq1 = in.readDouble();
		long gett1 = in.readLong();
		String getUser = in.readUTF();
		long gett2 = in.readLong();
		double getq2 = in.readDouble();
		byte[] getDigest1 = new byte[10000];
		int getDigestLength = in.read(getDigest1);
		byte[] getDigest2 = new byte[getDigestLength];
		for(int i=0;i<getDigestLength;i++){
			getDigest2[i]=getDigest1[i];
		}
		
		byte[] serverDigest1 = Protection.makeDigest(getUser, lookupPassword(getUser), gett1, getq1);
		byte[] serverDigest2 = Protection.makeDigest(serverDigest1, gett2, getq2);
		
		for(int i=0;i<serverDigest2.length;i++){
			if(serverDigest2[i]!=getDigest2[i])
				return false;
		}
		
		return true;
		
	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
//		System.out.println("connection accepted");

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
	}
}