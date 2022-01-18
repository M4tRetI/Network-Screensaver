import java.awt.*;
import java.io.File;
import java.net.Socket;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

public class PallaNet {
	static final String PALLINA_IMG_ASSET_PATH = "../../assets/xp-logo.png";
	static final Point PALLINA_INITIAL_POS = new Point (-150, -150);
	static final String PALLINA_BOUNCE_AUDIO_ASSET_PATH = "../../assets/bounce.wav";
	
	static final String FIRST_COMMAND = "FDIM";
	static final String PALLINA_POS_COMMAND = "PPOS";
	static final String PALLINA_BOUNCE_ANNOUNCEMENT = "BOUNCE";
	
	static Server s = null;
	static UI gui = null;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println ("Non hai inserito l'indirizzo a cui collegarsi");
			return;
		}
		connect (args[0]);
	}
	
	public static void connect (String server_ip) {
		try { s = new Server (new Socket (server_ip, 4000)); }
		catch (Exception e) {
			System.out.println ("Server non raggiungibile");
			s = null;
			return;
		}
		if (s == null) return;
		s.enableReceiver ();
	}
	
	static ImageComponent createPallina (Image img) {
		return new ImageComponent (img, PALLINA_INITIAL_POS);
	}
	public static void createGUI (Dimension dim) {
		ImageComponent pallina = createPallina (new ImageIcon (PALLINA_IMG_ASSET_PATH).getImage ());
		gui = new UI (dim, pallina);
		new Thread (gui).start ();
	}
	
	public static void onMessageReceive (String msg) {
		String[] fields = msg.split (",");
		if (fields.length != 3) return;
		
		String command = fields[0];
		fields = Arrays.copyOfRange(fields, 1, fields.length);
		int[] parameters = Arrays.stream (fields).mapToInt (Integer::parseInt).toArray();
		
		messageHandler (command, parameters);
	}
	static void messageHandler (String command, int[] parameters) {
		switch (command) {
		case FIRST_COMMAND:
			createGUI (new Dimension (parameters[0], parameters[1]));
			break;
		case PALLINA_POS_COMMAND:
			gui.setPallinaPos (new Point (parameters[0], parameters[1]));
			break;
		case PALLINA_BOUNCE_ANNOUNCEMENT:
			try {
				AudioInputStream audioStream = 
						AudioSystem.getAudioInputStream (
						    new File (PALLINA_BOUNCE_AUDIO_ASSET_PATH)); 
				
				Clip clip = AudioSystem.getClip (); 
				clip.open (audioStream);
				clip.start ();
			} catch (Exception e) {}
			break;
			
		default: return;
		}
	}
}
