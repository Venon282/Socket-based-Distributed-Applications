package Multi_threaded_file_server.Basic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Worker extends Thread{
	
	Socket soc;
	
	Worker(Socket soc){
		this.soc = soc;
		this.start();
	}
	
	public void run() {
		System.out.println("Start");
		try {
			Process(this.soc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Stop");
	}
	
	void Process(Socket soc) throws IOException {
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
		fis.read(b);
		
		fis.close();
		dos.write(b);
		dos.flush();
		
		dos.close();
		os.close();
		soc.close();
	}
}
