package game;
import java.awt.Rectangle;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.tiled.TiledMap;

import levels.Level;


public class Ramzi{
	
	private float x = 560, y=180;
	private float dx = 0, dy = 0;
	private int direction, directionAttaque;
	private Animation[] animations = new Animation[8];
	private Animation[] animationsAttaque = new Animation[6];
	private TiledMap map;
	public static WorldMap worldMap;
	private int hp, maxHp;
	private boolean alive = true;
	private int atkToken = 0;
	private int atkTimer = 0;
	private boolean isAtking = false;
	private float mouseX, mouseY;
	private int immuniteCooldown = 0;
	
	private Polygon polygonTop, polygonRight, polygonBottom, polygonLeft;
	
	
	public Ramzi(TiledMap map, WorldMap worldMap) {
		this.map = map;
		Ramzi.worldMap = worldMap;
		this.maxHp = 12;
		this.hp = this.maxHp;
	}
	
	public Animation[] prepareAnimation() throws SlickException {

		SpriteSheet spriteRamzi = new SpriteSheet("ressources/sprites/Sprites_Ramzi.png", 64, 64);
		this.animations[0] = loadAnimation(spriteRamzi, 0, 1, 0);
		this.animations[1] = loadAnimation(spriteRamzi, 0, 1, 1);
		this.animations[2] = loadAnimation(spriteRamzi, 0, 1, 2);
		this.animations[3] = loadAnimation(spriteRamzi, 0, 1, 3);
		this.animations[4] = loadAnimation(spriteRamzi, 1, 8, 0);
		this.animations[5] = loadAnimation(spriteRamzi, 1, 8, 1);
		this.animations[6] = loadAnimation(spriteRamzi, 1, 8, 2);
		this.animations[7] = loadAnimation(spriteRamzi, 1, 8, 3);

		return animations;
	}
	
	public Animation[] prepareAnimationAttaque() throws SlickException {

		SpriteSheet spriteAttaque = new SpriteSheet("ressources/sprites/attaques/Ramzi/Attaques_Ramzi.png", 32, 32);
		this.animationsAttaque[0] = loadAttaqueAnimation(spriteAttaque, 0, 1, 0);
		this.animationsAttaque[1] = loadAttaqueAnimation(spriteAttaque, 1, 2, 0);
		this.animationsAttaque[2] = loadAttaqueAnimation(spriteAttaque, 2, 3, 0);
		this.animationsAttaque[3] = loadAttaqueAnimation(spriteAttaque, 3, 4, 0);

		return animations;
	}
		
	private void preparePolygons()
	{
		polygonTop = new Polygon(); 
		polygonTop.addPoint(0,0); 
		polygonTop.addPoint(877,0);
		polygonTop.addPoint(this.x-120,this.y+110);  
		//polygonTop.setLocation(this.x,this.y);
		
		polygonRight = new Polygon(); 
		polygonRight.addPoint(this.x-120,this.y+110); 
		polygonRight.addPoint(877,0); 
		polygonRight.addPoint(877,600); 
		//polygonRight.setLocation(this.x,this.y);
		
		polygonBottom = new Polygon(); 
		polygonBottom.addPoint(this.x-120,this.y+110); 
		polygonBottom.addPoint(0,600); 
		polygonBottom.addPoint(877,600); 
		//polygonBottom.setLocation(this.x,this.y);
		
		polygonLeft = new Polygon(); 
		polygonLeft.addPoint(0,0); 
		polygonLeft.addPoint(this.x-120,this.y+110); 
		polygonLeft.addPoint(0,600); 
		//polygonLeft.setLocation(this.x,this.y);
	}
		
	public void init() throws SlickException {		
		this.direction = 2;
		prepareAnimation();
		preparePolygons();		
		prepareAnimationAttaque();
		
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
	
	public void render(Graphics g) throws SlickException  {
		attaqueAnimationManagement(g);		
		g.setColor(new Color(0,0,0, 0.5f));
		g.fillOval(x - 16, y -8, 32, 16); //cr�ation d'une ombre
		g.drawAnimation(animations[direction + (isMoving() ? 4 : 0)], x-33, y-50);
	}
	
	private void attaqueAnimationManagement(Graphics g)
	{
		if(isAtking)
		{
			switch(atkTimer){
			case 1 :
				g.drawAnimation(animationsAttaque[0], x - 16, y - 48);
				break;
			case 2 :
				g.drawAnimation(animationsAttaque[1], x + 32, y - 16);
				break;
			case 3 :
				g.drawAnimation(animationsAttaque[2], x - 16, y-32);
				break;
			case 4 :
				g.drawAnimation(animationsAttaque[3], x-32, y + 16);
				break;
			}
		}
	}
	
	public void update(int delta) throws SlickException
	{
		moveManagement(delta);
		attaqueManagement();
		refreshImmunite();
	}
	
	private void attaqueManagement()
	{
		if(isAtking){
			if(atkTimer>=10){
				atkCAC();
				atkTimer=0;
			}
			atkTimer++;
		}
	}
	
	private void moveManagement(int delta)
	{
		if(this.isMoving()) {
			//on calcule les futures coordonn�es et si il y a une tuile � cet endroit
			// c'est qu'il y a collision et on stop le d�placement
			float futurX =  getFuturX(delta);
	        float futurY =  getFuturY(delta);
			
			//on cherche s'il y a une tuile collision � ces coordonn�es
			boolean collision = isCollision(futurX, futurY);
			
			
			if(collision) {
				//this.stopMoving();
			} else {
				this.x = futurX;
				this.y = futurY;
			}
			
			if (this.isMoving()) {
			    // ajouter la mise de la direction ici
			    updateDirection();
			    // suite de la mise � jour cf le�on 10
			  }
		}
	}
	
	private void updateDirection() {
		if (dx > 0 && dx >= Math.abs(dy)) { 
		    direction = 2;
		  } else if (dx < 0 && -dx >= Math.abs(dy)) {
		    direction = 1;
		  } else if (dy < 0) { 
		    direction = 3;
		  } else { 
		    direction = 0;
		  }
		
	}

	public void prepareAttaqueCAC(int mX, int mY)
	{
		if(mouseX==0){
			isAtking = true;
			mouseX = mX;
			mouseY = mY;
		}
	}

	public void atkCAC(){
		switch(atkToken)
		{
		case 0 : 
			Rectangle damageArea = createDamageArea();
			
			canHitEnnemis(damageArea);
			
			atkToken=1;
			break;
		case 1 :
			damageArea=null;
			atkToken=0;
			isAtking = false;
			mouseX=0;
			mouseY=0;
			break;
		}
		
	}
	
	private Rectangle createDamageArea()
	{
		directionAttaque = getDirectionAttaque(mouseX, mouseY);
		Rectangle damageArea = new Rectangle();
		switch(this.directionAttaque)
		{
		case 0 :
			damageArea.setSize(80, 80);
			damageArea.setLocation((int)this.x - 40,(int)this.y- 80);
			break;
		case 1 :
			damageArea.setSize(80, 80);
			damageArea.setLocation((int)this.x,(int)this.y- 40);
			break;
		case 2 :
			damageArea.setSize(80, 80);
			damageArea.setLocation((int)this.x- 40 ,(int)this.y);
			break;
		case 3 :
			damageArea.setSize(80, 80);
			damageArea.setLocation((int)this.x- 80 ,(int)this.y- 40 );
			break;
		}
		return damageArea;
	}
	
	private void canHitEnnemis(Rectangle damageArea)
	{
		if(WorldMap.tabEnnemi!=null){
			for(int i = 0; i<worldMap.getEnnemisDebut();i++){
				if(WorldMap.tabEnnemi[i]!=null){
					if(damageArea.contains(WorldMap.tabEnnemi[i].getX(), WorldMap.tabEnnemi[i].getY()))
					{
						WorldMap.tabEnnemi[i].takeDamage(3);
					}
				}
			}
		} else {
			try{
				if(damageArea.contains(worldMap.getBossLevel().getX(), worldMap.getBossLevel().getY()))
				{
					worldMap.getBossLevel().takeDamage(3);
				}
			} catch (Exception e){}
		}
	}
	
	private int getDirectionAttaque(float mouseX, float mouseY)
	{
		int directionAttaque = 0;
		if(polygonTop.contains(mouseX, mouseY))
		{
			directionAttaque = 0;
		} else if(polygonRight.contains(mouseX, mouseY))
		{
			directionAttaque = 1;
		} else if(polygonBottom.contains(mouseX, mouseY))
		{
			directionAttaque = 2;
		} else if(polygonLeft.contains(mouseX, mouseY))
		{
			directionAttaque= 3;
		}
		return directionAttaque;
	}
	
	public void attackADistance(int mX, int mY) throws SlickException
	{
		int directionProjectile = getDirectionAttaque(mX, mY);
		//worldMap.createRamziProjectile(mX, mY);
		worldMap.createRamziProjectile(directionProjectile);
	}
	
	public boolean isCollision(float x, float y) {
	    int tileW = this.map.getTileWidth();
	    int tileH = this.map.getTileHeight();
	    int collisionLayer = this.map.getLayerIndex("collision");
	    Image tile = this.map.getTileImage((int) x / tileW, (int) y / tileH, collisionLayer);
	    boolean collision = tile != null;
	    if (collision) {
	      Color color = tile.getColor((int) x % tileW, (int) y % tileH);
	      collision = color.getAlpha() > 0;
	    }
	    return collision;
	  }
	
	private float getFuturX(int delta) {
			return this.x + .1f * delta * this.dx *2;
		}

	private float getFuturY(int delta) {
		return this.y + .1f * delta * this.dy *2;
	}

	public float getX() {return x;}
	public float getY() {return y;}
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	public void setDx(float dx){ this.dx = dx; }
	public void setDy(float dy){ this.dy = dy; }
	//public Point getCursor() {return cursor;}
	public int getDirection() { return direction;}
	public void setDirection(int direction) { 
		this.direction = direction; 
		  switch (direction)
		  {
			  // ajouter la mise � jour de dx et dy en fonction de la direction
			  case 0: dx =  0; dy = -1; break;
			  case 1: dx = -1; dy =  0; break;
			  case 2: dx =  0; dy =  1; break;
			  case 3: dx =  1; dy =  0; break; 
			  default: dx = 0; dy =  0; break;
		  } 
		}
	
	public boolean isMoving() { 
		return dx != 0 || dy != 0;
	}
	
	public void stopMoving(){
		dx = 0; dy = 0;
	}

	private void refreshImmunite(){
		if(this.immuniteCooldown != 0){
			this.immuniteCooldown++;
			if(this.immuniteCooldown==36){
				this.immuniteCooldown = 0;
			}
		}
	}

	public void takeDamage(int dmg) 
	{
		if(this.immuniteCooldown==0){
			this.immuniteCooldown = 1;
			this.hp -= dmg;
			if(this.hp<=0){
				this.dead();
			}
		}
	}
	
	public void dead(){
		this.alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
	/**return le max hp de ramzi*/
	public int getHp()
	{
		return this.maxHp;
	}
	/**return les hp de ramzi apr�s les d�gats*/
	public int getCurrentHp()
	{
		return this.hp;
	}
	
	public int getImmuniteCooldown()
	{
		return this.immuniteCooldown;
	}
	

}
