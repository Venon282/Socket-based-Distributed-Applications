package v4.Considering_large_files;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	int port;
	int backlog;
	String racine;
	
	Server() throws IOException, InterruptedException{
		this.port=4320;
		this.backlog=3;
		this.racine="src/";
		Main();
	}
	
	Server(int port) throws IOException, InterruptedException{
		this.port=port;
		this.backlog=3;
		this.racine="src/";
		Main();
	}
	
	Server(int port,String racine) throws IOException, InterruptedException{
		this.port=port;
		this.backlog=3;
		this.racine=racine;
		Main();
	}
	
	/*
	 * Function which print the document in a link
	 * Not use in this programme 
	 */
	public void printDoc(String link) {
		File repertoire = new File(link);
        String liste[] = repertoire.list();      
 
        if (liste != null) {         
            for (int i = 0; i < liste.length; i++) {
                System.out.println(liste[i]);
            }
        } else {
            System.err.println("Invalid link");
        }
	}
	
	public void Main() throws IOException, InterruptedException {
		ServerSocket listenSoc = new ServerSocket(port, backlog);
		ProdConsBuffer clientsBuffer = new ProdConsBuffer(10);
		
		for(int i=0;i<5;i++)
			new Worker(clientsBuffer,this.racine);
		
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
