package client.level.tile;

import client.graphics.Screen;
import client.graphics.Sprite;

public class Tile {

	protected int x, y;
	public Sprite sprite;
	public TileCoordinate coords;

	public static final Tile VOID = new VoidTile(Sprite.voidSprite);
	public static final Tile GRASS = new GrassTile(Sprite.grass);
	public static final Tile DIRT = new VoidTile(Sprite.dirt);
	public static final Tile ROCK = new VoidTile(Sprite.rock);
	public static final Tile SAND = new VoidTile(Sprite.sand);
	
	/**
	 * Handles construction of this Tile object
	 * @param sprite - the Sprite which the Tile shows
	 */
	public Tile(Sprite sprite){
		this.sprite = sprite;
	}
	
	/**
	 * Renders this tile on the specified screen with the specified coordinates
	 * @param x - x position to render at
	 * @param y - y position to render at
	 * @param screen - screen to render on
	 */
	public void render(int x, int y, Screen screen){
		
	}
	
	/**
	 * Does this tile prevent movement through it?
	 * @return
	 */
	public boolean solid(){
		return false;
	}
}
