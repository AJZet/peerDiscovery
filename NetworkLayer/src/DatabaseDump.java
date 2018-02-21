import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/*Prints both PeerTable and Databases on the client's screen.
Client must connect with Client Server on port = 4243.

Note1: My own database exists in DatabaseTabe, but I'm not listed in PeerTable

Note2: If peer exists in PeerTable but not in Database table, it means that
it's either heard or inconsistent AND I haven't received LIST message from it yet.
PeerTable keeps track of all the peers that are not expired.
DatabaseTable keeps track only of Peers that are synchronized, OR were once synchronized AND are
not expired.

Note3: If peer expires I remove it BOTH from PeerTable and DatabaseTable on the occasion
of sending the next HELLO message. It might make sense to keep peer's Database even after
it expires, in case it comes back with the same database. (It doesn't need to send me his whole
database again.) However, if many peers expire and not come back, I risk wasting a lot of memory
on databases of peers that no longer exist, so I decide against it. */

public class DatabaseDump implements java.lang.Runnable{
	static void handleConnection(Socket socket) {
		
		try {
			//output PrintStream object, writes response to the client
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.print(PeerTable.printTable());
			out.println(DatabaseTable.printDatabaseTable());
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void run(){
		ServerSocket sSocket = null;
		try {
			sSocket = new ServerSocket(4243);
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
