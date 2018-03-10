package rmi;

import java.awt.Point;
import java.rmi.RemoteException;

import game.PointHeuristic;

public interface CarAIInterface extends CarInterface {

	PointHeuristic calculateHeuristic(Point location, Point velocity) throws RemoteException;

	PointHeuristic offTrackHeuristic(Point location, Point velocity) throws RemoteException;

}
