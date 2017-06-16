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
	private boolean attaquePrimaireEnCours = false;
	private boolean listPicsVide = true;
	private List<AttaqueBunNysterio> attaqueBunNysterio = new ArrayList<AttaqueBunNysterio>();
	private int nbPicsTotal;
	private int nbPicsActuel = 0;
	private float xPic, yPic;
	private int timerIntervalPics = 0;
	
	
	public BunNysterio(TiledMap map, Ramzi player, float xMadMouseSpawn, float yMadMouseSpawn)
	{
		super.map = map;
		super.player = player;
		super.x = xMadMouseSpawn;
		super.y = yMadMouseSpawn;
		super.maxPv = 5;
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
			
		if(listPicsVide == false)
		{
			for(int i = 0 ; i < attaqueBunNysterio.size() ; i++)
			{
				if(this.attaqueBunNysterio.get(i) !=null)
				{
					this.attaqueBunNysterio.get(i).render(g);
				}
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
		//System.out.println("cooldownAttaqueCAC : " + cooldownAttaqueCAC);
		if(cooldownAttaque>=bossTimer){
			action=(int)(Math.random() * (4-2))+2;
			cooldownAttaque=0;
		}
		
		if(attaquePrimaireEnCours && listPicsVide == false)
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
		
		for(int i = 0 ; i < attaqueBunNysterio.size() ; i++)
		{
			System.out.println(this.attaqueBunNysterio.get(i));
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
			listPicsVide = false;
			attaquePrimaireEnCours = true;
			//xPic = this.x;
			//yPic = this.y;
			break;
		case 3 :
			attaqueTunnels();
			break;
		}
	}
	
	private void attaqueTunnels()
	{
		
		action = 1;
	}
	
	private void attaquerPrimaire()
	{		
		nbPicsTotal = (int)(Math.random() * (15-10)) + 10;

		if(timerIntervalPics % 5 == 0)
		{
			placerPics();
			System.out.println(direction + " --- " + xPic + " / " + yPic);
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
		System.out.println("destroyPics()");
		for(int i = 0 ; i < attaqueBunNysterio.size() ; i++)
		{
			xPic = 0;
			yPic = 0;
			this.attaqueBunNysterio.remove(i);
			this.listPicsVide = true;
			
		}
	}
	
	
	
}
