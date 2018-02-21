
public class SeqNumIncreamenter implements java.lang.Runnable{
	public void run(){
		while(true) {
			Database d = null;
			d = DatabaseTable.databaseTable.get(PeerDiscovery.senderId);
			d.seqNum++;
			d.content = d.content.concat("--UPDATE--" + d.seqNum);
			DatabaseTable.updateEntry(PeerDiscovery.senderId, d);
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
