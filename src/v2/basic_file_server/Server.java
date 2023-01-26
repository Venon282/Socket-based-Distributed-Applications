package v2.basic_file_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	int port;
	int backlog;
	
	Server() throws IOException{
		this.port=4320;
		this.backlog=3;
		Main();
	}
	
	Server(int port) throws IOException{
		this.port=port;
		this.backlog=3;
		Main();
	}
	
	public void Main() {
		ServerSocket listenSoc = null;
		try {
			listenSoc = new ServerSocket(port, backlog);
			while(true) {
				// wait for a connection request
				Socket soc = listenSoc.accept();
				if (soc != null) Process(soc);
			}
		} catch (IOException e) {
			try {
				listenSoc.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
	}
	
	static boolean Process(Socket soc) throws IOException {
		//Reading
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		
		//Writing
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		
		String file_link = dis.readUTF();
		
		File file = new File(file_link);
		
		if(!file.exists()) {
			dos.writeUTF("File not found");
			return false;
		}
					
		dos.writeUTF(file.getName());
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[(int)file.length()];
		dos.writeInt(b.length);
		dos.flush();
		fis.read(b);
		dos.write(b);
		dos.flush();
		
		fis.close();
		dos.close();
		os.close();
		soc.close();
		return true;
		
	}
	
	public static void main(String[] args) throws IOException {
		new Server(4320);
	}

}
