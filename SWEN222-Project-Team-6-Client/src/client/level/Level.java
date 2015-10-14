package client.level;

import java.util.ArrayList;
import java.util.List;

import client.GameClient;
import client.entity.ArrowProjectile;
import client.entity.Entity;
import client.entity.Projectile;
import client.entity.mob.Mob;
import client.entity.mob.Monster;
import client.entity.mob.Player;
import client.graphics.Screen;
import client.level.tile.Tile;

public class Level {

	protected int width, height;
	protected int[] tilesInt;
	protected int[] tiles;
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Projectile> projectiles = new ArrayList<Projectile>();
	private List<Player> players = new ArrayList<Player>();	
	
	
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
		xScroll -=  GameClient.DEFAULT_SPAWN.getX();
		yScroll -=  GameClient.DEFAULT_SPAWN.getY();
		screen.setOffset(xScroll , yScroll);
		int x0 = xScroll >> 6;//xScroll/64 (2^6)     -- LEFT HAND SIDE OF TILE
		int x1 = (xScroll + screen.getWidth() + GameClient.TILE_WIDTH) >> 6;// RIGHT HAND SIDE OF TILE(adding 64 to render extra tile)
		int y0 = yScroll >> 6;// TOP SIDE
		int y1 = (yScroll + screen.getHeight() + GameClient.TILE_WIDTH) >> 6;// BOTTOM SIDE (adding 64 to render extra tile)
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
	
	public void addEntity(Entity e){
		entities.add(e);
	}
	
	public void addPlayer(Player p){
		players.add(p);
	}
	
	public void addProjectile(Projectile p){
		p.initialise(this);
		projectiles.add(p);
	}
	
	public boolean tileCollision(double x, double y, double xa, double ya, int size){
		boolean solid = false;
		for(int i = 0; i < 4; i++){
			double xt = ((x + xa) + i % 2 * size)/64;
			double yt = ((y + ya) + i / 2 * size)/64;
			if(getTile((int)xt, (int)yt).solid()) {
				solid = true;
				return solid;
			}
		}
		return solid;
	}
//	
//	public boolean mobProjectileCollision(double x, double y, double xa, double ya, int size){
//		boolean solid = false;
//		double xt = x + xa;
//		double yt = y + ya;
//		//System.out.println("mobCollison() xt: " + xt + "  yt: " + yt);
//		Mob mob = getMob(xt, yt);
//		
//		//System.out.println("Mob: " + mob);
//		//System.out.println("Shooter: " + shooter);
//		if(mob != null){ //can't collide with itself
//			System.out.println("Player shot " + mob);
//			solid = true;
//			mob.damage(10);
//			//System.out.println(mob);
//			//System.out.println("Mob health: " + mob.getHealth() );
//			if(mob.getHealth() <= 0){
//				entities.remove(mob);
//			}
//			return solid;
//		}
//		return solid;
//	}

		
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
		if(tiles[x + y*width] == 0xff551500) return Tile.WOOD;
		if(tiles[x + y*width] == 0xff000000) return Tile.WOOD_WALL;
		if(tiles[x + y*width] == 0xff454c35) return Tile.TREE;
		if(tiles[x + y*width] == 0xff322500) return Tile.NORTH_WALL;
		if(tiles[x + y*width] == 0xff322501) return Tile.SOUTH_WALL;
		if(tiles[x + y*width] == 0xff322502) return Tile.WEST_WALL;
		if(tiles[x + y*width] == 0xff322503) return Tile.EAST_WALL;
		if(tiles[x + y*width] == 0xff322504) return Tile.NORTH_WEST_CORNER;
		if(tiles[x + y*width] == 0xff322505) return Tile.NORTH_EAST_CORNER;
		if(tiles[x + y*width] == 0xff322506) return Tile.SOUTH_WEST_CORNER;
		if(tiles[x + y*width] == 0xff322507) return Tile.SOUTH_EAST_CORNER;
		return Tile.VOID;
	}

//	public Mob getMob(double x, double y){
//		for(Entity e : entities){
//			if(e instanceof Mob){
//				//System.out.println("Get mob() x: " + x + "   y: " + y);
//				//System.out.println("Mob position x: " + ((Mob)e).getX() + "  y: " + ((Mob)e).getY());
//				int mobXStart = ((Mob)e).getX();
//				int mobYStart = ((Mob)e).getY();
//				int mobXEnd = mobXStart + 64;
//				int mobYEnd = mobYStart + 64;
//				
//				if(x >= mobXStart && x <= mobXEnd && y >= mobYStart && x <= mobYEnd && e instanceof Player ){
//					//System.out.println(e);
//					return (Mob)e;
//				}
//			}
//		}
//		return null;	
//	}
	
	public List<Projectile> getProjectiles(){
		return projectiles;
	}
	
	public List<Entity> getEntities(){
		return entities;
	}
	
	public List<Player> getPlayers(){
		return players;
	}
	
	public Player getClientPlayer(){
		return players.get(0);
	}
	
	public void removeMob(Mob m){
		entities.remove(m);
	}
	
	
}
