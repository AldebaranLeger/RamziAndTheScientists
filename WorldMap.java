import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class WorldMap extends BasicGameState implements ComponentListener{
	
	public int id;
	//GameContainer contient le contexte dans lequel notre jeu sera exécuté
	private StateBasedGame sbg;
	private GameContainer container;
	private TiledMap map;
	private Ramzi player;
	private Pnj pnj;
	private Camera camera;
	private Ennemi[] tabEnnemi = new Ennemi[100];
	private int nbEnnemis;
	private float x, y;
	//private Hud hud;
	
	//menu pause
	private boolean escapeMenu = false;
	private Image btnResume, btnExit, btnMainMenu;
	private MouseOverArea resume, exit, mainMenu;
	
	public WorldMap (int id) {
		this.id = id;
	}
		
	/**
	 * initialise le contenu du jeu, charge les ressources (les graphismes, la musique, etc.)
	 */	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.sbg = sbg;
		this.container = gc;

		/*  -- Placement des ennemis --  */
		
		nbEnnemis = 50;

		this.map = new TiledMap("ressources/map/map1.tmx");
		player  = new Ramzi(map);
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
				if(inMap || inCollision)
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
				
			}while(!inMap || inCollision || outRayonRamzi);
		}
		
		
		pnj  = new Pnj(map,player,650,100);
		player.setPnj(pnj);
		this.player.init();		
		this.pnj.init();
		for(int i = 0 ; i < nbEnnemis; i ++ )
		{
			tabEnnemi[i].init();
		}	
		//this.hud.init(container);
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

	}

	
	/**
	 * affiche le contenu du jeu
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		//caméra suiveuse
		g.translate(container.getWidth() / 2 - player.getX(), container.getHeight() / 2 - player.getY());	
		this.map.render(0,0,0);
		this.map.render(0,0,1);
		this.player.render(g);
		this.pnj.render(g);
		for(int i = 0 ; i < nbEnnemis; i ++ )
		{
			tabEnnemi[i].render(g);
		}

		//this.hud.render(g);
		
		if (escapeMenu == true) {
			//fixer le menu en stoppant la caméra
			g.resetTransform();
			g.fillRect(0, 0, gc.getWidth() + 200, gc.getHeight());
			g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.03f));
			resume.render(gc, g);
			mainMenu.render(gc, g);
			exit.render(gc, g);
			g.drawString("Exit (Q)", 300, 300);

		}
	}

	
	/**
	 * met à jour les éléments de la scène en fonction des entrées utilisateurs ou autre
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		if(escapeMenu == true) {
			// on met le jeu en pause
			gc.pause();
		} else {
			this.player.update(delta);
			this.pnj.update(delta);
			for(int i = 0 ; i < nbEnnemis; i ++ )
			{
				tabEnnemi[i].update(delta);
			}
			camera.update(container);
			gc.resume();
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
	
	public void componentActivated(AbstractComponent source) {
		if(source == resume) {
			escapeMenu = false;
		} else if(source == mainMenu) {
			sbg.enterState(0);
		} else {
			container.exit();
		}
	}

}
