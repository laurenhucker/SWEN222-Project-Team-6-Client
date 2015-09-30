package client.level;

import java.util.ArrayList;
import java.util.List;

import client.Client;
import client.entity.ArrowProjectile;
import client.entity.Entity;
import client.entity.Projectile;
import client.graphics.Screen;
import client.level.tile.Tile;

public class Level {

	protected int width, height;
	protected int[] tilesInt;
	protected int[] tiles;
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Projectile> projectiles = new ArrayList<Projectile>();
	
	
	public Level(int w, int h){
		this.width = w;
		this.height = h;
		tilesInt = new int[width*height];
		generateLevel();
	}
	
	public Level(String path){
		loadLevel(path);
		generateLevel();
	}

	protected void loadLevel(String path) {
		
	}

	public void generateLevel() {
		
	}
	
	public void update(){
		for(Entity e : entities){
			e.update();
		}
		
		for(Projectile p : projectiles){
				p.update();
		}
	}
	
	public void render(int xScroll, int yScroll, Screen screen){
		xScroll -=  Client.DEFAULT_SPAWN.getX();
		yScroll -=  Client.DEFAULT_SPAWN.getY();
		screen.setOffset(xScroll , yScroll);
		int x0 = xScroll >> 6;//xScroll/64 (2^6)     -- LEFT HAND SIDE OF TILE
		int x1 = (xScroll + screen.getWidth() + Client.TILE_WIDTH) >> 6;// RIGHT HAND SIDE OF TILE(adding 64 to render extra tile)
		int y0 = yScroll >> 6;// TOP SIDE
		int y1 = (yScroll + screen.getHeight() + Client.TILE_WIDTH) >> 6;// BOTTOM SIDE (adding 64 to render extra tile)
		for(int y = y0; y < y1; y++){
			for(int x = x0; x < x1; x++){
				getTile(x, y).render(x, y, screen);
				//tiles[x + y * Game.TILE_WIDTH].render(x, y, screen);
			}
		}
		for(Entity e : entities){
			e.render(xScroll, yScroll,screen);
		}
		
		for(Projectile p : projectiles){
				p.render(screen);
			
		}
	}
	
	public void add(Entity e){
		entities.add(e);
	}
	
	public void addProjectile(Projectile p){
		projectiles.add(p);
	}
	
	
	
	/**
	 * 0xff00ff00 - GRASS
	 * 0xff808000 - DIRT
	 * 0xffffff00 - SAND
	 * 0xffff0000 - ROCK
	 */
	public Tile getTile(int x, int y){
		if(x < 0 || y < 0 || x >= width || y >= height){
			//System.out.printf("x:%d y:%d width:%d height:%d\n", x, y, width, height);
			return Tile.VOID;
		}
		if(tiles[x + y*width] == 0xff00ff00) return Tile.GRASS;
		if(tiles[x + y*width] == 0xff808000) return Tile.DIRT;
		if(tiles[x + y*width] == 0xffff0000) return Tile.ROCK;
		if(tiles[x + y*width] == 0xffffff00) return Tile.SAND;
		return Tile.VOID;
	}
	
	public List<Projectile> getProjectiles(){
		return projectiles;
	}
}
