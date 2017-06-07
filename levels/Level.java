package levels;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import game.*;
import levels.level1.*;
import levels.level2.*;

public abstract class Level {
	
	private TiledMap map;
	private MadMouse boss; //type variable à changer par la classe mère des boss
	private Ennemi[] tabEnnemi;
	private int nbEnnemisDebut;
	
	public Level(TiledMap map, MadMouse boss) 
	{
		this.map = map;
		this.boss = boss;
	}
	
	public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException 
	{
		//instanciation de la map
		//ajout des ennemis en fonction des valeurs (nbEnnemisDebut)
	}
	
	public void render(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException 
	{
		
	}
	
	public void update(GameContainer container, StateBasedGame stateBasedGame, int delta) throws SlickException
	{
		
	}
	
	public int getNbEnnemisDebut() {
		return this.nbEnnemisDebut;
	}
	
	public Ennemi[] getTabEnnemi() {
		return this.tabEnnemi;
	}


}
