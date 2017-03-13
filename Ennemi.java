import java.sql.Array;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

public abstract class Ennemi {

	protected float x, y;
	protected int direction = 2;
	protected boolean moving = false;
	protected Animation[] animations = new Animation[8];
	private TiledMap map;
	protected int distanceVue;
	protected int ptVie;
	protected int var = 0;
	protected Graphics g;

	public Ennemi(TiledMap m, float x, float y) {
		this.map = m;
		this.x = x;
		this.y = y;
	}
	
	public void init() throws SlickException
	{}
	
	public void update(int delta) throws SlickException
	{}
	
	public Animation[] prepareAnimation(String srcSprite) throws SlickException {

		SpriteSheet spriteSouris = new SpriteSheet("ressources/sprites/" + srcSprite, 64, 64);
		this.animations[0] = loadAnimation(spriteSouris, 0, 1, 0);
		this.animations[1] = loadAnimation(spriteSouris, 0, 1, 1);
		this.animations[2] = loadAnimation(spriteSouris, 0, 1, 2);
		this.animations[3] = loadAnimation(spriteSouris, 0, 1, 3);
		this.animations[4] = loadAnimation(spriteSouris, 1, 9, 0);
		this.animations[5] = loadAnimation(spriteSouris, 1, 9, 1);
		this.animations[6] = loadAnimation(spriteSouris, 1, 9, 2);
		this.animations[7] = loadAnimation(spriteSouris, 1, 9, 3);

		return animations;
	}

	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX; x < endX; x++) 
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		
		return animation;
	}

	public void render(Graphics g) throws SlickException
	{
		this.g = g;
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillOval(x - 16, y - 8, 32, 16); // création d'une ombre
		g.drawAnimation(animations[direction + (moving ? 4 : 0)], x - 32, y - 60);

	}
	
	public boolean isCollision(float x, float y) 
	{
		int tileW = map.getTileWidth();
		int tileH = map.getTileHeight();
		int collisionLayer = this.map.getLayerIndex("collision");
		Image tile = this.map.getTileImage((int) x / tileW, (int) y / tileH, collisionLayer);
		boolean collision = tile != null;
		if (collision)
		{
			Color color = tile.getColor((int) x % tileW, (int) y % tileH);
			collision = color.getAlpha() > 0;
		}
		
		return collision;
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

	/*
	public float[] calcHitBox(float hitX, float hitY) {
		float[] hitBox = new float[4];
		int distHitBox;

		hitBox[0] = (this.x - hitX/2);
		hitBox[1] = (this.x + hitX/2);
		hitBox[2] = (this.y - hitY/2);
		hitBox[3] = (this.y + hitY/2);

		return hitBox;
	}*/

	public float[] getVueEnnemi(int dist) {
		float[] aireVue = new float[4];
		this.distanceVue = dist;

		aireVue[0] = (this.x - distanceVue);
		aireVue[1] = (this.x + distanceVue);
		aireVue[2] = (this.y - distanceVue);
		aireVue[3] = (this.y + distanceVue);

		return aireVue;

	}

	public void suivrePlayer(Ramzi player, double vitesse, int delta, boolean collision) {		
		float gX, gY;

		if (player.getX() > this.x)
			gX = player.getX() - this.x;
		else
			gX = this.x - player.getX();

		if (player.getY() > this.y)
			gY = player.getY() - this.y;
		else
			gY = this.y - player.getY();

		if (gX > gY) {
			if (player.getX() > this.x)
				setDirection(3);
			if (player.getX() < this.x)
				setDirection(1);
		} else {
			if (player.getY() > this.y)
				setDirection(2);
			if (player.getY() < this.y)
				setDirection(0);
		}

		float diffX = player.getX() - x;
		float diffY = player.getY() - y;
		float angle = (float) Math.atan2(diffY, diffX);
	  	if(collision){
	  		if(isCollision(this.x,this.y-1)){
				x += Math.cos(angle) * vitesse;
	  			y+=1;
	  			System.out.println("1");
	  		}
	  		if(isCollision(this.x,this.y+1)){
				x += Math.cos(angle) * vitesse;
	  			y-=1;
	  			System.out.println("2");
	  		}
	  		if(isCollision(this.x+1,this.y)){
	  			x-=1;
				y += Math.sin(angle) * vitesse;
				System.out.println("3");
	  		}
	  		if(isCollision(this.x-1,this.y)){
	  			x+=1;
				y += Math.sin(angle) * vitesse;
				System.out.println("4");
	  		}
	  		

	  	} else {
	 
			x += Math.cos(angle) * vitesse;
			y += Math.sin(angle) * vitesse;
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setRandomDirection() {
		this.direction = (int) (Math.random() * 4);
	}

	public boolean getMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}
}
