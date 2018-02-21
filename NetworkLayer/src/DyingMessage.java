
public class DyingMessage {
	String senderID;
	
	public DyingMessage(String s){
		String[] a = s.split(";");
		if (a.length != 2) {
			throw new IllegalArgumentException("1 - DYING message is of format: DYING;senderID;");
		}
		if (!a[0].equals("DYING")) {
			throw new IllegalArgumentException("2 - DYING message is of format: DYING;senderID;");
		}
		if (!a[1].matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("senderID is a string of up to 16 characters, containing the characters A-Z a-z 0-9 (only)");
		}
		this.senderID = a[1];
	}
	public String getDyingMessageAsEncodedString() {
		String s = "DYING;" + this.senderID + ";";
		return s;	
	}

}
