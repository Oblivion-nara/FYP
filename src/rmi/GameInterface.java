package rmi;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {

	void update() throws RemoteException;

}
