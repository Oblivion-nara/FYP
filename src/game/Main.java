package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;

public class Main extends JFrame {

	private static boolean running;
	private static int FPS;

	public static InputHandler input;
	public static Random random;
	public static int width, height;
	private static GraphicsDevice device;

	private Graphics g;
	private Image offImage;
	private Game game;

	public static void main(String[] args) {
		try {
			device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[1];
		} catch (Exception e) {
			System.err.println("no second display");
		}
		new Main().run();
	}

	public void run() {
		init();
		loop();
	}

	public void init() {
		running = true;
		input = new InputHandler(this);
		random = new Random();
		FPS = 60;

		this.setTitle("Racing");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1000, 1000);
		this.setLocationRelativeTo(null);
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// this.setUndecorated(true);
		this.setVisible(true);

		width = this.getWidth();
		height = this.getHeight();

		offImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = this.getGraphics();

		game = new Game(1);
	}

	public void loop() {

		long beforeTime, afterTime, goalTime;
		goalTime = 1000 / FPS;
		while (running) {
			beforeTime = System.currentTimeMillis();
			update();
			if (running) {
				draw();
			}
			afterTime = System.currentTimeMillis();
			if (afterTime - beforeTime < goalTime) {
				try {
					Thread.sleep(goalTime - (afterTime - beforeTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		this.dispose();

	}

	private void update() {
		width = this.getWidth();
		height = this.getHeight();

		if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
			running = false;
			return;
		}
		if (input.isKeyDown(KeyEvent.VK_R)) {
			// game.newGame();
			input.artificialKeyReleased(KeyEvent.VK_R);
		}

		game.update();

	}

	private void draw() {

		Graphics offg = offImage.getGraphics();
		offg.setColor(Color.gray);
		offg.fillRect(0, 0, width, height);
		game.draw(offg);
		Point mouse = input.getMousePositionRelativeToComponent();
		if (mouse != null) {
			offg.setColor(Color.blue);
			offg.fillRect(mouse.x, mouse.y, 2, 2);
		}
		g.drawImage(offImage, 0, 0, width, height, null);

	}

}
