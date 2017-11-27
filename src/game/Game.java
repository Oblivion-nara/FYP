package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

public class Game {

	private boolean gameOver, isAI;
	private int tiles, size, spacing, score, highScore, snakes;
	private Point start, food;
	private BufferedImage grid;
	private ArrayList<Point> walls;
	private ArrayList<Snake> players;
	private ArrayList<AI> ais;
	private Font font;

	public Game(int tiles, Point start, int size) {
		this.tiles = tiles;
		this.start = start;
		this.size = size;
		snakes = 20;
		isAI = false;
		spacing = size / tiles;
		highScore = 0;
		grid = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) grid.getGraphics();
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.white);
		g.fillRect(0, 0, size, size);
		g.setColor(Color.black);
		for (int i = 0; i <= tiles; i++) {

			int space = i * spacing;
			g.drawLine(0, space, size, space);
			g.drawLine(space, 0, space, size);

		}
		walls = new ArrayList<Point>();
		players = new ArrayList<Snake>();
		ais = new ArrayList<AI>();
		font = new Font("Verdana", Font.BOLD, 40);
		newGame();

	}

	public void newGame() {
		score = 0;
		gameOver = false;
		walls.clear();
		newFood();
		players.clear();
		ais.clear();
		for (int i = 0; i < snakes; i++) {
			players.add(new Snake(new Point(5*i,5*i), tiles,
					new Color(Main.random.nextInt(255), Main.random.nextInt(255), Main.random.nextInt(255))));
			ais.add(new AI(players.get(i), tiles));
			ais.get(i).newFood(food);
		}

	}

	public void update() {

		if (score > highScore) {
			highScore = score;
		}
		if (gameOver) {
			newGame();
			return;
		}
		if (Main.input.isKeyDown(KeyEvent.VK_P)) {
			isAI = !isAI;
			Main.input.artificialKeyReleased(KeyEvent.VK_P);
		}
		if (isAI) {
			for (AI ai : ais) {
				ai.move();
			}
		}
		for (Snake snake : players) {
			snake.update(isAI);
		}

		for (Snake snake : players) {
			if (snake.eat(food)) {
				newFood();
				for (AI ai : ais) {
					ai.newFood(food);
				}
				score += 10;
				break;
			}
		}

		ArrayList<Snake> dead = new ArrayList<>();
		for (Snake snake : players) {
			ArrayList<Point> body = snake.collidedSelf();
			if (!body.isEmpty()) {
				walls.addAll(body);
				break;
			}
			for (Snake snake2 : players) {
				if (snake == snake2) {
					break;
				}
				if (snake.collide(snake2)) {
					dead.add(snake);
					break;
				}
			}
		}
		for(Snake snake:dead){
			walls.addAll(snake.getBody());
			ais.remove(snake.getAI());
			players.remove(snake);
		}

		for (Snake snake : players) {
			if (snake.collided(walls)) {
				gameOver = true;
				break;
			}
		}

	}

	private void newFood() {
		do {
			food = new Point(Main.random.nextInt(tiles), Main.random.nextInt(tiles));
		} while (walls.contains(food));
	}

	public void draw(Graphics g) {

		g.drawImage(grid, start.x, start.y, null);

		for (Snake snake : players) {
			g.setColor(snake.getColor());
			for (Point body : snake.getBody()) {
				g.fillRect(start.x + (body.x * spacing) + 2, start.y + (body.y * spacing) + 2, spacing - 3,
						spacing - 3);
			}
		}
		g.setColor(Color.red);
		for (Snake snake : players) {
			g.fillRect(start.x + (snake.getBody().get(0).x * spacing) + 2,
					start.y + (snake.getBody().get(0).y * spacing) + 2, spacing - 3, spacing - 3);
		}

		g.setColor(Color.darkGray);
		for (Point wall : walls) {
			g.fillRect(start.x + (wall.x * spacing) + 2, start.y + (wall.y * spacing) + 2, spacing - 3, spacing - 3);
		}

		g.setColor(Color.blue);
		g.fillRect(start.x + (food.x * spacing) + 2, start.y + (food.y * spacing) + 2, spacing - 3, spacing - 3);

		g.setFont(font);
		FontMetrics met = g.getFontMetrics();
		if (gameOver) {
			int x = (Main.width - met.stringWidth("Game Over. \nYour score is: " + score)) / 2 - 20;
			int y = Main.height / 2 - met.getHeight() - 20;
			g.setColor(Color.black);
			g.fillRect(x, y, met.stringWidth("Game Over. \nYour score is: " + score) + 40, met.getHeight() * 2 + 20);
			g.setColor(Color.red);

			g.drawString("Game Over. \nYour score is: " + score,
					(Main.width - met.stringWidth("Game Over. \nYour score is: " + score)) / 2,
					(Main.height - met.getHeight()) / 2);
			g.drawString("Press R to Restart", (Main.width - met.stringWidth("Press R to Restart")) / 2,
					(Main.height + met.getHeight()) / 2);
		} else {
			g.setColor(Color.black);
			g.drawString("Current score: " + score, (start.x - met.stringWidth("Current score: " + score)) / 2,
					met.getHeight());
			g.drawString("High score: " + highScore, (start.x - met.stringWidth("Current score: " + score)) / 2,
					met.getHeight() * 2);

		}

	}

}
