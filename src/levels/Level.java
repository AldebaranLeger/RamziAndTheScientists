package levels;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import game.*;
import levels.level1.*;

public abstract class Level {
	
	protected WorldMap worldMap;
	protected Ramzi player;
	protected TiledMap map;
	protected MadMouse boss; //type variable à changer par la classe mère des boss
	protected List<Ennemi> tabEnnemi;
	protected int nbEnnemisDebut;
	protected int nbEnnemisSauves;
	protected int levelId;
	protected int totalEnnemisSauves;
	protected boolean bossArrives=false;	
	protected float xEnnemiSauve, yEnnemiSauve; //coordonnées de l'ennemi enregistrée lorsque ses points de contamination sont à zéro (ennemi sauvé)
	
	/*public Level(WorldMap worldMap, TiledMap map) 
	{
		this.worldMap = worldMap;
		this.map = map;
	}*/
	public Level(WorldMap worldMap, TiledMap map, Ramzi player){
		this.worldMap = worldMap;
		this.map = map;
		this.player = player;
	}
	
	public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException 
	{
		
	}
	
	public void render(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException 
	{
		
	}
	
	public void update(GameContainer container, StateBasedGame stateBasedGame, int delta) throws SlickException
	{
		
	}
	
	public void setTabEnnemi(List<Ennemi> tabEnnemi) {
		this.tabEnnemi = tabEnnemi;
	}
	
	public int getNbEnnemisDebut() {
		return this.nbEnnemisDebut;
	}
	public int getNbEnnemisSauves() {
		return this.nbEnnemisSauves;
	}
	
	public List<Ennemi> getTabEnnemi() {
		return this.tabEnnemi;
	}
	
	public TiledMap getMap() { return this.map;	}
	public int getLevelId(){return this.levelId;}
	public MadMouse getBoss() {return this.boss;}
}
