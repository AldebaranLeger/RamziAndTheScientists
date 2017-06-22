package levels.level2;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import game.Ennemi;
import game.Ramzi;
import game.WorldMap;

/*Lapins qui se déplace aléatoirement et qui lance des projectiles sur le joueur dès que celui ci est dans son champ de vision*/
public class Lapin2 extends Ennemi {
	private Ramzi player;
	private int maxDeplacement;
	private int compteurPas = 0;
	private boolean targetPlayer; //action de l'ennemi (marche aléatoirement ou vise le joueur)
	private Level2 level2;
	private int bulletTimer = 0; // Timer qui permet de déclencher au bout d'un certain temps le lancer de carottes

	public Lapin2(WorldMap worldmap, TiledMap map, Ramzi player, float x, float y, int idEnnemi, Level2 level2) {
		super(worldmap, map, player, x, y, idEnnemi);
		super.ptVie = 3;
		super.ennemiDirection = (int) (Math.random()*(3-1)+1);
		this.player = player;
		this.level2 = level2;
	}
	
	public void init() throws SlickException
	{		
		moving = true;
		targetPlayer = false; //n'attaque pas le joueur
		animations = super.prepareAnimationRabbit("lapins_level2.png");
		dyingSmoke = super.prepareSmokeAnimation();
		littleEnnemi = super.prepareLittleEnnemiAnimation("lapin-sauve.png");
	}
	
	public void update(int delta) throws SlickException
	{		
		realiserAction(delta);
	}
	
	private void realiserAction(int delta) throws SlickException 
	{		
		float futurX = getFuturX(delta, 1.5);
		float futurY = getFuturY(delta, 1.5);
		float[] vueSouris = super.getVueEnnemi(distanceVue);
		distanceVue = getDistanceVue();
		
		if(targetPlayer == false) {
			this.changerDirection(delta);
		} else
		{
			this.shouldAvoidCollision(delta, futurX, futurY);
		}
		prepareCarrotBullet(delta, vueSouris);
	}
	
	private void prepareCarrotBullet(int delta, float[] vueSouris) throws SlickException
	{
		bulletTimer++;
		if(bulletTimer%50==0) {
			throwCarrotBullet(delta, vueSouris);
		}
	}
	
	private void shouldAvoidCollision(int delta, float futurX, float futurY) throws SlickException
	{
		if (moving) {
			if (!super.isCollision(futurX, futurY)) {
				this.suivrePlayer(player, delta, false);
			} else{
				this.suivrePlayer(player, delta, true);
			}
		}
	}
	
	public void suivrePlayer(Ramzi player, int delta, boolean collision) throws SlickException {
		float diffX, diffY;

		diffX = getDiffPosition(player.getX(), this.x);
		diffY = getDiffPosition(player.getY(), this.y);

		if (diffX > diffY) {
			if (player.getX() > this.x){
				setDirection(3);
			}
			if (player.getX() < this.x){
				setDirection(1);
			}
		} else {
			if (player.getY() > this.y){
				setDirection(2);
			}
			if (player.getY() < this.y){
				setDirection(0);
			}
		}		
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
			} else {
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
	
	private int getDistanceVue()
	{
		if (!targetPlayer)
			return 180; // La souris va voir le joueur à 180 (px?) ...
		else {
			return 250; // ... et le perdera de vue à 280 px
		}
	}
	
	private void throwCarrotBullet(int delta, float [] vueSouris) throws SlickException
	{
		if ((vueSouris[0] < player.getX() && player.getX() < vueSouris[1])
				&& (vueSouris[2] < player.getY() && player.getY() < vueSouris[3])) // si le player dans le rayon de l'ennemi alors on déclenche l'attaque
		{
			if(!super.isSaved()){
				targetPlayer=true;
				level2.createEnnemiProjectile(this, direction);
			}
		}
		else {
			bulletTimer=0;
			targetPlayer=false;
		}
	}
}
