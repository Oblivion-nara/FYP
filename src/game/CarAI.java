package game;

import java.awt.Color;
import java.awt.Point;

public class CarAI extends Car {

	private int level;
	private Track track;

	public CarAI(Point start, Color color, Track track, int level) {
		super(start, color, track.getTrackWidth());
		this.track = track;
		this.level = level;
	}

	private PointHeuristic calculateHeuristic(Point location){
		PointHeuristic heu = new PointHeuristic(location, 0);
		if(!track.onTrack(location)){
			return heu;
		}
		//calc distance along track and how middel it is
		return null;
	}
	
	private PointHeuristic checkSpaces(Point location, Point velocity, int level) {
			PointHeuristic heu = calculateHeuristic(location);
		if (level >= 0) {
			return heu;
		}
		Point trackloc = track.getNearestTrackPoint(location);
		for (int x = -movement; x < movement; x += 5) {
			for (int y = -movement; y < movement; y += 5) {
				Point travel = new Point((int) location.getX() + (int) velocity.getX() + x, (int) location.getY() + (int) velocity.getY() + y);
				double distance = travel.distance(location.getX() + velocity.getX(), location.getY() + velocity.getY());
				if (distance > movement) {
					continue;
				}

			}
		}
		return heu;
	}

	public boolean update(Point offset) {
		checkSpaces(location, velocity, level);
		return true;
	}

}
