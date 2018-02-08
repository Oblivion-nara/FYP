package mokapot;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Car {
	
	public Point mouse;
	public boolean myTurn, drawMovement, onTrack;
	public Point location, velocity, trackReturn;
	public int movement = 40, trackSize;

	public Car(Point start,int trackSize) {
		location = new Point(start);
		trackReturn = new Point(start);
		this.trackSize = trackSize;
		myTurn = false;
		drawMovement = false;
		onTrack = true;
		velocity = new Point(0, 0);
	}

	public Point2D getLocation() {
		return location;
	}

	public Point2D getTrackReturn() {
		return trackReturn;
	}

	public boolean onTrack() {
		return onTrack;
	}

	public void setTrack(boolean onTrack) {
		this.onTrack = onTrack;
	}

	public void go() {
		myTurn = true;
	}

	public void setTrackReturn(Point trackReturn) {
		this.trackReturn = trackReturn;
	}

	public boolean update(Point offset) {

		boolean ret = false;

		if (myTurn) {
			mouse = Main.input.getMouseZoomed();
			if (mouse != null) {
				mouse.move(mouse.x + offset.x, mouse.y + offset.y);
				if (mouse.distance(location.getX() + velocity.getX(), location.getY() + velocity.getY()) < movement) {
					drawMovement = true;
					if (Main.input.isMouseDown(1)) {
						if (onTrack) {
							trackReturn = (Point)location.clone();
						}
						velocity.setLocation(mouse.getX() - location.getX(), mouse.getY() - location.getY());
						location.setLocation(mouse.x, mouse.y);
						ret = true;
						myTurn = false;
					}
					Main.input.artificialMouseReleased(1);
				} else {
					drawMovement = false;
				}
			}
		}

		return ret;

	}

}
