import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileGenerator implements java.lang.Runnable{
	
	public void run(){
		String currentDir = System.getProperty("user.dir");
		File directory = new File(currentDir + "/users/azdeb/rootfolder/" + PeerDiscovery.senderId);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(directory.isDirectory()) {
			
			for(int i = 0; i < 100; i++) {
				String fileName = "file" + Integer.toString(i);
				fileName = currentDir + "/users/azdeb/rootfolder/" + PeerDiscovery.senderId + "/" + fileName;
				File f = new File(fileName);
				try {
					FileOutputStream fos = new FileOutputStream(f);
					fos.write((char)33);
					fos.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

}
