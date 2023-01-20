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
	
	Worker(ProdConsBuffer clientsBuffer){
		this.clientsBuffer=clientsBuffer;
		read_len=512;
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
		try {
			sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Reading
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		
		//Writing
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		
		//Get the link file
		String file_link = dis.readUTF();
		
		//Get the file
		File file = new File(file_link);
		if(!file.exists()) {
			dos.writeUTF("File not found");
			return;
		}
		
		
		dos.writeUTF(file.getName());
		
		long size = file.length();		//file size
		byte[] b = new byte[read_len];	//buffer of data
		
		FileInputStream fis = new FileInputStream(file);
		long index=0;
		while(index<size) {
			long dif=size-index;
			if(dif>=this.read_len) {
				dos.writeInt(this.read_len);	//Send the size of sending
				dos.flush();
				fis.read(b,(int)index,read_len);
				index+=this.read_len
						//TODO
						//Finir l'envoie des 512 + la reception
			}else {
				dos.writeInt(dif);	//Send the size of sending
				dos.flush();
				fis.read(b,index,dif);
				index+=dif;
			}
			dos.write(b);
			dos.flush();
		}
		dos.writeUTF("EOF");
		fis.close();
		dos.close();
		os.close();
		soc.close();
	}
}
