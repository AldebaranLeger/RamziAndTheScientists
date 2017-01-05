import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Hud {
	// position de la barre
	private static final int P_BAR_X = 10;
	private static final int P_BAR_Y = 10;
	private static final int I_BAR_X = 250;
	private static final int I_BAR_Y = 410;
	private Image playerlifefull;
	private Image playerlifehalf;
	private Image playerlifeempty;
	private Image playeritems;
		

	  public void init() throws SlickException {
	    this.playerlifefull = new Image("hud/ui_heart_full.png");
	    this.playerlifehalf = new Image("hud/ui_heart_half.png");
	    this.playerlifeempty = new Image("hud/ui_heart_empty.png");
	    this.playeritems = new Image("hud/ui_items.png");
	  }
	  
	  public void render(Graphics g) {
		  g.resetTransform();		 
		  g.drawImage(this.playerlifefull, P_BAR_X, P_BAR_Y);
		  g.drawImage(this.playerlifeempty, P_BAR_X+40, P_BAR_Y);
		  g.drawImage(this.playerlifeempty, P_BAR_X+80, P_BAR_Y);
		  g.drawImage(this.playeritems, I_BAR_X, I_BAR_Y);
		}
	  
	  
}
