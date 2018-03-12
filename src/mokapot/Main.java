package mokapot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import rmi.CarInterface;
import rmi.TrackInterface;
import xyz.acygn.mokapot.CommunicationAddress;
import xyz.acygn.mokapot.DistributedCommunicator;

public class Main extends JFrame {

	public static InputHandler input;
	public static Random random;
	public static int width = 1000, height = 1000;

	private boolean running;
	private int FPS;
	private float zoom;
	private Graphics mainG;
	private Image finalImage, offImage;
	private Game game;
	private Track track;
	private ArrayList<Car> cars;
	private CommunicationAddress remoteAddress;

	public static void main(String[] args) {
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
		finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		mainG = this.getGraphics();

		try {
			// remoteAddress =
			// int players = 0, ais = 1, trackWidth = 40, trackLength = 40,
			// trackSegLength = 80, aiDifficulty = 3;
			SetupServer setup = new SetupServer();
			game = setup.game();
			track = game.getRemoteTrack();
			cars = game.getRemoteCars();
		} catch (Exception e) {
			System.err.println("failed");
			e.printStackTrace();
			this.dispose();
			System.exit(0);
		}

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
		game = null;
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
			// game = new Game(1, 1, 40, 40, 40, 3,remoteAddress);
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

		game.update(input.getMouseZoomed(), input.isMouseDown(1));
		input.artificialMouseReleased(1);

	}

	private void draw() {

		Graphics offg = offImage.getGraphics();
		offg.setColor(Color.gray);
		offg.fillRect(0, 0, width, height);
		Game.draw(offg, game,track,cars);

		Point mouse = input.getMouseZoomed();
		if (mouse != null) {
			offg.setColor(Color.blue);
			offg.fillRect(mouse.x, mouse.y, 2, 2);
		}

		Graphics finalG = finalImage.getGraphics();
		finalG.drawImage(offImage, -(int) (zoom * width / 2f), -(int) (zoom * height / 2f), (int) ((1 + zoom) * width),
				(int) ((1 + zoom) * width), null);
		Game.drawui(finalG, game);
		mainG.drawImage(finalImage, 0, 0, null);

	}

}
