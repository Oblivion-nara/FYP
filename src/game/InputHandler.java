package game;

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
 * @author mxw596
 */
public class InputHandler implements KeyListener, MouseListener, MouseWheelListener{
	
	private boolean[] keyArray = new boolean[256];
	private boolean[] mouseArray = new boolean[MouseInfo.getNumberOfButtons()];
	private boolean overComp, mouseWheelUp = false, mouseWheelDown = false;
	private String typedAcum = "";
	private Component c;
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static Point2D.Double midPoint = new Point2D.Double((double)screenSize.width/2, (double)screenSize.height/2);
	
	/**
	 * adds this class as a listener for a given Component
	 * @param c the Component being listened to
	 */
	public InputHandler(Component c){
		c.addKeyListener(this);
		c.addMouseListener(this);
		c.addMouseWheelListener(this);
		this.c = c;
	}
	
	/**
	 * returns the position of the mouse on the screen
	 * @return the position of the mouse on the screen as a Point
	 */
	public Point getMousePositionOnScreen(){
		try {
			return MouseInfo.getPointerInfo().getLocation();
		} catch (Exception e) {
			e.printStackTrace();
			return c.getMousePosition();
		}
	}
	
	/**
	 * gets the position of the mouse on the component this class is listening to
	 * @return the position of the mouse on the component
	 */
	public Point getMousePositionRelativeToComponent(){
		try {
			return c.getMousePosition();
		} catch (Exception e){
			e.printStackTrace();
			return MouseInfo.getPointerInfo().getLocation();
		}
	}
	
	/**
	 * checks whether the key at a position in he array of keys is being pressed
 	 * @param keyCode the id of the key being checked
	 * @return a true or false value indicating whether the key has been pressed
	 */
	public boolean isKeyDown(int keyCode){
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
	
	public String getTypedAcum(){
		return typedAcum;
	}
	
	public void clearTypedAcum(){
		typedAcum = "";
	}

	@Override
	public void keyTyped(KeyEvent e) {
		typedAcum += e.getKeyChar();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public boolean isMouseOverComp(){
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
	
	public boolean isMouseDown(int mouseButton){
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
	
	public boolean getMouseWheelUp(){
		return mouseWheelUp;
	}
	
	public boolean getMouseWheelDown(){
		return mouseWheelDown;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() < 0){
			mouseWheelUp = true;
			mouseWheelDown = false;
		}else if (e.getWheelRotation() > 0){
			mouseWheelUp = false;
			mouseWheelDown = true;
		}else{
			mouseWheelUp = false;
			mouseWheelDown = false;
		}
	}
	
	public void stopMouseWheel(){
		mouseWheelUp = false;
		mouseWheelDown = false;
	}

}
