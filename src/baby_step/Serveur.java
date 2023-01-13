package baby_step;
import java.io.*;
import java.net.*;

public class Serveur {
	public static void main(String[] args) throws IOException {

		int port = 4320;
		int backlog = 3;
		
		ServerSocket listenSoc = new ServerSocket(port, backlog);
		
		while(true) {
			// wait for a connection request
			Socket soc = listenSoc.accept();
			if (soc != null) Process(soc);
		}
	}
	
	static boolean Process(Socket soc) throws IOException {
		//Reading
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		
		//Writing
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		
		//Read the name of the client
		int length = dis.readInt();
		byte[] b = new byte[length];
		dis.readFully(b);
		String name = new String(b,"UTF-8");	
		
		//Send answer
		String answer = "Hello "+name;
		b=answer.getBytes("UTF-8");
		dos.writeInt(b.length);
		dos.write(b);
		return true;
	}
}
