
package client.entity.mob;

import client.entity.ArrowProjectile;
import client.entity.Entity;
import client.entity.FireballProjectile;
import client.entity.Projectile;
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
		Projectile p;
		if(getProjectileType(this).equals("arrow")){
			p = new ArrowProjectile(x, y, dir);
			
		} else if(getProjectileType(this).equals("fireball")){
			p = new FireballProjectile(x, y, dir);
		} else {
			p = new ArrowProjectile(x, y, dir);
		}
		level.addProjectile(p);	
		p.whoShotArrow(this);
		
	}
	
	public String getProjectileType(Mob m){
		if(m instanceof Player){
			Player p = (Player)m;
			System.out.println(p.getPlayerClass());
			if(p.getPlayerClass().equals(Player.PLAYER_CLASS.ARCHER)){
				
				return "arrow";
			} else if(p.getPlayerClass().equals(Player.PLAYER_CLASS.MAGE)){
				return "fireball";
			}
		}
		return "fireball";
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
