package client.graphics;

import java.util.Random;

import client.entity.Item;
import client.entity.mob.Monster;
import client.entity.mob.Player;
import client.level.tile.Tile;

public class Screen {

	private int width;
	private int height;
	public int xOffset, yOffset;
	
	private InventoryGraphics inventory;
	
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
		inventory = new InventoryGraphics(w, h);
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
				renderHealthBar(xPos, yPos, player.getHealth(), player.getMaxHealth());
			}
		}
	}
	
	public void renderOtherPlayer(int xPos, int yPos, Player player){
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
				//renderHealthBar(xPos, yPos, player.getHealth(), player.getMaxHealth());
			}
		}
	}
	
	public void renderMonster(int xPos, int yPos, Monster monster){
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0; y < monster.getSprite().SIZE; y++){
			int yAbs = yPos + y;
			for(int x = 0; x < monster.getSprite().SIZE; x++){
				int xAbs = xPos + x;
				if(xAbs < -monster.getSprite().SIZE || xAbs >= width || yAbs < 0 || yAbs >= height) break;
				if(xAbs < 0) xAbs = 0;
				int col = monster.getSprite().pixels[x + y * monster.getSprite().SIZE];
				if(col != 0xffff00ff)
					pixels[xAbs + yAbs * width] = monster.getSprite().pixels[x + y * monster.getSprite().SIZE];
				if(xPos > 0 && yPos > 0)
					renderHealthBar(xPos, yPos, monster.getHealth(), monster.getMaxHealth());
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
		int col = inventory.getColour();
		int w = inventory.getWidth(), h = inventory.getHeight();
		
		int alpha = 1;
		
		for(int i = inventory.getX(); i < w; i += alpha){//Draws inventory background
			for(int j = inventory.getY(); j < h; j += alpha){
				pixels[i + (j*width)] = col;
			}
		}
		
		for(int i = 0; i < player.getItems().size(); i++){//Render each item in inventory
			int itemX = inventory.getItemXCoord(i);//Get current item's pixel x and y on screen
			int itemY = inventory.getItemYCoord(i);
			int spriteX = 0, spriteY = 0;//Just to count from 0-64
			Sprite itemSprite = player.getItems().get(i).getSprite();
			if(itemSprite == null) continue;
			for(int x = itemX; x < itemX + itemSprite.SIZE; x++){
				for(int y = itemY; y < itemY + itemSprite.SIZE; y++){
					int itemPixel = itemSprite.pixels[spriteX + (spriteY*itemSprite.SIZE) % 4096];
					if(itemPixel != 0xffff00ff)//dont render pink
						pixels[x + (y*width)] = itemPixel;
					spriteY++;
				}
				spriteX++;
			}
			
		}
		
	}
	
	private void renderHealthBar(int x, int y, int health, int maxHealth){
		if(health <= 0) return;
		int xPos = x - 18;//18/64/18 for health bar split
		int yPos = y - 20;//Slightly above the entity
		if(xPos < 0 || yPos < 0){
			return;
		}
		int hpBarThickness = 10, redBar = 0x800000, greenBar = 0x038000;
		for(int i = xPos; i < xPos + 100; i++){//fill red bar underneath
			for(int j = yPos; j < yPos + hpBarThickness; j++){
				pixels[i + (j*this.width)] = redBar;
			}
		}
		for(int i = xPos; i < xPos + ((health*100)/maxHealth); i++){//fill green bar on top
			for(int j = yPos; j < yPos + hpBarThickness; j++){
				pixels[i + (j*this.width)] = greenBar;
			}
		}
	}
	
	public void setOffset(int xOff, int yOff){
		this.xOffset = xOff;
		this.yOffset = yOff;
	}
	
	public int getWidth(){ return this.width; }
	public int getHeight(){ return this.height; }
	
}
