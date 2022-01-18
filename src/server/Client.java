import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class Client {
	public static final int JFRAME_WIDTH = 500;
	public static final int JFRAME_HEIGHT = 350;
	Socket s;
	OutputStreamWriter osw;
	
	Client (Socket _s) throws IOException {
		s = _s;
		osw = new OutputStreamWriter (s.getOutputStream (), StandardCharsets.UTF_8);
	}
	
	public void closeCommunication () throws IOException {
		s.close ();
	}
	
	public synchronized BufferedReader getBufferedReader () throws IOException {
		return new BufferedReader (new InputStreamReader (s.getInputStream (), StandardCharsets.UTF_8));
	}
	public synchronized void write (String text) throws IOException {
		osw.append (text + "\n").flush ();
	}
}
