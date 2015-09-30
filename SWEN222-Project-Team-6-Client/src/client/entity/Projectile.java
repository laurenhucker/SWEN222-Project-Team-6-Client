package client.entity;

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
	
	public Projectile(int x, int y, double dir){
		xOrigin = x;
		yOrigin = y;
		angle = dir;
		this.x = x;
		this.y = y;
	}
	
	public void render(Screen s){}
	
	
}
