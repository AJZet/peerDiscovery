
//SYNs sent periodically, every 5s to each in inconsistent peer in PeerTable
//min interval between 2 SYN messages to the same peer = 5s, if SYN lost, wait another 5 s.

public class SynSender implements java.lang.Runnable{

	private MuxSimple myMux = null;

    //syn constructor to give connect to muxSimple
    SynSender (MuxSimple mux){
    		this.myMux = mux;
    }
	
    public void run(){
        while (true){
    			String peerId = null;
    			int seqNum = 0;
    			
    			 for (String key : PeerTable.map.keySet()) {
			    		//get a peer for the key
			    		PeerTable.Peer peer = new PeerTable.Peer();
			    		peer = PeerTable.map.get(key);
			    		
			    		//for each inconsistent peer (that I haven't sent syn to recently)
			    		//in PeerTable send SYN message
			    		if (peer.PeerState == "inconsistent") {
			    			peerId = peer.peerID;
			    			seqNum = peer.peerSeqNum;
				    		// Generate message
			    			try {
				    			SynMessage m = new SynMessage(PeerDiscovery.senderId, peerId, seqNum);
				    			String msg = m.getSynMessageAsEncodedString();
			            		System.out.println("SynSender: Send msg " + msg);
				    			myMux.send(msg);
			    			}catch(IllegalArgumentException e) {
			            		System.out.println("SynSender: Unproper syn message was attemtet contruct ");
			            }
			    		}
    			 }
    			 //don't send SYN messages to the same peer more often than every 5s
    			try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            
        }
    }
}

