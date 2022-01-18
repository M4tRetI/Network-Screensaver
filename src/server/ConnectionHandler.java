import java.io.*;
import java.net.*;

public class ConnectionHandler implements Runnable {
	ServerSocket _ss;
	
	ConnectionHandler (ServerSocket ss) {
		_ss = ss;
	}
	
	public void run () {
		while (true) {
			try {
				Client newClient = new Client (_ss.accept ());
				PallaNet.addClient (newClient);
			} catch (IOException e) {
				System.out.println ("Un client ha provato a connettersi senza successo");
				e.printStackTrace();
			}
		}
	}
}
