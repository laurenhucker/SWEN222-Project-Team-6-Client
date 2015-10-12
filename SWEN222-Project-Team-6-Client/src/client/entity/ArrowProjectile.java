package client.entity;

import client.entity.mob.Player;
import client.graphics.Screen;
import client.graphics.Sprite;

public class ArrowProjectile extends Projectile {
	private static final int FIRE_RATE = 15; //higher is slower

	public ArrowProjectile(int x, int y, double dir){
		super(x, y, dir);
		speed = 20;
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
	if(!level.tileCollision(x, y, nx, ny, 64)){
		x += nx;
		y += ny;
	}
	
	if(distance() > range) {
		remove();
	}
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
	
	
}
