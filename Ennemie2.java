import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;


public class Ennemie2 {
	
	private float x, y;
	private int direction = 2;
	private boolean moving = false;
	private Animation[] animations = new Animation[8];
	private TiledMap map;
	private SourisEnnemie player;
	
	public Ennemie2(TiledMap map, SourisEnnemie p, float x, float y)
	{
		this.map = map;
		this.player = p;
		this.x = x;
		this.y = y;
	}
	
	public void init() throws SlickException {
		SpriteSheet spriteSouris = new SpriteSheet("ressources/sprites/BODY_skeleton.png", 64, 64);
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
		g.drawAnimation(animations[direction + (moving ? 4 : 0)], x-32, y-60);
	
	}
	
	public void update(int delta) throws SlickException
	{
		
		float futurX =  getFuturX(delta);
        float futurY =  getFuturY(delta);
        float gX, gY;

		boolean collision = isCollision(futurX, futurY);
		
		if(player.getX() > this.x)
			gX = player.getX() - this.x;
		else
			gX = this.x - player.getX();

		if(player.getY() > this.y)
			gY = player.getY() - this.y;
		else
			gY = this.y - player.getY();		
		
		//System.out.println("gX = " + gX + " - gY = " + gY);
		float distVueSouris = 100;		
		float vueSourisXMin = (this.x - distVueSouris);
		float vueSourisXMax = (this.x + distVueSouris);
		float vueSourisYMin = (this.y - distVueSouris);
		float vueSourisYMax = (this.y + distVueSouris);
		
		if((vueSourisXMin < player.getX() && player.getX() < vueSourisXMax) && (vueSourisYMin < player.getY() && player.getY() < vueSourisYMax) )
			moving = true;
		if(moving)
		{
			if(collision)
			{
				moving = false;
			} else {
				this.x = futurX;
				this.y = futurY;
				
				if(gX > gY)
					suivreEnX();
				else
					suivreEnY();
			}		
		}
	}
	
	public void suivreEnX()
	{
		if(player.getX() > this.x)
			changeDirection(3);
		if(player.getX() < this.x)
			changeDirection(1);
	}
	public void suivreEnY()
	{
		if(player.getY() > this.y)
			changeDirection(2);
		if(player.getY() < this.y)
			changeDirection(0);
	}
	
	public void changeDirection(int d)
	{
		if(d == -1)
			this.direction = (int) (Math.random()*4);
		else
			this.direction = d;
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
		  float futurX = this.x;
		  switch (this.direction) {
		  case 1: futurX = this.x - .1f * delta; break;
		  case 3: futurX = this.x + .1f * delta; break;
		  }
		  return futurX;
		}

		private float getFuturY(int delta) {
		  float futurY = this.y;
		  switch (this.direction) {
		  case 0: futurY = this.y - .1f * delta; break;
		  case 2: futurY = this.y + .1f * delta; break;
		  }
		  return futurY;
		}
	
	public float getX() {return x;}
	public float getY() {return y;}
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	public int getDirection() { return direction; }
	public void setDirection(int direction) { this.direction = direction; }
	public boolean isMoving() { return moving; }
	public void setMoving(boolean moving) { this.moving = moving; }

}
