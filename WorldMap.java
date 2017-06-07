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
	
	public WorldMap ()
	{
		
	}
	public int id;
	//GameContainer contient le contexte dans lequel notre jeu sera exécuté
		private StateBasedGame sbg;
		private GameContainer container;
		private TiledMap map;
		private Ramzi player;
		private Camera camera;
		public static Ennemi[] tabEnnemi = new Ennemi[100];
		private int nbMorts;
		public static int nbEnnemis;
		private float x, y;
		private Bullet bullet;
		public static float cursorX, cursorY;
		private Hud hud;
		private float xMort, yMort;
		public static MadMouse madMouse = null;
		private ProjCheese tabCheese[] = new ProjCheese[10];
		private boolean madMouseArrives=false;
		
		private int totalMorts;
		private int z = 0;
		
		//menu pause
		private boolean escapeMenu;
		private Image btnResume, btnExit, btnMainMenu, txtGameOver;
		private MouseOverArea resume, exit, mainMenu;
		
		//écran Game Over
		private boolean gameOver = false; 
		
		
		public WorldMap (int id) {
			this.id = id;
		}
		
		/**
		 * initialise le contenu du jeu, charge les ressources (les graphismes, la musique, etc.)
		 */	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.escapeMenu=false;
		this.gameOver = false;
		this.sbg = sbg;
		this.container = gc;

						/*  -- Placement des ennemis --  */
		
		nbEnnemis = 10;
		this.nbMorts=0;

		this.map = new TiledMap("ressources/map/map1.tmx");
		player  = new Ramzi(map, this);
		this.hud = new Hud(player);
		int tileW = map.getTileWidth();
		int tileH = map.getTileHeight();
		int collisionLayer = this.map.getLayerIndex("collision");
		int mapLayer = this.map.getLayerIndex("sol");

		
		for(int i = 0 ; i < nbEnnemis; i ++ )
		{
			boolean inMap = false;
			boolean inCollision = false;
			boolean outRayonRamzi;
			do
			{
				outRayonRamzi  = false;
				y = (float)(Math.random() * (map.getHeight() * map.getTileHeight() - 0));
				x = (float)(Math.random() * (map.getWidth() * map.getTileWidth() - 0));
				

				Image tileMap = this.map.getTileImage((int) x / tileW, (int) y / tileH, mapLayer);
				Image tileCol = this.map.getTileImage((int) x / tileW, (int) y / tileH, collisionLayer);
				inMap = tileMap != null;
				inCollision = tileCol != null;
				if(inMap)
				{
					int random = (int)(Math.random() * 2 +1);
					if(random == 1)
						tabEnnemi[i] = new Souris1(map,x,y);
					else
						tabEnnemi[i] = new Souris2(map,player,x,y);
				}
				if (( x > (player.getX() - 200) && x < (player.getX() + 200))
						&& ( y > (player.getY() - 200) && ( y < player.getY() + 200))) // si player dans le rayon de la souris
				{
					outRayonRamzi = true;
				}
				
			}while(!inMap || outRayonRamzi);
			
		}

		
		this.player.init();	
		for(int i = 0 ; i < nbEnnemis; i ++ )
		{
			tabEnnemi[i].init();
		}	
		this.hud.init(this);

		Controle control = new Controle(player);
		this.container.getInput().addKeyListener(control);
		this.container.getInput().addMouseListener(control);
		camera = new Camera(player);
		
		//menu pause
		btnResume = new Image("ressources/boutons/resume.png");
		btnExit = new Image("ressources/boutons/btnExit.png");
		btnMainMenu = new Image ("ressources/boutons/mainmenu.png");
		resume = new MouseOverArea(gc, btnResume, 300, gc.getHeight() - btnResume.getHeight()*10, this);
		mainMenu = new MouseOverArea(gc, btnMainMenu, 300, gc.getHeight() - btnMainMenu.getHeight()*10, this);
		exit = new MouseOverArea(gc, btnExit, 320, gc.getHeight() - btnExit.getHeight() *6, this);
		
		//écran game over
		txtGameOver = new Image("ressources/boutons/gameover.png");

	}

	
	/**
	 * affiche le contenu du jeu
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

			if(madMouse!=null && !madMouseArrives){
				madMouseArrives=true;
				System.out.println("MadMouse apparait.");
				madMouse.init();
			}
		//caméra suiveuse
				g.translate(container.getWidth() / 2 - player.getX(), container.getHeight() / 2 - player.getY());
				this.map.render(0,0,0);
				this.map.render(0,0,1);
				this.player.render(g);
				if(madMouseArrives){
					if(madMouse!=null){
						if(!madMouse.isDead()){
							this.madMouse.render(gc, sbg, g);
						} else {
							madMouse = null;
						}
					}
				}
				if(tabEnnemi!=null){
					for(int i = 0 ; i < nbEnnemis; i ++ )
					{
						if(tabEnnemi[i]!=null)
						tabEnnemi[i].render(g);
					}
				}
				camera.place(container, g);
						
				
				if (escapeMenu == true) {
					//fixer le menu en stoppant la caméra
					//fixer le menu en stoppant la caméra
					g.resetTransform();
					g.fillRect(0, 0, gc.getWidth() + 200, gc.getHeight());
					g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.03f));
					resume.render(gc, g);
					resume.setMouseOverColor(Color.orange);
					mainMenu.render(gc, g);
					mainMenu.setMouseOverColor(Color.orange);
					exit.render(gc, g);
					exit.setMouseOverColor(Color.orange);
				}else if(gameOver == true){
					g.resetTransform();
					g.fillRect(0, 0, gc.getWidth() + 200, gc.getHeight());
					g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.03f));
					txtGameOver.draw(340, gc.getHeight() - txtGameOver.getHeight()*10+20);
					mainMenu.render(gc, g);
					mainMenu.setMouseOverColor(Color.orange);
					exit.render(gc, g);
					exit.setMouseOverColor(Color.orange);
				}else {
					this.hud.render(g);
				}
				
				if(bullet!=null)
				{
					bullet.render(gc, g);
				}
				for(int i = 0 ; i < 10 ; i ++)
				{
					if(tabCheese[i]!=null)
					{
						tabCheese[i].render(gc, g);
					}
				}
				
	}

	
	/**
	 * met à jour les éléments de la scène en fonction des entrées utilisateurs ou autre
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		if(escapeMenu == true || gameOver== true) {
			// on met le jeu en pause
			gc.pause();
		} else
		{
			this.player.update(delta);

			
			int nbMorts = 0;

			if((tabEnnemi!=null)){
				for(int i = 0 ; i < nbEnnemis; i ++ )
				{
						if(tabEnnemi[i]!=null){
							tabEnnemi[i].update(delta);
							if(tabEnnemi[i].isDead()){
								xMort = tabEnnemi[i].getX();
								yMort = tabEnnemi[i].getY();
								if(tabEnnemi[i].agony()){
									tabEnnemi[i]=null;
									this.totalMorts++;
								}
							}
						}
						else {
							nbMorts++;
						}
						if(nbMorts==nbEnnemis){
							tabEnnemi=null;
							
							madMouse = new MadMouse(this,map, player, xMort, yMort);
							
						}
				}
			} else if(madMouseArrives) {
				if(madMouse!=null){
					if(!madMouse.isDead()){
						madMouse.update(gc, sbg, delta);
					} else {
						gameOver = true;
					}
				}
			}
			
			
			camera.update(container);
			
			if(bullet!=null){
				bullet.update(delta);
			}
			
			for(int i = 0 ; i < 10 ; i ++)
			{
				if(tabCheese[i]!=null)
				{
					tabCheese[i].update(delta, i);
				}
			}
			
			gc.resume();
			
			if(!this.player.isAlive()){
				gameOver=true;
			}
		}
	}
	
	public void keyReleased(int key, char c) {
		//à l'appui sur la touche ECHAP, on quitte le jeu
		if ((Input.KEY_ESCAPE) == key) {
			container.exit();
		}
		if(Input.KEY_P == key) {
			//sbg.enterState(2);
			escapeMenu = true;
		}
		if(Input.KEY_E == key)
			escapeMenu = false;
	}

	public int getID() {
		return id;
	}
	
	public void createProjectile(int xClic, int yClic)
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
	
	public int getEnnemis() { return this.nbEnnemis;}
	public int getNbMorts() {return this.totalMorts;}	
	
	public void componentActivated(AbstractComponent source) {
		System.out.println(source);
		if(source == resume) {
			escapeMenu = false;
		} else if(source == mainMenu) {
			sbg.enterState(0);
		} else {
			container.exit();
		}
	}
}
