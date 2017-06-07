import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Options extends BasicGameState {

	private int id;
	private GameContainer gc;
	private StateBasedGame sbg;
	private Image back;
	
	public Options(int id) {
		this.id = id;
	}
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.gc = gc;
		this.sbg = sbg;
		back = new Image("ressources/background/back.jpg");
	}

	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		//g.setColor(Color.black);
		//g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		back.draw(0, 0, gc.getWidth(), gc.getHeight());
		g.setColor(Color.white);
		g.drawString("CREDITS", 680, 100);
		g.drawString("Thanh-Thao HOANG", 650, 150);
		g.drawString("Erwan LE CORVIC", 650, 180);
		g.drawString("Valentin LAMPRIERE", 650, 210);
		g.drawString("Aldébaran LEGER", 650, 240);
		g.drawString("Romain NEGRI", 650, 270);
		g.drawString("RETOUR AU MENU PRINCIPAL (R)", 400, 400);
	}

	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {}
	
	public void keyReleased(int key, char c) {
		if(Input.KEY_R == key) {
			sbg.enterState(WindowGame.STARTMENU);
		}
	}
	
	public int getID() {
		return id;
	}

}
