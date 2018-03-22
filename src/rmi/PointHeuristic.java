package rmi;

import java.awt.Point;

public class PointHeuristic implements Comparable<PointHeuristic> {

	private int move;
	private double heuristic;
	private Point location, velocity;
	private PointHeuristic previous;

	public PointHeuristic(int move, Point location, Point velocity, double heuristic, PointHeuristic previous) {
		this.location = location;
		this.velocity = velocity;
		this.heuristic = heuristic;
		this.previous = previous;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public int getMove() {
		return move;
	}

	public Point getVelocity() {
		return velocity;
	}

	public PointHeuristic getPrevious() {
		return previous;
	}

	public void setVelocity(Point velocity) {
		this.velocity = velocity;
	}

	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

	@Override
	public int compareTo(PointHeuristic pointHeu) {
		double heu = ((PointHeuristic)pointHeu).getHeuristic();
		if (Math.abs(heuristic - heu) < 0.000005) {
			return 0;
		} else if (heuristic < heu) {
			return 1;
		}
		return -1;
	}
	
	public boolean equals(Object obj){
		PointHeuristic other = (PointHeuristic)obj;
		
		return location.equals(other.location) && velocity.equals(other.velocity);
	}

	public String toString() {
		return heuristic + ", " + location + ", " + velocity;
	}

}
