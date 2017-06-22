package levels;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import game.Adn;
import game.Boss;
import game.Ennemi;
import game.Ramzi;
import game.WorldMap;

public abstract class Level {
	
	protected WorldMap worldMap;
	protected Ramzi player;
	protected TiledMap map;
	protected Boss boss;
	protected List<Ennemi> tabEnnemi;
	public int nbEnnemisDebut;
	protected int nbEnnemisSauves;
	protected int totalEnnemisSauves;
	protected boolean bossArrives=false;	
	protected float xLadder = 0, yLadder = 0;
	protected float xEnnemiSauve, yEnnemiSauve; //coordonnées de l'ennemi enregistrée lorsque ses points de contamination sont à zéro (ennemi sauvé)
	protected boolean youWin = false, ladderSquareVisible = false, treasureSquareVisible = false, createLadder = false, openTreasure = false;
	protected int timerLadder = 0;
	public float bossLastX, bossLastY; //dernière coordonnées du boss quand le joueur le vainc
	protected boolean inMap = false, inCollision = false, inRamziRayon;
	protected int tileW, tileH, collisionLayer, mapLayer;
	protected Animation[] animations = new Animation[1];
	protected Image closedTreasure;
	protected Rectangle ladderSquare, treasureSquare;
	protected static List<Adn> adn;
	
	/*public Level(WorldMap worldMap, TiledMap map) 
	{
		this.worldMap = worldMap;
		this.map = map;
	}*/
	public Level(WorldMap worldMap, TiledMap map, Ramzi player){
		this.worldMap = worldMap;
		this.map = map;
		this.player = player;
		adn =  new ArrayList<Adn>();
	}
	
	protected Animation[] prepareAnimationTreasure(String srcSprite) throws SlickException {
		SpriteSheet treasure = new SpriteSheet("ressources/sprites/" + srcSprite, 64, 64);
		this.animations[0] = loadAnimation(treasure, 0, 3, 0);
		this.animations[0].setLooping(false);
		return animations;
	} 
	
	protected Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX; x < endX; x++) 
			animation.addFrame(spriteSheet.getSprite(x, y), 250);		
		return animation;
	}
	
	public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException 
	{}
	
	public void render(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException 
	{}
	
	public void update(GameContainer container, StateBasedGame stateBasedGame, int delta) throws SlickException
	{}
	
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
	
	public void dropHeart(Ramzi player, float xHeart, float yHeart, int index)
	{
		adn.add(new Adn(this, player, xHeart, yHeart, index));
	}
	
	public void destroyHeart(int i)
	{
		this.adn.remove(i);
	}
	
	public TiledMap getMap() { return this.map;	}
	public Boss getBoss() {return this.boss;}
	public int getTotalEnnemisSauves(){return this.totalEnnemisSauves;}
}
