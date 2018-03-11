package rmi;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CarInterface extends Remote {

	Color getColor() throws RemoteException;

	Point2D getLocation() throws RemoteException;

	Point2D getTrackReturn() throws RemoteException;

	Point getMouse() throws RemoteException;

	boolean isMyTurn() throws RemoteException;

	boolean isDrawMovement() throws RemoteException;

	Point getVelocity() throws RemoteException;

	int getMovement() throws RemoteException;

	int getTrackSize() throws RemoteException;

	boolean onTrack() throws RemoteException;

	void setTrack(boolean onTrack) throws RemoteException;

	void go() throws RemoteException;

	void setTrackReturn(Point trackReturn) throws RemoteException;

	boolean update(Point offset, Point mouse, boolean click) throws RemoteException;

}