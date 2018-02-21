public class ListMessage {
	
	String senderID;
	String peerID;
	int sequenceNo;
	int totalParts;
	int partNum;
	String data;
	
	//constructor string
	public ListMessage(String s){
		String[] a = s.split(";");
		if (a.length < 7) {
			throw new IllegalArgumentException("Cannot create ListMessage of this string");
		}
		if (!a[0].equals("LIST")) {
			throw new IllegalArgumentException("Hello message must be in a for of HELLO;senderID;sequence#;HelloInterval;NumPeers;peer1;peer2;….;peerN");
		}
		this.senderID = a[1];
		if (!senderID.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("senderID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		this.peerID = a[2];
		if (!peerID.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("peerID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		if (!a[3].matches("[0-9]+")) {
			throw new IllegalArgumentException("sequence#  is an integer");
		}
		this.sequenceNo = Integer.parseInt(a[3]);
		if (!a[4].matches("[0-9]+")) {
			throw new IllegalArgumentException("totalParts  is an integer");
		}
		this.totalParts = Integer.parseInt(a[4]);
		if (!a[5].matches("[0-9]+") || Integer.parseInt(a[5]) > this.totalParts) {
			throw new IllegalArgumentException("partNum  is an integer smaller or equal to totalParts");
		}
		this.partNum = Integer.parseInt(a[5]);
		this.data = a[6];
		if (data.length() > 255) {
			throw new IllegalArgumentException("part cannot be longer than 255 chars");
		}
	}
	
	//constructor 
	public ListMessage(String senderID, String peerID , int sequenceNo, int totalParts, int partNum, String data) {
		this.senderID = senderID;
		if (!senderID.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("senderID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		this.peerID = peerID;
		if (!peerID.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("peerID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		this.sequenceNo = sequenceNo;
		this.totalParts = totalParts;
		this.partNum = partNum;
		this.data = data;
		if (partNum > totalParts) {
			throw new IllegalArgumentException("part# cannot be bigger than Total Number of Parts");		
		}
		if (data.length() > 255) {
			throw new IllegalArgumentException("part cannot be longer than 255 chars");
		}

	}
	
	public String getListMessageAsEncodedString() {
		
		String s = "LIST;" + senderID + ";" + peerID + ";" + Integer.toString(sequenceNo) + ";" 
				+ Integer.toString(totalParts) + ";" + Integer.toString(partNum) + ";" + data + ";";
		return s;
		 
	}
	
	public String toString() {
		String s = this.getListMessageAsEncodedString();
		System.out.println(s);
		return s;
	}

}
