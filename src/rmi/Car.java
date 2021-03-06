package rmi;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.rmi.RemoteException;

public class Car implements CarInterface {

	private Point mouse;
	private Color color;
	protected boolean myTurn, drawMovement, onTrack;
	protected Point location, velocity, trackReturn;
	protected int movement = 40, trackSize;

	public Car(Point start, Color color, int trackSize) {
		location = new Point(start);
		trackReturn = new Point(start);
		this.color = color;
		this.trackSize = trackSize;
		myTurn = false;
		drawMovement = false;
		onTrack = true;
		velocity = new Point(0, 0);
	}

	public Color getColor() {
		return color;
	}

	public Point2D getLocation() {
		return location;
	}

	public Point2D getTrackReturn() {
		return trackReturn;
	}

	public Point getMouse() {
		return mouse;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public boolean isDrawMovement() {
		return drawMovement;
	}

	public Point getVelocity() {
		return velocity;
	}

	public int getMovement() {
		return movement;
	}

	public int getTrackSize() {
		return trackSize;
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

	public void setMouse(Point mouse) {
		this.mouse = mouse;
	}

	public boolean update(Point offset, Point mouse, boolean click) {

		boolean ret = false;

		if (myTurn) {
			this.mouse = mouse;
			if (mouse != null) {
				mouse.move(mouse.x + offset.x, mouse.y + offset.y);
				if (mouse.distance(location.getX() + velocity.getX(), location.getY() + velocity.getY()) < movement) {
					drawMovement = true;
					if (click) {
						if (onTrack) {
							trackReturn = (Point) location.clone();
						}
						velocity.setLocation(mouse.getX() - location.getX(), mouse.getY() - location.getY());
						location.setLocation(mouse.x, mouse.y);
						ret = true;
						myTurn = false;
					}
				} else {
					drawMovement = false;
				}
			}
		}

		return ret;

	}

	public static void draw(Graphics g, CarInterface car) {

		Graphics2D g2 = (Graphics2D) g;
		try {

			if (car.isMyTurn() && !car.onTrack()) {
				g2.setColor(Color.BLACK);
				Font font = new Font("Verdana", Font.BOLD, 40);
				g.setFont(font);
				FontMetrics met = g.getFontMetrics();
				String text = "Return to Track";
				int width = met.stringWidth(text);
				g.drawString(text, (Main.width - width) / 2, Main.height / 5);
				Point2D trackReturn = car.getTrackReturn();
				int trackSize = car.getTrackSize();
				g2.fillOval((int) trackReturn.getX() - trackSize / 2, (int) trackReturn.getY() - trackSize / 2,
						trackSize, trackSize);
			}
			g2.setColor(car.getColor());
			Point2D location = car.getLocation();
			g2.fillRect((int) location.getX() - 5, (int) location.getY() - 5, 10, 10);
			// g2.setColor(Color.black);
			// g2.fillRect((int) trackReturn.getX() - 5, (int)
			// trackReturn.getY() - 5, 10, 10);

			if (car.isMyTurn()) {
				g2.setColor(Color.red);
				Point velocity = car.getVelocity();
				Point mouse = car.getMouse();
				int movement = car.getMovement();

				g2.fillRect((int) (location.getX() + velocity.getX()) - 1,
						(int) (location.getY() + velocity.getY()) - 1, 2, 2);
				g2.drawOval((int) (location.getX() + velocity.getX() - movement),
						(int) (location.getY() + velocity.getY() - movement), movement * 2, movement * 2);
				if (car.isDrawMovement()) {
					g2.setColor(Color.WHITE);
					g2.drawLine((int) location.getX(), (int) location.getY(), mouse.x, mouse.y);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

}
