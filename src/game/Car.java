package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Car {

	private Point2D location, velocity, trackReturn;
	private Point mouse;
	private float thrust, breakForce, steering;
	private int movement = 40;
	private boolean myTurn, drawMovement, onTrack;
	private Color color;

	public Car(Point start, Color color) {
		location = new Point(start);
		trackReturn = new Point(start);
		this.color = color;
		myTurn = false;
		drawMovement = false;
		onTrack = true;
		velocity = new Point2D.Double(0, 0);
	}

	public Color getColor() {
		return color;
	}

	public Point2D getLocation() {
		return location;
	}
	
	public Point2D getTrackReturn(){
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
	
	public void setTrackReturn(Point trackReturn){
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
							trackReturn = (Point2D) location.clone();
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

	public void draw(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fillRect((int) location.getX() - 5, (int) location.getY() - 5, 10, 10);
		g2.setColor(Color.BLACK);
		g2.fillRect((int) trackReturn.getX() - 5, (int) trackReturn.getY() - 5, 10, 10);

		if (myTurn) {
			g2.setColor(Color.red);
			g2.fillRect((int) (location.getX() + velocity.getX()) - 1, (int) (location.getY() + velocity.getY()) - 1, 2,
					2);
			g2.drawOval((int) (location.getX() + velocity.getX() - movement),
					(int) (location.getY() + velocity.getY() - movement), movement * 2, movement * 2);
			if (drawMovement) {
				g2.setColor(Color.WHITE);
				g2.drawLine((int) location.getX(), (int) location.getY(), mouse.x, mouse.y);
			}
		}

	}

}
