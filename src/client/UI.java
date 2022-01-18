import javax.swing.*;
import java.awt.*;

public class UI implements Runnable {
	Dimension dim;
	JFrame frame;
	ImageComponent pallina;
	boolean pallinaIsMounted;
	
	UI (Dimension _dim, ImageComponent _pallina) {
		dim = _dim;
		pallina = _pallina;
		pallinaIsMounted = false;
	}
	
	public void run () {
		frame = new JFrame ("PallaNet Client");
		frame.add (pallina);
    	frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    	frame.setSize (dim.width, dim.height);
    	frame.setLocation (350, 150);
    	frame.setResizable (false);
    	frame.setVisible (true);
	}
	
	public void setPallinaPos (Point pos) {
		pallina.setPos (pos).updateView ();
	}
}
