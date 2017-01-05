
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
	private Ramzi ramzi = new Ramzi();
	private SourisEnnemie souris;
	private Camera camera;
	private Hud hud = new Hud();
	
	public WindowGame() {
		super("Ramzi & The Scientists");
	}
	
	/**
	 * initialise le contenu du jeu, charge les graphismes, la musique, etc.
	 */
	public void init(GameContainer container) throws SlickException {
		this.container = container ;
		this.hud.init();
		//this.map.init();
		this.map = new TiledMap("ressources/map/map_niv1.tmx");
		souris  = new SourisEnnemie(map);
		this.ramzi.init();
		this.souris.init();
		camera = new Camera(souris);
		
	}
	
	/**
	 * affiche le contenu du jeu
	 */
	public void render(GameContainer container, Graphics g) throws SlickException  {
		//caméra suiveuse
		g.translate(container.getWidth() / 2 - souris.getX(), container.getHeight() / 2 - souris.getY());
		// chargement des calques
		this.map.render(0,0,0);
		this.map.render(0,0,1);
		this.ramzi.render(g);
		this.souris.render(g);
		this.hud.render(g);
		camera.place(container, g);
	}
	
	/**
	 * met à jour les éléments de la scène en fonction du delta tps qui est survenu.
	 */
	public void update(GameContainer container, int delta) throws SlickException {
		//this.ramzi.update(delta);
		this.souris.update(delta);
		camera.update(container);

	}
	
	public void keyPressed(int key, char c) {
		//this.ramzi.keyPressed(key, c);
		this.souris.keyPressed(key, c);
	}
	
	/**
	 * appelée à chaque relachement de touche
	 * @param args
	 */
	public void keyReleased(int key, char c) {
		//this.ramzi.keyReleased(key, c);
		this.souris.keyReleased(key, c);
		
	}
	
	 public static void main(String[] args) throws SlickException {
		 new AppGameContainer(new WindowGame(), 640, 480, false).start();
	 }

}
