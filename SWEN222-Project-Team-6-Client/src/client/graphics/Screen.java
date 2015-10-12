package client.graphics;

import java.util.Random;

import client.entity.mob.Player;
import client.level.tile.Tile;

public class Screen {

	private int width;
	private int height;
	public int xOffset, yOffset;
	
	private Random rand = new Random();
	public static int NUM_TILES;
	public static final int TILE_SIZE = 64;
	
	public int[] pixels;
	public int[] tiles = new int[NUM_TILES];
	
	public Screen(int w, int h){
		this.width = w;
		this.height = h;
		this.pixels = new int[width*height];
		NUM_TILES = (w/TILE_SIZE)*(h/TILE_SIZE);
	}
	
	public void clear(){
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = 0;
		}
	}
	
	public void renderTile(int xPos, int yPos, Tile tile){
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0; y < tile.sprite.SIZE; y++){
			int yAbs = yPos + y;
			for(int x = 0; x < tile.sprite.SIZE; x++){
				int xAbs = xPos + x;
				if(xAbs < -tile.sprite.SIZE || xAbs >= width || yAbs < 0 || yAbs >= height) break;
				if(xAbs < 0) xAbs = 0;
				pixels[xAbs + yAbs * width] = tile.sprite.pixels[x + y * tile.sprite.SIZE];
			}
		}
	}
	
	public void renderProjectile(int xPos, int yPos, Sprite sprite){
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0; y < sprite.SIZE; y++){
			int yAbs = yPos + y;
			for(int x = 0; x < sprite.SIZE; x++){
				int xAbs = xPos + x;
				if(xAbs < -sprite.SIZE || xAbs >= width || yAbs < 0 || yAbs >= height) break;
				if(xAbs < 0) xAbs = 0;
				pixels[xAbs + yAbs * width] = sprite.pixels[x + y * sprite.SIZE];
			}
		}
	}
	
	private Sprite getSpriteToUse(Player player){
		int dir = player.getDir();
		int spriteToUse = 0;
		if(player.getWalkCycle() >= 0 && player.getWalkCycle() < 15){
			spriteToUse = (dir*3) - 1;
		} else if(player.getWalkCycle() >= 15 && player.getWalkCycle() < 30){
			spriteToUse = (dir*3) - 2;
		} else if(player.getWalkCycle() >= 30 && player.getWalkCycle() < 45){
			spriteToUse = (dir*3) - 3;
		} else {
			spriteToUse = (dir*3) - 2;
		}
		return player.sprites[spriteToUse];
	}
	
	public void renderPlayer(int xPos, int yPos, Player player){
		xPos -= this.xOffset;
		yPos -= this.yOffset;
		Sprite sprite = getSpriteToUse(player);
		for(int y = 0; y < sprite.SIZE; y++){
			int yAbs = yPos + y;
			for(int x = 0; x < sprite.SIZE; x++){
				int xAbs = xPos + x;
				if(xAbs < -sprite.SIZE || xAbs >= width || yAbs < 0 || yAbs >= height) break;
				if(xAbs < 0) xAbs = 0;
				int col = sprite.pixels[x + y * sprite.SIZE];
				if(col != 0xffff00ff)//dont render pink
					pixels[xAbs + yAbs * width] = col;
			}
		}
	}
	
	public void renderItem(int xPos, int yPos, Sprite sprite){
		xPos -= this.xOffset;
		yPos -= this.yOffset;
		for(int y = 0; y < sprite.SIZE; y++){
			int yAbs = yPos + y;
			for(int x = 0; x < sprite.SIZE; x++){
				int xAbs = xPos + x;
				if(xAbs < -sprite.SIZE || xAbs >= width || yAbs < 0 || yAbs >= height) break;
				if(xAbs < 0) xAbs = 0;
				int col = sprite.pixels[x + y * sprite.SIZE];
				if(col != 0xffff00ff)//dont render pink
					pixels[xAbs + yAbs * width] = col;
			}
		}
	}
	
	public void renderInventory(Player player){
		
	}
	
	public void setOffset(int xOff, int yOff){
		this.xOffset = xOff;
		this.yOffset = yOff;
	}
	
	public int getWidth(){ return this.width; }
	public int getHeight(){ return this.height; }
	
}
