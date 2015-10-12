package client.graphics;

import client.GameClient;

public class Sprite {

	public final int SIZE = GameClient.TILE_WIDTH;
	private int x, y;
	public int[] pixels;
	private SpriteSheet sheet;
	
	/*
	 * Start of static Sprites taken from SpriteSheets in SpriteSheet class
	 */
	public static Sprite voidSprite = new Sprite(0, 0, SpriteSheet.TILES);
	public static Sprite grass = new Sprite(1, 0, SpriteSheet.TILES);
	public static Sprite dirt = new Sprite(2, 0, SpriteSheet.TILES);
	public static Sprite rock = new Sprite(3, 0, SpriteSheet.TILES);
	public static Sprite sand = new Sprite(4, 0, SpriteSheet.TILES);

	public static Sprite fireball = new Sprite(1, 0, SpriteSheet.PROJECTILES);
	public static Sprite bullet = new Sprite(2, 0, SpriteSheet.PROJECTILES);
	
	public static Sprite penisMob = new Sprite(0, 15, SpriteSheet.ENTITIES);
	
	public static Sprite[] player1 = {
		new Sprite(0, 0, SpriteSheet.ENTITIES),//NORTH LEFT FOOT
		new Sprite(1, 0, SpriteSheet.ENTITIES),//NORTH STILL
		new Sprite(2, 0, SpriteSheet.ENTITIES),//NORTH RIGHT FOOT
		new Sprite(3, 0, SpriteSheet.ENTITIES),//EAST
		new Sprite(4, 0, SpriteSheet.ENTITIES),//EAST
		new Sprite(5, 0, SpriteSheet.ENTITIES),//EAST
		new Sprite(6, 0, SpriteSheet.ENTITIES),//SOUTH
		new Sprite(7, 0, SpriteSheet.ENTITIES),//SOUTH
		new Sprite(8, 0, SpriteSheet.ENTITIES),//SOUTH
		new Sprite(9, 0, SpriteSheet.ENTITIES),//WEST
		new Sprite(10, 0, SpriteSheet.ENTITIES),//WEST
		new Sprite(11, 0, SpriteSheet.ENTITIES)//WEST
	};
	
	public static Sprite[] player2 = {
		new Sprite(0, 1, SpriteSheet.ENTITIES),//NORTH LEFT FOOT
		new Sprite(1, 1, SpriteSheet.ENTITIES),//NORTH STILL
		new Sprite(2, 1, SpriteSheet.ENTITIES),//NORTH RIGHT FOOT
		new Sprite(3, 1, SpriteSheet.ENTITIES),//EAST
		new Sprite(4, 1, SpriteSheet.ENTITIES),//EAST
		new Sprite(5, 1, SpriteSheet.ENTITIES),//EAST
		new Sprite(6, 1, SpriteSheet.ENTITIES),//SOUTH
		new Sprite(7, 1, SpriteSheet.ENTITIES),//SOUTH
		new Sprite(8, 1, SpriteSheet.ENTITIES),//SOUTH
		new Sprite(9, 1, SpriteSheet.ENTITIES),//WEST
		new Sprite(10, 1, SpriteSheet.ENTITIES),//WEST
		new Sprite(11, 1, SpriteSheet.ENTITIES)//WEST
	};
	
	public static Sprite[] player3 = {
		new Sprite(0, 2, SpriteSheet.ENTITIES),//NORTH LEFT FOOT
		new Sprite(1, 2, SpriteSheet.ENTITIES),//NORTH STILL
		new Sprite(2, 2, SpriteSheet.ENTITIES),//NORTH RIGHT FOOT
		new Sprite(3, 2, SpriteSheet.ENTITIES),//EAST
		new Sprite(4, 2, SpriteSheet.ENTITIES),//EAST
		new Sprite(5, 2, SpriteSheet.ENTITIES),//EAST
		new Sprite(6, 2, SpriteSheet.ENTITIES),//SOUTH
		new Sprite(7, 2, SpriteSheet.ENTITIES),//SOUTH
		new Sprite(8, 2, SpriteSheet.ENTITIES),//SOUTH
		new Sprite(9, 2, SpriteSheet.ENTITIES),//WEST
		new Sprite(10, 2, SpriteSheet.ENTITIES),//WEST
		new Sprite(11, 2, SpriteSheet.ENTITIES)//WEST
	};
	
	public Sprite( int x, int y, SpriteSheet sheet){
		this.x = x*SIZE;
		this.y = y*SIZE;
		this.pixels = new int[SIZE*SIZE];
		this.sheet = sheet;
		load();
	}
	
	private void load(){
		for(int y = 0; y < SIZE; y++){
			for(int x = 0; x < SIZE; x++){
				pixels[x+y*SIZE] = sheet.pixels[(x+this.x) + (y+this.y)*sheet.SIZE];
			}
		}
	}
	
}
