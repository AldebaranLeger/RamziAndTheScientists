package game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

//implémente la boucle infinie
public class WindowGame extends StateBasedGame {	
	
	//chaque state doit avoir un id
	public static final int STARTMENU = 0;
	public static final int WORLDMAP = 1; 
	public static final int OPTIONS = 2;
	private static AppGameContainer app;
	private boolean difficulte;
	private Options options;
	private WorldMap worldMap;
	
	public WindowGame()
	{
		super("Ramzi & The Scientists");
		this.addState(new Menu(STARTMENU));
		System.out.println(difficulte);
		this.worldMap = new WorldMap(WORLDMAP);
		this.addState(worldMap);
		this.options = new Options(this, OPTIONS);
		this.addState(options);		
	}
	
	/**
	 * Initialise la liste des boucles d'affichage dont on a besoin
	 */
	public void initStatesList(GameContainer container) throws SlickException {
		this.getState(STARTMENU).init(container, this);
		this.getState(WORLDMAP).init(container, this);
		//au lancement du jeu, on arrive sur le menu
		this.enterState(STARTMENU);
	}

	 public static void main(String[] args) throws SlickException {
		 try {
			app = new AppGameContainer(new WindowGame(), 877, 600, false);
			app.setTargetFrameRate(30);
			app.start();

		 }catch (SlickException e){
			 e.printStackTrace();
		 }
	 }

	public void setDifficulte(boolean difficulte) {
		this.difficulte = difficulte;
		worldMap.setDifficulte(difficulte);
	}

}
