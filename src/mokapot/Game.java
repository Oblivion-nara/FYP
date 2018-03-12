package mokapot;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import xyz.acygn.mokapot.DistributionUtils;


public class Game {

	public ArrayList<Car> players;
	public Point offset, prevOffset;
	public Track track;
	public boolean gameWon;
	public float interpolation = 1f;
	public long seed = 1000l;
	public int playersTurn;

	public Game(int players, int trackWidth, int maxSegments, int segLength, int ais, int aiDifficulty) {
		
		track = new Track(seed, trackWidth, maxSegments, segLength);
		gameWon = false;
		playersTurn = 0;
		Point start = track.getStart();
		this.players = new ArrayList<>();
		for (int i = 0; i < players; i++) {
			this.players.add(new Car(start, track.getTrackWidth()));
		}
		for (int i = 0; i < ais; i++) {
			this.players.add(new CarAI(start, track, aiDifficulty));
		}
		this.players.get(0).go();
		offset = prevOffset = new Point(0, 0);
	}
	
	public Track getRemoteTrack(){
		return DistributionUtils.makeLongReference(track);
	}
	
	public ArrayList<Car> getRemoteCars(){
		return DistributionUtils.makeLongReference(players);
	}

	public float getInterpolation() {
		return interpolation;
	}

	public Point getOffset() {
		return offset;
	}

	public Point getPrevOffset() {
		return prevOffset;
	}

	public ArrayList<Car> getPlayers() {
		return players;
	}

	public Track getTrack() {
		return track;
	}

	public int getPlayersTurn() {
		return playersTurn;
	}

	public boolean isGameWon() {
		return gameWon;
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

	private Point checkMove(int iterations, Point loc, Point previous) {
		if (iterations <= 0) {
			return null;
		}

		Point mid = new Point((int) (loc.getX() + previous.getX()) / 2, (int) (loc.getY() + previous.getY()) / 2);
		if (!track.onTrack(mid)) {
			return mid;
		}
		Point temp = checkMove(iterations - 1, loc, mid);
		if (temp != null) {
			return temp;
		}
		temp = checkMove(iterations - 1, mid, loc);
		if (temp != null) {
			return temp;
		}
		return null;

	}

	public void update(Point mouse, boolean click) {
		if (gameWon) {
			return;
		}

		boolean next = false;
		if(players.get(playersTurn) == null){
			System.out.println("Game.update() NULL");
		}
		try {
			next = ((CarAI) players.get(playersTurn)).update(offset);
		} catch (ClassCastException e) {
			next = players.get(playersTurn).update(offset,mouse,click);
		}
		
		if (next) {
			Car player = players.get(playersTurn);
			Point loc = (Point) player.getLocation();
			Point offTrack = checkMove(5, (Point) loc, (Point) player.getTrackReturn());
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
			} else if (onTrack && offTrack != null) {
				player.setTrackReturn(
						track.getNearestTrackPoint(getPointOnTrack(10, offTrack, (Point) player.getTrackReturn())));
				player.setTrack(false);
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

	public static void drawui(Graphics g, Game game) {

		// tells the players whos turn it is
		Font font = new Font("Verdana", Font.BOLD, 40);
		g.setFont(font);
		g.setColor(Color.black);
		FontMetrics met = g.getFontMetrics();
		int playersTurn = game.getPlayersTurn();
		String turn = "Players turn:  " + (playersTurn + 1);
		int width = met.stringWidth(turn);
		g.drawString(turn, 50, 100);
		// tells the players whos won
		if (game.isGameWon()) {
			String winner = "The winner is: Player " + (playersTurn + 1);
			width = met.stringWidth(winner);
			g.drawString(winner, (Main.width - width) / 2, 300);
		}
	}

	public static void draw(Graphics g, Game game, Track track,ArrayList<Car> cars) {

		float interpolation = game.getInterpolation();
		double offsetX = game.getOffset().getX();
		double offsetY = game.getOffset().getY();
		double prevOffsetX = game.getPrevOffset().getX();
		double prevOffsetY = game.getPrevOffset().getY();
		g.translate(-(int) (interpolation * offsetX + (1 - interpolation) * prevOffsetX),
				-(int) (interpolation * offsetY + (1 - interpolation) * prevOffsetY));
		Track.draw(g, track);
		for(Car car : cars){
			Car.draw(g, car);
		}
		// will show all the points on the track
		// for (int x = offset.x; x < Main.width-offset.x; x += 5) {
		// for (int y = offset.y; y < Main.height-offset.y; y += 5) {
		// if (track.onTrack(new Point(x, y))) {
		// PointHeuristic heu =
		// ((CarAI)players.get(playersTurn)).calculateHeuristic(new Point(x, y),
		// new Point (0,0));
		//// double heu = track.getDistanceAlong(new Point(x,y));
		// g.setColor(new
		// Color(0f,(float)heu.getHeuristic(),(float)heu.getHeuristic()));
		// g.drawRect(x, y, 1, 1);
		// } else {
		// PointHeuristic heu =
		// ((CarAI)players.get(playersTurn)).offTrackHeuristic(new Point(x, y),
		// new Point (0,0));
		// g.setColor(new
		// Color((float)heu.getHeuristic(),(float)heu.getHeuristic(),(float)heu.getHeuristic()));
		// g.drawRect(x, y, 1, 1);
		// }
		// }
		// }

		g.translate((int)offsetX, (int)offsetY);
	}

}
