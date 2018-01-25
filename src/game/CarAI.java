package game;

import java.awt.Color;
import java.awt.Point;

public class CarAI extends Car {

	private int level;
	private Track track;
	private long moveTime;

	public CarAI(Point start, Color color, Track track, int level) {
		super(start, color, track.getTrackWidth());
		this.track = track;
		this.level = level;
	}

	public void go() {
		myTurn = true;
		moveTime = System.currentTimeMillis() + 1000;
	}

	public PointHeuristic calculateHeuristic(Point location, Point velocity) {
		PointHeuristic heu = new PointHeuristic(location, velocity, -1);
		if (!track.onTrack(location)) {
			return offTrackHeuristic(location, velocity);
		}
		if (track.wins(location)) {
			heu.setHeuristic(1.0);
			return heu;
		}
		// calc distance along track
		heu.setHeuristic(track.getDistanceAlong(location));
		if (heu.getHeuristic() < 0)
			System.out.println("CarAI.calculateHeuristic(), ERROR negativ heuristic");
		return heu;
	}

	public PointHeuristic offTrackHeuristic(Point location, Point velocity) {

		// function (x=0,y=1) (x->infinity,y->0)
		return new PointHeuristic(location, velocity, 1.0 / (trackReturn.distance(location) + 1.0));
	}

	private PointHeuristic checkSpaces(Point location, Point velocity, int level) {
		PointHeuristic heu;
		if (onTrack) {
			heu = calculateHeuristic(location, velocity);
		} else {
			heu = offTrackHeuristic(location, velocity);
		}
		if (level <= 0) {
			return heu;
		}
		if (heu.getHeuristic() <= 0) {
			System.err.println("CarAI.checkSpaces(), heu <= 0"+ heu.getLocation());
		}
		heu.setHeuristic(0);
		for (int x = -movement; x < movement; x += 10) {
			for (int y = -movement; y < movement; y += 10) {
				Point travel = new Point((int) (location.getX() + velocity.getX()) + x,
						(int) (location.getY() + velocity.getY()) + y);
				double distance = travel.distance(location.getX() + velocity.getX(), location.getY() + velocity.getY());
				if (distance > movement) {
					continue;
				}
				PointHeuristic nextHeu = checkSpaces(travel, new Point(velocity.x + x, velocity.y + y), level - 1);
				if (nextHeu.getHeuristic() >= heu.getHeuristic()) {
					// System.err.println("n > c " + nextHeu.getHeuristic());
					heu.setHeuristic(nextHeu.getHeuristic());
					heu.setVelocity(new Point(velocity.x + x, velocity.y + y));
				}

			}
		}
		// if (heu.getLocation().equals(location)) {
		// heu = new PointHeuristic(
		// new Point((int) (location.getX() + velocity.getX()), (int)
		// (location.getY() + velocity.getY())),
		// velocity, 0);
		// }
		return heu;
	}

	public boolean update(Point offset) {
		if (System.currentTimeMillis() < moveTime) {
			return false;
		}
		if (onTrack) {
			trackReturn = location;
		}
		PointHeuristic heu = checkSpaces(location, velocity, level);
		this.location.translate(heu.getVelocity().x, heu.getVelocity().y);
		this.velocity = heu.getVelocity();
		myTurn = false;
		return true;
	}

}
