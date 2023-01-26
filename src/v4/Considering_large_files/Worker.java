package v4.Considering_large_files;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Worker extends Thread{
	ProdConsBuffer clientsBuffer;
	int read_len;
	String racine;
	
	Worker(ProdConsBuffer clientsBuffer,String racine){
		this.clientsBuffer=clientsBuffer;
		read_len=512;
		this.racine=racine;
		this.start();
	}
	
	public void run() {
		while(true) {
			try {
				Socket soc= clientsBuffer.get();
				Process(soc);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	void Process(Socket soc) throws IOException {
		//Reading
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		
		//Writing
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		
		//Get the link file
		String name = dis.readUTF();
		
		//Get the file
		File file = new File(this.racine + name);
		if(!file.exists()) {
			dos.writeUTF("File not found");
			return;
		}
		
		//Name of the file
		dos.writeUTF(file.getName());
		
		//Send necessary information
		long size = file.length();		//file size
		byte[] b = new byte[read_len];	//buffer of data
		dos.writeLong(size);
		dos.writeInt(this.read_len);	//Send the size of sending
		dos.flush();
		
		//Send file information
		FileInputStream fis = new FileInputStream(file);
		while(fis.read(b)>0) {
			dos.write(b);
		}
		
		//close all
		fis.close();
		dos.close();
		os.close();
		soc.close();
	}
}
