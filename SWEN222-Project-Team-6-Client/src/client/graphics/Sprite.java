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
	public static Sprite voidSprite = new Sprite(0, 0, SpriteSheet.TILES),
			grass = new Sprite(1, 0, SpriteSheet.TILES),
			dirt = new Sprite(2, 0, SpriteSheet.TILES),
			rock = new Sprite(3, 0, SpriteSheet.TILES),
			sand = new Sprite(4, 0, SpriteSheet.TILES),
			wood = new Sprite(5, 0, SpriteSheet.TILES),
			wood_wall = new Sprite(6, 0, SpriteSheet.TILES),
			tree = new Sprite(7, 0, SpriteSheet.TILES),
			south_wall_face = new Sprite(0, 1, SpriteSheet.TILES),
			north_wall_face = new Sprite(1, 1, SpriteSheet.TILES),
			east_wall_face = new Sprite(2, 1, SpriteSheet.TILES),
			west_wall_face = new Sprite(3, 1, SpriteSheet.TILES),
			south_west_wall_corner = new Sprite(4, 1, SpriteSheet.TILES),
			south_east_wall_corner = new Sprite(5, 1, SpriteSheet.TILES),
			north_west_wall_corner = new Sprite(6, 1, SpriteSheet.TILES),
			north_east_wall_corner = new Sprite(7, 1, SpriteSheet.TILES);

	public static Sprite sword_wood = new Sprite(0, 0, SpriteSheet.ITEMS),
			sword_metal = new Sprite(0, 1, SpriteSheet.ITEMS),
			sword_crystal = new Sprite(0, 2, SpriteSheet.ITEMS),
			axe_wood = new Sprite(1, 0, SpriteSheet.ITEMS),
			axe_metal= new Sprite(1, 1, SpriteSheet.ITEMS),
			axe_crystal = new Sprite(1, 2, SpriteSheet.ITEMS),
			staff_wood = new Sprite(2, 0, SpriteSheet.ITEMS),
			staff_metal = new Sprite(2, 1, SpriteSheet.ITEMS),
			staff_crystal = new Sprite(2, 2, SpriteSheet.ITEMS),
			bow_wood = new Sprite(3, 0, SpriteSheet.ITEMS),
			bow_metal = new Sprite(3, 1, SpriteSheet.ITEMS),
			bow_crystal = new Sprite(3, 2, SpriteSheet.ITEMS);

	public static Sprite shield = new Sprite(0, 0, SpriteSheet.PROJECTILES),
			fireball = new Sprite(1, 0, SpriteSheet.PROJECTILES),
			bullet = new Sprite(2, 0, SpriteSheet.PROJECTILES);

	public static Sprite penisMob = new Sprite(0, 15, SpriteSheet.ENTITIES),
			chestMob = new Sprite(1, 15, SpriteSheet.ENTITIES),
			ghostMob = new Sprite(2, 15, SpriteSheet.ENTITIES),
			knightMob = new Sprite(3, 15, SpriteSheet.ENTITIES),
			davidMob = new Sprite(4, 15, SpriteSheet.ENTITIES);
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
