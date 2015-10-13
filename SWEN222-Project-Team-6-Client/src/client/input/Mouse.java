package client.input;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import client.entity.mob.Player;
import client.graphics.InventoryGraphics;

public class Mouse implements MouseListener, MouseMotionListener {
	
	private static int mouseX = -1;
	private static int mouseY = -1;
	private static int mouseB = -1;


	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		mouseB = e.getButton();
	}

	public void mouseReleased(MouseEvent e) {
		mouseB = -1;
	}
	
	
	
	public static int getX(){
		return mouseX;
	}
	
	public static int getY(){
		return mouseY;
	}
	
	public static int getButton(){
		return mouseB;
	}
	
	//----------------------//
	// methods for tool tip //
	//----------------------//
	
	/**
	 * used to determine where and what to draw regarding the tool tip
	 * @param g the graphics pane
	 */
	public void drawToolTip(Graphics g, Player p){
		InventoryGraphics ig = new InventoryGraphics(1366, 768);
		int width = ig.getWidth();
		int topX = ig.getX();
		int topY = ig.getY();
		int xOfMouse = getX();
		int yOfMouse = getY();
		
		width -= topX;
		int sizeOfInv = width/3;

		if(xOfMouse > topX && yOfMouse > topY){
			if(xOfMouse <= topX+sizeOfInv){
				calcToolTip(0, yOfMouse, topY, topX, sizeOfInv, g, p);
			}
			else if(xOfMouse <= topX+(sizeOfInv*2)){
				calcToolTip(1, yOfMouse, topY, topX, sizeOfInv, g, p);
			}
			else if(xOfMouse <= topX+(sizeOfInv*3)){
				calcToolTip(2, yOfMouse, topY, topX, sizeOfInv, g, p);
			}
		}
	}
	
	/**
	 * used to claculate the position of the tool tip
	 * @param col the col the mouse is in
 	 * @param yOfMouse current y coordinate of mouse
	 * @param topY upper-left x of the inventory
	 * @param topX upper-left y of the inventory
	 * @param sizeOfInv size of a space in the inventory
	 * @param g the graphics pane to draw on to
	 * @param player the player to show the inventory to
	 */
	private void calcToolTip(int col, int yOfMouse, int topY, int topX, int sizeOfInv, Graphics g, Player player){
		int row = findCol(yOfMouse, topY, sizeOfInv)-1;
		int index = (col+(row*3));
		String nameOfItem = "No Item Here";
		
		if(index < player.getItems().size()){
			nameOfItem = player.getItems().get(index).getItemName();
		}
		if(col < 2){
			drawRect(topX+(sizeOfInv*col), topY+(sizeOfInv*row), nameOfItem, g);
		}
		else{
			drawRect(topX+(sizeOfInv*col)-100, topY+(sizeOfInv*row), nameOfItem, g);
		}
	}
	
	/**
	 * used by the drawToolTip method to reduce duplicate code
	 * @param x upper-left x of the rectangle
	 * @param y upper-left y of the rectangle
	 * @param name name of the item
	 * @param g graphics pane
	 */
	private void drawRect(int x, int y, String name, Graphics g){
		g.setColor(Color.lightGray);
		g.fillRect(x, y, 120, 30);
		g.setColor(Color.black);
		
		g.drawRect(x, y, 120, 30);
		//draw the text
		g.setFont(new Font("Verdana", 0, 12));
		g.drawString(name, x+10, y+20);
	}
	
	/**
	 * used by the draw toolTip to find the col where the mouse is
	 * @param yOfMouse y position of the mouse
	 * @param topY upper-left y of the inventory
	 * @param sizeOfInv size of each space in inventory
	 * @return row the mouse is on
	 */
	private int findCol(int yOfMouse, int topY, int sizeOfInv) {
		if(yOfMouse <= topY+sizeOfInv){
			return 1;
		}
		else if(yOfMouse <= topY+(sizeOfInv*2)){
			return 2;
		}
		else if(yOfMouse <= topY+(sizeOfInv*3)){
			return 3;
		}
		else{
			return 4;
		}
	}

}
