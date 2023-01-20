package v3.Multi_threaded_file_server.Pool_Based;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class Server {
	
	int port;
	int backlog;
	String racine;
	
	Server() throws IOException, InterruptedException{
		this.port=4320;
		this.backlog=3;
		this.racine="/home/venon/Ecole/java/Workshop/";
		Main();
	}
	
	Server(int port) throws IOException, InterruptedException{
		this.port=port;
		this.backlog=3;
		Main();
	}
	
	public void Main() throws IOException, InterruptedException {
		ServerSocket listenSoc = new ServerSocket(port, backlog);
		ProdConsBuffer clientsBuffer = new ProdConsBuffer(10);
		
		for(int i=0;i<5;i++)
			new Worker(clientsBuffer);
		
		while(true) {
			// wait for a connection request
			Socket soc = listenSoc.accept();
			if (soc != null) clientsBuffer.put(soc);
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		new Server(4320);
	}

}
