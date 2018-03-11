package rmi;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface TrackInterface extends Remote {

	Point getStart() throws RemoteException;

	boolean wins(Point2D loc) throws RemoteException;

	boolean inRange(Point loc, Point returnLoc) throws RemoteException;

	Point getNearestTrackPoint(Point loc) throws RemoteException;

	double getDistanceAlong(Point location) throws RemoteException;

	boolean onTrack(Point location) throws RemoteException;

	public int getTrackWidth() throws RemoteException;

	public ArrayList<Point> getSegments() throws RemoteException;

	public int getMaxSegments() throws RemoteException;
}
