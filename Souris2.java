import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

public class Souris2 extends Ennemi {

	double vitesseDeplacement;
	
	public Souris2(WorldMap worldmap, TiledMap map, Ramzi player, float x, float y, int idEnnemi) {
		super(worldmap, map, player, x, y, idEnnemi);
		super.player = player;
		vitesseDeplacement = (int)(Math.random()*(4-2)+2);
		super.ptVie = 4;
		super.littleMouseDirection = (int) (Math.random()*(3-1)+1);
	}

	public void init() throws SlickException {
		animations = super.prepareAnimation("ramzi.png");
		dyingSmoke = super.prepareSmokeAnimation();
		littleMouse = super.prepareLittleMouseAnimation();
		super.setRandomDirection();
		super.calcZoneCollision();
	}

	public void update(int delta) throws SlickException
	{
		
		float futurX = getFuturX(delta, 0.5);
		float futurY = getFuturY(delta, 0.5);
		float[] vueSouris = super.getVueEnnemi(distanceVue);

		distanceVue = getDistanceVue();

		this.shouldMove(vueSouris);

		this.shouldAvoidCollision(delta, futurX, futurY);

		super.canHitRamzi();
		//super.calcHitBox();
	}
	
	private int getDistanceVue()
	{
		if (!moving)
			return 100; // La souris va voir le joueur à 100 (px?) ...
		else {
			return 250; // ... et le perdera de vue à 250 px
		}
	}
	
	private void shouldMove(float [] vueSouris)
	{
		if ((vueSouris[0] < player.getX() && player.getX() < vueSouris[1])
				&& (vueSouris[2] < player.getY() && player.getY() < vueSouris[3])) // si player dans le rayon de la souris
		{
			if(!super.isSaved()){
				moving = true;	
			}
		}
		else {
			moving = false;
		}
	}
	
	private void shouldAvoidCollision(int delta, float futurX, float futurY)
	{
		if (moving) {
			if (!super.isCollision(futurX, futurY)) {
				super.suivrePlayer(player, vitesseDeplacement, delta, false);
			} else{
				super.suivrePlayer(player, vitesseDeplacement, delta, true);
			}
		}
		
		//super.calcHitBox();
	}
}