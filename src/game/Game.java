package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game {

	private ArrayList<Car> players;
	private Track track;
	private long seed = 1000l;
	public Game(int players) {

		track = new Track(seed);
		Point start = track.getStart();
		this.players = new ArrayList<>();
		for (int i = 0; i < players; i++) {
			this.players.add(new Car(start,new Color(Main.random.nextInt(255),Main.random.nextInt(255),Main.random.nextInt(255))));
		}
		this.players.get(0).go();
	}

	public void update() {
		int playersTurn = -1;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).update()) {
				playersTurn = (i + 1) % players.size();
			}
		}
		if (playersTurn >= 0) {
			System.out.println(playersTurn);
			players.get(playersTurn).go();
		}
		/* testing track generation
		if(Main.input.isKeyDown(KeyEvent.VK_W)){
			seed++;
			track = new Track(seed);
			Main.input.artificialKeyReleased(KeyEvent.VK_W);
		}
		*/
	}

	public void draw(Graphics g) {
		track.draw(g);
		players.forEach(x->x.draw(g));
	}

}
