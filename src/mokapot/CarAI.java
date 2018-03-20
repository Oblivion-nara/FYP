package mokapot;

import java.awt.Point;

public class CarAI extends Car{

	public int level;
	public Track track;
	public long moveTime;

	public CarAI(Point start, Track track, int level) {
		super(start, track.getTrackWidth());
		this.track = track;
		this.level = level;
	}

	public void go() {
		myTurn = true;
		moveTime = System.currentTimeMillis() + 500;
	}

	public PointHeuristic calculateHeuristic(Point location, Point velocity) {
		PointHeuristic heu = new PointHeuristic(location, velocity, -1);
		if (!track.onTrack(location)) {
			return new PointHeuristic(location, velocity, 0);
		}
		if (track.wins(location)) {
			heu.setHeuristic(1.0);
			System.out.println("winner");
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
		double hueristic = 1.0 / (trackReturn.distance(location) / (Main.width / 2) + 1.0);
		return new PointHeuristic(location, velocity, hueristic);
	}

	private PointHeuristic checkSpaces(Point location, Point velocity, int level) {

		PointHeuristic heu = new PointHeuristic(location, velocity, -1);
		for (int x = -movement; x < movement; x += 10) {
			for (int y = -movement; y < movement; y += 10) {
				Point travel = new Point((int) (location.getX() + velocity.getX()) + x,
						(int) (location.getY() + velocity.getY()) + y);
				double distance = travel.distance(location.getX() + velocity.getX(), location.getY() + velocity.getY());
				if (distance > movement) {
					continue;
				}
				PointHeuristic temp;
				if (level <= 0) {
					if (onTrack) {
						temp = calculateHeuristic(travel, new Point(velocity.x + x, velocity.y + y));
					} else {
						temp = offTrackHeuristic(travel, new Point(velocity.x + x, velocity.y + y));
					}
				} else {
					temp = checkSpaces(travel, new Point(velocity.x + x, velocity.y + y), level - 1);
				}
				if (temp.getHeuristic() > heu.getHeuristic()) {
					heu.setLocation(travel);
					heu.setVelocity(velocity);
					heu.setHeuristic(temp.getHeuristic());
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
			trackReturn.setLocation(location);
		}
//		PointHeuristic heu = checkSpaces(location, velocity, level);
//		this.location.setLocation(heu.getLocation());
//		this.velocity.setLocation(heu.getVelocity());
//		myTurn = false; 
		return true;
	}

}
