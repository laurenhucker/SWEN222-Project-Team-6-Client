package client.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private boolean[] keys = new boolean[65536];/*maximum num in char[]*/
	public boolean up, down, left, right, e, esc;
	
	/**
	 * Called inside update() in Game class.
	 * So 60 times per second, these booleans are updated.
	 * And 60 times per second, if any of these are true, 
	 * the image will be offset respectively
	 */
	public void update(){
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		e = keys[KeyEvent.VK_E];
		esc = keys[KeyEvent.VK_ESCAPE];
	}
	
	/**
	 * When key is pressed down, set that particular key's position in the array to true
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	/**
	 * When key is then released, set that particular key's position in the array to false
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void forceRelease(int k){
		keys[k] = false;
	}

}
