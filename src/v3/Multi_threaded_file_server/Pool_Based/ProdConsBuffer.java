package v3.Multi_threaded_file_server.Pool_Based;

import java.net.Socket;
import java.util.concurrent.Semaphore;


/*
Opération 				Pre-action 		Garde 					Post-action
Produce(Message m) 						notfull					new message
Message Consume() 						not empty				consume message
  
 */

public class ProdConsBuffer implements IProdConsBuffer {
	private int nmessage, nmessage_buffer; 	//nb total message+ nb buffer message
	private int bifSz;						//Buffer size
	private Socket[] buffer;
	private Semaphore notFull,notEmpty,mutexIn, mutexOut,fifo;
	int in,out;								//Buffer position
	
	ProdConsBuffer(int bifSz){
		//Init buffer
		this.bifSz=bifSz;
		if(this.bifSz>0)
			this.buffer=new Socket[this.bifSz];
		else this.buffer=new Socket[1];
		this.in=0;
		this.out=0;
		
		//Init info
		this.nmessage=0;
		this.nmessage_buffer=0;
		
		//Init sémaphore
		this.notFull = new Semaphore(this.bifSz);
		this.notEmpty = new Semaphore(0,true);
		this.mutexIn = new Semaphore(1,true);
		this.mutexOut = new Semaphore(1,true);
	}
	
	private boolean full() {
		return nmsg() == this.bifSz;
	}
	
	private boolean empty() {
		return nmsg() == 0;
	}

	@Override
	public void put(Socket m) throws InterruptedException {
		this.notFull.acquire();
		
		this.mutexIn.acquire();
			this.buffer[this.in]=m;
			this.in=(this.in+1)%this.bifSz;
			this.nmessage_buffer++;	
			this.nmessage++;
		this.mutexIn.release();
		
		notEmpty.release();
		System.out.println("put "+ m.toString());
	}

	
	@Override
	public synchronized Socket get() throws InterruptedException {
		this.notEmpty.acquire();
		
		this.mutexOut.acquire();
			Socket m=this.buffer[out];
			this.out=(out+1)%this.bifSz;
			this.nmessage_buffer--;
		this.mutexOut.release();
		
		this.notFull.release();
		System.out.println("get " + m.toString());
		return m;
	}


	@Override
	public int nmsg() {
		return this.nmessage_buffer;
	}

	@Override
	public int totmsg() {
		return this.nmessage;
	}


	

}