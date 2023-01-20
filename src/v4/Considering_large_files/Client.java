package v4.Considering_large_files;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	String serverHost;
	int serverPort;
	String path_file;
	String name_file;
	
	Client(String serverHost, int serverPort,String path_file,String name_file) throws IOException{
		this.serverHost=serverHost;
		this.serverPort=serverPort;
		this.path_file=path_file;
		this.name_file=name_file;
		System.out.println("Start");
		Main();
		System.out.println("End");
	}
	
	void Main() throws IOException {
		//Connexion
		Socket soc = new Socket(serverHost,serverPort);
			
		//Writing
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
				
		//Reading
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
				
		//Send the name of the file
		dos.writeUTF(path_file+name_file);
				
		//Receive answer
		String answer = dis.readUTF();
		
		//Error
		if(!answer.equals(name_file)) {
			System.out.println(answer);
			dos.close();
			os.close();
			soc.close();
			return;
		}
				
		File file = new File(name_file);
		if(!file.exists()) {
			file.createNewFile();
		}else {
			int i=1;
			do {
				file = new File(name_file+"("+i+")");
				i++;
			}while(file.exists());
		}
		
		FileOutputStream fos = new FileOutputStream(file);
		int length = dis.readInt();
		byte[] b = dis.readNBytes(length);
		boolean eof=false;
		do {
			answer = dis.readUTF();
			if(answer.equals("EOF"))
				eof=true;
			else {
				fos.write(b);
				fos.flush();
			}
		}while(!eof);
		
		
		fos.close();
		dos.close();
		os.close();
		soc.close();
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		new Client("127.0.0.1",4320,"src/","test.txt");
	}
}
