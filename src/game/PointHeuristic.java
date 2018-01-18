package game;

import java.awt.Point;

public class PointHeuristic {

	private Point location;
	private double heuristic;
	
	public PointHeuristic(Point location, double heuristic){
		this.location = location;
		this.heuristic = heuristic;
	}

	public Point getLocation() {
		return location;
	}

	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}
	
	
	
}
