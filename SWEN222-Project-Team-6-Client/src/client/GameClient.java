package client;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



import client.entity.Entity;
import client.entity.Item;
import client.entity.mob.Monster;
import client.entity.mob.Player;
import client.entity.mob.monsters.ChestMonster;
import client.entity.mob.monsters.GhostMonster;
import client.graphics.Screen;
import client.graphics.Sprite;
import client.input.Keyboard;
import client.input.Mouse;
import client.level.Level;
import client.level.SpawnLevel;
import client.level.tile.TileCoordinate;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

enum STATE {
	LOGIN,
	CHARACTER_SELECTION,
	EXISTING_CHARACTER,
	GAME
}

public class GameClient extends Canvas implements Runnable{
	
	
	private String[] words;
	
	public Client client;
	public int id;
	private String user;
	private String pass;
	
	public static final String TITLE = "Dylan is lame";
	public static final int SCALE = 1,
			NUM_TILES = 21,
			TILE_WIDTH = 64,
			WIDTH = NUM_TILES * TILE_WIDTH,
			HEIGHT = (WIDTH / 16 * 9) + (TILE_WIDTH - ((WIDTH/16*9) % TILE_WIDTH));//round to nearest tile but still at 16/9 ratio

	public static final int WALK_SPEED = 5;
	public static final TileCoordinate SPAWN_LOCATION = new TileCoordinate(124, 115);
	public static final TileCoordinate DEFAULT_SPAWN = new TileCoordinate(10, 6);
	
	private boolean running = false;
	private Thread thread;
	private JFrame gameFrame, loginFrame;
	private JPanel panel;
	private Screen screen;
	private Keyboard key;
	private Mouse mouse;
	private Level level;
	private Monster penisMob, chestMob, ghostMob;
	private Player player;
	private int frames;
	private STATE state = STATE.LOGIN;
	private static ArrayList<GameClient> gameClients = new ArrayList<GameClient>();
	
	private Socket socket;
	
	private BufferedImage image,
			loginScreenImg,
			newCharButtonImg,
			existingCharButtonImg,
			warriorButtonImg,
			archerButtonImg,
			mageButtonImg;
	private int[] pixels;
	private int counter = 0;
	
	public GameClient(){	
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		loadImages();
		initFrames();
		loginScreen();
		//InitializeConnection();
	}
	
	public void InitializeConnection(){
		client = new Client();
		new Thread(client).start();
		registerPackets();
		client.addListener(new Listener(){
			public void connected(Connection connection) {
				handleConnect(connection);
			}

			public void disconnected(Connection connection) {
				handleDisonnect(connection);
			}
			
			public void received(Connection connection, Object object) {
				handleMessage(connection.getID(), object);
			}
			
		});
		
	}
	
	protected void handleDisonnect(Connection connection) {
		//handleDisonnect(connection);
	}

	protected void handleConnect(Connection connection) {
		String send=("login" +"," + this.user+ ","+this.pass);
		System.out.println(user +"    " + pass);
		sendMessageUDP(send);	
	}

	public void handleMessage(int playerId, Object message) {
		if(message instanceof String){
			words=((String) message).split(",");
			
			if(words[0].equalsIgnoreCase("login")){
				String ans = words[1];
				System.out.println("[CLIENT] ans is" + ans);			
				if(ans.equalsIgnoreCase("true")){					
					System.out.println("accepted");	
					verify(true);
				}else{
					verify(false);
				}
			}else if (words[0].equalsIgnoreCase("player")){
				System.out.println("[CLIENT] is type player");
				int x = Integer.parseInt(words[1]);
				int y = Integer.parseInt(words[2]);
				String playerClass = words[3];
				
			}
		}
	}
	
	public void registerPackets(){
		Kryo kryo = client.getKryo();
		
	}
	
	public void sendMessage(Object message) {
		System.out.println(client.isConnected());
		if (client.isConnected()) {
			client.sendTCP(message);
		}
	}
	public void sendMessageUDP(Object message) {
		System.out.println(client.isConnected());
		if (client.isConnected()) {
			client.sendUDP(message);
		}
	}
	
	public void connectLocal() {
		connect("localhost");
	}
	
	public void connect(String host) {
		try {
			client.connect(10000, host, 2555, 2556);//, Network.portUdp);
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
	public void verify(boolean verified){
				
		if (verified == false){
			client.stop();
			client.close();		
			System.out.println("NO");
		}else{
			//Only reaches here after verification
			state = STATE.GAME;
			loginFrame.setVisible(false);
			initGame(Player.PLAYER_CLASS.WARRIOR);//Change this to the player's saved class
		}
	}

	private void loadImages(){
		try {
			loginScreenImg = ImageIO.read(GameClient.class.getResource("/textures/login/LOGIN_SCREEN.PNG"));
			newCharButtonImg = ImageIO.read(GameClient.class.getResource("/textures/login/NEW_CHARACTER_BUTTON.PNG"));
			existingCharButtonImg = ImageIO.read(GameClient.class.getResource("/textures/login/EXISTING_CHARACTER_BUTTON.PNG"));
			warriorButtonImg = ImageIO.read(GameClient.class.getResource("/textures/login/CHARACTER_SELECTION_1.PNG"));
			archerButtonImg = ImageIO.read(GameClient.class.getResource("/textures/login/CHARACTER_SELECTION_2.PNG"));
			mageButtonImg = ImageIO.read(GameClient.class.getResource("/textures/login/CHARACTER_SELECTION_3.PNG"));
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
				
				JFrame loginBox = new JFrame();
				JPanel loginPanel = new JPanel(new GridLayout(1, 3));
				JTextField username = new JTextField("Username");
				JTextField password = new JTextField("Password");
				JButton loginButton = new JButton("Log in!");
				loginButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent a1) {
							//Stanton use these two strings and send them to server to request login.
							user = username.getText();
							pass = password.getText();
							InitializeConnection();
							connect("dylanteam6.no-ip.biz");							
					}
				});
				loginPanel.add(username);
				loginPanel.add(password);
				loginPanel.add(loginButton);
				loginBox.add(loginPanel);
				loginBox.setLocationRelativeTo(null);
				loginBox.pack();
				loginBox.setResizable(false);
				loginBox.setVisible(true);
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
				initGame(Player.PLAYER_CLASS.WARRIOR);
			}
		});
		
		archerButton.setIcon(new ImageIcon(this.archerButtonImg));
		archerButton.setBorder(BorderFactory.createEmptyBorder());
		archerButton.setContentAreaFilled(false);
		archerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				loginFrame.setVisible(false);
				initGame(Player.PLAYER_CLASS.ARCHER);
			}
		});
		
		mageButton.setIcon(new ImageIcon(this.mageButtonImg));
		mageButton.setBorder(BorderFactory.createEmptyBorder());
		mageButton.setContentAreaFilled(false);
		mageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				loginFrame.setVisible(false);
				initGame(Player.PLAYER_CLASS.MAGE);
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
	
	private void initGame(Player.PLAYER_CLASS pClass){
		key = new Keyboard();/*Initialise KeyBoard object*/
		mouse = new Mouse();
		//level = new RandomLevel(128, 128);
		level = new SpawnLevel("/textures/map/MAP_3.PNG");
		//level.generateLevel();
		penisMob = new ChestMonster(116*64, 116*64, Sprite.penisMob, 15, 100, false, true);
		chestMob = new ChestMonster(116*64, 120*64, Sprite.chestMob, 0, 1000, false, false);
		ghostMob = new GhostMonster(116*64, 124*64, Sprite.ghostMob, 0, 50, true, false);
		
		player = new Player(SPAWN_LOCATION.getX(), SPAWN_LOCATION.getY(), key, pClass);
		
		player.getItems().add(new Item("SWORD_WOOD"));
		player.getItems().add(new Item("AXE_CRYSTAL"));
		player.getItems().add(new Item("BOW_METAL"));
		player.getItems().add(new Item("BOW_CRYSTAL"));
		player.getItems().add(new Item("STAFF_CRYSTAL"));
		player.getItems().add(new Item("STAFF_CRYSTAL"));
		player.getItems().add(new Item("AXE_METAL"));
		player.getItems().add(new Item("AXE_WOOD"));
		player.getItems().add(new Item("SWORD_CRYSTAL"));
		player.initialise(level);
		penisMob.initialise(level);
		chestMob.initialise(level);
		ghostMob.initialise(level);
		level.addEntity(penisMob);
		level.addEntity(chestMob);
		level.addEntity(ghostMob);
		level.addEntity(player);
		level.addPlayer(player);
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
		otherKeysCheck();
		counter++;
		if(counter == 5){
			String send = "player" + "," + player.x + "," + player.y + "," + player.getPlayerClass();	
			sendMessage(send); //what we want to send
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
		for(Entity e : level.getEntities()){
			if(e instanceof Monster)
				((Monster)e).render(screen);
		}
		//penisMob.render(screen);
		chestMob.render(screen);
		player.render(player.x, player.y, screen);
		//screen.render(xOffset, yOffset);/*Now render screen*/
		screen.renderInventory(player);
		
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
		mouse.drawToolTip(g, player);
		mouse.drawRightClick(gameFrame, player);
		g.dispose();/*Dont need these graphics any more. Throw away or game will crash from memory overload*/
		buffStrat.show();
	}
	
	public static void main(String[] args){
		gameClients.add(new GameClient());
	}


	private void otherKeysCheck(){
		if(key.e){
			System.out.println("PRESSED E DO SOME STUFF");
		}
		if(key.esc){
			key.forceRelease(KeyEvent.VK_ESCAPE);//Because it gets stuck down if I don't force it to be released before I close everything.
			GameClient clientToRemove = gameClients.get(0);
			gameClients.remove(0);
			gameClients.add(new GameClient());
			clientToRemove.gameFrame.dispose();
			clientToRemove.running = false;
		}
	}
	
}
