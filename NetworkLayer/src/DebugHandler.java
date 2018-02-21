import java.net.InetAddress;

public class DebugHandler implements RunnableHandler {

    private BlockingListQueue incoming = new BlockingListQueue(20);
    //private InetAddress senderIP = null;

	@Override
	public void handleMessage(String m, InetAddress a){
        this.incoming.enqueue(m);
        //this.senderIP = a;
    }
	
	public void run(){
		
		while (true){
			int unknownMessage = 0;
            String msg = incoming.dequeue();
            try {
	        		HelloMessage m1 = new HelloMessage(msg);
	    			//System.out.println("DebugHandler: Proper hello message: " + m1);
            }catch(IllegalArgumentException e) {
            		unknownMessage++;
            }
            try {
	    			ListMessage m3 = new ListMessage(msg);
	    			//System.out.println("DebugHandler: Proper LIST message: " + m3);
            }catch(IllegalArgumentException e) {
        			unknownMessage++;
            }
            try {
	    			SynMessage m2 = new SynMessage(msg);
	    			//System.out.println("DebugHandler: Proper SYN message: " + m2);
            }catch(IllegalArgumentException e) {
        			unknownMessage++;
            }
            try {
	    			DyingMessage m4 = new DyingMessage(msg);
	    			//System.out.println("DebugHandler: Proper SYN message: " + m2);
            }catch(IllegalArgumentException e) {
        			unknownMessage++;
            }
            if (unknownMessage == 4)
            		System.out.println("DebugHandler: Unknown message received: " + msg);

		}

	}
}
