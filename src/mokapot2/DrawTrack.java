package mokapot2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public class DrawTrack {

	private Game track;

	public DrawTrack(Game track) {
		this.track = track;
	}

	public void draw(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		Stroke defaultStroke = g2.getStroke();
		Stroke stroke = new BasicStroke(track.getTrack().getTrackWidth());
		g2.setStroke(stroke);

		g2.setColor(Color.lightGray);
		for (int i = 0; i < track.getTrack().getMaxSegments() - 1; i++) {

			g2.drawLine((int) track.getTrack().getSegments().get(i).getX(),
					(int) track.getTrack().getSegments().get(i).getY(),
					(int) track.getTrack().getSegments().get(i + 1).getX(),
					(int) track.getTrack().getSegments().get(i + 1).getY());

		}
		// draw finish
		Point finish = track.getTrack().getSegments().get(track.getTrack().getMaxSegments() - 1);
		g2.setColor(Color.BLACK);
		g2.drawLine(finish.x, finish.y, finish.x, finish.y);
		g2.setColor(Color.white);
		int strokeSize = track.getTrack().getTrackWidth();
		g2.setStroke(new BasicStroke(strokeSize / 4));
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if ((i + j) % 2 == 0) {
					g2.drawLine(finish.x - (strokeSize / 8 * 3) + (strokeSize / 4 * i),
							finish.y - (strokeSize / 8 * 3) + (strokeSize / 4 * j),
							finish.x - (strokeSize / 8 * 3) + (strokeSize / 4 * i),
							finish.y - (strokeSize / 8 * 3) + (strokeSize / 4 * j));
				}
			}

		}
		g2.setStroke(defaultStroke);

	}

}
