import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Hud {
	// position de la barre
	private static final int P_BAR_X = 10;
	private static final int P_BAR_Y = 10;
	private static final int I_BAR_X = 250;
	private static final int I_BAR_Y = 425;
	private Image playerlifefull;
	private Image playerlifehalf;
	private Image playerlifeempty;
	private Image playeritems;
	private StateBasedGame sbg;
		
	public Hud(){

	}

	  public void init(GameContainer gc) throws SlickException {
		    /*this.playerlifefull = new Image("ressources/hud/ui_heart_full.png");
		    this.playerlifehalf = new Image("ressources/hud/ui_heart_half.png");
		    this.playerlifeempty = new Image("ressources/hud/ui_heart_empty.png");
		    this.playeritems = new Image("ressources/hud/ui_items.png");*/
	  } 
	  
	  public void render(Graphics g) {
		  //annule la caméra car hud fixe
		  g.resetTransform();		 
		  /*g.drawImage(this.playerlifefull, P_BAR_X, P_BAR_Y);
		  g.drawImage(this.playerlifeempty, P_BAR_X+40, P_BAR_Y);
		  g.drawImage(this.playerlifeempty, P_BAR_X+80, P_BAR_Y);
		  g.drawImage(this.playeritems, I_BAR_X, I_BAR_Y);*/
	  }


	  
	  
}
