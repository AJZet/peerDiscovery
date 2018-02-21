import java.net.InetAddress;

class SynHandler implements RunnableHandler{

    private BlockingListQueue incoming = new BlockingListQueue(20);
    //private InetAddress senderIP = null;
    private MuxSimple myMux = null;

    //SynHandler constructor to give connect to muxSimple
    SynHandler (MuxSimple mux){
    		this.myMux = mux;
    }
    
    // put the message it in line to analyze
    public void handleMessage(String m, InetAddress a){
        this.incoming.enqueue(m);
        //this.senderIP = a;
    }
	
    public void run(){
        while (true){
            String msg = incoming.dequeue();
            try {
        			SynMessage m = new SynMessage(msg);
	    			//print the message
	    			//System.out.println("SynHandler: Proper SYN message: " + msg);
        			//if it's not my own SYN message
        			if(!m.senderID.equals(PeerDiscovery.senderId)) {
    	    				System.out.println("SynHandler: Proper SYN message from other peer: " + msg);
		    			//send a LIST messages with my database
		    			String senderId = PeerDiscovery.senderId;
		    			//the sender of the received message is the new receiver of the LIST message
		    			String peerId = m.senderID;
		    			int seqNum = PeerDiscovery.myDatabase.getDatabaseSequenceNumber();
		    			String[] parts = PeerDiscovery.myDatabase.getFileNames();
		    			//String[] parts = PeerDiscovery.myDatabase.content.split(", ");
		    			int totalParts = parts.length;
		    			for (int i = 0; i < totalParts; i++) {
		    				
		    				ListMessage listMessage = new ListMessage(senderId, peerId, seqNum, totalParts, i, parts[i]);
		    				String toSend = listMessage.getListMessageAsEncodedString();
		    				//System.out.println("SynHandler: TOsend: " + toSend);
		    				myMux.send(toSend);
		    				
		    			}
	    			}

	    		}catch(IllegalArgumentException e) {
	        		//System.out.println("SynHandler: Unproper SYN message: " + msg);
	        }
	    		
	   
        }
    }
}

