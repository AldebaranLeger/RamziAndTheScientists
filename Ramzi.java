
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

//classe qui instancie Ramzi
public class Ramzi {
	
	private float dx =0, dy=0; //création des vecteurs de déplacements
	private float x=300, y=300;	
	private int direction;
	//private boolean moving = false;
	private Animation[] animations = new Animation[12];
	
	public Ramzi() {
		
	}
	
	public void init() throws SlickException {
		SpriteSheet spriteSkeleton = new SpriteSheet("ressources/sprites/BODY_skeleton.png", 64, 64);
		this.animations[0] = loadAnimation(spriteSkeleton, 0, 1, 0);
		this.animations[1] = loadAnimation(spriteSkeleton, 0, 1, 1);
		this.animations[2] = loadAnimation(spriteSkeleton, 0, 1, 2);
		this.animations[3] = loadAnimation(spriteSkeleton, 0, 1, 3);
		
		this.animations[4] = loadAnimation(spriteSkeleton, 1, 13, 0);
		this.animations[5] = loadAnimation(spriteSkeleton, 1, 13, 1);
		this.animations[6] = loadAnimation(spriteSkeleton, 1, 13, 2);
		this.animations[7] = loadAnimation(spriteSkeleton, 1, 13, 3);
	
		
	}
	
	/**
	 * @param spriteSheet
	 * @param startX : colonne de départ
	 * @param endX : colonne après la dernière colonne affichée
	 * @param y : la ligne 
	 * @return 
	 */
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}
	
	public void render(Graphics g) throws SlickException  {
		g.setColor(new Color(0,0,0, 0.5f));
		g.fillOval(x - 16, y -8, 32, 16);
		g.drawAnimation(animations[direction + (isMoving() ? 4 : 0)], x-32, y-60);
	
		
	}
	
	public void update(int delta) throws SlickException {
		if(this.isMoving()) {
			float futurX = getFuturX(delta);
		    float futurY = getFuturY(delta);
		      this.x = futurX;
		      this.y = futurY;
			updateDirection();
			/*switch(this.direction) {
			//0.1f correspond à la vitesse et delta le temps écoulé
				case 0 : this.y -= .1f * delta ; break;
				case 1 : this.x -= .1f * delta ; break;
				case 2 : this.y += .1f * delta *10; break;
				case 3 : this.x += .1f * delta*10; break;
			}*/
		}
	}
	
	public void updateDirection() {
		if (dx > 0 && dx >= Math.abs(dy)) {
			direction = 3 ; //droite
		} else if (dx < 0 && -dx >= Math.abs(dy)) {
			direction = 1; // gauche
		} else if (dy < 0) {
			direction =0 ; //haut
		} else {
			direction = 2; //bas
		}
	}
	
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_Z : this.direction = 0; break;
		case Input.KEY_Q : this.direction = 1 ; break;
		case Input.KEY_S : this.direction = 2 ; break;
		case Input.KEY_D : this.direction = 3 ; break;
		}
	}
	
	public void keyReleased(int key, char c) {
		stopMoving();
	}

	
	public float getDx() { return dx;}
	public float getX() {return x;}
	public float getY() {return y;}
	public void setDx(int x) {this.dx = dx;}
	public float getDy() {return dy;}
	public void setDy(int y) { this.dy = dy ; }
	public int getDirection() { return direction; }
	
	private float getFuturX(int delta) {
		  return this.x + .1f * delta * this.dx;
		}

		private float getFuturY(int delta) {
		  float futurY = this.y + .1f * delta * this.dy;
		 
		  return futurY;
		}
	
	public void setDirection(int direction) {
		this.direction = direction ;
		switch(direction) {
		case 0 : dx = 0 ; dy = -1 ; break;
		case 1 : dx = -1 ; dy = 0; break;
		case 2 : dx = 0 ; dy = 1; break;
		case 3 : dx = 1 ; dy = 0 ; break;
		default : dx =0 ; dy = 0 ; break;
		}
	}
	
	public boolean isMoving() {
		return dx !=0 || dy != 0;
	}
	
	public void stopMoving() {
		dx = 0 ; dy = 0;
	}
	
	
	
}
