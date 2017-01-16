import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;


public class Souris1 extends Ennemi{

	private int maxDeplacement;
	private int compteurPas = 0;
	
	public Souris1(TiledMap m, float x, float y)
	{
		super(m, x, y);
	}
	
	public void init() throws SlickException
	{
		moving = true;
		animations = super.prepareAnimation("BODY_skeleton.png");
	}
	
	public void update(int delta) throws SlickException
	{
		
		float futurX =  getFuturX(delta, 1);
        float futurY =  getFuturY(delta, 1);
        maxDeplacement = 500; 
        
		
		boolean collision = isCollision(futurX, futurY);
		if(collision){
			setRandomDirection();
		} else {
			this.x = futurX;
			this.y = futurY;
			compteurPas++;
			if(compteurPas == maxDeplacement)
			{
				setRandomDirection();
				compteurPas = 0;
				maxDeplacement = (int) (Math.random()*(500-100)+100);
			}
		}
	}
}
