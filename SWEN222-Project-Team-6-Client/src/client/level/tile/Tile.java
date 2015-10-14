package client.level.tile;

import client.graphics.Screen;
import client.graphics.Sprite;
import client.level.tile.wallTiles.EastWallFaceTile;
import client.level.tile.wallTiles.NorthEastWallCornerTile;
import client.level.tile.wallTiles.NorthWallFaceTile;
import client.level.tile.wallTiles.NorthWestWallCornerTile;
import client.level.tile.wallTiles.SouthEastWallCornerTile;
import client.level.tile.wallTiles.SouthWallFaceTile;
import client.level.tile.wallTiles.SouthWestWallCornerTile;
import client.level.tile.wallTiles.WestWallFaceTile;
import client.level.tile.wallTiles.WoodWallTile;

public class Tile {

	protected int x, y;
	public Sprite sprite;
	public TileCoordinate coords;

	public static final Tile VOID = new VoidTile(Sprite.voidSprite);
	public static final Tile GRASS = new GrassTile(Sprite.grass);
	public static final Tile DIRT = new DirtTile(Sprite.dirt);
	public static final Tile ROCK = new RockTile(Sprite.rock);
	public static final Tile SAND = new SandTile(Sprite.sand);
	public static final Tile WOOD = new WoodTile(Sprite.wood);
	public static final Tile WOOD_WALL = new WoodWallTile(Sprite.wood_wall);
	public static final Tile TREE = new WoodWallTile(Sprite.tree);
	public static final Tile NORTH_WALL = new NorthWallFaceTile(Sprite.north_wall_face);
	public static final Tile SOUTH_WALL = new SouthWallFaceTile(Sprite.south_wall_face);
	public static final Tile EAST_WALL = new EastWallFaceTile(Sprite.east_wall_face);
	public static final Tile WEST_WALL = new WestWallFaceTile(Sprite.west_wall_face);
	public static final Tile NORTH_EAST_CORNER = new NorthEastWallCornerTile(Sprite.north_east_wall_corner);
	public static final Tile SOUTH_EAST_CORNER = new SouthEastWallCornerTile(Sprite.south_east_wall_corner);
	public static final Tile NORTH_WEST_CORNER = new NorthWestWallCornerTile(Sprite.north_west_wall_corner);
	public static final Tile SOUTH_WEST_CORNER = new SouthWestWallCornerTile(Sprite.south_west_wall_corner);
	
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
