package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Game {

	private ArrayList<Car> players;
	private Point offset;
	private Track track;
	private long seed = 1000l;
	private int playersTurn;

	public Game(int players) {

		track = new Track(seed);
		Point start = track.getStart();
		this.players = new ArrayList<>();
		for (int i = 0; i < players; i++) {
			this.players.add(new Car(start,
					new Color(Main.random.nextInt(255), Main.random.nextInt(255), Main.random.nextInt(255))));
		}
		this.players.get(0).go();
		offset = new Point(0, 0);
	}
	public void next(){

		track = new Track(++seed);
		Point start = track.getStart();
		int size = players.size();
		this.players.removeAll(players);
		for (int i = 0; i < size; i++) {
			this.players.add(new Car(start,
					new Color(Main.random.nextInt(255), Main.random.nextInt(255), Main.random.nextInt(255))));
		}
		this.players.get(0).go();
		offset = new Point(0, 0);
	}

	public void update() {

		boolean next = players.get(playersTurn).update(offset);
		if (next) {
			playersTurn = (playersTurn + 1) % players.size();
			players.get(playersTurn).go();
			offset = new Point((Point) players.get(playersTurn).getLocation());
			offset.move(offset.x - track.getStart().x, offset.y - track.getStart().y);
		}
		/*
		 * testing track generation if(Main.input.isKeyDown(KeyEvent.VK_W)){
		 * seed++; track = new Track(seed);
		 * Main.input.artificialKeyReleased(KeyEvent.VK_W); }
		 */
	}

	public void draw(Graphics g) {
		g.translate(-offset.x, -offset.y);
		track.draw(g);
		players.forEach(x -> x.draw(g));
		// will show all the points on the track
//		for (int x = 1; x < Main.width; x +=5) {
//			for (int y = 1; y < Main.height; y +=5) {
//				if (track.onTrack(new Point(x, y))) {
//					g.setColor(Color.green);
//					g.drawRect(x, y, 1, 1);
//				} else {
//					g.setColor(Color.red);
//					g.drawRect(x, y, 1, 1);
//				}
//			}
//		}
		
		g.translate(offset.x, offset.y);
	}

}
