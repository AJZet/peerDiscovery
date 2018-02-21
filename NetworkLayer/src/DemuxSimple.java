import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

//must know handler, to send the message to it

class DemuxSimple implements java.lang.Runnable{
	
	private static final Charset CONVERTER = StandardCharsets.UTF_8;
	
	DatagramSocket myS = null;
	//MulticastSocket myS = null;
 
    //array of message handlers, all having public void handleMessage(string s)
    private SimpleMessageHandler[] myMessageHandlers;

    //constructor with multicast
    DemuxSimple (SimpleMessageHandler[] h, MulticastSocket s){
        this.myS = s;
        this.myMessageHandlers = h;
    }
    //constructor with broadcast
    DemuxSimple (SimpleMessageHandler[] h, DatagramSocket s){
        this.myS = s;
        this.myMessageHandlers = h;
    }

    //One thread, which will read from the socket (blocking). When a message arrives,
    //this thread (receiverThread) will read it, and dispatch a copy of the message to each 
    //of the MessageHandlers.
    public void run(){
        //read message from the socket
        while(!Thread.interrupted()) {
			try {
				byte[] buffer = new byte[2048];
				//DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), 4242);
				
				//System.out.println(Thread.currentThread() + " Demux wait to receive a message");
				myS.receive(dp);
				buffer = dp.getData();
				String message = new String(buffer, 0, dp.getLength(), CONVERTER);
				InetAddress senderIP = dp.getAddress();
				//System.out.println("Demux: senderIP: " + senderIP);
				//message sent to every handler
				for (int i=0; i<myMessageHandlers.length; i++){
					//System.out.println(Thread.currentThread() + " Demux handle the message -> to MessageHandler" + i + " " + message);
                    myMessageHandlers[i].handleMessage(message, senderIP);
             
					}
			}
			catch (IOException e){ 
				e.printStackTrace();
				Thread.currentThread().interrupt();
				return;
			}		
        }
    }


}
