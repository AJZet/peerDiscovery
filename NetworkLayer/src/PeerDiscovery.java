import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

public class PeerDiscovery {

	//generate random SenderId for this program instance
	static Random rand = new Random();
	static int  n = rand.nextInt(50);
	static String senderId = "AnetaSender" + Integer.toString(n);
	static String mycontent = "";
	
	//create my own Database
	static Database myDatabase = new Database(-1, mycontent);
	
	//List of files to downolad from peers
	static BlockingListQueue filesToDownload = new BlockingListQueue(30);
	
	static MulticastSocket doMulticast(){
		//create multicast socket for communication with other peers
		MulticastSocket mySocket = null;
		try {
			mySocket = new MulticastSocket(4242);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InetAddress group = null;
		try {
			group = InetAddress.getByName("224.0.0.3");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mySocket.joinGroup(group);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mySocket;
	}
	
	static DatagramSocket doBroadcast() {
		DatagramSocket mySocket = null;
		try {
			mySocket = new DatagramSocket(4242);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mySocket.setBroadcast(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mySocket;
	}
	
	public static void main(String[] args) {
		//https://github.com/hazelcast/hazelcast/issues/9081
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		//put my datatabase in databaseTable
		DatabaseTable.putEntry(senderId, myDatabase);
		
		//database dump
		DatabaseDump dump = new DatabaseDump();
		new Thread(dump).start();
		
		//file downloader
		FileDownloader dl = new FileDownloader();
		new Thread(dl).start();
		
		//file server
		FileServer fs = new FileServer();
		new Thread(fs).start();
		
		//check for changes in file directory
		DirectoryCheck dc = new DirectoryCheck();
		new Thread(dc).start();
		
		//file generator --> put new file in my direcotry every few secs
		FileGenerator fg = new FileGenerator();
		new Thread(fg).start();
		

		
		//do Multicast
		//MulticastSocket mySocket = null;
		//mySocket = doMulticast();
		
		//or do Broadcast
		DatagramSocket mySocket = null;
		mySocket = doBroadcast();
		
		//send messages

		//Mux only has to know about the socket, no handlers
		MuxSimple mux = new MuxSimple(mySocket);
		HelloSender hs = new HelloSender(mux);
		SynSender ss = new SynSender(mux);
		DyingSender ds = new DyingSender(mux);
		new Thread(hs).start();
		new Thread(ss).start();
		
		//anticipate dying
		Runtime.getRuntime().addShutdownHook(new Thread(ds));

		//sender thread
		new Thread(mux).start();
		
		//receive messages
		
		//array of runnable handlers
		RunnableHandler [] handlers = new RunnableHandler[5];
		handlers[0]= new HelloHandler();
		handlers[1]= new DebugHandler();
		//a link between mux and demux for SynHandler: receive SYN messages from demux, send LIST messages to mux
		handlers[2] = new SynHandler(mux);
		handlers[3] = new ListHandler();
		handlers[4] = new DyingHandler();
		
		//receiver thread
		DemuxSimple demux = new DemuxSimple(handlers, mySocket);
		
		new Thread(handlers[0]).start();
		new Thread(handlers[1]).start();
		new Thread(handlers[2]).start();
		new Thread(handlers[3]).start();
		new Thread(handlers[4]).start();
		new Thread(demux).start();

	}

}
