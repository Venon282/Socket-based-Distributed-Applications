package v3.Multi_threaded_file_server.Basic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	int port;
	int backlog;
	String racine;
	
	Server() throws IOException{
		this.port=4320;
		this.backlog=3;
		this.racine="/home/venon/Ecole/java/Workshop/";
		Main();
	}
	
	Server(int port) throws IOException{
		this.port=port;
		this.backlog=3;
		Main();
	}
	
	public void Main() throws IOException {
		ServerSocket listenSoc = new ServerSocket(port, backlog);
		
		while(true) {
			// wait for a connection request
			Socket soc = listenSoc.accept();
			if (soc != null) new Worker(soc);
		}
	}

	
	public static void main(String[] args) throws IOException {
		new Server(4320);
	}

}
