package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

public class AI {

	private int tiles, dir;
	private Snake snake;
	private Point food, movement;

	public AI(Snake snake, int tiles) {
		this.tiles = tiles;
		this.snake = snake;
		movement = new Point(0, 0);
	}

	public void newFood(Point food) {
		this.food = food;
	}

	private void moveUp() {
		movement.setLocation(0, -1);
		dir = 0;
	}

	private void moveDown() {
		movement.setLocation(0, 1);
		dir = 1;
	}

	private void moveRight() {
		movement.setLocation(1, 0);
		dir = 3;
	}

	private void moveleft() {
		movement.setLocation(-1, 0);
		dir = 2;
	}

	public void move() {
		dir = 0;
		int x = snake.location().x;
		int y = snake.location().y;

		boolean down = false, right = false;

		int difx, dify;
		difx = food.x - x;
		if (Math.abs(difx) > tiles / 2) {
			difx = tiles - Math.abs(difx);
		}
		dify = food.y - y;
		if (Math.abs(dify) > tiles / 2) {
			dify = tiles - Math.abs(dify);
		}

		if (difx < dify) {

			if ((y < food.y && food.y - y < tiles / 2) || (food.y - y < -tiles / 2)) {
				moveDown();
			} else {
				if (y == food.y) {
					if ((x < food.x && food.x - x < tiles / 2) || (food.x - x < -tiles / 2)) {
						moveRight();
					} else {
						moveleft();
					}
				} else {
					moveUp();
				}
			}

		} else {

			if ((x < food.x && food.x - x < tiles / 2) || (food.x - x < -tiles / 2)) {
				moveRight();
			} else {
				if (x == food.x) {
					if ((y < food.y && food.y - y < tiles / 2) || (food.y - y < -tiles / 2)) {
						moveDown();
					} else {
						moveUp();
					}
				} else {
					moveleft();
				}
			}
		}
		snake.move(movement);

	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		switch (dir) {
		case 0:
			g.drawLine(50, 50, 50, 0);
			break;
		case 1:
			g.drawLine(50, 50, 50, 100);
			break;
		case 2:
			g.drawLine(50, 50, 0, 50);
			;
			break;
		case 3:
			g.drawLine(50, 50, 100, 50);
			break;
		}
		g.drawString(movement.toString(), 20, 150);
		g.drawString(food.toString(), 20, 250);
		g.drawString(snake.location().toString(), 20, 350);

	}

}
