package rmi;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.rmi.Remote;

public interface TrackInterface extends Remote {

	Point getStart();

	boolean wins(Point2D loc);

	boolean inRange(Point loc, Point returnLoc);

	Point getNearestTrackPoint(Point loc);

	double getDistanceAlong(Point location);

	boolean onTrack(Point location);
}
