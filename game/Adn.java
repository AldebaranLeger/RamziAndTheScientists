package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;

public class Adn
{
	private Animation[] animations = new Animation[1];
	private WorldMap worldMap;
	private Ramzi player;
	private float xHeart, yHeart;
	private int indexInHeratArray;
	private Circle zoneCollision;
	
	public Adn(WorldMap worldMap, Ramzi player, float xHeart, float yHeart, int i)
	{
		this.worldMap = worldMap;
		this.player = player;
		this.xHeart = xHeart;
		this.yHeart = yHeart;
		this.indexInHeratArray = i;
	}
	
	public Animation[] prepareAnimation() throws SlickException {

		SpriteSheet spriteMadMouse = new SpriteSheet("ressources/items/ADN.png", 32, 32);
		this.animations[0] = loadAnimation(spriteMadMouse, 0, 1, 0);

		return animations;
	}

	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 250);
		}
		return animation;
	}

	
	public void init() throws SlickException
	{
		prepareAnimation();
		calcZoneCollision();
	}
	
	public void render(Graphics g) throws SlickException
	{
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillOval(xHeart - 8, yHeart - 4, 16, 8); // création d'une ombre
		g.drawAnimation(animations[0], xHeart - 16, yHeart - 24);
	}
	
	public void update(int delta) throws SlickException
	{
		int nbpointGagne = 1;
		if(player.calcZoneCollision().intersects(this.calcZoneCollision()))
		{
			if(player.getCurrentHp() + nbpointGagne <= player.getHp())
			{
				player.gagnerVie(nbpointGagne);
				worldMap.destroyHeart(this.indexInHeratArray);
			}
		}

	}
	
	public Circle calcZoneCollision()
	{
		zoneCollision = new Circle(this.xHeart, this.yHeart, 8);
		return zoneCollision;

	}
}
