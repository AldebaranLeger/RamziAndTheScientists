
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

//implémente la boucle infinie
public class WindowGame extends BasicGame {
	
	private GameContainer container;
	private TiledMap map;
	private Ramzi player;
	private Pnj pnj;
	private Camera camera;
	private Ennemi[] tabEnnemi = new Ennemi[100];
	private int nbEnnemis;
	private float x, y;
	//private Hud hud = new Hud();
	
	public WindowGame()
	{
		super("Ramzi & The Scientists");
	}
	
	/**
	 * initialise le contenu du jeu, charge les graphismes, la musique, etc.
	 */
	public void init(GameContainer container) throws SlickException
	{
		
		/*  -- Placement des ennemis --  */
		
		nbEnnemis = 2;

		this.container = container ;
		this.map = new TiledMap("ressources/map/map_niv1.tmx");
		player  = new Ramzi(map);
		int tileW = map.getTileWidth();
		int tileH = map.getTileHeight();
		int collisionLayer = this.map.getLayerIndex("collision");
		int mapLayer = this.map.getLayerIndex("Calque de Tile 1");

		
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
		//this.hud.init();
		Controle control = new Controle(player);
		this.container.getInput().addKeyListener(control);
		this.container.getInput().addMouseListener(control);
		camera = new Camera(player);
		
	}
	
	/**
	 * affiche le contenu du jeu
	 */
	public void render(GameContainer container, Graphics g) throws SlickException  {
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
		camera.place(container, g);
		
		
		//this.hud.render(g);
	}
	
	/**
	 * met à jour les éléments de la scène en fonction du delta tps qui est survenu.
	 */
	public void update(GameContainer container, int delta) throws SlickException {

		this.player.update(delta);
		this.pnj.update(delta);
		
		for(int i = 0 ; i < nbEnnemis; i ++ )
		{
			tabEnnemi[i].update(delta);
		}
		camera.update(container);

		
	}
	 public static void main(String[] args) throws SlickException {
		 new AppGameContainer(new WindowGame(), 800, 600, false).start();
	 }

}
