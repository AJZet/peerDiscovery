import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


public class FileServer implements java.lang.Runnable{
	static void handleConnection(Socket socket) {
		
		try {
			//input reads request from a client
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//output PrintStream object, writes response to the client
			PrintStream out = new PrintStream(socket.getOutputStream());
			
			//read request
			String l;
			String fileName;
			l = in.readLine();
			System.out.println("FileServer got a request: " + l);
			if(l.startsWith("get ")) {
				l = l.substring(4, l.length());
				fileName = l;
				System.out.println("FileServer file to be sent: " + fileName);
				l = System.getProperty("user.dir") + "/users/azdeb/rootfolder/" + PeerDiscovery.senderId + "/" + fileName;
				File reqFile = new File(l);
				if(reqFile.exists()) {
					out.println(fileName);
					System.out.println(fileName);
					out.println(reqFile.length());
					System.out.println(reqFile.length());
					//send file
					BufferedReader reader = new BufferedReader(new FileReader(reqFile));
					int i;
					while ((i = reader.read()) != -1){
						System.out.print((char) i);
					    out.write((char) i);
					}
					reader.close();					}
			}
			
			//out.print(PeerTable.printTable());
			//out.println(DatabaseTable.printDatabaseTable());
			//socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void run(){
		ServerSocket sSocket = null;
		int port = 4242;
		try {
			sSocket = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Socket mySocket = new Socket();
		
		//InetSocketAddress ad = new InetSocketAddress(4242);
		try {
			while (true) {
				//System.out.println("Server waiting for connection");
				Socket cSocket = sSocket.accept();
				//System.out.println("Connection accepted");
				handleConnection(cSocket);
				cSocket.close();
				//System.out.println("Connection closed");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//System.out.println("Close the server");
			try {
				sSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}