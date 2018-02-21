import java.io.File;
import java.net.InetAddress;

//If dying message from other peer --> remove his Directory + update PeerTable
//If dying message from myself --> remove all the directory kept for me and the peers
public class DyingHandler implements RunnableHandler{

    private BlockingListQueue incoming = new BlockingListQueue(20);
    
    public static boolean deleteDirectory(File dir) {
    		if (dir.isDirectory()) {
    			File[] children = dir.listFiles(); 
    			for (int i = 0; i < children.length; i++) {
    				boolean success = deleteDirectory(children[i]);
    				if (!success) { 
    					return false; 
    				} 
    			} 
    		}
    		System.out.println("removing file or directory : " + dir.getName()); 
    		return dir.delete(); 
    	}
    

    public void run(){
        while (true){
            String msg = incoming.dequeue();
            try {
            		DyingMessage m = new DyingMessage(msg);
            		System.out.println("Dying message received: " + msg);
            		
            		String currentDir = System.getProperty("user.dir");
            		if(m.senderID.equals(PeerDiscovery.senderId)) {
            			File toRemove = new File(currentDir + "/users");
        				if(deleteDirectory(toRemove)) {
        					System.out.println("Whole directory deleted.");
        				}
            		}
            		//remove from DatabaseTable
            		DatabaseTable.removeEntry(m.senderID);
            		//change PeerState
            		PeerTable.Peer p = PeerTable.map.get(m.senderID);
            		p.PeerState = "dying";
            		PeerTable.updateEntry(p);
            		//delete its directory
        			System.out.println("to remove: " + currentDir + "/users/azdeb/rootfolder/" + m.senderID);
				File toRemove = new File(currentDir + "/users/azdeb/rootfolder/" + m.senderID);
				if(deleteDirectory(toRemove)) {
					System.out.println("Directory deleted: " + currentDir + "/users/azdeb/rootfolder/" + m.senderID);
				}
            }catch(IllegalArgumentException e) {
            }
        }
    }
	@Override
	public void handleMessage(String m, InetAddress a) {
		this.incoming.enqueue(m);
    }
}
