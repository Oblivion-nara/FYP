package game;

import java.awt.Color;
import java.awt.Point;

public class CarAI extends Car {

	public CarAI(Point start, Color color, int trackSize) {
		super(start, color, trackSize);
	}
	
	public boolean update(Point offset) {
		return true;
	}
	
}
