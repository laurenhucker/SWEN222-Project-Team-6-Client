package client.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import client.Client;

public class SpriteSheet {

	private final String PATH;
	public final int SIZE;
	public int[] pixels;
	
	/*
	 * Start of SpriteSheets
	 */
	public static final SpriteSheet TILES = new SpriteSheet("/textures/TILE_SHEET.png", Client.TILE_WIDTH*16);
	public static final SpriteSheet ENTITIES = new SpriteSheet("/textures/ENTITY_SHEET.PNG", Client.TILE_WIDTH*16);
	public static final SpriteSheet ITEMS = new SpriteSheet("/item/stars.png", Client.TILE_WIDTH);
	
	/**
	 * Handles construction of SpriteSheet object
	 * @param p - path to SpriteSheet file
	 * @param s - name of SpriteSheet file
	 */
	public SpriteSheet(String p, int s){
		this.PATH = p;
		this.SIZE = s;
		pixels = new int[SIZE*SIZE];
		loadSheet();
	}
	
	/**
	 * Creates image from spritesheet from PATH
	 */
	private void loadSheet() {
		try {
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(PATH));
			int width = image.getWidth();
			int height = image.getHeight();
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {e.printStackTrace();}
	}
	
}
