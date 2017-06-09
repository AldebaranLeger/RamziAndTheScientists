package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import levels.*;
import levels.level1.*;

public class WorldMap extends BasicGameState implements ComponentListener {
	
	public WorldMap (){}
		public int id;
		private StateBasedGame stateBasedGame; //isolated different stages of the game (menu, ingame, hiscores, etc) into different states so they can be easily managed and maintained.
		private GameContainer container; //GameContainer contient le contexte dans lequel notre jeu sera exécuté
		private TiledMap map1; //une map pour chaque niveau
		private Ramzi player;
		private Camera camera;
		public static Ennemi[] tabEnnemi;
		private Bullet bullet;
		public static float cursorX, cursorY;
		public Hud hud;
		//public static MadMouse madMouse = null;
		private ProjCheese tabCheese[] = new ProjCheese[10];
		//menu pause
		private boolean escapeMenu;
		private Image btnResume, btnExit, btnMainMenu, txtGameOver, txtYouWin;
		private MouseOverArea resume, exit, mainMenu;		
		//écran Game Over
		private boolean gameOver = false; 
		//écran Gagné !
		private boolean youWin = false;		
		
		private Level currentLevel; 
		
		public WorldMap (int id) {
			this.id = id;
			WorldMap.tabEnnemi =  new Ennemi[100];
		}
		
		/**
		 * initialise le contenu du jeu, charge les ressources (les graphismes, la musique, etc.)
		 */	
	public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		this.stateBasedGame = stateBasedGame;
		this.container = container;
		this.map1 = new TiledMap("ressources/map/map1.tmx"); //initialisation de la map (niveau 1)
		
		player = new Ramzi(map1, this);	
		player.init();
		
		this.currentLevel = new Level1(this, map1, player); //niveau 1 au démarrage du jeu (sauvegarde impossible)	
		currentLevel.setTabEnnemi(tabEnnemi);
		currentLevel.init(container, stateBasedGame);
		
		this.hud = new Hud(player);
		this.hud.init(this);
		camera = new Camera(player);
				
		Controle control = new Controle(player);
		this.container.getInput().addKeyListener(control);
		this.container.getInput().addMouseListener(control);
		
		createMenu();
	
	}
	
	/**
	 * affiche le contenu du jeu
	 */
	public void render(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException 
	{		
		g.translate(container.getWidth() / 2 - player.getX(), container.getHeight() / 2 - player.getY());//caméra suiveuse
		this.currentLevel.render(container, stateBasedGame, g); //affiche map, ennemis et boss du niveau courant
		camera.place(container, g);
		renderMenu(g);	
		playerBulletRefresh(g);
		bossBulletRefresh(g);				
	}
	
	/**
	 * met à jour les éléments de la scène en fonction des entrées utilisateurs ou autre
	 */
	public void update(GameContainer container, StateBasedGame stateBasedGame, int delta) throws SlickException
	{
		if(escapeMenu == true || gameOver== true) {
			// on met le jeu en pause
			container.pause();
		} else
		{
			this.player.update(delta);
			currentLevel.setTabEnnemi(tabEnnemi);
			this.currentLevel.update(container, stateBasedGame, delta);
			tabEnnemi = currentLevel.getTabEnnemi();
			camera.update(container);
			playerBulletUpdate(delta);
			bossBulletUpdate(delta);			
			container.resume(); //continue les updates dans le GameContainer
			
			if(!this.player.isAlive()){
				gameOver=true;
			}
		}
	}
	
	/**
	 * Créer les menus
	 * @throws SlickException
	 */
	public void createMenu() throws SlickException {
		this.escapeMenu=false;
		this.gameOver = false;
		//menu pause
		btnResume = new Image("ressources/boutons/resume.png");
		btnExit = new Image("ressources/boutons/btnExit.png");
		btnMainMenu = new Image ("ressources/boutons/mainmenu.png");
		resume = new MouseOverArea(container, btnResume, 300, container.getHeight() - btnResume.getHeight()*10, this);
		mainMenu = new MouseOverArea(container, btnMainMenu, 300, container.getHeight() - btnMainMenu.getHeight()*10, this);
		exit = new MouseOverArea(container, btnExit, 320, container.getHeight() - btnExit.getHeight() *6, this);
		//écran game over
		txtGameOver = new Image("ressources/boutons/gameover.png");
		//écran Gagné
		txtYouWin = new Image("ressources/boutons/youWin.png");
	}
	
	/**
	 * Affiche les attaques à distance du joueur (Ramzi)
	 * @param g
	 * @throws SlickException 
	 */
	public void playerBulletRefresh(Graphics g) throws SlickException {
		if(bullet!=null)
		{
			bullet.render(container, g);
		}
	}
	
	/**
	 * Affiche les attaques à distance des boss
	 * @param g
	 * @throws SlickException 
	 */
	public void bossBulletRefresh(Graphics g) throws SlickException {
		for(int i = 0 ; i < 10 ; i ++)
		{
			if(tabCheese[i]!=null)
			{
				tabCheese[i].render(container, g);
			}
		}
	}
	
	/**
	 * affiche le menu en fonction du choix du joueur (touche clavier)
	 * @param g
	 */
	public void renderMenu(Graphics g) {
		if (escapeMenu == true) {
			g.resetTransform(); //fixer le menu en stoppant la caméra
			g.fillRect(0, 0, container.getWidth() + 200, container.getHeight());
			g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.03f));
			resume.render(container, g);
			resume.setMouseOverColor(Color.orange);
			mainMenu.render(container, g);
			mainMenu.setMouseOverColor(Color.orange);
			exit.render(container, g);
			exit.setMouseOverColor(Color.orange);
		}else if(gameOver == true){
			g.resetTransform();
			g.fillRect(0, 0, container.getWidth() + 200, container.getHeight());
			g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.03f));
			txtGameOver.draw(340, container.getHeight() - txtGameOver.getHeight()*10+20);
			mainMenu.render(container, g);
			mainMenu.setMouseOverColor(Color.orange);
			exit.render(container, g);
			exit.setMouseOverColor(Color.orange);
		} else if(youWin == true){
			g.resetTransform();
			g.fillRect(0, 0, container.getWidth() + 200, container.getHeight());
			g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.03f));
			txtYouWin.draw(340, container.getHeight() - txtYouWin.getHeight()*11);
			mainMenu.render(container, g);
			mainMenu.setMouseOverColor(Color.orange);
			exit.render(container, g);
			exit.setMouseOverColor(Color.orange);			
		}else {
			this.hud.render(g);
		}
	}
	
	public void playerBulletUpdate(int delta){
		if(bullet!=null){
			bullet.update(delta);
		}
	}
	
	public void bossBulletUpdate(int delta) {
		for(int i = 0 ; i < 10 ; i ++)
		{
			if(tabCheese[i]!=null)
			{
				tabCheese[i].update(delta, i);
			}
		}
	}
	
	public void keyReleased(int key, char c) {
		//à l'appui sur la touche ECHAP, on quitte le jeu
		if ((Input.KEY_ESCAPE) == key) {
			container.exit();
		}
		if(Input.KEY_P == key) {
			escapeMenu = true; //menu pause
		}
		if(Input.KEY_E == key)
			escapeMenu = false; //on reprend la partie
	}

	public int getID() { return id;}
	
	public void createRamziProjectile(int xClic, int yClic)
	{
		int xDirection, yDirection;
		//this.projectile = new Projectile(this.player, xRamzi, yRamzi, xClic, yClic);
		
		if(xClic < container.getWidth() / 2)
			xDirection = xClic - container.getWidth() / 2;
		else
			xDirection = xClic;
		if(yClic < container.getHeight() / 2)
			yDirection = yClic - container.getHeight() / 2;
		else
			yDirection = yClic;
	
		this.bullet = new Bullet( new Vector2f(container.getWidth() / 2, container.getHeight() / 2) , new Vector2f(xDirection,yDirection) );
		
	}
	
	public void destroyProjectile()
	{
		//this.projectile = null;
	}
	
	public void createMadMouseCheese(MadMouse mm, int delta, int nbCheese)
	{
		//System.out.println("WorldMap : " + madMouse.getX() + ", " + madMouse.getY());
		tabCheese = new ProjCheese[10];
		for(int i = 1; i<=nbCheese; i++){
			System.out.println("WorldMap mm.getX() = "+mm.getX());
			tabCheese[i]= new ProjCheese(this.player,mm);
		}
	}

	public void destroyMadMouseCheese(int i)
	{
		tabCheese[i] = null;
	}
//	public void bossAtk(int e){
//		bossAtkState = e;
//	}
	
	public void componentActivated(AbstractComponent source) {
		System.out.println(source);
		if(source == resume) {
			escapeMenu = false;
		} else if(source == mainMenu) {
			stateBasedGame.enterState(0);
		} else {
			container.exit();
		}
	}
	
	public void setYouWin(boolean bool) {this.youWin = bool;}
	public int getEnnemisDebut() { return currentLevel.getNbEnnemisDebut();}
	public int getEnnemisSauves() {return currentLevel.getNbEnnemisSauves();}
	public MadMouse getBossLevel() {return currentLevel.getBoss();}	
}
