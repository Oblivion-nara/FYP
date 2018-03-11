package rmi;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {

	void update(Point mouse, boolean click) throws RemoteException;

	public Point getOffset() throws RemoteException;

	public Point getPrevOffset() throws RemoteException;

	public boolean isGameWon() throws RemoteException;

	public float getInterpolation() throws RemoteException;

	public int getPlayersTurn() throws RemoteException;
}
