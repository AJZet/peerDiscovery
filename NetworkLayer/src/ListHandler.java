import java.io.File;
import java.net.InetAddress;
import java.util.Arrays;

public class ListHandler implements RunnableHandler {
    private BlockingListQueue incoming = new BlockingListQueue(20);
    //private InetAddress senderIP = null;
	@Override
	public void run() {
		 while (true){
	        String msg = incoming.dequeue();
	        try {
	        		ListMessage m = new ListMessage(msg);
	        		//System.out.println("ListHandler: Proper list message " + msg);
	        		//System.out.println("ListHandler: PeerID " + m.peerID);
	        		//System.out.println("ListHandler: MyID " + PeerDiscovery.senderId);

	        		//for me
	        		if (m.peerID.equals(PeerDiscovery.senderId)) {
    					System.out.println("ListHandler: List message for me " + m.senderID);
	        			PeerTable.Peer peer = PeerTable.map.get(m.senderID);
	        			if (peer == null) {
	        				System.out.println("ListHandler: No peerTable entry for this peer " + m.senderID);
	        			}
	        			//synchronize only if peer is inconsistent OR (synchronised AND the database carried in LIST mes is a newer version)
	        			else if(peer.PeerState == "inconsistent" || (peer.PeerState == "synchronised" && m.sequenceNo > peer.peerSeqNum)) {
	        				//create or get peerDatabase
	        				Database peerDatabase = null;
		        			if (DatabaseTable.databaseTable.containsKey(m.senderID)) {
		        				//get peerDatabase
		        				peerDatabase = DatabaseTable.databaseTable.get(m.senderID);
		        			}
						else {
							//create new Database of peer and put it in DatabaseTable
		    					peerDatabase = new Database(m.sequenceNo);
		    					DatabaseTable.databaseTable.put(m.senderID, peerDatabase);
		    					System.out.println("ListHandler: New database created " + m.senderID);
						}
	    					//initialize new content if it hasn't been done yet (if it's a first list mes of this database, not necessarily part 1)
		        			if(peerDatabase.totalPart == -1) {
		        				peerDatabase.totalPart = m.totalParts;
		        				peerDatabase.partsInContent = 0;
		        				peerDatabase.contentToBe = new String[m.totalParts];
		        				//DatabaseTable.updateEntry(m.senderID, peerDatabase);
		        				System.out.println("ListHandler: New content initialized " + m.senderID);
		        			}
	    					peerDatabase.contentToBe[m.partNum] = m.data;
	    					peerDatabase.partsInContent ++;
	    					peerDatabase.seqNum = m.sequenceNo;
	        				DatabaseTable.updateEntry(m.senderID, peerDatabase);
	    					
	    					//if all the parts already received
	    					if(peerDatabase.totalPart == peerDatabase.partsInContent) {
	    						
	    						//check if some file wasn't removed from the database
	    						if (peerDatabase.content != null) {
		    						boolean isRemoved = true;
		    						String[] a = peerDatabase.content.split(", ");
		    						for(int i = 0; i< a.length; i++) {
		    							for(int j = 0; j<peerDatabase.contentToBe.length; j++) {
		    								if(a[i].equals(peerDatabase.contentToBe[j])) {
		    									isRemoved = false;
		    								}
		    							}
		    							if (isRemoved) {
		    								String toDelete = a[i];
		    								String currentDir = System.getProperty("user.dir");
		    								File toRemove = new File(currentDir + "/users/azdeb/rootfolder/" + m.senderID + "/" + toDelete);
		    								toRemove.delete();
		    							}
		    							isRemoved = true;
		    						}
	    						}
	    						
	    						//put ContentToBe in content
	    						peerDatabase.content = Arrays.toString(peerDatabase.contentToBe);
	    						peerDatabase.content = peerDatabase.content.substring(1, peerDatabase.content.length()-1);

	    						//update PeerTable
	    						System.out.println("ListHandler: Set peer state of " + m.senderID + " to synchronised with Seq num: " + m.sequenceNo);
	    						peer.PeerState = "synchronised";
	    						peer.peerSeqNum = m.sequenceNo;
	    						PeerTable.updateEntry(peer);
	    						//update DatabaseTable
		        				DatabaseTable.updateEntry(m.senderID, peerDatabase);
		        				//TODO consult with FilesToDownload queue, add all the files to the queue, FileDownloader will check if file already exists.
		        				for (int i = 0; i<peerDatabase.totalPart; i++) {
		        					String entry = m.senderID + ";" + peerDatabase.contentToBe[i];
		        					//add to the queue
		        					PeerDiscovery.filesToDownload.enqueue(entry);
		        				}
	    						//erase ContentToBe
	    						peerDatabase.totalPart = -1;
	    						peerDatabase.contentToBe = null;
		        				
	    					}
	        			}
	        		}
	        		else {
	        			//System.out.println("ListHandler: List message not for me " + m.senderID);
	        		}
	        }catch(IllegalArgumentException e) {
	        		//System.out.println("ListHandler: Unproper list message: " + msg);
	        }
	    }
	}

	@Override
	public void handleMessage(String m, InetAddress a) {
		this.incoming.enqueue(m);
        //this.senderIP = a;
	}

}
