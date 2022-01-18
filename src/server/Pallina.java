import java.awt.*;

public class Pallina {
	Point pos;
	static final int WIDTH = 60;
	static final int HEIGHT = 48;
	static final int ORIZONTAL_MARGIN = 15;
	static final int VERTICAL_MARGIN = 35;
	
	Pallina (Point _startPos) {
		pos = _startPos;
	}

	public void setPosition (Point _newPos) { pos = _newPos; }
	public Point getPosition () { return pos; }
	public void moveForward (Dimension step) {
		pos.x += step.width;
		pos.y += step.height;
	}
	public static int TOTAL_WIDTH () { return WIDTH + ORIZONTAL_MARGIN; }
	public static int TOTAL_HEIGHT () { return HEIGHT + VERTICAL_MARGIN; }
}
