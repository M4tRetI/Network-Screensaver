import java.awt.*;

public class Animation implements Runnable {
	final int STEP = 3;
	final int MOVE_SPEED = 15; // ms
	Pallina p;
	Dimension totalScreen;
	int conrnerBounceCounter;
	
	// Bounces
	boolean destra;
	boolean basso;
	
	Animation () {
		p = new Pallina (new Point (0, 0));
		totalScreen = new Dimension (0, Client.JFRAME_HEIGHT);
		destra = true;
		basso = true;
		conrnerBounceCounter = 0;
	}
	
	public void addClientScreenDimension () {
		totalScreen.width += Client.JFRAME_WIDTH;
	}
	public int getRelativePallinaXPos () {
		return p.getPosition ().x % Client.JFRAME_WIDTH;
	}
	public void updateIstanceClient () {
		Point pallinaPos = p.getPosition ();
		int numIstance = pallinaPos.x / Client.JFRAME_WIDTH;
		Point relPos = new Point (getRelativePallinaXPos (), pallinaPos.y);
		
		String updateMsg = PallaNet.buildCommand (
			PallaNet.PALLINA_POS_COMMAND,
			relPos.x, relPos.y
		);
				
		try {
			manageInterpolation (relPos, numIstance);
			PallaNet.updateClient (numIstance, updateMsg);
		}	
		catch (ClientIstanceNotExistsException e) {}
		
		if (manageBounces ()) {
			try { PallaNet.announceClient (numIstance, PallaNet.buildCommand (PallaNet.PALLINA_BOUNCE_ANNOUNCEMENT, 0, 0)); }
			catch (ClientIstanceNotExistsException e) {}
		}
	}
	void manageInterpolation (Point relPos, int numIstance) throws ClientIstanceNotExistsException {
		if (relPos.x + Pallina.TOTAL_WIDTH () > Client.JFRAME_WIDTH) {
			String updateMsgPartialIstance = PallaNet.buildCommand (
					PallaNet.PALLINA_POS_COMMAND,
					-Pallina.WIDTH - (Client.JFRAME_WIDTH - (relPos.x + Pallina.WIDTH)) , relPos.y
				);
			PallaNet.updateClient (numIstance + 1, updateMsgPartialIstance);
		} else if (relPos.x + 15 < 0) {
			String updateMsgPartialIstance = PallaNet.buildCommand (
					PallaNet.PALLINA_POS_COMMAND,
					Client.JFRAME_WIDTH - relPos.x , relPos.y
				);
			PallaNet.updateClient (numIstance - 1, updateMsgPartialIstance);
		}
	}
	boolean manageBounces () {
		Point pallinaPos = p.getPosition ();
		boolean pallinaHasBounced = false;
		
		if (pallinaPos.x <= 0) {
			destra = true;
			pallinaHasBounced = true;
		}
		else if (pallinaPos.x + Pallina.TOTAL_WIDTH () >= totalScreen.width) {
			destra = false;
			pallinaHasBounced = true;
		}
		if (pallinaPos.y <= 0) {
			basso = true;
			pallinaHasBounced = true;
		}
		else if (pallinaPos.y + Pallina.TOTAL_HEIGHT () >= totalScreen.height) {
			basso = false;
			pallinaHasBounced = true;
		}
		// DA RIGUARDARE QUESTO!!!!!!
		if (pallinaHasBounced) {
			if (checkCornerBounce (pallinaPos))
				System.out.println ("La pallina ha rimbalzato " + ++conrnerBounceCounter + " volte in un angolo");
		}
		
		return pallinaHasBounced;
	}
	boolean checkCornerBounce (Point pallinaPos) {
		boolean pallinaIsNearTopLeftCorner = pallinaPos.y < 5 && pallinaPos.x < 5;
		boolean pallinaIsNearTopRightCorner = pallinaPos.y < 5 && pallinaPos.x > Client.JFRAME_WIDTH - 5;
		boolean pallinaIsNearBottomLeftCorner = pallinaPos.y + Pallina.TOTAL_HEIGHT () > Client.JFRAME_HEIGHT - 5 && pallinaPos.x < 5;
		boolean pallinaIsNearBottomRightCorner = pallinaPos.y + Pallina.TOTAL_HEIGHT () > Client.JFRAME_HEIGHT - 5 && pallinaPos.x > Client.JFRAME_WIDTH - 5;
		
		return (pallinaIsNearTopLeftCorner || pallinaIsNearTopRightCorner || pallinaIsNearBottomLeftCorner || pallinaIsNearBottomRightCorner);
	}
	
	
	public void run () {
		while (true) {
			if (totalScreen.width > 0) {
				Dimension moveStep = new Dimension ();
				moveStep.width = (destra ? STEP : -STEP);
				moveStep.height = (basso ? STEP : -STEP);
				p.moveForward (moveStep);
				updateIstanceClient ();
			}

			try {
				Thread.sleep (MOVE_SPEED);
			} catch (InterruptedException e) {}
		}
		
	}
}
