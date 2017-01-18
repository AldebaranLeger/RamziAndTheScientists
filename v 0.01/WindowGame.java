
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

//implémente la boucle infinie
public class WindowGame extends BasicGame {
	
	private GameContainer container;
	private TiledMap map;
	private Ramzi souris;
	private Pnj pnj;
	private Souris1 ennemie;
	private Souris1 ennemie1;
	private Souris2 ennemie2;
	private Camera camera;
	//private Hud hud = new Hud();
	
	public WindowGame() {
		super("Ramzi & The Scientists");
	}
	
	/**
	 * initialise le contenu du jeu, charge les graphismes, la musique, etc.
	 */
	public void init(GameContainer container) throws SlickException {
		this.container = container ;
		this.map = new TiledMap("ressources/map/map_niv1.tmx");
		souris  = new Ramzi(map);
		pnj  = new Pnj(map,souris,650,100);
		souris.setPnj(pnj);
		ennemie  = new Souris1(map,850,200);
		ennemie1  = new Souris1(map,500,500);
		ennemie2  = new Souris2(map, souris,300,200);
		this.souris.init();
		this.pnj.init();
		this.ennemie.init();
		this.ennemie1.init();
		this.ennemie2.init();
		//this.hud.init();
		Controle control = new Controle(souris);
		this.container.getInput().addKeyListener(control);
		this.container.getInput().addMouseListener(control);
		camera = new Camera(souris);
		
	}
	
	/**
	 * affiche le contenu du jeu
	 */
	public void render(GameContainer container, Graphics g) throws SlickException  {
		//caméra suiveuse
		g.translate(container.getWidth() / 2 - souris.getX(), container.getHeight() / 2 - souris.getY());
		this.map.render(0,0,0);
		this.map.render(0,0,1);
		//this.ramzi.render(g);
		this.souris.render(g);
		this.pnj.render(g);
		this.ennemie.render(g);
		this.ennemie1.render(g);
		this.ennemie2.render(g);
		camera.place(container, g);
		
		
		//this.hud.render(g);
	}
	
	/**
	 * met à jour les éléments de la scène en fonction du delta tps qui est survenu.
	 */
	public void update(GameContainer container, int delta) throws SlickException {

		this.souris.update(delta);
		this.pnj.update(delta);
		this.ennemie.update(delta);
		this.ennemie1.update(delta);
		this.ennemie2.update(delta);
		camera.update(container);

		
	}
	 public static void main(String[] args) throws SlickException {
		 new AppGameContainer(new WindowGame(), 800, 600, false).start();
	 }

}
