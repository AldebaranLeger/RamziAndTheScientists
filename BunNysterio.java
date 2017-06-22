import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class BunNysterio extends Boss
{
	private int action = 1;
	private List<AttaqueBunNysterio> attaqueBunNysterio = new ArrayList<AttaqueBunNysterio>();
	private int nbPicsTotal;
	private int nbPicsActuel = 0;
	private float xPic, yPic;
	private boolean attaquePrimaireEnCours = false;
	private boolean attaqueTunnelEnCours = false;
	private int timerIntervalPics = 0;
	private int timerTunnel = 0;
	private int pasDeplacementTunnelX = 10;
	private int pasDeplacementTunnelY = 10;
	
	
	public BunNysterio(TiledMap map, Ramzi player, float xMadMouseSpawn, float yMadMouseSpawn)
	{
		super.map = map;
		super.player = player;
		super.x = xMadMouseSpawn;
		super.y = yMadMouseSpawn;
		super.maxPv = 100;
		super.ptVie = maxPv;
		super.srcSpriteBoss = "bunNysterio";
		super.nbColonne = 8;
	}

	public void init() throws SlickException
	{
		super.calcZoneCollision();
		super.direction = 2;
		prepareAnimation();
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillOval(x - 32, y - 16, 64, 32); // création d'une ombre
		
		g.drawAnimation(animations[direction + (4)], x - 64, y - 120);
			
		for(int i = 0 ; i < attaqueBunNysterio.size() ; i++)
		{
			if(this.attaqueBunNysterio.get(i) !=null)
			{
				this.attaqueBunNysterio.get(i).render(g);
			}
		}
		
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		realiserAction(delta);
		super.canHitRamzi();
		
		if(cooldownAttaque==0){
			bossTimer = (int)(Math.random() * (150-50))+50;
		}
		cooldownAttaque ++ ;
		if(cooldownAttaque>=bossTimer){
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
				suivrePlayer(player, 1.5, delta, false);
			}
			break;
		case 2 : 
			attaquePrimaireEnCours = true;
			//xPic = this.x;
			//yPic = this.y;
			break;
		case 3 :
			//attaqueTunnelEnCours = true;
			action =1;
			break;
		}
	}
	
	private void attaqueTunnels()
	{
		if(isCollision(x+10, y+10)
		|| isCollision(x+10, y-10)
		|| isCollision(x-10, y+10)
		|| isCollision(x-10, y-10))
		{
			if(isCollision(x+15, y+10) || isCollision(x+15, y-10))
			{
				pasDeplacementTunnelX = -10;
			}
			else if(isCollision(x-15, y+10) || isCollision(x-15, y-10))
			{
				pasDeplacementTunnelX = +10;
			}
			else if(isCollision(x+10, y+15) || isCollision(x-10, y+15))
			{
				pasDeplacementTunnelY = -10;
			}
			else if(isCollision(x+10, y-15) || isCollision(x-10, y-15))
			{
				pasDeplacementTunnelY = +10;
			}
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
			nbPicsTotal = (int)(Math.random() * (20-10)) + 10;
		}
		if(timerIntervalPics % 6 == 0 && nbPicsActuel < nbPicsTotal)
		{
			placerPics();
			attaqueBunNysterio.add(new AttaqueBunNysterio(player, xPic, yPic, direction));
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
	
	private void placerPics()
	{
		int randomEspacement = (int)(Math.random() * 100)-50;
		int intervalEntreDeuxPics = 32;//(int)(Math.random() * 96 - 32 + 32);

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
				yPic = y + intervalEntreDeuxPics; break;
			case 1 : 
				yPic = y; break;
			case 2 : 
				yPic = y - intervalEntreDeuxPics; break;
			case 3 : 
				yPic = y; break;
			}
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
