package game;
import org.newdawn.slick.Color;
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
	
	private static final int SPACE = 430;
	private StateBasedGame stateBasedGame;
	private GameContainer container;
	private Image background, btnPlay, btnOptions, btnExit;
	//bouton Play et Exit
	private MouseOverArea play, options, exit;
	private int id;
	private WorldMap game;
	private int newGame = 0;
	
	public Menu(int id) {
		this.id = id;
		this.game=null;
	}

	public void init(GameContainer gc, StateBasedGame stateBasedGame) throws SlickException {
		this.stateBasedGame = stateBasedGame;
		this.container = gc;
		background = new Image("ressources/background/Background_menu.png");
		System.out.println(background.getHeight());
		btnPlay = new Image("ressources/boutons/Bouton_Jouer.png");
		btnOptions = new Image("ressources/boutons/Bouton_Option.png");
		btnExit = new Image("ressources/boutons/Bouton_Quitter.png");
		play = new MouseOverArea(gc, btnPlay, SPACE, gc.getHeight() - btnPlay.getHeight()*3-80, this);
		options = new MouseOverArea(gc, btnOptions, SPACE+120, gc.getHeight() - btnOptions.getHeight()*2-50, this);
		exit = new MouseOverArea(gc, btnExit, SPACE+230, gc.getHeight() - btnExit.getHeight()*2-50, this);
	}
	
	public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
		background.draw(0, 0, gc.getWidth(), gc.getHeight());
		btnPlay.setRotation(-35);
		btnOptions.setRotation(-55);
		btnExit.setRotation(-50);
		play.render(gc, g);
		play.setMouseOverColor(Color.gray);
		options.render(gc, g);
		options.setMouseOverColor(Color.gray);
		exit.render(gc, g);
		exit.setMouseOverColor(Color.gray);
	}
	
	public void update(GameContainer gc, StateBasedGame stateBasedGame, int delta) throws SlickException {
		if(newGame == 1){
			newGame = 0;
			stateBasedGame.getState(WindowGame.WORLDMAP).init(container, stateBasedGame);
			stateBasedGame.enterState(WindowGame.WORLDMAP);
		}
	}
	
	 /**
	   * L'identifiant permet d'identifier les diff�rentes boucles.
	   * Pour passer de l'une � l'autre.
	   */
	public int getID() {
		return id;
	}
	
	public void componentActivated(AbstractComponent source) {
		if(source == play) {
			game = null;
			game = new WorldMap(WindowGame.WORLDMAP);
			try {
				newGame = 1;
				update(container, stateBasedGame, 1);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			
		}else if(source==options){
			stateBasedGame.enterState(WindowGame.OPTIONS);
		}else if (source == exit) {
			container.exit();
		}
	}

}
