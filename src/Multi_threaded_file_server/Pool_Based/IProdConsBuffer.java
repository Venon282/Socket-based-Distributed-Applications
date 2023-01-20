package Multi_threaded_file_server.Pool_Based;

import java.net.Socket;

public interface IProdConsBuffer {
	/**
	* Put the message m in the buffer
	**/
	public void put(Socket m) throws InterruptedException;
	
	/**
	* Retrieve a message from the buffer,
	* following a FIFO order (if M1 was put before M2, M1
	* is retrieved before M2)
	**/
	public Socket get() throws InterruptedException;
	
	/**
	* Returns the number of messages currently available in
	* the buffer
	**/
	public int nmsg();
	
	/**
	* Returns the total number of messages that have
	* been put in the buffer since its creation
	**/
	public int totmsg();
}
