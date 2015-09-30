package client.entity.mob;

import java.util.ArrayList;
import java.util.List;

import client.entity.ArrowProjectile;
import client.entity.Entity;
import client.entity.Projectile;
import client.graphics.Screen;
import client.graphics.Sprite;
import client.level.Level;
import client.level.tile.Tile;

public abstract class Mob extends Entity{

	protected Sprite sprite;
	protected int dir = 1;//direction - 1:NORTH 2:EAST 3:SOUTH 4:WEST
	protected boolean moving = false;
	
	
	public boolean collision(int x, int y){
		Tile tileToCheck = level.getTile(x, y);
		if(tileToCheck.solid()){
			//System.out.println("Colliding with " + tileToCheck + " at " + x + ":" + y);
			return false;
		}
		return false;
	}
	
	public void shoot(int x, int y, double dir){
		Projectile p = new ArrowProjectile(x, y, dir);
		level.addProjectile(p);	
	}
	
	public void move(int xa, int ya){
		System.out.println("Move");
	}
	
	public int getDir(){return this.dir;}
	
	public void update(){}
	
	public void render(int x, int y, Screen screen){}
	
}
