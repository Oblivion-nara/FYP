package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Car {

	private Point2D location, velocity;
	private float thrust, breakForce, steering;
	private int movement = 25;
	private boolean myTurn;
	private Color color;

	public Car(Point start, Color color) {
		location = new Point(start);
		this.color = color;
		myTurn = false;
		velocity = new Point2D.Double(0, 0);
	}
	
	public Color getColor(){
		return color;
	}

	public void go() {
		myTurn = true;
	}

	public boolean update() {

		boolean ret = false;

		if (myTurn && Main.input.isMouseDown(1)) {
			Point mouse = Main.input.getMousePositionRelativeToComponent();
			if (mouse != null && mouse.distance(location.getX() + velocity.getX(),
					location.getY() + velocity.getY()) < movement) {
				velocity.setLocation(mouse.getX() - location.getX(), mouse.getY() - location.getY());
				location.setLocation(mouse);
				ret = true;
				myTurn = false;
			}
			Main.input.artificialMouseReleased(1);
		}
		
		return ret;

	}

	public void draw(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fillRect((int) location.getX()-5, (int) location.getY()-5, 10, 10);

		if (myTurn) {
			g2.setColor(Color.red);
			g2.fillRect((int) (location.getX() + velocity.getX())-1, (int) (location.getY() + velocity.getY())-1, 2, 2);
			g2.drawOval((int) (location.getX() + velocity.getX() - movement),
					(int) (location.getY() + velocity.getY() - movement), movement * 2, movement * 2);
		}

	}

}
