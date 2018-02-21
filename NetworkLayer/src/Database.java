

public class Database {
	int seqNum;
	String content;
	
	//new content to be updated to when all the parts arrive
	int totalPart;
	int partsInContent;
	//array of strings with all the list messages data in it
	String[] contentToBe;

	
	//constructor
	public Database (int seqNum, String content) {
		this.seqNum = seqNum;
		this.content = content;
		//initialize total part of new version as -1, as it doesn't exist yet
		this.totalPart = -1;
	}
	
	//constructor without a content
	public Database (int seqNum) {
		this.seqNum = seqNum;
		//initialize total part of new version as -1, as it doesn't exist yet
		this.totalPart = -1;
	}
	
	public int getDatabaseSequenceNumber() {
		return seqNum;		
	}
	
	//get Parts for dividing content into 256-Bytes parts
	public String[] getParts() {
		int length = content.length();
		//System.out.println("length: " + length);
		int index = (length/255) + 1;
		System.out.println("index: " + index);
		String[] a = new String[index];
		//int lastStringLength = length%255;
		//System.out.println("lastStringLength: " + lastStringLength);
		for (int i = 0; i<(index-1); i++) {
			if(length > 255 + (i*256)) {
				a[i] = content.substring(0 + (i*256), 255 + (i*256));
			}
		}
		a[index-1] = content.substring(256*(index-1), length);
		return a;		
	}
	
	//get FileNames for sending FileNames from my file directory
	public String[] getFileNames() {
		String[] a = this.content.split(",");
		String[] fileNames = new String[a.length];
		if (content.isEmpty())
			return fileNames;
		for(int i = 0; i < a.length; i++) {
			String[] a2 = a[i].split("/");
			fileNames[i] = a2[a2.length-1];
		}
		fileNames[a.length - 1] = fileNames[a.length - 1].substring(0, fileNames[a.length - 1].length() - 1);
		return fileNames;
	}
	
	public void update(String content) {
		this.content = content;
		this.seqNum ++;
		System.out.println(printDatabase());	
	}
	
	String printDatabase() {
		String print = "";
		//System.out.println("seqNum: " + this.seqNum);
		print = print.concat("seqNum: " + this.seqNum + "\r\n");
		//System.out.println("content: " + this.content);
		//this.content = Arrays.toString(getFileNames());
		print = print.concat("content: " + this.content + "\r\n");
		
		return print;
	}
		
}
