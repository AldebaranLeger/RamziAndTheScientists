import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;


public class Souris1 extends Ennemi{

	private int maxDeplacement;
	private int compteurPas = 0;
	 
	public Souris1(WorldMap worldmap, TiledMap map, Ramzi player, float x, float y, int idEnnemi)
	{
		super(worldmap, map, player, x, y, idEnnemi);
		super.ptVie = 2;
		super.littleMouseDirection = (int) (Math.random()*(3-1)+1);
		
	}
	
	public void init() throws SlickException
	{
		moving = true;
		animations = super.prepareAnimation("ramzi.png");
		dyingSmoke = super.prepareSmokeAnimation();
		littleMouse = super.prepareLittleMouseAnimation();
		super.calcZoneCollision();
	}
	
	public void update(int delta) throws SlickException
	{		
		this.changerDirection(delta);
		super.canHitRamzi();
		//super.calcHitBox();
	}
	
	private void changerDirection(int delta)
	{
		float futurX =  getFuturX(delta, 1);
        float futurY =  getFuturY(delta, 1);
        maxDeplacement = 500; 
		boolean collision = isCollision(futurX, futurY);
		if(!super.isSaved()){
			if(collision){
				setRandomDirection();
			} else if(super.isCollisionPersos())
			{
				super.knockBack(super.whereIsCollisionPerso);
			}
			else {
				this.walk(futurX, futurY);
			}
		}
	}
	
	private void walk(float futurX, float futurY)
	{
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
