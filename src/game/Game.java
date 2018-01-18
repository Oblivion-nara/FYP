package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Game {

	private ArrayList<Car> players;
	private Point offset, prevOffset;
	private Track track;
	private boolean gameWon;
	private float interpolation = 1f;
	private long seed = 1000l;
	private int playersTurn;

	public Game(int players, int trackWidth, int trackLength, int trackSegLength, int aiDifficulty) {

		gameWon = false;
		track = new Track(seed, trackWidth,trackLength,trackSegLength);
		Point start = track.getStart();
		this.players = new ArrayList<>();
		for (int i = 0; i < players; i++) {
			this.players.add(new Car(start,
					new Color(Main.random.nextInt(255), Main.random.nextInt(255), Main.random.nextInt(255)),
					track.getTrackWidth()));
		}
//		this.players.add(new CarAI(start, new Color(Main.random.nextInt(255), Main.random.nextInt(255), Main.random.nextInt(255)), track, aiDifficulty));
		this.players.get(0).go();
		offset = prevOffset = new Point(0, 0);
	}

	// does a binary search to find the edge where the car left the track
	private Point getPointOnTrack(int iterations, Point loc, Point ret) {

		Point mid = new Point((int) (loc.getX() + ret.getX()) / 2, (int) (loc.getY() + ret.getY()) / 2);

		if (track.onTrack(mid)) {
			if (iterations < 0) {
				return mid;
			}
			return getPointOnTrack(--iterations, loc, mid);
		} else {
			if (iterations < 0) {
				return ret;
			}
			return getPointOnTrack(--iterations, mid, ret);
		}

	}

	public void update() {
		if (gameWon) {
			return;
		}

		boolean next = players.get(playersTurn).update(offset);
		if (next) {
			Car player = players.get(playersTurn);
			Point loc = (Point) player.getLocation();
			boolean onTrack = player.onTrack();

			if (onTrack && !track.onTrack(loc)) {
				player.setTrackReturn(
						track.getNearestTrackPoint(getPointOnTrack(10, loc, (Point) player.getTrackReturn())));
				player.setTrack(false);

			} else if (!onTrack && track.inRange(loc, (Point) player.getTrackReturn())) {
				player.setTrack(true);

			} else if (onTrack && track.wins(loc)) {

				gameWon = true;
				return;
			}
			playersTurn = (playersTurn + 1) % players.size();
			players.get(playersTurn).go();
			prevOffset = offset;
			offset = new Point((Point) players.get(playersTurn).getLocation());
			offset.move(offset.x - track.getStart().x, offset.y - track.getStart().y);
			interpolation = 0f;
		}

		// testing track generation
		// if (Main.input.isKeyDown(KeyEvent.VK_W)) {
		// seed++;
		// track = new Track(seed);
		// Main.input.artificialKeyReleased(KeyEvent.VK_W);
		// }
		if (interpolation < 1) {
			interpolation += 0.1f;
		}

	}

	public void draw(Graphics g) {

		g.translate(-(int) (interpolation * offset.x + (1 - interpolation) * prevOffset.x),
				-(int) (interpolation * offset.y + (1 - interpolation) * prevOffset.y));
		track.draw(g);
		players.forEach(x -> x.draw(g));
		// will show all the points on the track
		// for (int x = 1; x < Main.width; x +=5) {
		// for (int y = 1; y < Main.height; y +=5) {
		// if (track.onTrack(new Point(x, y))) {
		// g.setColor(Color.green);
		// g.drawRect(x, y, 1, 1);
		// } else {
		// g.setColor(Color.red);
		// g.drawRect(x, y, 1, 1);
		// }
		// }
		// }

		g.translate(offset.x, offset.y);

		// tells the players whos turn it is
		Font font = new Font("Verdana", Font.BOLD, 40);
		g.setFont(font);
		FontMetrics met = g.getFontMetrics();
		String turn = "Players turn:  " + (playersTurn + 1);
		int width = met.stringWidth(turn);
		g.drawString(turn, 50, 100);
		// tells the players whos won
		if (gameWon) {
			String winner = "The winner is: Player " + (playersTurn + 1);
			width = met.stringWidth(winner);
			g.drawString(winner, (Main.width - width) / 2, 300);
		}
	}

}
