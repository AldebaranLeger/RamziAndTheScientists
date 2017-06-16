import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;

public class AttaqueBunNysterio
{
	private int bunNysterioDirection;
	private Ramzi player;
	private float xPic, yPic;
	private Animation[] animationPics = new Animation[4];
	private Circle zoneCollision;
	
	public AttaqueBunNysterio(Ramzi player, float xPic, float yPic, int direction)
	{
		this.player = player;
		this.xPic = xPic;
		this.yPic = yPic;
		this.bunNysterioDirection= direction;
		try {
			preparePicAnimation();
		} catch (SlickException e) {}
	}
	
	public Animation[] preparePicAnimation() throws SlickException {
		SpriteSheet spriteEarthQuake = new SpriteSheet("ressources/sprites/spriteEarthquake.png", 64, 64);
		this.animationPics[1] = loadAnimation(spriteEarthQuake, 0, 3, 0);
		this.animationPics[3] = loadAnimation(spriteEarthQuake, 0, 3, 1);
		this.animationPics[0] = loadAnimation(spriteEarthQuake, 0, 3, 2);
		this.animationPics[2] = loadAnimation(spriteEarthQuake, 0, 3, 3);
		this.animationPics[0].setLooping(false);
		this.animationPics[1].setLooping(false);
		this.animationPics[2].setLooping(false);
		this.animationPics[3].setLooping(false);
		return animationPics;
	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX; x < endX; x++) 
			animation.addFrame(spriteSheet.getSprite(x, y), 150);
		
		return animation;
	}

	public void render(Graphics g) throws SlickException
	{
		Graphics cerclePic = new Graphics();
		cerclePic.fillOval(this.xPic+16, this.yPic+32, 32, 32);
		cerclePic.setColor(Color.blue);
		g.drawAnimation(animationPics[bunNysterioDirection], xPic, yPic);
	}
	
	public void update(int delta) throws SlickException
	{
		if(this.calcZoneCollision().intersects(player.calcZoneCollision())){
			player.takeDamage(3);
		}
	}
	
	public Circle calcZoneCollision()
	{
		zoneCollision = new Circle(this.xPic +16, this.yPic+32, 32);
		return zoneCollision;
	}
}
