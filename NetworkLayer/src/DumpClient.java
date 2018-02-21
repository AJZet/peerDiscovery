import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class DumpClient {
	public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println(
                "Usage: java DumpClient <host name> ");
            System.exit(1);
        }
        String hostName = args[0];
		Socket mySocket = null;
		BufferedReader in = null;
		String l = null;
		try {
			mySocket = new Socket(hostName, 4243);
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			while (!(l = in.readLine()).isEmpty()) {
				System.out.println(l);
		}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
