import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MuxSimple implements java.lang.Runnable{
	//queue of outgoing messages of any type
	private BlockingListQueue outgoing = new BlockingListQueue(20);
	//MulticastSocket myS = null;
	DatagramSocket myS = null;
	
    //constructor with multicast
    MuxSimple (MulticastSocket s){
        this.myS = s;
    }
    //constructor with broadcast
    MuxSimple (DatagramSocket s){
        this.myS = s;
    }
	
	public void run() {
		while(true) {
			//System.out.println(Thread.currentThread() + " Mux Try to dequeue from outgoing");
			String msg = outgoing.dequeue();
			//System.out.println(Thread.currentThread() + " Mux managed to dequeue from outgoing: " + msg);
			byte[] buffer = new byte[2048];
			buffer = msg.getBytes();
			
			InetAddress group = null;
			try {
				group = InetAddress.getByName("224.0.0.3");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	
			DatagramPacket dp = null;
			//dp = new DatagramPacket(buffer, buffer.length, group, 4242);
			try {
				dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), 4242);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				//System.out.println(Thread.currentThread() + " Mux send a message");
				myS.send(dp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    //add a message to the synchronised queue from which SenderThread is reading.
    public void send(String s){
		//System.out.println(Thread.currentThread() + " mux.send add to outgoing queue: " + s);
        outgoing.enqueue(s);	
    }

}
