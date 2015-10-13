
package client.entity.mob;

import client.entity.ArrowProjectile;
import client.entity.Entity;
import client.graphics.Screen;
import client.graphics.Sprite;
import client.level.tile.Tile;

public abstract class Mob extends Entity{

	protected Sprite sprite;
	protected int dir = 1;//direction - 1:NORTH 2:EAST 3:SOUTH 4:WEST
	protected boolean moving = false;
	protected final int MAX_HEALTH = 100;
	protected int health;
	
	
	public boolean collisionWithTile(int x, int y){
		Tile tileToCheck = level.getTile(x, y);
		if(tileToCheck.solid()){
			//System.out.println("Colliding with " + tileToCheck + " at " + x + ":" + y);
			return true;
		}
		return false;
	}
	
	public boolean collisionWithEntity(int x, int y){
		//System.out.println(level.getMob(x, y));
//		if(level.getMob(x, y) != null){
//			//System.out.println("Colliding with " + tileToCheck + " at " + x + ":" + y);
//			return true;
//		}		
		return false;
	}
	
	public void shoot(int x, int y, double dir){
		ArrowProjectile p = new ArrowProjectile(x, y, dir);
		p.whoShotArrow(this);
		level.addProjectile(p);	
	}
	
	public void move(int xa, int ya){
		System.out.println("Move");
	}
	
	public int getDir(){return this.dir;}
	
	public void update(){}
	
	public void render(int x, int y, Screen screen){}
	
	public void damage(int d){
		health -= d;
		if(health <= 0) health = 0;
	}
	
	public int getHealth(){
		return health;
	}
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	
	
}
