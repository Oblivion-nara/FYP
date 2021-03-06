package rmi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import game.StaticVars;

public class Main extends JFrame {

	private static boolean running;
	private static int FPS;

	public static InputHandler input;
	public static Random random;
	public static int width, height;

	private float zoom;
	private Graphics mainG;
	private Image finalImage, offImage;
	private GameInterface game;
	private TrackInterface track;
	private ArrayList<CarInterface> cars;

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
			Registry registry = LocateRegistry.getRegistry(2001);
			game = (GameInterface) registry.lookup("Game");
			track = (TrackInterface) registry.lookup("Track");
			cars = new ArrayList<>();
			for (int i = 0; i < StaticVars.numPlayers + StaticVars.ais; i++) {
				cars.add((CarInterface) registry.lookup("Car" + i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			running = false;
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
		this.dispose();

	}

	private void update() {
		width = this.getWidth();
		height = this.getHeight();

		if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
			running = false;
			return;
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

		try {
			game.update(input.getMouseZoomed(), input.isMouseDown(1));
			input.artificialMouseReleased(1);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void draw() {

		Graphics offg = offImage.getGraphics();
		offg.setColor(Color.gray);
		offg.fillRect(0, 0, width, height);
		try {
			Game.draw(offg, game, track, cars);

			Point mouse = input.getMouseZoomed();
			if (mouse != null) {
				offg.setColor(Color.blue);
				offg.fillRect(mouse.x, mouse.y, 2, 2);
			}

			Graphics finalG = finalImage.getGraphics();
			finalG.drawImage(offImage, -(int) (zoom * width / 2f), -(int) (zoom * height / 2f),
					(int) ((1 + zoom) * width), (int) ((1 + zoom) * width), null);
			Game.drawui(finalG, game);
		} catch (RemoteException e) {
			e.printStackTrace();
			running = false;
		}
		mainG.drawImage(finalImage, 0, 0, null);

	}

}
