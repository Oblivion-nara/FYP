package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class Track {

	private Random random;
	private final int maxSegments = 20;
	private final float seglength = 50;
	private ArrayList<Point> segments;

	public Track(long seed) {

		random = new Random(seed);
		segments = new ArrayList<>();
		initTrack();

	}

	private void initTrack() {

		Point current = new Point(Main.width / 2, Main.height / 2);
		float direction = 0; // 0* is right -> going clockwise 0-2pi
		segments.add(new Point(current));

		for (int i = 1; i < maxSegments; i++) {
			float angle = (float) (random.nextFloat() * Math.PI - Math.PI / 2);
			direction += angle;
			direction %= (Math.PI * 2);
			current.translate((int) (seglength * Math.cos(direction)), (int) (seglength * Math.sin(direction)));
			segments.add(new Point(current));
			System.out.println(current);
		}

	}

	public Point getStart() {
		return segments.get(0);
	}

	public void update() {

	}

	public void draw(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		Stroke defaultStroke = g2.getStroke();
		Stroke stroke = new BasicStroke(20);
		g2.setStroke(stroke);

		g2.setColor(Color.lightGray);
		for (int i = 0; i < maxSegments - 1; i++) {

			g2.drawLine((int) segments.get(i).getX(), (int) segments.get(i).getY(), (int) segments.get(i + 1).getX(),
					(int) segments.get(i + 1).getY());

		}
		g2.setStroke(defaultStroke);

	}
}
