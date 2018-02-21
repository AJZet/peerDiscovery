import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HelloSender implements java.lang.Runnable{

    private MuxSimple myMux = null;

    //hello constructor to give connect to muxSimple
    HelloSender (MuxSimple mux){
    		this.myMux = mux;
    }
    
    public void run(){
        while (true){
    			int helloInterval = 10;
            
            // Generate message
    			HelloMessage m = new HelloMessage(PeerDiscovery.senderId, PeerDiscovery.myDatabase.seqNum, helloInterval);
    			
    			//peer table empty -> send Hello Message without any peers in it
    			if(PeerTable.map.isEmpty()) {
            		String msg = m.getHelloMessageAsEncodedString();
        			//System.out.println(Thread.currentThread() + " HelloSender send to myMux.send: " + msg);
                myMux.send(msg);
    			}
    			//not empty -> send Hello Message with all peers from PeerTable in it
    			else {		
    			    for (String key : PeerTable.map.keySet()) {
    			    		//get a peer for the key
    			    		PeerTable.Peer peer = new PeerTable.Peer();
    			    		peer = PeerTable.map.get(key);
    			    		
    			    		//if expTime is lower than current time
    			    		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    			    		//current time
    			    		Calendar now = Calendar.getInstance();
    			    		//System.out.println(Thread.currentThread() + "Now time: " + sdf.format(now.getTime())); 
    			    		//exp time just to print
    			    		Calendar exp = Calendar.getInstance();
    			    		try {
						exp.setTime(sdf.parse(peer.expTime));
						//System.out.println(Thread.currentThread() + "Exp time: " + peer.expTime);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    			    		//for some reason method Calendar.before didn't work, another solution:
    			    		String[] aNow = sdf.format(now.getTime()).split(":");
    			    		String[] aExp = peer.expTime.split(":");

    			    		int nowSec = Integer.parseInt(aNow[0])*3600 + Integer.parseInt(aNow[1])*60 + Integer.parseInt(aNow[2]);
    						//System.out.println(Thread.currentThread() + "nowSec: " + Integer.toString(nowSec));
    			    		int expSec = Integer.parseInt(aExp[0])*3600 + Integer.parseInt(aExp[1])*60 + Integer.parseInt(aExp[2]);
    						//System.out.println(Thread.currentThread() + "expSec: " + Integer.toString(expSec));
    			    		if(expSec <= nowSec) {
    			    			//delete record of the expired peer from PeerTable and DatabaseTable
    			    			//System.out.println(Thread.currentThread() + "exp before now -> Remove record from both tables");
    			    			PeerTable.removeEntry(key);
    			    			DatabaseTable.removeEntry(key);
    			    		}
    			    		else {
    			    			//System.out.println(Thread.currentThread() + "exp after now -> Add record to the message");
    			    			m.addPeer(key);
    			    		}
    			    }

    			    String msg = m.getHelloMessageAsEncodedString();
        			//System.out.println(Thread.currentThread() + " HelloSender send to myMux.send: " + msg);
                myMux.send(msg);
    			}
    			//send message periodically, every 5s
    			try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            
        }
    }
}

