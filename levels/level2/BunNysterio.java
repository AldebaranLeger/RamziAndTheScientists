package levels.level2;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import game.AttaqueBunNysterio;
import game.Boss;
import game.Ramzi;

public class BunNysterio extends Boss
{
	private int action = 1;
	private boolean attaquePrimaireEnCours = false;
	private boolean attaqueTunnelEnCours = false;
	private List<AttaqueBunNysterio> attaqueBunNysterio = new ArrayList<AttaqueBunNysterio>();
	private int nbPicsTotal;
	private int nbPicsActuel = 0;
	private float xPic, yPic;
	private int timerIntervalPics = 0;
	private int timerTunnel = 0;
	private int pasDeplacementTunnelX = 0;
	private int pasDeplacementTunnelY = 0;
	protected Animation[] animationsTunnel = new Animation[1];
	
	
	public BunNysterio(TiledMap map, Ramzi player, float xMadMouseSpawn, float yMadMouseSpawn)
	{
		super(map, player, xMadMouseSpawn, yMadMouseSpawn);
		super.map = map;
		super.player = player;
		super.x = xMadMouseSpawn;
		super.y = yMadMouseSpawn;
		super.maxPv = 5;
		super.ptVie = maxPv;
		super.srcSpriteBoss = "bunNysterio";
		super.nbColonne = 8;
		super.nomBoss = "BunNysterio";
	}

	
	public Animation[] prepareAnimationTunnel() throws SlickException {

		SpriteSheet spriteBoss = new SpriteSheet("ressources/sprites/Attaques/Bosses/BunNysterio/tunnel.png", 128, 128);
		this.animationsTunnel[0] = loadAnimation(spriteBoss, 0, 2, 0);

		return animationsTunnel;
	}

	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}
	
	public void init() throws SlickException
	{
		super.calcZoneCollision();
		super.direction = 2;
		prepareAnimation();
		prepareAnimationTunnel();
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		Graphics circle = new Graphics();
		circle.setColor(Color.red);
		circle.fillOval(this.x-10, this.y-10, 20, 20);
		
		if(attaqueTunnelEnCours){
			g.drawAnimation(animationsTunnel[0], x - 64, y - 64);
		}
		else{
			g.setColor(new Color(0, 0, 0, 0.5f));
			g.fillOval(x - 32, y - 16, 64, 32); // création d'une ombre
			g.drawAnimation(animations[direction + (4)], x - 64, y - 120);
		}
		
		for(int i = 0 ; i < attaqueBunNysterio.size() ; i++)
		{
			if(this.attaqueBunNysterio.get(i) !=null){
				this.attaqueBunNysterio.get(i).render(g);
			}
		}
		
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		int vitesseDeplacementTunnelMax;
		if(this.getPtVie() > this.getMaxPv()/2){
			vitesseDeplacementTunnelMax = 10;
		}else{
			vitesseDeplacementTunnelMax = 20;
		}
		super.canHitRamzi();

		realiserAction(delta);
		if(cooldownAttaque==0 && attaqueTunnelEnCours == false){
			bossTimer = (int)(Math.random() * (250-100))+100;
		}
		if(attaqueTunnelEnCours == false  && attaqueTunnelEnCours == false)
		{
			cooldownAttaque ++ ;
		}
		if(cooldownAttaque>=bossTimer && attaqueTunnelEnCours == false ){
			action=(int)(Math.random() * (4-2))+2;
			cooldownAttaque=0;
		}
		
		if(attaquePrimaireEnCours)
		{
			this.timerIntervalPics ++;
			this.attaquerPrimaire();
			
			for(int i = 0 ; i < attaqueBunNysterio.size() ; i++)
			{
				if(this.attaqueBunNysterio.get(i) !=null)
				{
					this.attaqueBunNysterio.get(i).update(delta);
				}
			}
		}
		if(attaqueTunnelEnCours)
		{
			timerTunnel++;
			if(timerTunnel > 20)
			{
			
				if(this.pasDeplacementTunnelX < vitesseDeplacementTunnelMax && this.pasDeplacementTunnelY < vitesseDeplacementTunnelMax
						&& this.pasDeplacementTunnelX > -vitesseDeplacementTunnelMax && this.pasDeplacementTunnelY > -vitesseDeplacementTunnelMax){
					if(pasDeplacementTunnelX > 0){
						pasDeplacementTunnelX ++;
						pasDeplacementTunnelY ++;
					}else{
						pasDeplacementTunnelX --;
						pasDeplacementTunnelY --;
					}
				}
				attaqueTunnels();
				if(timerTunnel > 200)
				{
					arreterTunnel();
				}
			}
		}
		
	}
	
	private void realiserAction(int delta)
	{
		float futurX = getFuturX(delta, 1.5);
		float futurY = getFuturY(delta, 1.5);
		
		switch(action){
		case 0 : break;
		case 1 :
			if (isCollision(futurX, futurY)) {
				if(attaquePrimaireEnCours == false)
				{
					suivrePlayer(player, 1.5, delta, true);
				}
			}else if (isCollisionPersos()){
		  		super.knockBack(super.whereIsCollisionPerso);
		  	} else{
		  		if(this.getPtVie() > this.getMaxPv() / 2){
					suivrePlayer(player, 1, delta, false);
				}else{
					suivrePlayer(player, 3, delta, false);
				}
			}
			break;
		case 2 : 
			attaquePrimaireEnCours = true;
			//xPic = this.x;
			//yPic = this.y;
			break;
		case 3 :
			attaqueTunnelEnCours = true;
			break;
		}
	}
	
	private void attaqueTunnels()
	{
		if(isCollision(x+20, y) || isCollision(x-20, y)){
			pasDeplacementTunnelX = -pasDeplacementTunnelX;
		}
		else if(isCollision(x, y+20) || isCollision(x, y-20)){
			pasDeplacementTunnelY = -pasDeplacementTunnelY;
		}
		else if(isCollision(x+20, y+20) || isCollision(x+20, y-20) || isCollision(x-20, y+20) || isCollision(x-20, y-20))
		{
			pasDeplacementTunnelY = -pasDeplacementTunnelY;
			pasDeplacementTunnelX = -pasDeplacementTunnelX;
		}
		x += pasDeplacementTunnelX;
		y += pasDeplacementTunnelY;
	}
	
	private void arreterTunnel()
	{
		attaqueTunnelEnCours = false;
		timerTunnel = 0;
		action = 1;
	}
	
	private void attaquerPrimaire()
	{	if(nbPicsTotal == 0){

			if(this.getPtVie() > this.getMaxPv()/2){
				nbPicsTotal = (int)(Math.random() * (20-10)) + 10;
			}else{
				nbPicsTotal = (int)(Math.random() * (50-25)) + 25;
			}
		}
		if(timerIntervalPics % 3 == 0 && nbPicsActuel < nbPicsTotal)
		{
			if(this.getPtVie() > this.getMaxPv()/2){
				placerPics();
				attaqueBunNysterio.add(new AttaqueBunNysterio(player, xPic, yPic, direction));
			}
			else{
				placerPics();
				attaqueBunNysterio.add(new AttaqueBunNysterio(player, xPic, yPic, (int)(Math.random() * 4)));
			}
			nbPicsActuel++;
		}
		if(nbPicsActuel == nbPicsTotal)
		{
			nbPicsActuel = 0;
			timerIntervalPics = 0;
			attaquePrimaireEnCours = false;
			action = 1;
			
			destroyPics();
		}
	}
	
	private void setFirstXYPics(int intervalEntreDeuxPics)
	{
		if(xPic == 0){
			switch(direction)
			{
			case 0 :
				xPic = x; break;
			case 1 : 
				xPic = x - intervalEntreDeuxPics; break;
			case 2 : 
				xPic = x; break;
			case 3 : 
				xPic = x + intervalEntreDeuxPics; break;
			}
		}
		if(yPic == 0){
			switch(direction)
			{
			case 0 :
				yPic = y -128 + intervalEntreDeuxPics; break;
			case 1 : 
				yPic = y; break;
			case 2 : 
				yPic = y - intervalEntreDeuxPics; break;
			case 3 : 
				yPic = y; break;
			}
		}
	}
	
	
	private void placerPics()
	{	
		int randomEspacement;
		int intervalEntreDeuxPics;
		if(this.getPtVie() > this.getMaxPv()/2)
		{
			randomEspacement = (int)(Math.random() * 100)-50;
			intervalEntreDeuxPics = 32;
		}
		else
		{
			randomEspacement = (int)(Math.random() * 300)-150;
			intervalEntreDeuxPics = (int)(Math.random() * 300)-150;
		}

		if(xPic == 0 || yPic == 0){
			setFirstXYPics(intervalEntreDeuxPics);
		}
		
		switch(direction)
		{
		case 0 :
			xPic = x + randomEspacement;
			yPic = yPic - intervalEntreDeuxPics;
			break;
		case 1 : 
			xPic = xPic - intervalEntreDeuxPics;
			yPic = y + randomEspacement;
			break;
		case 2 : 
			xPic = x + randomEspacement;
			yPic = yPic + intervalEntreDeuxPics;
			break;
		case 3 : 
			xPic = xPic + intervalEntreDeuxPics;
			yPic = y + randomEspacement;
			break;
		}
	}

	private void destroyPics()
	{	
		nbPicsTotal = 0;
		xPic = 0;
		yPic = 0;
		this.attaqueBunNysterio.clear();			
		
	}
}
