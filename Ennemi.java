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
	protected Ramzi player;
	protected int distanceVue;


	public Ennemi(TiledMap m, Ramzi p, float x, float y) {
		this.map = m;
		this.player = p;
		this.x = x;
		this.y = y;
	}
	public Ennemi(TiledMap m, float x, float y) {
		this.map = m;
		this.x = x;
		this.y = y;
	}

	public Animation[] prepareAnimation(String srcSprite) throws SlickException
	{
		
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
		for (int x = startX; x < endX; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}

	public void render(Graphics g) throws SlickException {
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillOval(x - 16, y - 8, 32, 16); // création d'une ombre
		g.drawAnimation(animations[direction + (moving ? 4 : 0)], x - 32, y - 60);

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

	protected float getFuturX(int delta) {
		float futurX = this.x;
		switch (this.direction) {
		case 1:
			futurX = (float) (this.x - .1f * delta * 1.5);
			break;
		case 3:
			futurX = (float) (this.x + .1f * delta * 1.5);
			break;
		}
		return futurX;
	}

	protected float getFuturY(int delta) {
		float futurY = this.y;
		switch (this.direction) {
		case 0:
			futurY = (float) (this.y - .1f * delta * 1.5);
			break;
		case 2:
			futurY = (float) (this.y + .1f * delta * 1.5);
			break;
		}
		return futurY;
	}
 
	public float[] getVueEnnemi(int dist)
	{
		float[] aireVue = new float[4];
		this.distanceVue = dist;

		aireVue[0] = (this.x - distanceVue);
		aireVue[1] = (this.x + distanceVue);
		aireVue[2] = (this.y - distanceVue);
		aireVue[3] = (this.y + distanceVue);
		
		return aireVue;
			
	}
	
	public void suivrePlayer() {

		float gX, gY;

		if (player.getX() > this.x)
			gX = player.getX() - this.x;
		else
			gX = this.x - player.getX();

		if (player.getY() > this.y)
			gY = player.getY() - this.y;
		else
			gY = this.y - player.getY();
		
		if (gX > gY)
		{
			if (player.getX() > this.x)
				setDirection(3);
			if (player.getX() < this.x)
				setDirection(1);
		}
		else
		{
			if (player.getY() > this.y)
				setDirection(2);
			if (player.getY() < this.y)
				setDirection(0);
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
