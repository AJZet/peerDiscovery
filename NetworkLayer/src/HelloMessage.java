import java.util.ArrayList;
import java.util.List;

public class HelloMessage {
	
	String senderID;
	int sequenceNo;
	int HelloInterval;
	int NumPeers;
	//private String[] peer;
	List<String> peer = new ArrayList<String>();
	
	//Constructor 1
	// takes a string formatted as above, and populates the attributes of the HelloMessage object accordingly.
	public HelloMessage(String s){
		String[] a = s.split(";");
		if (a.length < 5) {
			throw new IllegalArgumentException("Hello message must be in a for of HELLO;senderID;sequence#;HelloInterval;NumPeers;peer1;peer2;….;peerN");
		}
		if (!a[0].equals("HELLO")) {
			throw new IllegalArgumentException("Hello message must be in a for of HELLO;senderID;sequence#;HelloInterval;NumPeers;peer1;peer2;….;peerN");
		}
		senderID = a[1];
		if (!senderID.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("senderID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		//-?[1-9]\\d*|0 for minus and plus
		if (!a[2].matches("[0-9]+")) {
			throw new IllegalArgumentException("sequence#  is an integer");
		}
		sequenceNo = Integer.parseInt(a[2]);
		if ((!a[3].matches("[0-9]+")) || Integer.parseInt(a[3]) > 255 || Integer.parseInt(a[3]) < 0) {
			throw new IllegalArgumentException("HelloInterval is an integer [0;255]");
		}
		HelloInterval = Integer.parseInt(a[3]);
		if ((!a[4].matches("[0-9]+")) || Integer.parseInt(a[4]) > 255 || Integer.parseInt(a[4]) < 0) {
			throw new IllegalArgumentException("NumPeers is an integer [0;255]");
		}
		NumPeers = Integer.parseInt(a[4]);

		int n = a.length - 5;
		for (int i = 0; i < n; i++) {
			if (!a[i+5].matches("^[a-zA-Z0-9]*$")) {
				throw new IllegalArgumentException("peerID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
			}
			peer.add(a[i+5]);
		}
		
	}
	
	//constructor 2
	//creates a HelloMessage object which has (at the time of creation) no peers associated.
	public HelloMessage(String senderID, int sequenceNo, int HelloInterval) {
		this.senderID = senderID;
		if (!senderID.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("senderID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		this.sequenceNo = sequenceNo;
		this.HelloInterval = HelloInterval;
		if (this.HelloInterval > 255 || this.HelloInterval < 0) {
			throw new IllegalArgumentException("HelloInterval is an integer [0;255]");
		}
	}
	
	public String getHelloMessageAsEncodedString() {
		NumPeers = peer.size();
		if (NumPeers > 255) {
			throw new IllegalArgumentException("Number of Peers must be between 0 and 255");
		}
		String peers = "";
		for (int i = 0; i < NumPeers; i++) {
			if( i != 0) {
				peers = peers.concat(";");
			}
			 peers = peers.concat(peer.get(i));
		}
		
		String s = "HELLO;" + senderID + ";" + Integer.toString(sequenceNo) + ";" + 
				Integer.toString(HelloInterval) + ";" + NumPeers +
				";" + peers;
		if(s.endsWith(";")) {
			s = s.substring(0, s.length()-1);
		}
		return s;
		 
	}
	
	public void addPeer(String peerID) {
		peer.add(peerID);
		
	}
	
	public String toString() {
		String s = this.getHelloMessageAsEncodedString();
		System.out.println(s);
		return s;
	}

	/*public static void main(String[] args) {
		// TODO Auto-generated method stub

	}*/

}
