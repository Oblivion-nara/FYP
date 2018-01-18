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

	private float zoom;
	private Graphics mainG;
	private Image finalImage, offImage, ui;
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
		zoom = 0f;

		this.setTitle("Racing");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1000, 1000);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// this.setUndecorated(true);
		this.setVisible(true);

		width = this.getWidth();
		height = this.getHeight();

		offImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		ui = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		mainG = this.getGraphics();

		int players = 1, trackWidth = 40, trackLength = 40, trackSegLength = 80, aiDifficulty = 3;
		game = new Game(players, trackWidth, trackLength, trackSegLength, aiDifficulty);
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
			game = new Game(2, 40, 40, 40, 3);
			input.artificialKeyReleased(KeyEvent.VK_R);
		}
		double wheel = input.getMouseWheelMovement();
		if (wheel != 0) {
			zoom -= wheel / 10f;
			if (zoom < 0) {
				zoom = 0;
			} else if (zoom > 2) {
				zoom = 2;
			}
			input.setZoom(zoom);
			input.stopMouseWheel();
		}

		game.update();

	}

	private void draw() {

		Graphics offg = offImage.getGraphics();
		ui.flush();
		Graphics uig = ui.getGraphics();
		offg.setColor(Color.gray);
		offg.fillRect(0, 0, width, height);
		game.draw(offg);
		game.drawui(uig);

		Point mouse = input.getMouseZoomed();
		if (mouse != null) {
			offg.setColor(Color.blue);
			offg.fillRect(mouse.x, mouse.y, 2, 2);
		}

		Graphics finalG = finalImage.getGraphics();
		finalG.drawImage(offImage, -(int) (zoom * width / 2f), -(int) (zoom * height / 2f), (int) ((1 + zoom) * width),
				(int) ((1 + zoom) * width), null);
		finalG.drawImage(ui, 0, 0, null);
		mainG.drawImage(finalImage, 0, 0, null);

	}

}
