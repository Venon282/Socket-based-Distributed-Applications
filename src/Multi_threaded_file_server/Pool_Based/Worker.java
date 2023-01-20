package Multi_threaded_file_server.Pool_Based;

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
	
	Worker(ProdConsBuffer clientsBuffer){
		this.clientsBuffer=clientsBuffer;
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
		
		String file_link = dis.readUTF();
		
		File file = new File(file_link);
		
		if(!file.exists()) {
			dos.writeUTF("File not found");
			return;
		}
					
		dos.writeUTF(file.getName());
		byte[] b = new byte[(int)file.length()];
		dos.writeInt(b.length);
		dos.flush();
		FileInputStream fis = new FileInputStream(file);
		synchronized(this){
			fis.read(b);
		}
		fis.close();
		dos.write(b);
		dos.flush();
		
		dos.close();
		os.close();
		soc.close();
	}
}
