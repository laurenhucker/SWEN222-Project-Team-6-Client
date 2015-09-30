package client.entity;

import client.graphics.Screen;
import client.graphics.Sprite;
import client.level.tile.Tile;

public class ArrowProjectile extends Projectile {
	Tile tile;

	public ArrowProjectile(int x, int y, double dir){
		super(x, y, dir);
		speed = 20;
		range = 200;
		damage = 15;
		rateOfFire = 15;
		//sprite = 
		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
		
	}
	
	public void update(){
		move();
	}
	
	protected void move(){
		x += nx;
		y += ny;
	}
	
	public void render(Screen screen){
		//screen.renderTile(x, y, sprite);
		
	}
	
}
