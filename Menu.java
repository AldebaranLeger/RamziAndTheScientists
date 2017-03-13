import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState implements ComponentListener {
	
	private static final int SPACE = 320;
	private StateBasedGame sbg;
	private GameContainer container;
	private Image background, btnPlay, btnExit;
	//bouton Play et Exit
	private MouseOverArea play, exit;
	
	public Menu() {
		
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.sbg = sbg;
		this.container = gc;
		background = new Image("ressources/background/menuBack.png");
		btnPlay = new Image("ressources/boutons/btnPlay.png");
		btnExit = new Image("ressources/boutons/btnExit.png");
		play = new MouseOverArea(gc, btnPlay, SPACE, gc.getHeight() - btnPlay.getHeight()*9, this);
		exit = new MouseOverArea(gc, btnExit, SPACE, gc.getHeight() - btnPlay.getHeight() *7, this);
	}

	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw(0, 0, gc.getWidth(), gc.getHeight());
		play.render(gc, g);
		exit.render(gc, g);
	}

	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	}
	
	 public void keyReleased(int key, char c) {
		 
	 }
	 
	 /**
	   * L'identifiant permet d'identifier les différentes boucles.
	   * Pour passer de l'une à l'autre.
	   */
	public int getID() {
		return 0;
	}

	
	public void componentActivated(AbstractComponent source) {
		if(source == play) {
			sbg.enterState(1);
		} else if (source == exit) {
			container.exit();
		}
	}

}
