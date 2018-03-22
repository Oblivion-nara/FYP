package rmi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class ASearch {

	private int movement;
	private Track track;
	private ArrayList<PointHeuristic> moves;
	private ArrayList<PointHeuristic> toExplore, explored;
	private Random rand;

	public ASearch(Track track, int movement) {
		this.track = track;
		this.movement = movement;
		toExplore = new ArrayList<PointHeuristic>();
		explored = new ArrayList<PointHeuristic>();
		moves = new ArrayList<PointHeuristic>();
		rand = new Random();

		Search();

	}
	
	private void Search() {
		PointHeuristic current = new PointHeuristic(0, track.getStart(), new Point(0, 0), 0, null);
		boolean goalReached = false;
		toExplore.add(current);
		int i = 0;
		while (!goalReached) {
			ArrayList<PointHeuristic> children = getChildren(current);
			i++;
			toExplore.remove(current);
			toExplore.addAll(children);
			toExplore.sort(null);

			current = toExplore.get(0);

			if (track.wins(current.getLocation())) {
				goalReached = true;
			}
		}

		while (current != null) {
			moves.add(current);
			current = current.getPrevious();
		}
		Collections.reverse(moves);
		moves.remove(0);

	}

	private ArrayList<PointHeuristic> getChildren(PointHeuristic parent) {

		ArrayList<PointHeuristic> children = new ArrayList<>();

		for (int x = -movement; x <= movement; x += 10) {
			for (int y = -movement; y <= movement; y += 10) {
				if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) > movement) {
					continue;
				}
				Point location = new Point(parent.getLocation().x + parent.getVelocity().x + x,
						parent.getLocation().y + parent.getVelocity().y + y);
				if (!track.onTrack(location)) {
					continue;
				}
				if (parent.getHeuristic() > track.getDistanceAlong(location)) {
					continue;
				}
				if (track.checkMove((Point) location, (Point) parent.getLocation()) != null) {
					continue;
				}
				if(rand.nextFloat() < 0.05){
					continue;
				}
				

				PointHeuristic point = new PointHeuristic(parent.getMove() + 1, location,
						new Point(parent.getVelocity().x + x, parent.getVelocity().y + y),
						track.getDistanceAlong(location), parent);
				if (point.equals(parent)) {
					continue;
				}
				children.add(point);
			}
		}
		return children;

	}

//	private void Search() {
//		PointHeuristic current = new PointHeuristic(0, track.getStart(), new Point(0, 0), 0, null);
//		boolean goalReached = false;
//		toExplore.add(current);
//		int i = 0;
//		while (!goalReached) {
//			ArrayList<PointHeuristic> children = getChildren(current);
//			i++;
//			toExplore.remove(current);
//			toExplore.addAll(children);
//			toExplore.sort(null);
//
//			current = toExplore.get(0);
//
//			if (track.wins(current.getLocation())) {
//				goalReached = true;
//			}
//		}
//
//		while (current != null) {
//			moves.add(current);
//			current = current.getPrevious();
//		}
//		Collections.reverse(moves);
//
//	}
//
//	private ArrayList<PointHeuristic> getChildren(PointHeuristic parent) {
//
//		ArrayList<PointHeuristic> children = new ArrayList<>();
//
//		for (int x = -movement; x <= movement; x += 10) {
//			for (int y = -movement; y <= movement; y += 10) {
//				if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) > movement) {
//					continue;
//				}
//				Point location = new Point(parent.getLocation().x + parent.getVelocity().x + x,
//						parent.getLocation().y + parent.getVelocity().y + y);
//				if (!track.onTrack(location)) {
//					continue;
//				}
//				if (parent.getHeuristic() > track.getDistanceAlong(location)) {
//					continue;
//				}
//				if (track.checkMove((Point) location, (Point) parent.getLocation()) != null) {
//					continue;
//				}
//
//				PointHeuristic point = new PointHeuristic(parent.getMove() + 1, location,
//						new Point(parent.getVelocity().x + x, parent.getVelocity().y + y),
//						track.getDistanceAlong(location), parent);
//				if (point.equals(parent)) {
//					continue;
//				}
//				children.add(point);
//			}
//		}
//		return children;
//
//	}

	public ArrayList<PointHeuristic> getMoves() {
		return moves;
	}

	// public static void main(String[] args) {
	// Track track = new Track(1000l, 40, 40, 80);
	//
	// new ASearch(track, 40).getMoves();
	// }

}
