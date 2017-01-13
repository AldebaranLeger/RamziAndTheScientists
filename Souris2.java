import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Souris2 extends Ennemi {

	public Souris2(TiledMap map, Ramzi p, float x, float y)
	{
		super(map, p, x, y);
	}

	public void init() throws SlickException
	{
		animations = super.prepareAnimation("BODY_skeleton.png");
	}

	public void update(int delta) throws SlickException
	{
		if(!moving)
			distanceVue = 100; // La souris va voir le joueur à 100 px ...
		else
			distanceVue = 200; // ... et le perdera de vue à 200 px
		float futurX = getFuturX(delta);
		float futurY = getFuturY(delta);
		float[] vueSouris = super.getVueEnnemi(distanceVue);	
	
		if(moving)
		{
			if (super.isCollision(futurX, futurY))
			{
				moving = false;
			}
			else
			{
				super.suivrePlayer();
				this.x = futurX;
				this.y = futurY;
			}
		}
		if ((vueSouris[0] < player.getX() && player.getX() < vueSouris[1])
				&& (vueSouris[2] < player.getY() && player.getY() < vueSouris[3]) )
		{
			
			moving = true;
			super.suivrePlayer();
//			this.x = futurX;
//			this.y = futurY;
			
		}
		else
		{
			moving = false;
		}
	}
}