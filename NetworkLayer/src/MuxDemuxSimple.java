//import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class MuxDemuxSimple implements Runnable, java.lang.Runnable{
	
	  private static final Charset CONVERTER = StandardCharsets.UTF_8;

	
	//The network socket, from which we are reading and to which we are writing. 
    //private DatagramSocket myS = null;
		MulticastSocket myS = null;

	  
	  //read message from MulticastSocket
	public String dolMulticast() throws UnknownHostException {

	    //private BufferedReader in;
	    
		byte[] buffer = new byte[2048];
		InetAddress group = null;
		
		group = InetAddress.getByName("224.0.0.3");
		//private DatagramPacket dp = null;

		DatagramPacket dp = new DatagramPacket(buffer, buffer.length, group, 4242);
		
		try {
			myS.receive(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buffer = dp.getData();
		String message = new String(buffer, 0, dp.getLength(), CONVERTER);
		
		return message;
		
	}
 
    //for generating messages or processing messages.
    //array of message handlers, all having public void handleMessage(string s)
    private SimpleMessageHandler[] myMessageHandlers;
    //to store outgoing messages
    private SynchronizedListQueue outgoing = new SynchronizedListQueue(20);

    //constructor
    MuxDemuxSimple (SimpleMessageHandler[] h, MulticastSocket s){
        this.myS = s;
        this.myMessageHandlers = h;
    }

    //One thread, which will read from the socket (blocking). When a message arrives,
    //this thread (receiverThread) will read it, and dispatch a copy of the message to each 
    //of the MessageHandlers.
    public void run(){
    		//set MuxDemux object to this to 3 message handlers from main
        for (int i=0; i<myMessageHandlers.length; i++){
            myMessageHandlers[i].setMuxDemux(this);
        }
        //read message from the socket
            while(!Thread.interrupted()) {
				try {
					//myS.receive(dp);
					//this.buffer = dp.getData();
					//String message = new String(buffer, 0, dp.getLength(), CONVERTER);
					String message = dolMulticast();
					//message sent to every handler
					for (int i=0; i<myMessageHandlers.length; i++){
	                    myMessageHandlers[i].handleMessage(message);
	             
						}
				}
				catch (IOException e){ 
					e.printStackTrace();
					Thread.currentThread().interrupt();
					return;
				}		
            }
    }

    //add a message to the synchronised queue from which SenderThread is reading.
    public void send(String s){
        outgoing.enqueue(s);	
    }
}
