package game;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

public class Snake {

	private int maxSize, tiles;
	private long updateTime, interval;
	private String player;
	private Point movement;
	private LinkedList<Point> body;
	private Color color;
	private AI ai;

	public Snake(Point head, int tiles, Color color) {

		this.tiles = tiles;
		this.color = color;
		body = new LinkedList<>();
		body.add(head);
		maxSize = 5;
		movement = new Point(0, 0);
		interval = 100000000;
		updateTime = System.nanoTime();

	}
	
	public void setAI(AI ai){
		this.ai = ai;
	}
	public AI getAI(){
		return ai;
	}
	
	public Color getColor(){
		return color;
	}

	public ArrayList<Point> collidedSelf() {
		Point head = body.getFirst();
		for (int i = 1; i < body.size() - 1; i++) {

			if (body.get(i) != null && head.x == body.get(i).x && body.get(i).y == head.y) {
				ArrayList<Point> walls = new ArrayList<>(body.subList(i + 1, body.size()));
				body.remove(i);
				body.removeAll(walls);
				maxSize = i;
				return walls;
			}

		}
		return new ArrayList<Point>();
	}

	public boolean collided(ArrayList<Point> walls) {
		Point head = body.getFirst();
		for (Point part : walls) {
			if (head.x == part.x && part.y == head.y) {
				return true;
			}
		}
		return false;
	}

	public boolean collide(Snake snake2) {
		Point head = body.get(0);
		for(Point body: snake2.getBody()){
			if(head.equals(body)){
				return true;
			}
		}
		return false;
	}

	public boolean eat(Point food) {
		if (body.peek().equals(food)) {
			maxSize++;
			interval *= 0.99f;
			return true;
		}
		return false;
	}

	public void move(Point direction) {

		if (System.nanoTime() < updateTime) {
			return;
		}
		updateTime += interval;
		if (movement.distance(direction) != 2) {
			movement.setLocation(direction);
		}

		Point head = body.getFirst();
		Point newHead = new Point(head);

		if (movement.x != 0) {
			newHead.x += movement.x;
			if (newHead.x < 0) {
				newHead.x = tiles - 1;
			}
			if (newHead.x >= tiles) {
				newHead.x = 0;
			}
		} else if (movement.y != 0) {
			newHead.y += movement.y;
			if (newHead.y < 0) {
				newHead.y = tiles - 1;
			}
			if (newHead.y >= tiles) {
				newHead.y = 0;
			}
		} else {
			return;
		}

		body.addFirst(newHead);
		if (body.size() > maxSize) {
			body.removeLast();
		}

	}

	public Point location() {
		return body.peek();
	}

	public LinkedList<Point> getBody() {
		return body;
	}

	public void update(boolean isAI) {
		if (!isAI) {
			if (Main.input.isKeyDown(KeyEvent.VK_W) && movement.y != 1) {
				movement.setLocation(0, -1);
				Main.input.artificialKeyReleased(KeyEvent.VK_W);
			} else if (Main.input.isKeyDown(KeyEvent.VK_S) && movement.y != -1) {
				movement.setLocation(0, 1);
				Main.input.artificialKeyReleased(KeyEvent.VK_S);
			} else if (Main.input.isKeyDown(KeyEvent.VK_A) && movement.x != 1) {
				movement.setLocation(-1, 0);
				Main.input.artificialKeyReleased(KeyEvent.VK_A);
			} else if (Main.input.isKeyDown(KeyEvent.VK_D) && movement.x != -1) {
				movement.setLocation(1, 0);
				Main.input.artificialKeyReleased(KeyEvent.VK_D);
			}
		}
		move(movement);

	}

}
