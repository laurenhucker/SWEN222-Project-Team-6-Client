package client.level.tile;

import client.graphics.Screen;
import client.graphics.Sprite;

public class WoodTile extends Tile{

	public WoodTile(Sprite sprite) {
		super(sprite);
	}
	
	public void render(int x, int y, Screen screen){
		screen.renderTile(x << 6, y << 6, this);
	}
	
	public String toString(){
		return "Wood Tile";
	}

}
