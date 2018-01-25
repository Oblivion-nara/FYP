package game;

import java.awt.Point;

public class PointHeuristic {

	private Point location, velocity;
	private double heuristic, futureHeu;

	public PointHeuristic(Point location, Point velocity, double heuristic) {
		this.location = location;
		this.velocity = velocity;
		this.heuristic = heuristic;
		futureHeu = 0;
	}

	public Point getLocation() {
		return location;
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

	public double getFutureHeu() {
		return futureHeu;
	}

	public void setFutureHeu(double futureHeu) {
		this.futureHeu = futureHeu;
	}

}
