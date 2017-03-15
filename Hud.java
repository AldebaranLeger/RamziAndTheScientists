import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Hud {
	// position de la barre (coin supérieur gauche)
	private static final int P_BAR_X = 10;
	private static final int P_BAR_Y = 10;
	private static final int BAR_X = 84 + P_BAR_X;
	private static final int I_BAR_X = 350;
	private static final int I_BAR_Y = 530;
	private static final int LIFE_BAR_Y = 4 + P_BAR_Y;
	private static final int ATQ_BAR_Y = 24 + P_BAR_Y;
	private static final int CPT_ENNEMI_BAR_Y = 44 + P_BAR_Y;
	private static final Color LIFE_COLOR = new Color(255, 0, 0);
	private static final Color ATQ_COLOR = new Color(0, 0, 255);
	private static final Color CPT_ENNEMI_COLOR = new Color(0, 255, 0);
	private static final int BAR_WIDTH = 80;
	private static final int BAR_HEIGHT = 16;
	private Image playerBars, playerItems;
	private Ramzi player;
	private int playerHp;
	private WorldMap wm;
	private int maxEnnemis, nbMorts;
		
	public Hud(Ramzi player){
		this.player = player;
	}

	  public void init(WorldMap wm) throws SlickException {	
		  this.wm = wm;
		  //nb ennemi au début du niveau
		  this.maxEnnemis = wm.getEnnemis();
		  this.nbMorts = wm.getNbMorts();
		  this.playerHp = player.getHp();
		  this.playerBars = new Image("ressources/hud/playerBar.png");
		  this.playerItems = new Image("ressources/hud/hud.png");
	  } 
	  
	  public void render(Graphics g) {
		  //annule la caméra car hud fixe
		  g.resetTransform();
		  g.drawImage(this.playerBars, P_BAR_X, P_BAR_Y);
		  g.setColor(LIFE_COLOR);
		  g.fillRect(BAR_X, LIFE_BAR_Y, ((float)this.playerHp-2)* BAR_WIDTH / (float)this.playerHp, BAR_HEIGHT);
		  g.setColor(ATQ_COLOR);
		  g.fillRect(BAR_X, ATQ_BAR_Y, .8f * BAR_WIDTH, BAR_HEIGHT);
		  g.setColor(CPT_ENNEMI_COLOR);
		  //jauge qui diminue à chaque ennemi tué
		  g.fillRect(BAR_X, CPT_ENNEMI_BAR_Y, (((float)this.maxEnnemis-(float)this.nbMorts))*BAR_WIDTH / (float)this.maxEnnemis , BAR_HEIGHT);
		  g.drawImage(this.playerBars, P_BAR_X, P_BAR_Y);
		  g.drawImage(this.playerItems, I_BAR_X, I_BAR_Y);
		  g.setColor(Color.white);
		  g.drawString("ClicL", I_BAR_X+5, I_BAR_Y +45 );
		  g.drawString("ClicR", I_BAR_X + 60, I_BAR_Y + 45);
	  }  
	  
}
