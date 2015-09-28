package client.entity.mob;

import client.Client;
import client.graphics.Screen;
import client.input.Keyboard;

enum PLAYER_CLASS {
	WARRIOR,
	ARCHER,
	MAGE
}

public class Player extends Mob {
	
	private Keyboard input;
	protected int dir = 3;
	private int walkCycle = 20;
	private PLAYER_CLASS playerClass;
	
	public Player(int x, int y, Keyboard input){
		this.x = x;
		this.y = y;
		this.xTile = ((this.x / Client.TILE_WIDTH) + ((Client.WIDTH / Client.TILE_WIDTH) / 2)) - Client.DEFAULT_SPAWN.getX();
		this.yTile = ((this.y / Client.TILE_WIDTH) + ((Client.HEIGHT / Client.TILE_WIDTH) / 2)) - Client.DEFAULT_SPAWN.getY();
		this.input = input;
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
			if(!collision(this.xTile, this.yTile - 1)){
				this.x += deltaX * Client.WALK_SPEED;
				this.y += deltaY * Client.WALK_SPEED;
			}
			break;
		case 2://east
			if(!collision(this.xTile + 1, this.yTile)){
				this.x += deltaX * Client.WALK_SPEED;
				this.y += deltaY * Client.WALK_SPEED;
			}
			break;
		case 3://south
			if(!collision(this.xTile, this.yTile + 1)){
				this.x += deltaX * Client.WALK_SPEED;
				this.y += deltaY * Client.WALK_SPEED;
			}
			break;
		case 4://west
			if(!collision(this.xTile - 1, this.yTile )){
				this.x += deltaX * Client.WALK_SPEED;
				this.y += deltaY * Client.WALK_SPEED;
			}
			break;
		}
		
		System.out.println(String.format("NORTH: %s, EAST: %s, SOUTH: %s, WEST: %s", 
				level.getTile(this.xTile, this.yTile - 1),
				level.getTile(this.xTile + 1, this.yTile),
				level.getTile(this.xTile, this.yTile + 1),
				level.getTile(this.xTile - 1, this.yTile)));
		
		if(this.walkCycle > 60) this.walkCycle = 0;
		this.walkCycle++;//swap between 0 and 1
	}
	
	public void update(){
		int xa = 0, ya = 0;
		if(input.up) ya--;
		if(input.down) ya++;
		if(input.left) xa--;
		if(input.right) xa++;
		
		if(xa != 0 || ya != 0) move(xa, ya);
		
		this.xTile = ((this.x / Client.TILE_WIDTH) + ((Client.WIDTH  / Client.TILE_WIDTH) / 2)) - (Client.DEFAULT_SPAWN.getX() / Client.TILE_WIDTH) + 1;
		this.yTile = ((this.y  / Client.TILE_WIDTH) + ((Client.HEIGHT  / Client.TILE_WIDTH) / 2)) - (Client.DEFAULT_SPAWN.getY()  / Client.TILE_WIDTH);
	}
	
	public void render(int x, int y, Screen screen){
		x -= Client.DEFAULT_SPAWN.getX();
		y -= Client.DEFAULT_SPAWN.getY();
		screen.renderPlayer(x + screen.getWidth()/2, y + screen.getHeight()/2, this);
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
	
}
