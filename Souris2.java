import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Souris2 extends Ennemi {

	double vitesseDeplacement;
	private Ramzi player;
	private int tailleHitbox;
	
	public Souris2(TiledMap map, Ramzi p, float x, float y) {
		super(map, x, y);
		this.player = p;
		vitesseDeplacement = 0.1;
		super.var = 2;
	}

	public void init() throws SlickException {
		animations = super.prepareAnimation("BODY_skeleton.png");
	}

	public void update(int delta) throws SlickException
	{
		
		float futurX = getFuturX(delta, 1.5);
		float futurY = getFuturY(delta, 1.5);
		float[] vueSouris = super.getVueEnnemi(distanceVue);
		if (!moving)
			distanceVue = 100; // La souris va voir le joueur à 100 (px?) ...
		else {
			distanceVue = 250; // ... et le perdera de vue à 250 px
		}


		if ((vueSouris[0] < player.getX() && player.getX() < vueSouris[1])
				&& (vueSouris[2] < player.getY() && player.getY() < vueSouris[3])) // si player dans le rayon de la souris
		{
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
	}
}