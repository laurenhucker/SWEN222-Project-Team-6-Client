package client.level.tile;

import client.graphics.Screen;
import client.graphics.Sprite;

public class GrassTile extends Tile {
	
	public GrassTile(Sprite sprite) {
		super(sprite);
	}

	public void render(int x, int y, Screen screen){
		screen.renderTile(x << 6, y << 6, this);
	}
	
	public String toString(){
		return "Grass Tile";
	}
	
}
