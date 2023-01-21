package v4.Considering_large_files;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
			dos.close();
			os.close();
			soc.close();
			return;
		}
		
		//Name define
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
		
		//Get informations
		FileOutputStream fos = new FileOutputStream(file);
		long size = dis.readLong();
		int length = dis.readInt();
		byte[] b = new byte[length]; //dis.readNBytes(length);
		
		//Infill the file
		int how=0;	//Number of bytes read
		while(size>0) {
			how=dis.read(b);
			
			if(size<length) {					//If last reading
				fos.write(b, 0, (int) size);
				break;
			}else fos.write(b);
			size-=how;
		}
		
		//Close all
		fos.close();
		dos.close();
		os.close();
		soc.close();
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		new Client("127.0.0.1",4320,"src/","test.txt");
	}
}
