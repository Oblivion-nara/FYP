package mokapot2;

import java.awt.Point;

public class PointHeuristic {

	private Point location, velocity;
	private double heuristic;

	public PointHeuristic(Point location, Point velocity, double heuristic) {
		this.location = location;
		this.velocity = velocity;
		this.heuristic = heuristic;
	}

	public Point getLocation() {
		return location;
	}
	
	public void setLocation(Point location){
		this.location = location;
	}

	public Point getVelocity() {
		return velocity;
	}

	public void setVelocity(Point point) {
		this.velocity = velocity;
	}

	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

}
