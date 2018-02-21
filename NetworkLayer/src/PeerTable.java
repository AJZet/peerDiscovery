import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

public class PeerTable {
	//synchronizedMap
	//synchronizedHashMap = Collections.synchronizedMap(new HashMap<String, String>());
	
	//constructor
	public PeerTable() {
		
	}
	enum PeerState{
		heard, synchronised, inconsistent, dying
	}
	
	static class Peer{
		String peerID;
		InetAddress peerIPAddress;
		int peerSeqNum;
		String expTime;
		String PeerState;
	}

	//ConcurrentHashMap
	static volatile ConcurrentHashMap<String, Peer> map = new ConcurrentHashMap<String, Peer>();
		
	static void addEntry(Peer p) {
		if(!map.containsKey(p.peerID)) {
			//System.out.println("Old map: " + map.toString());
			//System.out.println("put new entry to table: " + p.peerID);
			map.put(p.peerID, p);
			System.out.println("New entry in PeerTable: " + p.peerID + " " + p.PeerState);
			System.out.println(printTable());	
		}
		else {
			System.out.println("Peer exists, cannot add");
		}
		
	}
	static void updateEntry(Peer p) {
		if(map.containsKey(p.peerID)) {
			//System.out.println("update info about the peer table: " + p.peerID + " " + p.PeerState);
			map.remove(p.peerID);
			map.put(p.peerID, p);
			//System.out.println(printTable());	
		}
		else {
			System.out.println("Peer doesn't exist, cannot update");
		}
		
	}
	
	static void removeEntry(String peerID) {
		System.out.println("Remove entry from peer table: " + peerID);
		map.remove(peerID);
		System.out.println(printTable());	
	}
	
	static String printTable() {
		String print = "";
		//System.out.println("-----Peer table:------");
		print = print.concat("-------PeerTable:--------" + "\r\n");
		for (String key : map.keySet()) {
			//System.out.println("peerID: " + key);
			print = print.concat("peerID: " + key + "\r\n");
			PeerTable.Peer peer = new PeerTable.Peer();
    			peer = map.get(key);
			//System.out.println("peerIP: " + peer.peerIPAddress);
			print = print.concat("peerIP: " + peer.peerIPAddress + "\r\n");
			//System.out.println("peerSeqNum: " + peer.peerSeqNum);
			print = print.concat("peerSeqNum: " + peer.peerSeqNum + "\r\n");
			//System.out.println("expTime: " + peer.expTime);
			print = print.concat("expTime: " + peer.expTime + "\r\n");
			//System.out.println("state: " + peer.PeerState);
			print = print.concat("state: " + peer.PeerState + "\r\n");
			print = print.concat("----------------" + "\r\n");
		}
		//System.out.println("--------------------");
		print = print.concat("-----End of PeerTable----" + "\r\n");
		return print;

	}
	
}
