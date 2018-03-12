package mokapot;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

public class DrawGame {

	private Game game;
	private DrawTrack drawTrack;
	private ArrayList<DrawCar> drawCars;

	public DrawGame(Game game) {

		this.game = game;
		drawTrack = new DrawTrack(game);
		drawCars = new ArrayList<>();
		for (Car car : game.getPlayers()) {
			drawCars.add(new DrawCar(car));
		}
	}

	public void drawui(Graphics g) {

		// tells the players whos turn it is
		Font font = new Font("Verdana", Font.BOLD, 40);
		g.setFont(font);
		g.setColor(Color.black);
		FontMetrics met = g.getFontMetrics();
		String turn = "Players turn:  " + (game.getPlayersTurn() + 1);
		int width = met.stringWidth(turn);
		g.drawString(turn, 50, 100);
		// tells the players whos won
		if (game.isGameWon()) {
			String winner = "The winner is: Player " + (game.getPlayersTurn() + 1);
			width = met.stringWidth(winner);
			g.drawString(winner, (Main.width - width) / 2, 300);
		}
	}

	public void draw(Graphics g) {

		g.translate(
				-(int) (game.getInterpolation() * game.getOffset().x
						+ (1 - game.getInterpolation()) * game.getPrevOffset().x),
				-(int) (game.getInterpolation() * game.getOffset().y
						+ (1 - game.getInterpolation()) * game.getPrevOffset().y));
		drawTrack.draw(g);
		drawCars.get(0).draw(g);

		g.translate(game.getOffset().x, game.getOffset().y);
	}

}
