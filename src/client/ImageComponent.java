import javax.swing.*;
import java.awt.*;

public class ImageComponent extends JPanel {
	static final long serialVersionUID = 666;
	Image image;
	Point image_pos;
	
	ImageComponent (Image _image, Point initialPos) {
		super ();
		image = _image;
		image_pos = initialPos;
	}
	
	protected void paintComponent (Graphics g) {
		super.paintComponent (g);
		g.drawImage (image, image_pos.x, image_pos.y, this);
	}
	public ImageComponent setPos (Point pos) {
		image_pos = pos;
		return this;
	}
	public void updateView () {
		repaint ();
	}
}
