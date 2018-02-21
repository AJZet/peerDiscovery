
public class DyingSender implements java.lang.Runnable{

    private MuxSimple myMux = null;

    DyingSender (MuxSimple mux){
    		this.myMux = mux;
    }
    
    public void run(){
    		DyingMessage m = new DyingMessage("DYING;" + PeerDiscovery.senderId + ";");
    		for (int i=0; i<10; i++) {
    			
    			myMux.send(m.getDyingMessageAsEncodedString());
    			try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	
    }
}
