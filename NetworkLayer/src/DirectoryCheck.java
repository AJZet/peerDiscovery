import java.io.File;
import java.util.HashSet;

/*checks if my File Set has changed every 1s,
 * The removal of add of any file qualifies as a change
 * if files sub-directories have changed, but file set remains the same = no change
 */

public class DirectoryCheck implements java.lang.Runnable {
	
	public HashSet<File> listf(String directoryName, HashSet<File> files) {
	    File directory = new File(directoryName);

	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    if (fList != null) {
		    for (File file : fList) {
		    		if (!files.contains(file))
		        if (file.isFile()) {
		            files.add(file);
		        } else if (file.isDirectory()) {
		            listf(file.getAbsolutePath(), files);
		        }
		    }
	    }
	    return files;
	}
	
	public void run(){
		String currentDir = System.getProperty("user.dir");
		String dir = currentDir + "/users/azdeb/rootfolder/" + PeerDiscovery.senderId;
	    boolean result = false;
	    
	    try{
	    		new File(dir).mkdirs();
	        result = true;
	    } 
	    catch(SecurityException se){
	    		se.printStackTrace();
	    }        
	    if(result) {    
	        //System.out.println("myDIR created");  
	    }
	    
	    //go through list of files in my folder
	    HashSet<File> lastList = new HashSet<File>();
	    HashSet<File> newList = new HashSet<File>();
	    //set of files that were in last version of my database
	    lastList = listf(dir, lastList);
	    PeerDiscovery.myDatabase.update(lastList.toString());
	    /*PeerDiscovery.myDatabase.content = lastList.toString();
	    String[] myContent = PeerDiscovery.myDatabase.getFileNames();
	    String content = Arrays.toString(myContent);
	    content = content.substring(1, content.length()-1);
	    PeerDiscovery.myDatabase.update(content);*/

		while(true) {
			//set of files that are in my new version on my database
		    newList = listf(dir, newList);
		    //if the sets are different --> update myDatabase, newList is now the lastList
		    if(!lastList.equals(newList)) {
			    PeerDiscovery.myDatabase.update(newList.toString());
		    		/*PeerDiscovery.myDatabase.content = newList.toString();
		    	    myContent = PeerDiscovery.myDatabase.getFileNames();
		    	    content = Arrays.toString(myContent);
		    	    content = content.substring(1, content.length()-1);
		    	    PeerDiscovery.myDatabase.update(content);*/
		    		lastList.clear();
		    		lastList.addAll(newList);
		    		newList.clear();
		    }
		    
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
