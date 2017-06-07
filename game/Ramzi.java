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


public class Ramzi{
	
	private float x = 560, y=180;
	private float dx = 0, dy = 0;
	private int direction, directionAtk;
	private Animation[] animations = new Animation[8];
	private Animation[] animationsAttaque = new Animation[6];
	private TiledMap map;
	public static WorldMap wp;
	private int hp, maxHp;
	private boolean alive = true;
	private int atkToken = 0;
	private int atkTimer = 0;
	private boolean isAtking = false;
	private float mouseX, mouseY;
	private int atkStt = 0;
	
	private Polygon poly0, poly1, poly2, poly3;
	
	
	public Ramzi(TiledMap map, WorldMap wp) {
		this.map = map;
		this.wp = wp;
		this.maxHp = 12;
		this.hp= this.maxHp;
	}
	
	public void init() throws SlickException {
		SpriteSheet spriteSouris = new SpriteSheet("ressources/sprites/Sprites_Hero.png", 64, 64);
		this.direction = 2;
		this.animations[0] = loadAnimation(spriteSouris, 0, 1, 0);
		this.animations[1] = loadAnimation(spriteSouris, 0, 1, 1);
		this.animations[2] = loadAnimation(spriteSouris, 0, 1, 2);
		this.animations[3] = loadAnimation(spriteSouris, 0, 1, 3);
		this.animations[4] = loadAnimation(spriteSouris, 1, 8, 0);
		this.animations[5] = loadAnimation(spriteSouris, 1, 8, 1);
		this.animations[6] = loadAnimation(spriteSouris, 1, 8, 2);
		this.animations[7] = loadAnimation(spriteSouris, 1, 8, 3);

		poly0 = new Polygon(); 
		poly0.addPoint(0,0); 
		poly0.addPoint(877,0);
		poly0.addPoint(this.x-120,this.y+110);  
		//poly0.setLocation(this.x,this.y);
		
		poly1 = new Polygon(); 
		poly1.addPoint(this.x-120,this.y+110); 
		poly1.addPoint(877,0); 
		poly1.addPoint(877,600); 
		//poly1.setLocation(this.x,this.y);
		
		poly2 = new Polygon(); 
		poly2.addPoint(this.x-120,this.y+110); 
		poly2.addPoint(0,600); 
		poly2.addPoint(877,600); 
		//poly2.setLocation(this.x,this.y);
		
		poly3 = new Polygon(); 
		poly3.addPoint(0,0); 
		poly3.addPoint(this.x-120,this.y+110); 
		poly3.addPoint(0,600); 
		//poly3.setLocation(this.x,this.y);
		
		
		SpriteSheet spriteAttaque = new SpriteSheet("ressources/sprites/attaques/Ramzi/Attaques_Ramzi.png", 32, 32);

		this.animationsAttaque[0] = loadAtkAnimation(spriteAttaque, 0, 1, 0);
		this.animationsAttaque[1] = loadAtkAnimation(spriteAttaque, 1, 2, 0);
		this.animationsAttaque[2] = loadAtkAnimation(spriteAttaque, 2, 3, 0);
		this.animationsAttaque[3] = loadAtkAnimation(spriteAttaque, 3, 4, 0);
		
	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}
	
	private Animation loadAtkAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 1000);
		}
		return animation;
	}
	
	public void render(Graphics g) throws SlickException  {
		
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
		
		g.setColor(new Color(0,0,0, 0.5f));
		g.fillOval(x - 16, y -8, 32, 16); //création d'une ombre
		g.drawAnimation(animations[direction + (isMoving() ? 4 : 0)], x-33, y-50);
	}
	
	public void update(int delta) throws SlickException
	{
		//cursor = MouseInfo.getPointerInfo().getLocation() ;
		if(this.isMoving()) {
			//on calcule les futures coordonnées et si il y a une tuile à cet endroit
			// c'est qu'il y a collision et on stop le déplacement
			float futurX =  getFuturX(delta);
	        float futurY =  getFuturY(delta);
			
			//on cherche s'il y a une tuile collision à ces coordonnées
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
			    // suite de la mise à jour cf leçon 10
			  }
		}
		if(isAtking){
			if(atkTimer>=10){
				atkCAC();
				atkTimer=0;
			}
			atkTimer++;
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

	public void attack1(int mX, int mY)
	{
		if(mouseX==0){
			isAtking = true;
			mouseX = mX;
			mouseY = mY;
		}
		//System.out.println("Attaque 1 :\nPoint X : " + mX + "\nPoint Y : " + mY);
		

		
//		if(rect.contains(this.player.getX(), this.player.getY()))
//		{
//			this.player.takeDamage(3);
//		}
		
	}

	public void atkCAC(){
		switch(atkToken)
		{
		case 0 : 
			if(poly0.contains(mouseX, mouseY))
			{
				directionAtk = 0;
			} else if(poly1.contains(mouseX, mouseY))
			{
				directionAtk = 1;
			} else if(poly2.contains(mouseX, mouseY))
			{
				directionAtk = 2;
			} else if(poly3.contains(mouseX, mouseY))
			{
				directionAtk= 3;
			}
			
			Rectangle rect = new Rectangle();
			switch(this.directionAtk)
			{
			case 0 :
				rect.setSize(80, 80);
				rect.setLocation((int)this.x - 40,(int)this.y- 80);
				break;
			case 1 :
				rect.setSize(80, 80);
				rect.setLocation((int)this.x,(int)this.y- 40);
				break;
			case 2 :
				rect.setSize(80, 80);
				rect.setLocation((int)this.x- 40 ,(int)this.y);
				break;
			case 3 :
				rect.setSize(80, 80);
				rect.setLocation((int)this.x- 80 ,(int)this.y- 40 );
				break;
			}
			if(WorldMap.tabEnnemi!=null){
				for(int i = 0; i<WorldMap.nbEnnemisDebut;i++){
					if(WorldMap.tabEnnemi[i]!=null){
						if(rect.contains(WorldMap.tabEnnemi[i].getX(), WorldMap.tabEnnemi[i].getY()))
						{
							WorldMap.tabEnnemi[i].takeDamage(3);
						}
					}
				}
			} else {
				if(rect.contains(WorldMap.madMouse.getX(), WorldMap.madMouse.getY()))
				{
					WorldMap.madMouse.takeDamage(3);
				}
			}
			atkToken=1;
			break;
		case 1 :
			rect=null;
			atkToken=0;
			isAtking = false;
			mouseX=0;
			mouseY=0;
			break;
		}
		
	}
	
	public void attackADistance(int mX, int mY)
	{
//		System.out.println("Ramzi X :"+this.x+" Ramzi Y : "+this.y);
//		System.out.println("Ramzi X int :"+(int)this.x+" Ramzi Y int : "+(int)this.y);
		wp.createProjectile(mX, mY);
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
			  // ajouter la mise à jour de dx et dy en fonction de la direction
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

	public void takeDamage(int dmg) 
	{
		this.hp -= dmg;
		if(this.hp<=0){
			this.dead();
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
	/**return les hp de ramzi après les dégats*/
	public int getCurrentHp()
	{
		return this.hp;
	}
	
	

}
