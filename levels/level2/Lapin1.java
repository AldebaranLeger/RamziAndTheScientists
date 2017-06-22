package levels.level2;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import game.*;

/*Lapin 'charge' (immobiles puis poursuit le joueur quand ce dernier est dans son champ de vision)*/
public class Lapin1 extends Ennemi {
	
	private Ramzi player;
	protected double vitesseDeplacement;

	public Lapin1(WorldMap worldmap, TiledMap map, Ramzi player, float x, float y, int idEnnemi) {
		super(worldmap, map, player, x, y, idEnnemi);
		super.ptVie = 5;
		super.ennemiDirection = (int) (Math.random()*(3-1)+1);
		this.player = player;
		this.vitesseDeplacement = 1.5;
	}
	
	public void init() throws SlickException {
		animations = super.prepareAnimationRabbit("lapins_level2.png");
		dyingSmoke = super.prepareSmokeAnimation();
		littleEnnemi = super.prepareLittleEnnemiAnimation("lapin-sauve.png");
		super.setRandomDirection();
	}
	
	public void update(int delta) {
		float futurX = getFuturX(delta, 0.5);
		float futurY = getFuturY(delta, 0.5);
		float[] vueSouris = super.getVueEnnemi(distanceVue);
		distanceVue = getDistanceVue();

		this.accelerer();
		this.shouldMove(vueSouris);
		this.shouldAvoidCollision(delta, futurX, futurY);
		super.canHitRamzi();
	}
	
	/*le lapin accélère en suivant Ramzi*/
	private void accelerer() {
		if(this.vitesseDeplacement < 9 && super.moving) {
			this.vitesseDeplacement *= 1.05;
		}
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
				// this.x = futurX;
				// this.y = futurY;
			} else{
				super.suivrePlayer(player, vitesseDeplacement, delta, true);
			}
		}
	}

}
