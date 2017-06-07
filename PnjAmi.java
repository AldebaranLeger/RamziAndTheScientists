import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;


public class PnjAmi {
	
	private float x, y;
	private int direction = 2;
	private Animation[] animations = new Animation[8];
	private TiledMap map;
	private Ramzi player;
	private float gX, gY;
	private boolean parle = false;
	
	public PnjAmi(TiledMap map, Ramzi p, float x, float y)
	{
		this.map = map;
		this.player = p;
		this.x = x;
		this.y = y;
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
		g.drawAnimation(animations[direction], x-32, y-60);
	
	}
	
	public void update(int delta) throws SlickException
	{

		
		float distVueSouris = 50;		
		float vueSourisXMin = (this.x - distVueSouris);
		float vueSourisXMax = (this.x + distVueSouris);
		float vueSourisYMin = (this.y - distVueSouris);
		float vueSourisYMax = (this.y + distVueSouris);
		
		if(player.getX() > this.x)
			gX = player.getX() - this.x;
		else
			gX = this.x - player.getX();
		if(player.getY() > this.y)
			gY = player.getY() - this.y;
		else
			gY = this.y - player.getY();
		
		if((vueSourisXMin < player.getX() && player.getX() < vueSourisXMax) && (vueSourisYMin < player.getY() && player.getY() < vueSourisYMax) )
		{
			suivrePlayer(gX, gY);
		}
		parle = false;
	}
	
	public void suivrePlayer(float gX, float gY)
	{
		if(gX > gY)
		{
			if(player.getX() > this.x)
				setDirection(3);
			if(player.getX() < this.x)
				setDirection(1);
		}
		else
		{
			if(player.getY() > this.y)
				setDirection(2);
			if(player.getY() < this.y)
				setDirection(0);
		}
		if(parle)
			parle();
	}
	
	public void parle()
	{
		System.out.println("PNJ : \"Ramène moi de l'ADN !\"");
	}

	public void setParle() {parle = true;}
	public float getX() {return x;}
	public float getY() {return y;}
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	public int getDirection() { return direction; }
	public void setDirection(int direction) { this.direction = direction; }
	public void setRandomDirection(){this.direction = (int) (Math.random()*4);}

}
