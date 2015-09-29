package client;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;

import client.entity.mob.Player;
import client.graphics.Screen;
import client.input.Keyboard;
import client.level.Level;
import client.level.SpawnLevel;
import client.level.tile.TileCoordinate;


public class Client extends Canvas implements Runnable{
	
	public static final String TITLE = "Team 6 Lezg00oo0go0";
	public static final int SCALE = 1,
			NUM_TILES = 21,
			TILE_WIDTH = 64,
			WIDTH = NUM_TILES * TILE_WIDTH,
			HEIGHT = (WIDTH / 16 * 9) + (TILE_WIDTH - ((WIDTH/16*9) % TILE_WIDTH));//round to nearest tile but still at 16/9 ratio

	public static final int WALK_SPEED = 3;
	public static final TileCoordinate SPAWN_LOCATION = new TileCoordinate(7, 7);
	public static final TileCoordinate DEFAULT_SPAWN = new TileCoordinate(10, 6);
	
	private boolean running = false;
	private Thread thread;
	private JFrame frame;
	private Screen screen;
	private Keyboard key;
	private Level level;
	private Player player;
	private int frames;
	
	private Socket socket;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public Client(){
		connect();
		initFrame();
		initGame();
		
		
	}
	
	private void connect(){
		try {
			socket = new Socket("localhost",2560);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initFrame(){
		Dimension size = new Dimension(SCALE*WIDTH, SCALE*HEIGHT);
		this.setPreferredSize(size);
		//System.out.println(SCALE*WIDTH + ", " + SCALE*HEIGHT);
		screen = new Screen(WIDTH, HEIGHT);
		frame = new JFrame(TITLE);
	}
	
	private void initGame(){key = new Keyboard();/*Initialise KeyBoard object*/
		//level = new RandomLevel(128, 128);
		level = new SpawnLevel("/textures/map/MAP_1.PNG");
		//level.generateLevel();
		player = new Player(SPAWN_LOCATION.getX(),
				SPAWN_LOCATION.getY(),
				key);
		player.initialise(level);
		addKeyListener(key);
	}
	
	public synchronized void start(){
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop(){
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0/60;
		double delta = 0;
		frames = 0;
		int updates = 0;
		
		requestFocus();
		
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				frame.setTitle(TITLE + " - FPS:" + frames);
				frames = 0;
				updates = 0;
			}
		}
		stop();
	}
	
	/**
	 * Called 60 times per second from the run() method.
	 * Handles key presses for map movement
	 */
	public void update(){
		key.update();
		player.update();
	}

	/**
	 * Handles top level rendering of the screen.
	 */
	public void render(){
		BufferStrategy buffStrat = getBufferStrategy();
		if(buffStrat == null){
			createBufferStrategy(3);/*For triple buffering*/
			return;
		}
		
		screen.clear();/*Clear screen before rendering again*/
		level.render(player.x, player.y, screen);
		player.render(player.x, player.y, screen);
		//screen.render(xOffset, yOffset);/*Now render screen*/
		
		for(int i = 0; i < pixels.length; i++){/*Copy over pixels from screen object after rendering*/
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = buffStrat.getDrawGraphics();
		
		g.setColor(Color.DARK_GRAY);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);/*Draw the BufferedImage image on the screen*/
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Verdana", 0, 20));
		g.drawString("x:" + player.xTile + ", y:" + player.yTile, 20, 20);
		
		g.dispose();/*Dont need these graphics any more. Throw away or game will crash from memory overload*/
		buffStrat.show();
	}
	
	public static void main(String[] args){
		Client game = new Client();
		game.frame.setResizable(false);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}
	
}
