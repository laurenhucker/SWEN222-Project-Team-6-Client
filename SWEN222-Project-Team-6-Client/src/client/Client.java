package client;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.net.*;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.entity.mob.Player;
import client.graphics.Screen;
import client.input.Keyboard;
import client.input.Mouse;
import client.level.Level;
import client.level.SpawnLevel;
import client.level.tile.TileCoordinate;

enum STATE {
	LOGIN,
	CHARACTER_SELECTION,
	EXISTING_CHARACTER,
	GAME
}


public class Client extends Canvas implements Runnable{
	
	public static final String TITLE = "Lauren is cool  ";
	public static final int SCALE = 1,
			NUM_TILES = 21,
			TILE_WIDTH = 64,
			WIDTH = NUM_TILES * TILE_WIDTH,
			HEIGHT = (WIDTH / 16 * 9) + (TILE_WIDTH - ((WIDTH/16*9) % TILE_WIDTH));//round to nearest tile but still at 16/9 ratio

	public static final int WALK_SPEED = 3;
	public static final TileCoordinate SPAWN_LOCATION = new TileCoordinate(7, 7);
	public static final TileCoordinate DEFAULT_SPAWN = new TileCoordinate(10, 6);
	
	private boolean running = false, connected = false;
	private Thread thread;
	private JFrame gameFrame, loginFrame;
	private JPanel panel;
	private Screen screen;
	private Keyboard key;
	private Mouse mouse;
	private Level level;
	private Player player;
	private int frames;
	private STATE state = STATE.LOGIN;
	
	private Socket socket;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB),
			loginScreenImg,
			newCharButtonImg,
			existingCharButtonImg,
			warriorButtonImg,
			archerButtonImg,
			mageButtonImg;
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	private int counter = 0;
	
	public Client(){
		loadImages();
		initFrames();
		loginScreen();
	}

	private void loadImages(){
		try {
			loginScreenImg = ImageIO.read(Client.class.getResource("/textures/login/LOGIN_SCREEN.PNG"));
			newCharButtonImg = ImageIO.read(Client.class.getResource("/textures/login/NEW_CHARACTER_BUTTON.PNG"));
			existingCharButtonImg = ImageIO.read(Client.class.getResource("/textures/login/EXISTING_CHARACTER_BUTTON.PNG"));
			warriorButtonImg = ImageIO.read(Client.class.getResource("/textures/login/CHARACTER_SELECTION_1.PNG"));
			archerButtonImg = ImageIO.read(Client.class.getResource("/textures/login/CHARACTER_SELECTION_2.PNG"));
			mageButtonImg = ImageIO.read(Client.class.getResource("/textures/login/CHARACTER_SELECTION_3.PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loginScreen(){
		JButton newCharButton = new JButton();
		JButton existingCharButton = new JButton();
		
		newCharButton.setIcon(new ImageIcon(newCharButtonImg));
		newCharButton.setBorder(BorderFactory.createEmptyBorder());
		newCharButton.setContentAreaFilled(false);
		newCharButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				state = STATE.CHARACTER_SELECTION;
				panel.remove(newCharButton);
				panel.remove(existingCharButton);
				characterSelectScreen();
			}
		});
		
		existingCharButton.setIcon(new ImageIcon(existingCharButtonImg));
		existingCharButton.setBorder(BorderFactory.createEmptyBorder());
		existingCharButton.setContentAreaFilled(false);
		existingCharButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				state = STATE.GAME;
				loginFrame.setVisible(false);
				state = STATE.GAME;
				initGame();
			}
		});
		
		panel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if(state == STATE.LOGIN)
					g.drawImage(loginScreenImg, 0, 0, this);
			}
		};
		
		/*
		 * I want the buttons at coordinates 2,2 and 2,3 in the GridLayout to try and put them in the right place on the 
		 * screen. To fill the gaps I will put empty JLabel objects.
		 */
		panel.setLayout(new GridLayout(6, 3));
		panel.add(new JLabel());
		panel.add(new JLabel());
		panel.add(new JLabel());

		panel.add(new JLabel());
		panel.add(new JLabel());
		panel.add(new JLabel());
		
		panel.add(new JLabel());
		panel.add(newCharButton);
		panel.add(new JLabel());

		panel.add(new JLabel());
		panel.add(existingCharButton);
		panel.add(new JLabel());

		panel.add(new JLabel());
		panel.add(new JLabel());
		panel.add(new JLabel());
		
		panel.setPreferredSize(new Dimension(1344, 768));
		panel.setVisible(true);
		loginFrame.getContentPane().add(panel);
		loginFrame.setVisible(true);
	}
	
	private void characterSelectScreen(){
		JButton warriorButton = new JButton();
		JButton archerButton = new JButton();
		JButton mageButton = new JButton();
		
		warriorButton.setIcon(new ImageIcon(this.warriorButtonImg));
		warriorButton.setBorder(BorderFactory.createEmptyBorder());
		warriorButton.setContentAreaFilled(false);
		warriorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				loginFrame.setVisible(false);
				state = STATE.GAME;
				initGame();
			}
		});
		
		archerButton.setIcon(new ImageIcon(this.archerButtonImg));
		archerButton.setBorder(BorderFactory.createEmptyBorder());
		archerButton.setContentAreaFilled(false);
		archerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				loginFrame.setVisible(false);
				state = STATE.GAME;
				initGame();
			}
		});
		
		mageButton.setIcon(new ImageIcon(this.mageButtonImg));
		mageButton.setBorder(BorderFactory.createEmptyBorder());
		mageButton.setContentAreaFilled(false);
		mageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				loginFrame.setVisible(false);
				state = STATE.GAME;
				initGame();
			}
		});
		
		panel = new JPanel();
		
		panel.setLayout(new GridLayout(1, 3));
		panel.add(warriorButton);
		panel.add(archerButton);
		panel.add(mageButton);
		panel.setPreferredSize(new Dimension(1344, 768));
		panel.setVisible(true);
		loginFrame.getContentPane().add(panel);
		loginFrame.setVisible(true);
	}
	
	private boolean connect(){
		try {
			socket = new Socket("localhost",2560); //use '192.168.1.69' to connect to georges laptop
			return true;
		} catch (IOException e) {
			e.printStackTrace();
     	}
		return false;
	}
	
	
	private void initFrames(){
		screen = new Screen(WIDTH, HEIGHT);
		loginFrame = new JFrame(TITLE);
		gameFrame = new JFrame(TITLE);

		Dimension size = new Dimension(SCALE*WIDTH, SCALE*HEIGHT);
		this.setPreferredSize(size);
		loginFrame.setPreferredSize(size);
		loginFrame.setResizable(false);
		loginFrame.add(this);
		loginFrame.pack();
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setLocationRelativeTo(null);
		gameFrame.setPreferredSize(size);
		gameFrame.setResizable(false);
		gameFrame.add(this);
		gameFrame.pack();
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setLocationRelativeTo(null);
	}
	
	private void initGame(){
		if(!connect()) System.exit(1);
		key = new Keyboard();/*Initialise KeyBoard object*/
		mouse = new Mouse();
		//level = new RandomLevel(128, 128);
		level = new SpawnLevel("/textures/map/MAP_1.PNG");
		//level.generateLevel();
		player = new Player(SPAWN_LOCATION.getX(), SPAWN_LOCATION.getY(), key);
		player.initialise(level);
		addKeyListener(key);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		gameFrame.setVisible(true);
		this.start();
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
				gameFrame.setTitle(TITLE + " - FPS:" + frames);
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
		level.update();
		counter++;
		
		if(counter > 10){
			try {
				send();
			} catch (IOException e) {
				e.printStackTrace();
			}
			counter = 0;
		}
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

		//add mouse cursor
		g.setColor(Color.RED);
		g.fillOval(Mouse.getX() - 5, Mouse.getY() - 5, 10, 10);
		
		g.dispose();/*Dont need these graphics any more. Throw away or game will crash from memory overload*/
		buffStrat.show();
	}
	
	public void send() throws IOException {
		OutputStreamWriter outStream;
		String toSend = player.x + "\r" + player.y + "\n";
		try{
			outStream = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			outStream.write(toSend);
			outStream.flush();
		} catch (IOException e) {
	        System.err.print(e);
	    } finally {
	        socket.close();
	    }		
	}
	
	public static void main(String[] args){
		Client game = new Client();
	}
	
}
