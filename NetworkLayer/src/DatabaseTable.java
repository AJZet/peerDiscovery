import java.util.concurrent.ConcurrentHashMap;

public class DatabaseTable {
	//table of databases of me and peers by key = peerID, my copy of all system
	//ConcurrentHashMap
	static volatile ConcurrentHashMap<String, Database> databaseTable = new ConcurrentHashMap<String, Database>();
	
	static String printDatabaseTable() {
		String print = "";
		//System.out.println("-----Database table:-----");
		print = print.concat("-------Database table:-------\r\n");
		for (String key : databaseTable.keySet()) {
			//System.out.println("peerID: " + key);
			print = print.concat("peerID: " + key + "\r\n");
			Database db = databaseTable.get(key);
			print = print.concat(db.printDatabase());
			print = print.concat("------------\r\n");
		}
		//System.out.println("---------------------");
		print = print.concat("-----End of Database table----\r\n");
		return print;
	}
	static void removeEntry(String peerID) {
		System.out.println("Remove entry from databaseTable: " + peerID);
		databaseTable.remove(peerID);
		System.out.println(printDatabaseTable());	
	}
	static void putEntry(String key, Database value) {
		System.out.println("Put entry to databaseTable: " + key);
		databaseTable.put(key, value);
		System.out.println(printDatabaseTable());	
	}
	static void updateEntry(String key, Database value) {
		databaseTable.remove(key);
		databaseTable.put(key, value);
		System.out.println(printDatabaseTable());		
	}

}
