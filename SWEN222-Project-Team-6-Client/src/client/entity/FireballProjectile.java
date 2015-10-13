package client.entity;

import client.entity.mob.Mob;
import client.entity.mob.Monster;
import client.entity.mob.Player;
import client.graphics.Screen;
import client.graphics.Sprite;

public class FireballProjectile extends Projectile {
	private static final int FIRE_RATE = 15; //higher is slower
	private Mob whoShotArrow;

	public FireballProjectile(int x, int y, double dir){
		super(x, y, dir);
		speed = 5;
		speed = 5;
		range = 500;
		damage = 15;
		sprite = Sprite.fireball;
		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);

	}

	public void update(){
		move();
	}

	protected void move(){
		Mob hitMob = collidingWithMob();
		if(!level.tileCollision(x, y, nx, ny, 64)){// && !level.mobProjectileCollision(x, y, nx, ny, 64)){
			x += nx;
			y += ny;

			if(hitMob != null){
				hitMob.damage(10);
				System.out.println("hit " + hitMob);
				if(hitMob.getHealth() <= 0){
					System.out.println("Health is 0");
					level.getEntities().remove(hitMob);
				}
				this.remove();
			}
		}
		else{
			this.remove();
		}
		if(distance() > range) {
			this.remove();
		}
	}

	public Mob collidingWithMob(){
		for(Entity e : level.getEntities()){
			if(e instanceof Mob){
				int xStart = e.getX();
				int yStart = e.getY();
				int xEnd = xStart + 64;
				int yEnd = yStart + 64;
				if(whoShotArrow instanceof Player){
					if(x >= xStart && x <= xEnd && y >= yStart && y <= yEnd && !(e instanceof Player)){
						return (Mob)e;
					}
				} else {
					if(x >= xStart && x <= xEnd && y >= yStart && y <= yEnd && !(e instanceof Monster)){
						return (Mob)e;
					}
				}
			}
		}
		return null;
	}

	private double distance() {
		double dist = 0;
		dist = Math.sqrt(Math.abs((xOrigin - x)*(xOrigin - x) + (yOrigin - y)*(yOrigin - y)));
		return dist;
	}

	public void render(Screen screen){
		screen.renderItem((int)x, (int)y - 25, sprite);
		//ArrowProjectile.screen.renderProjectile((int)x, (int)y, sprite);

	}

	public static int getFireRate() {
		return FIRE_RATE;
	}
	
	public void whoShotArrow(Mob m){
		whoShotArrow = m;
	}
}

