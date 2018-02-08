package mokapot;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

/**
 * Handles input for a given Component
 * 
 * @author mxw596
 */
public class InputHandler implements KeyListener, MouseListener, MouseWheelListener {

	private boolean[] keyArray = new boolean[256];
	private boolean[] mouseArray = new boolean[MouseInfo.getNumberOfButtons()];
	private boolean overComp, mouseWheelUp = false, mouseWheelDown = false;
	private double mouseWheelMovement = 0f;
	private float zoom = 1;
	private String typedAcum = "";
	private Component c;
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static Point2D.Double midPoint = new Point2D.Double((double) screenSize.width / 2,
			(double) screenSize.height / 2);

	public void setZoom(float zoom) {
		this.zoom = zoom+1;
	}

	/**
	 * adds this class as a listener for a given Component
	 * 
	 * @param c
	 *            the Component being listened to
	 */
	public InputHandler(Component c) {
		c.addKeyListener(this);
		c.addMouseListener(this);
		c.addMouseWheelListener(this);
		this.c = c;
	}

	/**
	 * returns the position of the mouse on the screen
	 * 
	 * @return the position of the mouse on the screen as a Point
	 */
	public Point getMousePositionOnScreen() {
		try {
			return MouseInfo.getPointerInfo().getLocation();
		} catch (Exception e) {
			e.printStackTrace();
			return c.getMousePosition();
		}
	}

	/**
	 * gets the position of the mouse on the component this class is listening
	 * to
	 * 
	 * @return the position of the mouse on the component
	 */
	public Point getMousePositionRelativeToComponent() {
		try {
			return c.getMousePosition();
		} catch (Exception e) {
			e.printStackTrace();
			return MouseInfo.getPointerInfo().getLocation();
		}
	}

	public Point getMouseZoomed() {
		Point p = getMousePositionRelativeToComponent();
		if(p == null){
			return null;
		}
		p.translate(-Main.width/2, -Main.height/2);
		p = new Point((int) (p.x / zoom), (int) (p.y / zoom));
		p.translate(Main.width/2, Main.height/2);
		
		return p;
	}

	/**
	 * checks whether the key at a position in he array of keys is being pressed
	 * 
	 * @param keyCode
	 *            the id of the key being checked
	 * @return a true or false value indicating whether the key has been pressed
	 */
	public boolean isKeyDown(int keyCode) {
		return keyArray[keyCode];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyArray[e.getKeyCode()] = true;
	}

	public void artificialKeyPressed(int keyCode) {
		keyArray[keyCode] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyArray[e.getKeyCode()] = false;
	}

	public void artificialKeyReleased(int keyCode) {
		keyArray[keyCode] = false;
	}

	public String getTypedAcum() {
		return typedAcum;
	}

	public void clearTypedAcum() {
		typedAcum = "";
	}

	@Override
	public void keyTyped(KeyEvent e) {
		typedAcum += e.getKeyChar();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	public boolean isMouseOverComp() {
		return overComp;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		overComp = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		overComp = false;
	}

	public boolean isMouseDown(int mouseButton) {
		return mouseArray[mouseButton];
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseArray[e.getButton()] = true;
	}

	public void artificialMousePressed(int mouseButton) {
		mouseArray[mouseButton] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseArray[e.getButton()] = false;
	}

	public void artificialMouseReleased(int mouseButton) {
		mouseArray[mouseButton] = false;
	}

	public boolean getMouseWheelUp() {
		return mouseWheelUp;
	}

	public boolean getMouseWheelDown() {
		return mouseWheelDown;
	}

	public double getMouseWheelMovement() {
		return mouseWheelMovement;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheelMovement = e.getPreciseWheelRotation();
		if (mouseWheelMovement < 0) {
			mouseWheelUp = true;
			mouseWheelDown = false;
		} else if (mouseWheelMovement > 0) {
			mouseWheelUp = false;
			mouseWheelDown = true;
		} else {
			mouseWheelUp = false;
			mouseWheelDown = false;
		}
	}

	public void stopMouseWheel() {
		mouseWheelMovement = 0;
		mouseWheelUp = false;
		mouseWheelDown = false;
	}

}
