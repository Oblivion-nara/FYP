package game;

import java.awt.Point;

public class PointHeuristic {

	private Point location, velocity;
	private double heuristic;
	
	public PointHeuristic(Point location, Point velocity, double heuristic){
		this.location = location;
		this.velocity = velocity;
		this.heuristic = heuristic;
	}

	public Point getLocation() {
		return location;
	}
	
	public Point getVelocity(){
		return velocity;
	}

	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}
	
	
	
}
