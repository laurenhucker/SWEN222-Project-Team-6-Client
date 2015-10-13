package client.level.tile.wallTiles;

import client.graphics.Screen;
import client.graphics.Sprite;
import client.level.tile.Tile;

public class SouthWallFaceTile extends Tile {

	public SouthWallFaceTile(Sprite sprite) {
		super(sprite);
	}

	public void render(int x, int y, Screen screen){
		screen.renderTile(x << 6, y << 6, this);
	}
	
	public String toString(){
		return "South Wall Face Tile";
	}
	
	public boolean solid(){
		return true;
	}

}
