package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class Track {

	private Random random;
	private final int maxSegments = 20;
	private final float seglength = 50;
	private ArrayList<Point> segments;
	private int strokeSize = 20;

	public Track(long seed) {

		random = new Random(seed);
		segments = new ArrayList<>();
		initTrack();
	}

	private void initTrack() {

		Point current = new Point(Main.width / 2, Main.height / 2);
		float angle, direction = (float) (random.nextFloat() * Math.PI * 2); // 0*
																				// is
																				// right
																				// ->
																				// going
																				// clockwise
																				// 0-2pi
		segments.add(new Point(current));

		for (int i = 1; i < maxSegments; i++) {
			angle = (float) (random.nextFloat() * Math.PI - Math.PI / 2);
			direction += angle;
			direction %= (Math.PI * 2);
			Point temp = new Point(current);
			temp.translate((int) (seglength * Math.cos(direction)), (int) (seglength * Math.sin(direction)));
			if (basicIntersects(i - 1, current, temp)) {
				i--;
				continue;
			}
			current = temp;
			segments.add(new Point(current));
			System.out.println(current);

		}

	}
	
	public int getTrackWidth(){
		return strokeSize;
	}

	public boolean basicIntersects(int currentMax, Point start, Point end) {

		for (int i = 0; i < currentMax; i++) {
			if (segments.get(i).distance(end) < seglength) {
				return true;
			}
		}
		return false;

	}

	public Point getStart() {
		return segments.get(0);
	}

	public boolean wins(Point2D loc) {

		if (segments.get(maxSegments - 1).distance(loc) < strokeSize / 2) {
			return true;
		}
		return false;

	}

	public boolean inRange(Point loc, Point returnLoc) {
		return (loc.distance(returnLoc) <= strokeSize / 2);
	}

	public Point getNearestTrackPoint(Point loc) {

		Point ret = null;
		for (int i = 0; i < maxSegments - 1; i++) {
			Point seg = segments.get(i);
			Point next = segments.get(i + 1);
			if (seg.distance(loc) < seglength + strokeSize / 2 && next.distance(loc) < seglength + strokeSize / 2) {
				double dist = Math.sqrt(Math.pow(seg.distance(loc), 2) + Math.pow(getPerpDistance(seg, next, loc), 2));
				double factor = dist / seglength;
				ret = new Point((int) ((1 - factor) * seg.getX() + factor * next.getX()),
						(int) ((1 - factor) * seg.getY() + factor * next.getY()));

				return ret;
			}

		}
		return ret;

	}

	public boolean onTrack(Point location) {

		for (int i = 0; i < maxSegments - 1; i++) {

			if (segments.get(i).distance(location) < seglength + strokeSize / 2
					&& segments.get(i + 1).distance(location) < seglength + strokeSize / 2
					&& onLine(segments.get(i), segments.get(i + 1), location)) {
				return true;
			}

		}

		return false;

	}

	private float getPerpDistance(Point from, Point to, Point loc) {

		// y = mx + c => ax + by + c = 0
		// if m = a/b
		// mx - y + c = 0
		float m1, m2, c;

		m1 = from.y - to.y;
		m2 = from.x - to.x;

		if (m1 == 0) {
			return (float) Math.abs(loc.getY() - from.y);
		} else if (m2 == 0) {
			return (float) Math.abs(loc.getX() - from.x);
		} else {
			c = from.y - m1 / m2 * from.x;

			return (float) (Math.abs((m1 * loc.getX() - m2 * loc.getY() + m2 * c))
					/ Math.sqrt(Math.pow(m1, 2) + Math.pow(m2, 2)));
		}
	}

	private boolean onLine(Point p1, Point p2, Point loc) {
		if (getPerpDistance(p1, p2, loc) < strokeSize / 2) {
			return true;
		}
		return false;
	}

	public void draw(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		Stroke defaultStroke = g2.getStroke();
		Stroke stroke = new BasicStroke(strokeSize);
		g2.setStroke(stroke);

		g2.setColor(Color.lightGray);
		for (int i = 0; i < maxSegments - 1; i++) {

			g2.drawLine((int) segments.get(i).getX(), (int) segments.get(i).getY(), (int) segments.get(i + 1).getX(),
					(int) segments.get(i + 1).getY());

		}
		g2.setStroke(defaultStroke);

	}
}
