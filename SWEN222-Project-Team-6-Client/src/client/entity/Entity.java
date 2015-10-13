package client.entity;

import java.util.Random;

import client.graphics.Screen;
import client.graphics.Sprite;
import client.level.Level;

public abstract class Entity {

	public int x, y;
	public int xTile;
	public int yTile;
	private boolean removed = false;
	private int dir = 1;
	protected Level level;
	protected final Random random = new Random();
	public Sprite[] sprites;
	
	public void update(){
	}
	
	public void render(int x, int y, Screen screen){
		//System.out.println("rendering entity");
	}
	
	public int getDir(){return this.dir;}

	public void remove(){removed = true;}
	
	public boolean isRemoved(){return removed;}
	
	public void initialise(Level level){this.level = level;}
	
	
	
}
