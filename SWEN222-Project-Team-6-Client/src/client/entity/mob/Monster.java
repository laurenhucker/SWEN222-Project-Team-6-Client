package client.entity.mob;

import client.graphics.Screen;
import client.graphics.Sprite;

public class Monster extends Mob {
	
	private Sprite sprite;
	private int x, y;
	
	public Monster(int x, int y, Sprite sprite){
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		health = MAX_HEALTH;
	}
	
	public void update(){
		//System.out.println(String.format("This mob is here at %d:%d", x, y));
	}

	public void render(Screen screen){
		screen.renderMonster(x, y, this);
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
	

	
	
}
