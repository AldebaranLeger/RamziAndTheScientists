package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import levels.*;

public class Adn
{
	private Animation[] animations = new Animation[1];
	private Level level;
	private Ramzi player;
	private float xHeart, yHeart;
	private int indexInHeratArray;
	private int nbpointGagne = 1;
	
	public Adn(Level level, Ramzi player, float xHeart, float yHeart, int i)
	{
		this.level = level;
		this.player = player;
		this.xHeart = xHeart;
		this.yHeart = yHeart;
		this.indexInHeratArray = i;
		
		try {
			this.prepareAnimation();
		} catch (SlickException e) {}
	}
	
	public Animation[] prepareAnimation() throws SlickException {

		SpriteSheet spriteMadMouse = new SpriteSheet("ressources/Items/ADN.png", 32, 32);
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

	public void render(Graphics g) throws SlickException
	{
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillOval(xHeart - 8, yHeart - 4, 16, 8); // création d'une ombre
		g.drawAnimation(animations[0], xHeart - 16, yHeart - 24);
	}
	
	public void update(int delta, int i) throws SlickException
	{
		if(player.calcZoneCollision().intersects(this.calcZoneCollision()))
		{
			if(player.getCurrentHp() + nbpointGagne <= player.getHp())
			{
				player.gagnerVie(nbpointGagne);
				level.destroyHeart(i);
			}
		}

	}
	
	public Circle calcZoneCollision()
	{
		return new Circle(this.xHeart, this.yHeart, 8);

	}
}
