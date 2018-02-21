import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class HelloHandler implements RunnableHandler{

    private BlockingListQueue incoming = new BlockingListQueue(20);
    private InetAddress senderIP = null;

    //the message is hello, put it in line to analyze
    public void handleMessage(String m, InetAddress a){
        this.incoming.enqueue(m);
        this.senderIP = a;
    }
	
    public void run(){
        while (true){
            String msg = incoming.dequeue();
            try {
            		HelloMessage m = new HelloMessage(msg);
	    			//System.out.println("HelloHandler: Proper hello message: " + msg);
	    			//from me
		    		if (m.senderID.equals(PeerDiscovery.senderId)) {
		    			//System.out.println("hello message from myself: " + msg);
		    		}
		    		//from other peer
		    		else {
		    			//System.out.println("hello message from other peer: " + msg);
		    			
		    			//peer object of the peer sending the HELLO message
		    			PeerTable.Peer peer = new PeerTable.Peer();
		    			
		    			//peer ID
		    				peer.peerID = m.senderID;
		    			
		    			//set peer IP extracted from UDP packet header
			    			peer.peerIPAddress = this.senderIP;
			    			peer.peerSeqNum = m.sequenceNo;
		    			
		    			//set exp time
			    			//cal initialized with current date and time
			    			Calendar cal = Calendar.getInstance();
			    	        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			    	        String expT = sdf.format(cal.getTime());
			    	        //System.out.println("Current time: " + expT);
			    	        
			    	        cal.add(Calendar.SECOND, m.HelloInterval);
			    	        expT = sdf.format(cal.getTime());
			    	        //System.out.println("Exp time: " + expT);
	
			    	        peer.expTime = expT;
		    			
		    			//peer is unknown, add to table
		    			if (!PeerTable.map.containsKey(peer.peerID)) {

			    			peer.PeerState = "heard";
			    			peer.peerSeqNum = -1;
		    				PeerTable.addEntry(peer);
		    				}
		    			
		    			//update the table - timer, SeqNum and State
		    			else {
		    				PeerTable.Peer existingPeer = PeerTable.map.get(peer.peerID);
		    				//in most general case peer state is preserved
		    				peer.PeerState = existingPeer.PeerState;
		    				//if peer is heard, received another hello --> inconsistent
		    				if (existingPeer.PeerState == "heard") {
		    					//if I'm listed among peers
		    					if (m.peer.contains(PeerDiscovery.senderId)) {
		    						peer.PeerState = "inconsistent";
		    					}
		    				}
			    	        //if already inconsistent --> inconsistent
			    	        if (existingPeer.PeerState == "inconsistent") {
		    	        			peer.PeerState = "inconsistent";
			    	        }
			    	        //if peer is synchronized, received Hello with seqNum > existingPeer seqNum --> inconsistent
			    	        //if synchronized and peer seqNum == existingPeer --> stay synchronized
			    	        //if synchronized and peer seqNum < existingPeer --> awkward situation, suggesting that peer decreased his database version number
			    	        //or maybe some old lost Hello --> decided to ignore and stay synchronized
			    	        if (existingPeer.PeerState == "synchronised" && peer.peerSeqNum > existingPeer.peerSeqNum) {
	    	        				peer.PeerState = "inconsistent";
			    	        }

			    	        PeerTable.updateEntry(peer);
		    			}
		    		}

            }catch(IllegalArgumentException e) {
        			//System.out.println("HelloHandler: Unproper hello message: " + msg);
            }
        }
    }
}

