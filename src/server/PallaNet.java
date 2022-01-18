import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;
import java.net.*;
import java.io.*;

public class PallaNet {
	static final String FIRST_COMMAND = "FDIM";
	static final String PALLINA_POS_COMMAND = "PPOS";
	static final String PALLINA_BOUNCE_ANNOUNCEMENT = "BOUNCE";
	
	public static Vector <Client> connectedClients;
	public static Animation anim;

	public static void main(String[] args) {
		connectedClients = new Vector <Client> ();
		ServerSocket ss = null;
		try { ss = new ServerSocket (4000); }
		catch (IOException e) {
			System.out.println ("Impossibile avviare il server");
			System.exit (0x01);
		}
		if (ss == null) System.exit (0x02);
		new Thread (new ConnectionHandler (ss)).start ();
		anim = new Animation ();
		new Thread (anim).start ();
	}
	
	public static void addClient (Client c) {
		connectedClients.add (c);
		anim.addClientScreenDimension ();
		try { c.write (buildCommand (FIRST_COMMAND, Client.JFRAME_WIDTH, Client.JFRAME_HEIGHT)); }
		catch (IOException e) {
			System.out.println ("Errore durante l'invio della dimensione del nuovo client");
		}
	}
	
	public static String buildCommand (String command, int... params) {
		return command + ',' + Arrays.stream (params).mapToObj (String::valueOf).collect (Collectors.joining (","));
	}
	public static void updateClient (int numClient, String msg) throws ClientIstanceNotExistsException {
		if (numClient < 0 || numClient >= connectedClients.size ())
			throw new ClientIstanceNotExistsException (numClient);
		
		try { connectedClients.elementAt (numClient).write (msg); }
		catch (IOException e) {
			System.out.println ("Impossibile inviare l'aggiornamento della posizione al client n°" + numClient);
		}
	}
	
	public static void announceClient (int numClient, String msg) throws ClientIstanceNotExistsException {
		updateClient (numClient, msg);
	}
}

class ClientIstanceNotExistsException extends Exception {
	static final long serialVersionUID = 666;
	ClientIstanceNotExistsException (int numClientIstance) {
		super ("Numero di istanza client " + numClientIstance + " non esiste");
	}
}

