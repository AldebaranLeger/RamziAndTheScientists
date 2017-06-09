import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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

public class WorldMap extends BasicGameState implements ComponentListener {
	
	public WorldMap (){}
		public int id;
		private StateBasedGame stateBasedGame; //isolated different stages of the game (menu, ingame, hiscores, etc) into different states so they can be easily managed and maintained.
		private GameContainer container; //GameContainer contient le contexte dans lequel notre jeu sera exécuté
		private TiledMap map;
		private Ramzi player;
		private Camera camera;
		public static Ennemi[] tabEnnemi = new Ennemi[100];
		private int nbEnnemisSauves;
		public static int nbEnnemisDebut;
		private float xEnnemi, yEnnemi; //coordonnées de l'ennemi
		private List<Bullet> bullet = new ArrayList<Bullet>();
		public static float cursorX, cursorY;
		private Hud hud;
		private float xEnnemiSauve, yEnnemiSauve; //coordonnées de l'ennemi enregistrée lorsque ses points de contamination sont à zéro (ennemi sauvé)
		public static MadMouse madMouse = null;
		private ProjCheese tabCheese[] = new ProjCheese[10];
		private boolean madMouseArrives=false;		
		private int totalEnnemisSauves;
		//menu pause
		private boolean escapeMenu;
		private Image btnResume, btnExit, btnMainMenu, txtGameOver, txtYouWin;
		private MouseOverArea resume, exit, mainMenu;		
		//écran Game Over
		private boolean gameOver = false; 
		//écran Gagné !
		private boolean youWin = false;
		// Cooldown de l'attaque à distance
		private int attaqueDistanceCooldown = -1;
		
		public WorldMap (int id) {
			this.id = id;
		}
		
		/**
		 * initialise le contenu du jeu, charge les ressources (les graphismes, la musique, etc.)
		 */	
	public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		this.stateBasedGame = stateBasedGame;
		this.container = container;
		this.map = new TiledMap("ressources/map/map1.tmx");
		player  = new Ramzi(map, this);
		this.hud = new Hud(player);
		camera = new Camera(player);
		addEnnemis();
		this.player.init();
		for(int i = 0 ; i < nbEnnemisDebut; i ++ )
		{
			tabEnnemi[i].init();
		}	
		this.hud.init(this);
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
		//caméra suiveuse
		g.translate(container.getWidth() / 2 - player.getX(), container.getHeight() / 2 - player.getY());
		this.map.render(0,0,0);
		this.map.render(0,0,1);
		renderRamzi(g);
		renderBoss(g);
		renderEnnemis(g);
		//camera.place(container, g);
	//	g.resetTransform();
		playerBulletRefresh(g);
		bossBulletRefresh(g);		
		renderMenu(g);		
	}
	
	private void renderRamzi(Graphics g) throws SlickException
	{
		if(player.getImmuniteCooldown() == 0)
		{
			this.player.render(g);
		}
		else
		{
			if(player.getImmuniteCooldown()%4 == 1)
			{
				this.player.render(g);
			}
		}
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
			if((tabEnnemi!=null)){
				updatesEnnemis(delta);
			} else if(madMouseArrives) {
				if(madMouse!=null) {
					this.hud.setBossMaxHp(madMouse.getMaxPv());
					this.hud.updateBossHp(madMouse.getPtVie());
				}
				gameState(delta);
			}
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
	 * Placement des ennemis de manière aléatoire sur la carte
	 */
	public void addEnnemis() {
		int tileW = map.getTileWidth();
		int tileH = map.getTileHeight();
		int collisionLayer = this.map.getLayerIndex("collision");
		int mapLayer = this.map.getLayerIndex("sol");
		nbEnnemisDebut = 5;
		this.nbEnnemisSauves=0;
		for(int i = 0 ; i < nbEnnemisDebut; i ++ )
		{
			boolean inMap = false;
			boolean inCollision = false;
			boolean inRamziRayon;
			do
			{
				inRamziRayon  = false;
				//coordonnées de l'ennemi calculé aléatoirement
				yEnnemi = (float)(Math.random() * (map.getHeight() * map.getTileHeight() - 0)); 
				xEnnemi = (float)(Math.random() * (map.getWidth() * map.getTileWidth() - 0));
				
				Image tileMap = this.map.getTileImage((int) xEnnemi / tileW, (int) yEnnemi / tileH, mapLayer);
				Image tileCol = this.map.getTileImage((int) xEnnemi / tileW, (int) yEnnemi / tileH, collisionLayer);
				inMap = tileMap != null;
				inCollision = tileCol != null;
				//si les coordonnées de l'ennemi font partie intégrante de la map, alors on le crée.
				if(inMap)
				{
					int random = (int)(Math.random() * 2 +1);
					if(random == 1)
						tabEnnemi[i] = new Souris1(map,xEnnemi,yEnnemi);
					else
						tabEnnemi[i] = new Souris2(map,player,xEnnemi,yEnnemi);
				}
				// si l'ennemi est dans la rayon autour de Ramzi, alors on refait la boucle pour le placer ailleurs. 
				if (( xEnnemi > (player.getX() - 200) && xEnnemi < (player.getX() + 200))
						&& ( yEnnemi > (player.getY() - 200) && ( yEnnemi < player.getY() + 200))) 
				{
					inRamziRayon = true;
				}
				
			}while(!inMap || inRamziRayon);
			
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
	 * affiche les ennemis sur la carte
	 * @param g
	 * @throws SlickException 
	 */
	public void renderEnnemis(Graphics g) throws SlickException {
		if(tabEnnemi!=null){
			for(int i = 0 ; i < nbEnnemisDebut; i ++ )
			{
				if(tabEnnemi[i]!=null)
				tabEnnemi[i].render(g);
			}
		}
	}
	
	/**
	 * affiche le boss du niveau quand le dernier ennemi est sauvé.
	 * @throws SlickException 
	 */
	public void renderBoss(Graphics g) throws SlickException {
		if(madMouse!=null && !madMouseArrives){
			madMouseArrives=true;
			System.out.println("MadMouse apparait.");
			madMouse.init();
		}
		if(madMouseArrives){
			if(madMouse!=null){
				if(!madMouse.isSaved()){
					this.madMouse.render(container, stateBasedGame, g);
				} else {
					madMouse = null;
				}
			}
		}
		
	}
	
	/**
	 * Affiche les attaques à distance du joueur (Ramzi)
	 * @param g
	 * @throws SlickException 
	 */
	public void playerBulletRefresh(Graphics g) throws SlickException {
		if(bullet!=null){
			for(int i = 0; i< this.bullet.size(); i++){
				if(this.bullet.get(i)!=null){
					this.bullet.get(i).render(g);
				}
			}
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

	/**
	 * Met à jour l'état des ennemis (nombre, apparition du boss) 
	 * @throws SlickException 
	 */
	public void updatesEnnemis(int delta) throws SlickException {
		int nbEnnemisSauves = 0;
		for(int i = 0 ; i < nbEnnemisDebut; i ++ )
		{
			if(tabEnnemi[i]!=null){
				tabEnnemi[i].update(delta);
				if(tabEnnemi[i].isSaved()){
					xEnnemiSauve = tabEnnemi[i].getX();
					yEnnemiSauve = tabEnnemi[i].getY();
					if(tabEnnemi[i].estAMetamorphoser()){
						tabEnnemi[i]=null;
						this.totalEnnemisSauves++;
					}
				}
			}
			else {
				nbEnnemisSauves++;
			}
			if(nbEnnemisSauves==nbEnnemisDebut){
				tabEnnemi=null;
				madMouse = new MadMouse(map, player, xEnnemiSauve, yEnnemiSauve); //le boss MadMouse apparaît aux coordonnées du dernier ennemi sauvé
			}
		}
	}
	
	/**
	 * Définit si le joueur a gagné ou non en fonction de l'état du boss
	 * @param delta
	 * @throws SlickException 
	 */
	public void gameState(int delta) throws SlickException {
		if(madMouse!=null){
			if(!madMouse.isSaved()){
				madMouse.update(container, stateBasedGame, delta);
			} else {
				youWin = true;
			}
		}
	}
	
	public void playerBulletUpdate(int delta){
		if(bullet!=null){
			for(int i = 0; i<bullet.size(); i++){
				if(bullet.get(i)!=null){
					bullet.get(i).update(delta);
					if(!bullet.get(i).isAlive()){
						bullet.remove(i);
					}
				}
			}
		}
		if(this.attaqueDistanceCooldown != -1)
		{
			this.attaqueDistanceCooldown++;
			if(this.attaqueDistanceCooldown == 20){
				this.attaqueDistanceCooldown = -1;
			}
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
	
	public void createRamziProjectile(int directionProjectile) throws SlickException
	{
		/*
		int xDirection, yDirection;
		//this.projectile = new Projectile(this.player, xRamzi, yRamzi, xClic, yClic);
		
		if(xClic < container.getWidth() / 2)
			xDirection = (xClic - container.getWidth() /2) *2;
		else
			xDirection = xClic;
		if(yClic < container.getHeight() / 2)
			yDirection = (yClic - container.getHeight() /2 ) *2;
		else
			yDirection = yClic;
		System.out.println(xDirection +" / " + yDirection);
		this.bullet = new Bullet( 
				new Vector2f(container.getWidth() / 2, container.getHeight() / 2) , 
				new Vector2f(xDirection,yDirection)
				);
		*/
		if(this.attaqueDistanceCooldown == -1){
			if(this.bullet!= null){
				this.bullet.add(new Bullet(this, map, player,directionProjectile));
				this.bullet.get(this.bullet.size()-1).init();
			}
			this.attaqueDistanceCooldown = 0;
		}
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
	public int getEnnemis() { return this.nbEnnemisDebut;}
	public int getNbEnnemisSauves() {return this.totalEnnemisSauves;}	
	
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
}
