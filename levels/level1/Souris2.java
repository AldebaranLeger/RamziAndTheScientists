package levels.level1;
import game.*;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Souris2 extends Ennemi {

	double vitesseDeplacement;
	private Ramzi player;
	private int tailleHitbox;
	
	public Souris2(TiledMap map, Ramzi p, float x, float y) {
		super(map, x, y);
		this.player = p;
		vitesseDeplacement = 2;
		super.var = 2;
		super.ptVie = 4;
		super.littleMouseDirect = (int) (Math.random()*(3-1)+1);
		
	}

	public void init() throws SlickException {
		animations = super.prepareAnimation("ramzi.png");
		dyingSmoke = super.prepareSmokeAnimation();
		littleMouse = super.prepareLittleMouseAnimation();
	}

	public void update(int delta) throws SlickException
	{
		
		float futurX = getFuturX(delta, 0.5);
		float futurY = getFuturY(delta, 0.5);
		float[] vueSouris = super.getVueEnnemi(distanceVue);
		if (!moving)
			distanceVue = 100; // La souris va voir le joueur � 100 (px?) ...
		else {
			distanceVue = 250; // ... et le perdera de vue � 250 px
		}


		if ((vueSouris[0] < player.getX() && player.getX() < vueSouris[1])
				&& (vueSouris[2] < player.getY() && player.getY() < vueSouris[3])) // si player dans le rayon de la souris
		{
			if(!super.isSaved())
			moving = true;
		}
		else
			moving = false;

		if (moving) {

			if (!super.isCollision(futurX, futurY)) {
				super.suivrePlayer(player, vitesseDeplacement, delta, false);
				// this.x = futurX;
				// this.y = futurY;
			} else{
				super.suivrePlayer(player, vitesseDeplacement, delta, true);
			}
		}
		
		super.calcHitBox();
	}
}