package client.level.tile;

import client.GameClient;

public class TileCoordinate {
	
	private final int x;
	private final int y;
	
	public TileCoordinate(int x, int y){
		this.x = x<<6;
		this.y = y<<6;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}

}
