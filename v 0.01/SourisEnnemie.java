import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;


public class SourisEnnemie {
	
	private float x = 560, y=180;
	private float dx = 0, dy = 0;
	private int direction;
	private Animation[] animations = new Animation[8];
	private TiledMap map;
	
	public SourisEnnemie(TiledMap map) {
		this.map = map;
	}
	
	public void init() throws SlickException {
		SpriteSheet spriteSouris = new SpriteSheet("ressources/sprites/characters.png", 64, 64);
		this.animations[0] = loadAnimation(spriteSouris, 0, 1, 0);
		this.animations[1] = loadAnimation(spriteSouris, 0, 1, 1);
		this.animations[2] = loadAnimation(spriteSouris, 0, 1, 2);
		this.animations[3] = loadAnimation(spriteSouris, 0, 1, 3);
		this.animations[4] = loadAnimation(spriteSouris, 1, 9, 0);
		this.animations[5] = loadAnimation(spriteSouris, 1, 9, 1);
		this.animations[6] = loadAnimation(spriteSouris, 1, 9, 2);
		this.animations[7] = loadAnimation(spriteSouris, 1, 9, 3);
	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}
	
	public void render(Graphics g) throws SlickException  {
		g.setColor(new Color(0,0,0, 0.5f));
		g.fillOval(x - 16, y -8, 32, 16); //création d'une ombre
		g.drawAnimation(animations[direction + (isMoving() ? 4 : 0)], x-32, y-60);
	
	}
	
	public void update(int delta) throws SlickException {
		if(this.isMoving()) {
			//on calcule les futures coordonnées et si il y a une tuile à cet endroit
			// c'est qu'il y a collision et on stop le déplacement
			float futurX =  getFuturX(delta);
	        float futurY =  getFuturY(delta);
			
			//on cherche s'il y a une tuile collision à ces coordonnées
			boolean collision = isCollision(futurX, futurY);
			if(collision) {
				this.stopMoving();
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
	}
	
	private void updateDirection() {
		if (dx > 0 && dx >= Math.abs(dy)) { 
		    direction = 3;
		  } else if (dx < 0 && -dx >= Math.abs(dy)) {
		    direction = 1;
		  } else if (dy < 0) { 
		    direction = 0;
		  } else { 
		    direction = 2;
		  } 
		
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
	
	public void setDx(float dx){ this.dx = dx; };
	
	public void setDy(float dy){ this.dy = dy; };
	
	public int getDirection() { return direction; }
	
	public void setDirection(int direction) { 
		this.direction = direction; 
		  switch (direction) {
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
	

}
