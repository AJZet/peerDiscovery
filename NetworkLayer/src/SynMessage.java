
public class SynMessage {
	
	String senderID;
	String peerID;
	int sequenceNo;
	
	//consructor 1
	// takes a string and populates the attributes of the SYNMessage object accordingly.
	public SynMessage(String s){
		String[] a = s.split(";");
		if (a.length < 4) {
			throw new IllegalArgumentException("SYN message must be in a for of SYN;senderID;peerID;sequence#;");
		}
		if (!a[0].equals("SYN")) {
			throw new IllegalArgumentException("Hello message must be in a for of SYN;senderID;peerID;sequence#;");
		}
		this.senderID = a[1];
		if (!senderID.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("senderID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		this.peerID = a[2];
		if (!peerID.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("senderID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		if (!a[3].matches("[0-9]+")) {
			throw new IllegalArgumentException("sequence#  is an integer");
		}
		this.sequenceNo = Integer.parseInt(a[3]);
		
	}
	//constructor 2
	public SynMessage(String senderID, String peerID , int sequenceNo) {
		this.senderID = senderID;
		if (!senderID.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("senderID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		this.peerID = peerID;
		if (!peerID.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("senderID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		this.sequenceNo = sequenceNo;
	}
	
	public String getSynMessageAsEncodedString() {
		
		String s = "SYN;" + senderID + ";" + peerID + ";" + Integer.toString(sequenceNo) + ";";
		return s;
		 
	}
	
	public String toString() {
		String s = this.getSynMessageAsEncodedString();
		System.out.println(s);
		return s;
	}

}
