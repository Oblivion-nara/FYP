package rmi;

import java.awt.Color;
import java.awt.Point;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class Server {
        
    public static void main(String args[]) {
        
        try {
            Registry registry = LocateRegistry.getRegistry(2001);
        	Random random = new Random();
    		Track track = new Track(new Random().nextLong(), StaticVars.trackWidth, StaticVars.trackLength, StaticVars.trackSegLength);
    		Point start = track.getStart();
    		CarInterface remCar;
    		ArrayList<Car> players = new ArrayList<>();
    		for (int i = 0; i < StaticVars.numPlayers; i++) {
    			Car car = new Car(start,
    					new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)),
    					track.getTrackWidth());
    			players.add(car);
    			remCar = (CarInterface) UnicastRemoteObject.exportObject(car, 0);
                registry.bind("Car"+i, remCar);
    		}
    		for (int i = 0; i < StaticVars.ais; i++) {
    			CarAI car = new CarAI(start,
    					new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)), track,
    					StaticVars.aiDifficulty);
    			players.add(car);
    			remCar = (CarInterface) UnicastRemoteObject.exportObject(car, 0);
                registry.bind("Car"+(i+StaticVars.numPlayers), remCar);
    		}
    		players.get(0).go();
    		Game game = new Game(track, players, StaticVars.trackWidth, StaticVars.trackLength, StaticVars.trackSegLength, StaticVars.aiDifficulty);
    		GameInterface remGame = (GameInterface) UnicastRemoteObject.exportObject(game, 0);
            TrackInterface remTrack = (TrackInterface) UnicastRemoteObject.exportObject(track, 0);
    		
            // Bind the remote object's stub in the registry
            registry.bind("Game", remGame);
            registry.bind("Track", remTrack);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}