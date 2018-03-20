package mokapot2;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DrawCar {

	private Car car;

	public DrawCar(Car car) {
		this.car = car;
	}

	public void draw(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		if (car.myTurn && !car.onTrack) {
			g2.setColor(Color.BLACK);
			Font font = new Font("Verdana", Font.BOLD, 40);
			g.setFont(font);
			FontMetrics met = g.getFontMetrics();
			String text = "Return to Track";
			int width = met.stringWidth(text);
			g.drawString(text, (Main.width - width) / 2, Main.height / 5);
			g2.fillOval((int) car.trackReturn.getX() - car.trackSize / 2, (int) car.trackReturn.getY() - car.trackSize / 2, car.trackSize,
					car.trackSize);
		}
//		g2.setColor(Color.blue);
//		g2.fillRect((int) car.location.getX() - 5, (int) car.location.getY() - 5, 10, 10);
//		g2.setColor(Color.black);
//		g2.fillRect((int) car.trackReturn.getX() - 5, (int) car.trackReturn.getY() - 5, 10, 10);
		if (car.myTurn) {
			System.err.println("null car");
			g2.setColor(Color.red);
			g2.fillRect((int) (car.location.getX() + car.velocity.getX()) - 1, (int) (car.location.getY() + car.velocity.getY()) - 1, 2,
					2);
			g2.drawOval((int) (car.location.getX() + car.velocity.getX() - car.movement),
					(int) (car.location.getY() + car.velocity.getY() - car.movement), car.movement * 2, car.movement * 2);
			if (car.drawMovement) {
				g2.setColor(Color.WHITE);
				g2.drawLine((int) car.location.getX(), (int) car.location.getY(), car.mouse.x, car.mouse.y);
			}
		}

	}

}
