package mokapot;

import java.awt.Point;
import java.util.ArrayList;

import xyz.acygn.mokapot.CommunicationAddress;
import xyz.acygn.mokapot.DistributedCommunicator;

public class Game {

	public ArrayList<Car> players;
	public Point offset, prevOffset;
	public Track track;
	public boolean gameWon;
	public float interpolation = 1f;
	public long seed = 1000l;
	public int playersTurn;

	public Game(int players, int ais, int trackWidth, int trackLength, int trackSegLength, int aiDifficulty, CommunicationAddress remoteAddress) {

		gameWon = false;
		track = DistributedCommunicator.getCommunicator().runRemotely(() -> new Track(seed, trackWidth, trackLength, trackSegLength),remoteAddress);
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

	public void update() {
		if (gameWon) {
			return;
		}

		boolean next = players.get(playersTurn).update(offset);
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

}
