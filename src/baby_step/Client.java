package baby_step;
import java.io.*;
import java.net.*;

public class Client {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		String name="John Doe";
		String serverHost = "127.0.0.1";
		int serverPort = 4320;
		
		//Connexion
		Socket soc = new Socket(serverHost,serverPort);
		
		//Writing
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		
		//Reading
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		
		//Send his name
		byte[] b = name.getBytes("UTF-8");
		dos.writeInt(b.length);
		dos.write(b);
		
		//Read server answer
		int length = dis.readInt();
		b = new byte[length];
		dis.readFully(b);
		name = new String(b);		
		
		System.out.println(name);
	}
	
	
	
}
