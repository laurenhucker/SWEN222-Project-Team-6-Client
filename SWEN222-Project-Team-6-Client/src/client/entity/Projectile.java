package client.entity;

import client.entity.mob.Mob;
import client.graphics.Screen;
import client.graphics.Sprite;

public abstract class Projectile extends Entity {
	protected final int xOrigin, yOrigin;
	protected double angle;
	protected double x, y;
	protected Sprite sprite;
	protected double distance;
	protected double nx, ny;
	protected double speed, range, damage;
	protected int fireRate;
	protected int id;
	
	public Projectile(int x, int y, double dir){
		this.xOrigin = x;
		this.yOrigin = y;
		this.angle = dir;
		this.x = x;
		this.y = y;
		this.id = (int) (Math.random()*Integer.MAX_VALUE);
	}
	
	public Projectile(int x, int y, double dir, int id){
		this.xOrigin = x;
		this.yOrigin = y;
		this.angle = dir;
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	public void render(Screen s){}

	public void whoShotArrow(Mob mob) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean equals(Object o){
		if(o == null) return false;
		if(!(o instanceof Projectile)) return false;
		if(((Projectile)o).x == this.x && ((Projectile)o).y == this.y){
			if(((Projectile)o).id == this.id){
				return true;
			}
		}
		return false;
	}

	public int getID(){
		return this.id;
	}
	
	public String getType(){
		if(this instanceof ArrowProjectile) return "BULLET";
		if(this instanceof FireballProjectile) return "FIREBALL";
		if(this instanceof ShieldProjectile) return "SHIELD";
		return "SHIELD";
	}
	
}
