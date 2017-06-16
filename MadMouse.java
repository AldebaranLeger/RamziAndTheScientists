import java.awt.Rectangle;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class MadMouse extends Boss
{
	private int action = 0;
	private int attaqueCACTimer = 0; // Timer qui permet de d�clencher au bout d'un certain temps l'attaque C�C
	private int attaqueCACState = 0; // Etat de l'attaque au C�C
	private int prepareAttaqueState = 0;
	private int atkDistState=0;
	private int atkDistUpdate=0;
	private Rectangle fromage = null;
	private int timerFromage = 0;
	//Vector2f pos;
	//Vector2f dir;
	
	public MadMouse(TiledMap map, Ramzi player, float xMadMouseSpawn, float yMadMouseSpawn)
	{
		super.map = map;
		super.player = player;
		super.x = xMadMouseSpawn;
		super.y = yMadMouseSpawn;
		super.maxPv = 100;
		super.ptVie = maxPv;
		super.srcSpriteBoss = "madMouse";
	}
	
	
 	public Animation[] prepareAnimationAttaque() throws SlickException {

		SpriteSheet spriteMadMouseAttaque = new SpriteSheet("ressources/sprites/attaques/Boss/SpriteAttaqueBoss.png", 200, 500);
		this.animationsAttaque[0] = loadAttaqueAnimation(spriteMadMouseAttaque, 0, 1, 0);
		this.animationsAttaque[1] = loadAttaqueAnimation(spriteMadMouseAttaque, 1, 2, 0);
		this.animationsAttaque[2] = loadAttaqueAnimation(spriteMadMouseAttaque, 2, 3, 0);
		this.animationsAttaque[3] = loadAttaqueAnimation(spriteMadMouseAttaque, 3, 4, 0);

		return animations;
	}

	private Animation loadAttaqueAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 1000);
		}
		return animation;
	}


	
	public void init() throws SlickException
	{
		this.direction = 2;
		prepareAnimation();
		prepareAnimationAttaque();
		
		this.action = 1; // Action : suivre le joueur

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		realiserAction(delta);
		canHitRamzi();
		/*
		ATTAQUE A DISTANCE
		
		switch(atkDistState){
		case 0 : break;
		case 1 :
			//this.throwCheese();
			System.out.println("MM : " + x + ", " + y);
	//		wp.createMadMouseCheese(this, delta, 1);
			atkDistState = 0;			
			break;
		}
		if(atkDistUpdate==1){
			throwCheese();
		}
		
		*/
		
		if(attaqueCACState!=1){
			if(cooldownAttaque==0){
				bossTimer = (int)(Math.random() * (150-50))+50;
			}
			cooldownAttaque ++ ;
			//System.out.println("cooldownAttaqueCAC : " + cooldownAttaqueCAC);
			if(cooldownAttaque>=bossTimer){
				action=(int)(Math.random() * (4-2))+2;
				cooldownAttaque=0;
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
				suivrePlayer(player, 1, delta, true);
			}else if (isCollisionPersos()){
		  		super.knockBack(super.whereIsCollisionPerso);
		  		
		  	} else{
				suivrePlayer(player, 1, delta, false);
			}
			break;
		case 2 : 
			this.prepareAttaqueCAC();
			break;
		case 3 :
			atkDistState = 1;
			System.out.println("Lancer de fromHache");
			action=1;
			break;
		}
	}
	
	private void animateAttaqueCACbyTimer(Graphics g)
	{

		switch(attaqueCACState){
		case 0 :
			break;
		case 1 :
			switchPrepareAttaqueState(g);
			break;
		}
	}
	
	private void switchPrepareAttaqueState(Graphics g)
	{
		float rotation =0;
		switch(this.direction)
		{
			case 0 : rotation = 90;
			break;
			case 1 : rotation = 0;
			break;
			case 2 : rotation = 270;
			break;
			case 3 : rotation = 180;
			break;
		}
		switch(this.prepareAttaqueState){
		case 1 :
			super.srcSpriteBoss = "madMouseOrange";
			try {
				prepareAnimation();
			} catch (SlickException e1) {e1.printStackTrace();}
			break;
		case 20 :
			super.srcSpriteBoss = "madMouseRouge";
			try {
				prepareAnimation();
			} catch (SlickException e) {e.printStackTrace();}
			break;
		case 40 :
			super.srcSpriteBoss = "madMouse";
			try {
				prepareAnimation();
			} catch (SlickException e) {e.printStackTrace();}
			g.rotate(x, y, rotation);
			g.drawAnimation(animationsAttaque[0], x-216, y-250);
			
			g.resetTransform();
			attaquerCAC();
			break;
		case 45 :
			g.rotate(x, y, rotation);
			g.drawAnimation(animationsAttaque[1], x-216, y-250);
			g.resetTransform();
			attaquerCAC();
			break;
		case 46 :
			g.rotate(x, y, rotation);
			g.drawAnimation(animationsAttaque[2], x-216, y-250);
			g.resetTransform();
			attaquerCAC();
			break;
		case 47 :
			g.rotate(x, y, rotation);
			g.drawAnimation(animationsAttaque[3], x-216	, y-250);
			g.resetTransform();
			attaquerCAC();
			break;
		case 50 :
			action = 1;
			attaqueCACTimer=0;
			attaqueCACState = 0;
			prepareAttaqueState=0;
			break;
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillOval(x - 32, y - 16, 64, 32); // cr�ation d'une ombre
		
		if(this.prepareAttaqueState >1 && this.prepareAttaqueState <45)
		{
			if(this.prepareAttaqueState<20)
			{
				if(this.prepareAttaqueState%3 == 1){
					g.drawAnimation(animations[direction + (4)], x - 64, y - 120);
				}
			}else
			{
				if(this.prepareAttaqueState%2 == 1){
					g.drawAnimation(animations[direction + (4)], x - 64, y - 120);
				}
			}
		}
		else{
			g.drawAnimation(animations[direction + (4)], x - 64, y - 120);
		}
		animateAttaqueCACbyTimer(g);
	}

	/*
	private void throwCheese(){
		if(fromage==null){
			if(player.getX() > x)
				xScale = player.getX();
			else
				xScale = -player.getX();
			if(player.getY() > y)
				yScale = player.getY();
			else
				yScale = -player.getY();
			
			fromage = new Rectangle();
			fromage.setSize(20,20);
			fromage.setLocation((int)x-10,(int)y-10);
			
			pos = new Vector2f(x,y);
			dir = new Vector2f(xScale, yScale);
			
//			xDiff = x - player.getX();
//			yDiff = y - player.getY();
//			xScale = xDiff / 150;
//			yScale = yDiff / 150;
			timerFromage++;
			atkDistUpdate = 1;
		} else {
			if(timerFromage>=150){
				fromage = null;
				timerFromage = 0;
				atkDistUpdate = 0;
			} else {
				Vector2f realSpeed = dir.copy();
				realSpeed.scale(1/1000.0f);
				pos.add(realSpeed);
				timerFromage ++;
				
				fromage.setLocation((int)(pos.getX()), (int)(pos.getY()));
				timerFromage++;
				if(fromage.contains(this.player.getX(), this.player.getY())){
					this.player.takeDamage(6);
				}
			}
		}
	}
	*/
	
	private void prepareAttaqueCAC() {
		attaqueCACTimer++;
		prepareAttaqueState++;
		attaqueCACState = 1;
		if(attaqueCACTimer>=50)
		{
			attaquerCAC();
		}
	}
	
	private void attaquerCAC()
	{
		Rectangle damageArea = new Rectangle();
		switch(this.direction)
		{
		case 0 :
			damageArea.setSize(500, 200);
			damageArea.setLocation((int)x - 250, (int)y - 200);
			break;
		case 1 :
			damageArea.setSize(200, 500);
			damageArea.setLocation((int)x-200, (int)y - 250 );
			break;
		case 2 :
			damageArea.setSize(500, 200);
			damageArea.setLocation((int)x - 250, (int)y);
			break;
		case 3 :
			damageArea.setSize(200, 500);
			damageArea.setLocation((int)x, (int)y-250);
			break;
		}
		if(damageArea.contains(this.player.getX(), this.player.getY()))
		{
			this.player.takeDamage(3);
		}

	}

}
