package client.entity.mob;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import client.GameClient;
import client.entity.ArrowProjectile;
import client.entity.Item;
import client.entity.Projectile;
import client.graphics.InventoryGraphics;
import client.graphics.Screen;
import client.graphics.Sprite;
import client.input.Keyboard;
import client.input.Mouse;
import client.level.tile.TileCoordinate;


public class Player extends Mob {
	public static enum PLAYER_CLASS {
		WARRIOR,
		ARCHER,
		MAGE,
	}
	
	private Keyboard input;
	protected int dir = 3;
	private int walkCycle = 20;
	private PLAYER_CLASS playerClass;
	private int fireRate = 15;
	
	private int exp, lvl;
	private String name;
	
	private List<Item> inventory = new ArrayList<Item>();
	
	private int lastDir = 1;
	/**
	 * 1-NORTH, 2-EAST, 3-SOUTH, 4-WEST
	 */ 
	
	public Player(String name, int x, int y, Keyboard input, PLAYER_CLASS pClass){
		this.name = name;
		this.x = x;
		this.y = y;
		this.exp = 0;
		this.lvl = 1;
		this.xTile = ((this.x / GameClient.TILE_WIDTH) + ((GameClient.WIDTH / GameClient.TILE_WIDTH) / 2)) - GameClient.DEFAULT_SPAWN.getX();
		this.yTile = ((this.y / GameClient.TILE_WIDTH) + ((GameClient.HEIGHT / GameClient.TILE_WIDTH) / 2)) - GameClient.DEFAULT_SPAWN.getY();
		this.input = input;
		this.health = 100;
		playerClass = pClass;
		switch(pClass){
		case WARRIOR:
			this.sprites = Sprite.player1;
			break;
		case ARCHER:
			this.sprites = Sprite.player2;
			break;
		case MAGE:
			this.sprites = Sprite.player3;
			break;
		}
	}
	
	public Player(String name, int x, int y, int exp, int lvl, Keyboard input, PLAYER_CLASS pClass){
		this.name = name;
		this.x = x;
		this.y = y;
		this.exp = exp;
		this.lvl = lvl;
		this.xTile = ((this.x / GameClient.TILE_WIDTH) + ((GameClient.WIDTH / GameClient.TILE_WIDTH) / 2)) - GameClient.DEFAULT_SPAWN.getX();
		this.yTile = ((this.y / GameClient.TILE_WIDTH) + ((GameClient.HEIGHT / GameClient.TILE_WIDTH) / 2)) - GameClient.DEFAULT_SPAWN.getY();
		this.input = input;
		this.health = 100;
		this.playerClass = pClass;
		switch(pClass){
		case WARRIOR:
			this.sprites = Sprite.player1;
			break;
		case ARCHER:
			this.sprites = Sprite.player2;
			break;
		case MAGE:
			this.sprites = Sprite.player3;
			break;
		}
	}
	
	public Player(Keyboard input){
		this.input = input;
	}
	
	/**
	 * 1-NORTH, 2-EAST, 3-SOUTH, 4-WEST
	 */ 
	public void move(int deltaX, int deltaY){
		if(deltaX > 0) this.dir = 2;
		else if(deltaX < 0) this.dir = 4;
		else if(deltaY > 0) this.dir = 3;
		else if(deltaY < 0) this.dir = 1;
		/*if(!collision((x + deltaX) >> 6, (x + deltaY) >> 6)){
			this.x += deltaX * Game.WALK_SPEED;
			this.y += deltaY * Game.WALK_SPEED;
		}*/
		switch(this.dir){
		case 1://north
			if(!collisionWithTile(this.xTile, this.yTile - 1)){
				//this.x += deltaX * GameClient.WALK_SPEED;
				this.y += deltaY * GameClient.WALK_SPEED;
			}
			break;
		case 2://east
			if(!collisionWithTile(this.xTile + 1, this.yTile)){
				this.x += deltaX * GameClient.WALK_SPEED;
				//this.y += deltaY * GameClient.WALK_SPEED;
			}
			break;
		case 3://south
			if(!collisionWithTile(this.xTile, this.yTile + 1)){
				//this.x += deltaX * GameClient.WALK_SPEED;
				this.y += deltaY * GameClient.WALK_SPEED;
			}
			break;
		case 4://west
			if(!collisionWithTile(this.xTile - 1, this.yTile )){
				this.x += deltaX * GameClient.WALK_SPEED;
				//this.y += deltaY * GameClient.WALK_SPEED;
			}
			break;
		}
		
//		System.out.println(String.format("NORTH: %s, EAST: %s, SOUTH: %s, WEST: %s", 
//				level.getTile(this.xTile, this.yTile - 1),
//				level.getTile(this.xTile + 1, this.yTile),
//				level.getTile(this.xTile, this.yTile + 1),
//				level.getTile(this.xTile - 1, this.yTile)));
		
		if(this.walkCycle > 60) this.walkCycle = 0;
		this.walkCycle++;//swap between 0 and 1
	}
	
	public void update(){
		if(fireRate > 0) fireRate--;
		int xa = 0, ya = 0;
		if(input.up) ya--;
		if(input.down) ya++;
		if(input.left) xa--;
		if(input.right) xa++;

		
		if(xa != 0 || ya != 0) move(xa, ya);
		
		this.xTile = ((this.x / GameClient.TILE_WIDTH) + ((GameClient.WIDTH  / GameClient.TILE_WIDTH) / 2)) - (GameClient.DEFAULT_SPAWN.getX() / GameClient.TILE_WIDTH) + 1;
		this.yTile = ((this.y  / GameClient.TILE_WIDTH) + ((GameClient.HEIGHT  / GameClient.TILE_WIDTH) / 2)) - (GameClient.DEFAULT_SPAWN.getY()  / GameClient.TILE_WIDTH);
		
		clear();
		updateShooting();
	}
	
	public void clear() {
		for(int i = 0; i < level.getProjectiles().size(); i++){
			Projectile p = level.getProjectiles().get(i);
			if(p.isRemoved()){
				level.getProjectiles().remove(i);
			}
		}
//		for(Projectile p : projectiles){
//			if(p.isRemoved()){
//				//System.out.println("projectiles length " + projectiles.size());
//				projectiles.remove(p);
//			}
//		}
	}

	private void updateShooting() {
		InventoryGraphics ig = new InventoryGraphics(1344, 768);
		int topXOfInv = ig.getX();
		int topYOfInv = ig.getY();
		if(Mouse.getButton() == 1 && fireRate <= 0 
				&& !(topXOfInv < Mouse.getX() && topYOfInv < Mouse.getY())){
			
			double dx = Mouse.getX() - GameClient.WIDTH/2;
			double dy = Mouse.getY() - GameClient.HEIGHT/2;
			double dir = Math.atan2(dy, dx);
			shoot(x, y, dir);
			fireRate = ArrowProjectile.getFireRate();
		}
	}
	
	

	public void render(int x, int y, Screen screen){
		x -= GameClient.DEFAULT_SPAWN.getX();
		y -= GameClient.DEFAULT_SPAWN.getY();
		screen.renderPlayer(x + screen.getWidth()/2, y + screen.getHeight()/2, this);
	}
	
	public void renderOther(int x, int y, Screen screen){
		x -= GameClient.DEFAULT_SPAWN.getX();
		y -= GameClient.DEFAULT_SPAWN.getY();
		screen.renderOtherPlayer(x + screen.getWidth()/2, y + screen.getHeight()/2, this);
	}
	
	public int getWalkCycle(){
		return this.walkCycle;
	}
	
	public PLAYER_CLASS getPlayerClass(){
		return this.playerClass;
	}
	
	public int getDir(){
		return this.dir;
	}
	
	
	public List<Item> getItems(){
		return this.inventory;
	}
	
	public void setFireRate(int r){
		this.fireRate = r;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public void setPosition(TileCoordinate t){
		this.x = t.getX();
		this.y = t.getY();
	}
	
	public int getMaxHealth(){
		return this.MAX_HEALTH;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setWalkCycle(int n){
		this.walkCycle = n;
	}
	
	public int getLastDir(){
		return this.lastDir;
	}
	
	public void setLastDir(int n){
		this.lastDir = n;
	}
	
}
