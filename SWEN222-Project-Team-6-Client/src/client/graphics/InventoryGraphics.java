package client.graphics;

public class InventoryGraphics {

	private final int borderPadding = 10, spritePadding = 3;
	private final int spriteSize = 64;
	private final int numCol = 3, numRow = 4;

	private int x;
	private int y;

	private int width;
	private int height;

	private final int invColour = 0x573820;

	public InventoryGraphics(int screenWidth, int screenHeight){
		width = screenWidth - x;
		height = screenHeight - y;
		x = screenWidth - ((spriteSize + spritePadding) * numCol) - borderPadding;
		y = screenHeight - ((spriteSize + spritePadding) * numRow) - borderPadding;
	}

	/**
	 * Simple check to return the x position where a specified item should be.
	 * @param itemNum - index of item in inventory
	 * @return the x position where a specified item should be.
	 */
	public int getItemXCoord(int itemNum){
		int x = 0;//Coordinate precise location
		int xPos;//Pixel precise location
		switch(itemNum){
		case 0:
		case 3:
		case 6:
		case 9:
			x = 0;
			break;
		case 1:
		case 4:
		case 7:
		case 10:
			x = 1;
			break;
		case 2:
		case 5:
		case 8:
		case 11:
			x = 2;
			break;
		}
		
		xPos = this.x + (this.borderPadding + (this.spritePadding + this.spriteSize) * x);
		
		return xPos;
	}

	/**
	 * Simple check to return the y position where a specified item should be.
	 * @param itemNum - index of item in inventory
	 * @return the y position where a specified item should be.
	 */
	public int getItemYCoord(int itemNum){
		int y = 0;//Coordinate precise location
		int yPos;//Pixel precise location
		switch(itemNum){
		case 0:
		case 1:
		case 2:
			y = 0;
			break;
		case 3:
		case 4:
		case 5:
			y = 1;
			break;
		case 6:
		case 7:
		case 8:
			y = 2;
			break;
		case 9:
		case 10:
		case 11:
			y = 3;
			break;
		}

		yPos = this.y + (this.borderPadding + (this.spritePadding + this.spriteSize) * y);
		
		return yPos;
	}

	/**
	 * @return x value of top left corner of inventory
	 */
	public int getX(){
		return this.x;
	}

	/**
	 * @return y value of top left corner of inventory
	 */
	public int getY(){
		return this.y;
	}

	/**
	 * @return hexadecimal value of inventory colour
	 */
	public int getColour(){
		return this.invColour;
	}

	/**
	 * @return width of inventory
	 */
	public int getWidth(){
		return this.width;
	}

	/**
	 * @return height of inventory
	 */
	public int getHeight(){
		return this.height;
	}

}
