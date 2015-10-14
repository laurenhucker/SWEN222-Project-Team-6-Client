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

import client.entity.ArrowProjectile;
import client.entity.Entity;
import client.entity.Item;
import client.entity.Projectile;
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

	private boolean verified = false, newUser = false;;
	private String[] words;

	public Client client;
	public int id;
	private String user;
	private String pass;
	private Player.PLAYER_CLASS pClass;

	public static final String TITLE = "Dylan is a sick cunt";
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
	private ArrayList<Player> otherPlayers = new ArrayList<Player>();
	private ArrayList<Player> otherPlayersOnScreen = new ArrayList<Player>();

	public int[][] kniteCoords = {{149,37}, {149, 38}, {149, 39}, {149, 40}, {148, 29},
			{137, 44}, {141,46}, {166,31}, {166,32}, {162,15}, {164,14}, {167,13}, 
			{122,91}, {128, 91}, {142,164}, {127,200}, {105,218}, {103,244}, {135,248}, {20,27}, {33,107},
			{192,84}, {185,87}, {165,100}, {175,113}, {208,129}, {179,167}, {181,170}, {32,235},
			{94,121}, {94,114}, {107,122}, {121,147}, {128,147}, {155,125}, {155,118}};
			
public int[][] bossCoords = {{19,154}, {40,240}, {149,237}, {88,14}, {40, 99}, {142,36},
					{209,140}, {106,201}};

public int [][] treasureCoords = {{137,175},{133,180},{16,225},{145,57},{16,19},{72,75},
					{167,69}, {196,87}, {169,94}, {189,112}, {236,102}, {176,163}, {41,242}, {155,101}, {109,127}, {125,141}  };

public int [][] ghostCoords = {{125,158},{121,183},{85,225},{23,170},{215,49},{206,28},{184,13},{120,13},{31,15},{33,37},{42,109},
								{126,65}, {188,46}, {207,73}, {234,80}, {217,141}, {232,117}, {227,102}, {125,159}, {106,209}, {28,227}
								, {140,121}, {125,102}, {136,143}};

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
		if(newUser){
			String send = (String.format("newuser,%s,%s,%s", this.user, this.pass, this.pClass));
			System.out.println(user + "    " + pass);
			sendMessageUDP(send);
		} else {
			String send = (String.format("login,%s,%s", this.user, this.pass));
			System.out.println(user + "    " + pass);
			sendMessageUDP(send);
		}	

	}

	public void handleMessage(int playerId, Object message) {
		if(message instanceof String){
			words = ((String) message).split(",");

			if(words[0].equalsIgnoreCase("login")){//SHOULD RETURN "login,true,class,exp,lvl,x,y"	
				String ans = words[1];
				System.out.println("[CLIENT] ans is " + ans);	
				if(ans.equalsIgnoreCase("true")){					
					System.out.println("accepted");	
					loginPass(words[2], words[3], words[4], words[5], words[6], words[7]);
				} else {
					loginFail();
				}
			} else if (words[0].equalsIgnoreCase("player")){//SHOULD RETURN "player,name,x,y,class,"
				if(words.length == 5){
					String name = words[1];
					int x = Integer.parseInt(words[2]);
					int y = Integer.parseInt(words[3]);
					Player.PLAYER_CLASS c = getClassByName(words[4]);
					otherPlayers.add(new Player(name, x, y, null, c));
				} else {
					int x = Integer.parseInt(words[1]);
					int y = Integer.parseInt(words[2]);
					int clientHeight = 768, clientWidth = 1344;

					for(Player p : otherPlayers){
						if(!p.getName().equalsIgnoreCase(player.getName())){
							/*if((p.x > player.x - (clientWidth/2)) && (p.x < player.x + (clientWidth/2) && (p.y > player.y - (clientHeight/2) && (p.y < player.y + (clientHeight/2))))){

							} else {
								otherPlayersOnScreen.remove(p);
							}*/
							if(otherPlayersOnScreen.contains(p)){
								p.x = x;
								p.y = y;
							} else {
								otherPlayersOnScreen.add(p);
							}
						}
					}
				}
			} else if(words[0].equalsIgnoreCase("newuser")){//SHOULD RETURN "newuser,true"	
				String ans = words[1];
				String name = words[2];
				System.out.println("[CLIENT] - New user successfully created: " + ans);
				if(ans.equalsIgnoreCase("true")){
					loginPass(name, this.pClass.toString(), 0+"", 1+"", SPAWN_LOCATION.getX()+"", SPAWN_LOCATION.getY()+"");
				} else {
					loginFail();
				}
			} else if (words[0].equalsIgnoreCase("projectile")){
				System.out.println("is type projectile");      
				for(int i=1; i< (words.length);i=i+3){
					System.out.println(words[i] +"," + words[i+1]+"," +words[i+2]);
					Projectile p = new ArrowProjectile(Integer.parseInt(words[i]), Integer.parseInt(words[i+1]), Double.parseDouble(words[i+2]));
					level.addProjectile(p);     
				}
			}
		}
	}

	public void registerPackets(){
		Kryo kryo = client.getKryo();

	}

	public void sendMessage(Object message) {
		if (client.isConnected()) {
			client.sendTCP(message);
		}
	}
	public void sendMessageUDP(Object message) {
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

	public void loginFail(){
		client.stop();
		client.close();		
		System.out.println("NO");
	}

	public void loginPass(String name, String pClass, String exp, String lvl, String xPos, String yPos){
		Player.PLAYER_CLASS c = getClassByName(pClass);
		state = STATE.GAME;
		loginFrame.setVisible(false);
		initGame(name, Integer.parseInt(xPos), Integer.parseInt(yPos), c, Integer.parseInt(exp), Integer.parseInt(lvl));
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
						connectLocal();
						loginBox.setVisible(false);
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
		newUser = true;
		warriorButton.setIcon(new ImageIcon(this.warriorButtonImg));
		warriorButton.setBorder(BorderFactory.createEmptyBorder());
		warriorButton.setContentAreaFilled(false);
		warriorButton.addActionListener(new ActionListener() {
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
						pClass = Player.PLAYER_CLASS.WARRIOR;
						InitializeConnection();
						connectLocal();
						loginBox.setVisible(false);
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

		archerButton.setIcon(new ImageIcon(this.archerButtonImg));
		archerButton.setBorder(BorderFactory.createEmptyBorder());
		archerButton.setContentAreaFilled(false);
		archerButton.addActionListener(new ActionListener() {
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
						pClass = Player.PLAYER_CLASS.ARCHER;
						InitializeConnection();
						connectLocal();
						loginBox.setVisible(false);
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

		mageButton.setIcon(new ImageIcon(this.mageButtonImg));
		mageButton.setBorder(BorderFactory.createEmptyBorder());
		mageButton.setContentAreaFilled(false);
		mageButton.addActionListener(new ActionListener() {
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
						pClass = Player.PLAYER_CLASS.MAGE;
						InitializeConnection();
						connectLocal();
						loginBox.setVisible(false);
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

	public void initialiseMonsters(){
		for(int i = 0; i < kniteCoords.length; i++){
			Monster m = new ChestMonster((kniteCoords[i][0])*64, (kniteCoords[i][1])*64, Sprite.knightMob, 15, 100, false, true);
			m.initialise(level);
			level.addEntity(m);
		}
		
		for(int i = 0; i < bossCoords.length; i++){
			Monster m = new ChestMonster((bossCoords[i][0])*64, (bossCoords[i][1])*64, Sprite.knightMob, 15, 500, false, true);
			m.initialise(level);
			level.addEntity(m);
		}
		
		for(int i = 0; i < treasureCoords.length; i++){
			Monster m = new ChestMonster((treasureCoords[i][0])*64, (treasureCoords[i][1])*64, Sprite.chestMob, 0, 0, false, false);
			m.initialise(level);
			level.addEntity(m);
		}
		
		for(int i = 0; i < ghostCoords.length; i++){
			Monster m = new ChestMonster((ghostCoords[i][0])*64, (ghostCoords[i][1])*64, Sprite.ghostMob, 15, 100, true, false);
			m.initialise(level);
			level.addEntity(m);
		}
	}

	private void initGame(String name, Player.PLAYER_CLASS pClass){

		key = new Keyboard();/*Initialise KeyBoard object*/
		mouse = new Mouse();
		//level = new RandomLevel(128, 128);
		level = new SpawnLevel("/textures/map/MAP_3.PNG");
		//level.generateLevel();
		penisMob = new ChestMonster(116*64, 116*64, Sprite.penisMob, 15, 100, false, true);
		chestMob = new ChestMonster(116*64, 120*64, Sprite.chestMob, 0, 1000, false, false);
		ghostMob = new GhostMonster(116*64, 124*64, Sprite.ghostMob, 0, 50, true, false);
		//		Monster guardMonster1 = new ChestMonster(128*64, 91*64, Sprite.penisMob, 15, 100, false, true);
		//		Monster guardMonster2 = new ChestMonster(122*64, 91*64, Sprite.penisMob, 15, 100, false, true);

		player = new Player(name, SPAWN_LOCATION.getX(), SPAWN_LOCATION.getY(), key, pClass);

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

		initialiseMonsters();

		penisMob.initialise(level);
		chestMob.initialise(level);
		ghostMob.initialise(level);
		//guardMonster1.initialise(level);
		//guardMonster2.initialise(level);

		//level.addEntity(guardMonster1);
		//level.addEntity(guardMonster2);
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

	private void initGame(String name, int x, int y, Player.PLAYER_CLASS pClass, int exp, int lvl){

		key = new Keyboard();/*Initialise KeyBoard object*/
		mouse = new Mouse();
		//level = new RandomLevel(128, 128);
		level = new SpawnLevel("/textures/map/MAP_3.PNG");
		//level.generateLevel();
		penisMob = new ChestMonster(116*64, 116*64, Sprite.penisMob, 15, 100, false, true);
		chestMob = new ChestMonster(116*64, 120*64, Sprite.chestMob, 0, 1000, false, false);
		ghostMob = new GhostMonster(116*64, 124*64, Sprite.ghostMob, 0, 50, true, false);
		//		Monster guardMonster1 = new ChestMonster(128*64, 91*64, Sprite.penisMob, 15, 100, false, true);
		//		Monster guardMonster2 = new ChestMonster(122*64, 91*64, Sprite.penisMob, 15, 100, false, true);

		player = new Player(name, x, y, exp, lvl, key, pClass);

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

		initialiseMonsters();

		penisMob.initialise(level);
		chestMob.initialise(level);
		ghostMob.initialise(level);
		//guardMonster1.initialise(level);
		//guardMonster2.initialise(level);

		//level.addEntity(guardMonster1);
		//level.addEntity(guardMonster2);
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
		if(counter == 1){
			String send = "player," + player.getName() + "," + player.x + "," + player.y + "," + player.getPlayerClass(); 
			sendMessage(send); //what we want to send
			String projectile = "projectile" + "," ;
			for (Projectile p : level.getProjectiles()){
				projectile = projectile + p.getX()+ "," + p.getY() + ",";    
			}
			sendMessage(projectile);
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
		for(int i = 0; i < otherPlayersOnScreen.size(); i++){
			otherPlayersOnScreen.get(i).render(otherPlayersOnScreen.get(i).getX(), otherPlayersOnScreen.get(i).getY(), screen);
		}
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
		g.drawString("xP:" + player.x + ", yP:" + player.y, 20, 50);

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

	private String getUser() {
		return user;
	}

	private void setUser(String user) {
		this.user = user;
	}

	private String getPass() {
		return pass;
	}

	private void setPass(String pass) {
		this.pass = pass;
	}

	private Player.PLAYER_CLASS getClassByName(String pClass){
		switch(pClass){
		case "WARRIOR":
			return Player.PLAYER_CLASS.WARRIOR;
		case "ARCHER":
			return Player.PLAYER_CLASS.ARCHER;
		case "MAGE":
			return Player.PLAYER_CLASS.MAGE;
		}
		return null;
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
