import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class FileDownloader  implements java.lang.Runnable{
	static void handleConnection(Socket socket, String fileName, File reqFile) {
		//input reads data from a server
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//output PrintStream object, request
			PrintStream out = new PrintStream(socket.getOutputStream());
			
			out.println("get " + fileName);
			System.out.println("FileDownloader: sends a request: get " + fileName);

			String l = "";
			l = in.readLine();
			if (!l.equals(fileName)) {
				socket.close();
			}
			l = in.readLine();
			int fileSize = Integer.parseInt(l);
			FileOutputStream fos = new FileOutputStream(reqFile);
			int c;
			int carCount = 0;
            while (carCount < fileSize) {
            		c = in.read();
            		System.out.print((char) c); 
            		fos.write((char) c);
            		carCount++;
            }
            fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void run(){
		while(true) {
			String s = PeerDiscovery.filesToDownload.dequeue();
			String[] a = s.split(";");
			String peerName = a[0];
			String fileName = a[1];
			
			//check if peer directory exists
			String l = System.getProperty("user.dir") + "/users/azdeb/rootfolder/";
			l = l.concat(peerName);
			File reqFile = new File(l);

			if (!reqFile.isDirectory()) {
				reqFile.mkdir();		
			}
			
			l = l.concat("/" + fileName);
			reqFile = new File(l);
			//check if file exists and it's not .DS_Store
			if(!reqFile.exists() && !fileName.startsWith(".")) {
				PeerTable.Peer peer = PeerTable.map.get(peerName);
				InetAddress peerIP = peer.peerIPAddress;
				try {
					int port = 4242;
					Socket mySocket = new Socket();
					//timeout for connection, in case peer doesn't respond <- protection in case peer doesn't exist anymore
					mySocket.connect(new InetSocketAddress(peerIP, port));
					handleConnection(mySocket, fileName, reqFile);
					mySocket.close();
		
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

}