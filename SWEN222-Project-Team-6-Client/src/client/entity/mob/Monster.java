package client.entity.mob;

import client.GameClient;
import client.entity.ArrowProjectile;
import client.graphics.Screen;
import client.graphics.Sprite;
import client.input.Mouse;

public class Monster extends Mob {
	
	protected Sprite sprite;
	protected int x, y;
	protected int fireRate = 0;
	protected boolean moves;
	protected boolean shoots;
	
	public Monster(int x, int y, Sprite sprite){
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		health = MAX_HEALTH;
		fireRate = ArrowProjectile.getFireRate() + 100;
	}
	
	public void update(){
		if(fireRate > 0) fireRate--;
		updateShooting();
	}
	
	private void updateShooting() {
		Player p = level.getClientPlayer();
		if(!shoots) return;
		if(fireRate <= 0){
			double px = p.getX();
			double py = p.getY();
			double dir = Math.atan2(py - y + 16, px - x + 16);
			shoot(x, y, dir);
			fireRate = ArrowProjectile.getFireRate()  + 100;
		}
	}

	public void render(Screen screen){
		screen.renderMonster(x - 16, y-16, this);
	}
	
	public Sprite getSprite(){
		return this.sprite;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getMaxHealth(){
		return this.MAX_HEALTH;
	}
	
	
}
