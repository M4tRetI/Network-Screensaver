import java.io.*;
import java.net.SocketException;

public class ServerReceiver implements Runnable {
	Server _s;
	
	ServerReceiver (Server s) { _s = s; }
	
	public void run () {
		BufferedReader br = null;
		try { br = _s.getBufferedReader (); }
		catch (IOException e) {
			System.out.println ("Impossibile creare il ricevitore del server");
			return;
		}
		if (br == null) return;
		
		while (true) {
			String msg = "";
			try { msg = br.readLine (); }
			catch (SocketException e) {
				return;
			}
			catch (IOException e) {
				System.out.println ("Errore durante la ricezione del messaggio dal server");
			}
			if (msg != null && msg != "") {
				PallaNet.onMessageReceive (msg);
			}
		}
	}
}
