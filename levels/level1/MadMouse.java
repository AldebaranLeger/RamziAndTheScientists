package levels.level1;
import game.*;
import levels.Level;
import levels.level2.Level2;

import java.awt.Rectangle;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class MadMouse extends Boss
{
	private TiledMap map;
	private Ramzi player;
	private int direction;
	private Animation[] animations = new Animation[8];
	private Animation[] animationsAttaque = new Animation[4];
	private int action = 0;
	private int cooldownAttaqueCAC = 0;
	private int bossTimer = 0; // Timer qui permet de d�clencher les diff�rentes attaques
	private int attaqueCACTimer = 0; // Timer qui permet de d�clencher au bout d'un certain temps l'attaque C�C
	private int attaqueCACState = 0; // Etat de l'attaque au C�C
	private int prepareAttaqueState = 0;
	private Color atkClr = new Color(Color.transparent);
	Vector2f pos;
	Vector2f dir;
	private String srcMadMouse = "madMouse";
	private boolean living = true;
	private Level1 level;
	
	public MadMouse(TiledMap map, Ramzi player, float xMadMouseSpawn, float yMadMouseSpawn, Level1 level)
	{
		super(map, player, xMadMouseSpawn, yMadMouseSpawn);
		this.map = map;
		this.player = player;
		this.x = xMadMouseSpawn;
		this.y = yMadMouseSpawn;
		if(WorldMap.difficulte == true) {
			System.out.println(maxPv);
			this.maxPv = 50;
		} else {
			this.maxPv = 20;
		}
		this.ptVie = maxPv;
		this.level = level;
		super.nomBoss = "MadMouse";
	}
	
	public int getMaxPv(){
		return this.maxPv;
	}
	
	public Animation[] prepareAnimation() throws SlickException {

		SpriteSheet spriteMadMouse = new SpriteSheet("ressources/sprites/"+srcMadMouse+".png", 128, 128);
		this.animations[0] = loadAnimation(spriteMadMouse, 0, 1, 0);
		this.animations[1] = loadAnimation(spriteMadMouse, 0, 1, 1);
		this.animations[2] = loadAnimation(spriteMadMouse, 0, 1, 2);
		this.animations[3] = loadAnimation(spriteMadMouse, 0, 1, 3);
		this.animations[4] = loadAnimation(spriteMadMouse, 1, 9, 0);
		this.animations[5] = loadAnimation(spriteMadMouse, 1, 9, 1);
		this.animations[6] = loadAnimation(spriteMadMouse, 1, 9, 2);
		this.animations[7] = loadAnimation(spriteMadMouse, 1, 9, 3);

		return animations;
	}
	
	public Animation[] prepareAnimationAttaque() throws SlickException {

		SpriteSheet spriteMadMouseAttaque = new SpriteSheet("ressources/sprites/attaques/Boss/SpriteAttaqueBoss.png", 200, 500);
		this.animationsAttaque[0] = loadAttaqueAnimation(spriteMadMouseAttaque, 0, 1, 0);
		this.animationsAttaque[1] = loadAttaqueAnimation(spriteMadMouseAttaque, 1, 2, 0);
		this.animationsAttaque[2] = loadAttaqueAnimation(spriteMadMouseAttaque, 2, 3, 0);
		this.animationsAttaque[3] = loadAttaqueAnimation(spriteMadMouseAttaque, 3, 4, 0);

		return animations;
	}
	
	public void init() throws SlickException
	{
		this.direction = 2;
		prepareAnimation();
		prepareAnimationAttaque();
		
		this.action = 1; // Action : suivre le joueur

	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}

	private Animation loadAttaqueAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 1000);
		}
		return animation;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		realiserAction(delta);
		canHitRamzi();
		
		
		if(attaqueCACState!=1){
			if(cooldownAttaqueCAC==0){
				bossTimer = (int)(Math.random() * (150-50))+50;
			}
			cooldownAttaqueCAC ++ ;
			//System.out.println("cooldownAttaqueCAC : " + cooldownAttaqueCAC);
			if(cooldownAttaqueCAC>=bossTimer){
				action=(int)(Math.random() * (4-2))+2;
				cooldownAttaqueCAC=0;
			}
		}
		
		if(this.ptVie <= this.maxPv/2 && !this.enrage){
			this.enrage = true;
		}
		
	}
	
	private void realiserAction(int delta)
	{
		float futurX = getFuturX(delta, 1.5);
		float futurY = getFuturY(delta, 1.5);
		
		switch(action){
		case 0 : break;
		case 1 :
			if (isCollision(futurX, futurY)) {
				if(WorldMap.difficulte == true) {
					suivrePlayer(player, 1.5, delta, true);
				} else {
					suivrePlayer(player, 1, delta, true);
				}
			}else{
				if(WorldMap.difficulte == true) {
					suivrePlayer(player, 1.5, delta, false);
				} else {
					suivrePlayer(player, 1, delta, false);
				}				
			}
			break;
		case 2 : 
			this.prepareAttaqueCAC();
			break;
		case 3 :
			this.prepareAttaqueDistance();
			break;
		}
	}

	private void tirerFromage(){
		this.level.createMadMouseCheese(this.direction);
	}
	
	private void animateAttaqueCACbyTimer(Graphics g)
	{

		switch(attaqueCACState){
		case 0 :
			break;
		case 1 :
			switchColorState(g);
			switchDirection(g);
			break;
		}
	}
	
	private void switchColorState(Graphics g)
	{
		float rotation =0;
		switch(this.direction)
		{
			case 0 : rotation = 90;
			break;
			case 1 : rotation = 0;
			break;
			case 2 : rotation = 270;
			break;
			case 3 : rotation = 180;
			break;
		}
		switch(this.prepareAttaqueState){
		case 1 :
			this.srcMadMouse = "madMouseOrange";
			try {
				prepareAnimation();
			} catch (SlickException e1) {e1.printStackTrace();}
			//atkClr = new Color(Color.yellow);
			break;
		case 20 :
			this.srcMadMouse = "madMouseRouge";
			try {
				prepareAnimation();
			} catch (SlickException e) {e.printStackTrace();}
			//atkClr = new Color(Color.orange);
			break;
		case 40 :
			this.srcMadMouse = "madMouse";
			try {
				prepareAnimation();
			} catch (SlickException e) {e.printStackTrace();}
			atkClr = new Color(Color.transparent);
			g.rotate(x, y, rotation);
			g.drawAnimation(animationsAttaque[0], x-216, y-250);
			attaquerCAC();
			break;
		case 45 :
			g.rotate(x, y, rotation);
			g.drawAnimation(animationsAttaque[1], x-216, y-250);
			attaquerCAC();
			break;
		case 46 :
			g.rotate(x, y, rotation);
			g.drawAnimation(animationsAttaque[2], x-216, y-250);
			attaquerCAC();
			break;
		case 47 :
			g.rotate(x, y, rotation);
			g.drawAnimation(animationsAttaque[3], x-216	, y-250);
			attaquerCAC();
			break;
		case 50 :
			action = 1;
			attaqueCACTimer=0;
			attaqueCACState = 0;
			prepareAttaqueState=0;
			break;
		}
	}
	
	private void switchDirection(Graphics g)
	{
		switch(this.direction)
		{
		case 0 :
			g.setColor(atkClr);
			g.fillRect((int)x - 250 , (int)y-200, 500, 200);
			break;
		case 1 :
			g.setColor(atkClr);
			g.fillRect((int)x-200, (int)y-250, 200, 500);
			break;
		case 2 :
			g.setColor(atkClr);
			g.fillRect((int)x -250, (int)y, 500, 200);
			break;
		case 3 :
			g.setColor(atkClr);
			g.fillRect((int)x, (int)y-250, 200, 500);
			break;
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillOval(x - 32, y - 16, 64, 32); // cr�ation d'une ombre
		
		if(this.prepareAttaqueState >1 && this.prepareAttaqueState <45)
		{
			if(this.prepareAttaqueState<20)
			{
				if(this.prepareAttaqueState%3 == 1){
					this.srcMadMouse = "madMouseOrange";
					g.drawAnimation(animations[direction + (4)], x - 64, y - 120);
				}
			}else
			{
				if(this.prepareAttaqueState%2 == 1){
					this.srcMadMouse = "madMouseRouge";
					g.drawAnimation(animations[direction + (4)], x - 64, y - 120);
				}
			}
		}
		else{
			g.drawAnimation(animations[direction + (4)], x - 64, y - 120);
		}
		animateAttaqueCACbyTimer(g);
	}
	
//	public boolean isCollision(float x, float y) 
//	{
//		int tileW = map.getTileWidth();
//		int tileH = map.getTileHeight();
//		int collisionLayer = this.map.getLayerIndex("collision");
//		Image tile = this.map.getTileImage((int) x / tileW, (int) y / tileH, collisionLayer);
//		boolean collision = tile != null;
//		if (collision)
//		{
//			Color color = tile.getColor((int) x % tileW, (int) y % tileH);
//			collision = color.getAlpha() > 0;
//		}
//		
//		return collision;
//	}

	private void prepareAttaqueCAC() {
		attaqueCACTimer++;
		prepareAttaqueState++;
		attaqueCACState = 1;
		if(attaqueCACTimer>=50)
		{
			attaquerCAC();
		}
	}

	private void prepareAttaqueDistance() {
		attaqueCACTimer++;
		if(attaqueCACTimer>=20)
		{
			action=1;
			tirerFromage();
		}
	}
	
	private void attaquerCAC()
	{
		Rectangle damageArea = new Rectangle();
		switch(this.direction)
		{
		case 0 :
			damageArea.setSize(500, 200);
			damageArea.setLocation((int)x - 250, (int)y - 200);
			break;
		case 1 :
			damageArea.setSize(200, 500);
			damageArea.setLocation((int)x-200, (int)y - 250 );
			break;
		case 2 :
			damageArea.setSize(500, 200);
			damageArea.setLocation((int)x - 250, (int)y);
			break;
		case 3 :
			damageArea.setSize(200, 500);
			damageArea.setLocation((int)x, (int)y-250);
			break;
		}
		if(damageArea.contains(this.player.getX(), this.player.getY()))
		{
			this.player.takeDamage(3);
		}

	}
	
	public int getPtVie() {
		return this.ptVie;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void takeDamage(int dmg){
		this.ptVie-=dmg;
		if(this.ptVie<=0)
			this.death();
	}
	
	protected void death(){
		living=false;
	}
	
	public boolean isSaved(){
		if(!living){
			System.out.println("MadMouse est mort.");
			return true;
		} else {
			return false;
		}
	}
	
	protected float getFuturX(int delta, double vitesse)
	{

		float futurX = this.x;
		switch (this.direction) {
		case 1:
			futurX = (float) (this.x - .1f * delta * vitesse);
			break;
		case 3:
			futurX = (float) (this.x + .1f * delta * vitesse);
			break;
		}
		return futurX;
	}

	protected float getFuturY(int delta, double vitesse) {
		float futurY = this.y;
		switch (this.direction) {
		case 0:
			futurY = (float) (this.y - .1f * delta * vitesse);
			break;
		case 2:
			futurY = (float) (this.y + .1f * delta * vitesse);
			break;
		}
		return futurY;
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public boolean isEnrage(){
		return this.enrage;
	}

}

