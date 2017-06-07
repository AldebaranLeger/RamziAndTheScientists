import java.awt.Rectangle;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class MadMouse
{
	private WorldMap wp;
	private TiledMap map;
	private Ramzi player;
	private float x, y;
	private int direction;
	private Animation[] animations = new Animation[8];
	private Animation[] animationsAttaque = new Animation[4];
	private int action = 0;
	private int tim = 0;
	private int bossTimer = 0;
	private int atkTimer = 0;
	private int atkState = 0;
	private int clrCounter = 0;
	private Color atkClr;
	private int atkDistState=0;
	private int atkDistUpdate=0;
	private Rectangle fromage = null;
	private int timerFromage = 0;
	private float xDiff, yDiff, xScale, yScale;
	Vector2f pos;
	Vector2f dir;
	private int atkStt=0;
	private int ptVie;
	private boolean living = true;
	
	public MadMouse(WorldMap wp, TiledMap map, Ramzi p, float xM, float yM)
	{
		this.wp = wp;
		this.map = map;
		this.player = p;
		this.x = xM;
		this.y = yM;
		this.ptVie = 25;
	}
	
	public void init() throws SlickException
	{
		SpriteSheet spriteSouris = new SpriteSheet("ressources/sprites/madMouse.png", 128, 128);
		this.direction = 2;
		this.animations[0] = loadAnimation(spriteSouris, 0, 1, 0);
		this.animations[1] = loadAnimation(spriteSouris, 0, 1, 1);
		this.animations[2] = loadAnimation(spriteSouris, 0, 1, 2);
		this.animations[3] = loadAnimation(spriteSouris, 0, 1, 3);
		this.animations[4] = loadAnimation(spriteSouris, 1, 9, 0);
		this.animations[5] = loadAnimation(spriteSouris, 1, 9, 1);
		this.animations[6] = loadAnimation(spriteSouris, 1, 9, 2);
		this.animations[7] = loadAnimation(spriteSouris, 1, 9, 3);
		
		this.action = 1;
		
		SpriteSheet spriteAttaque = new SpriteSheet("ressources/sprites/attaques/Boss/SpriteAttaqueBoss.png", 200, 500);

		this.animationsAttaque[0] = loadAtkAnimation(spriteAttaque, 0, 1, 0);
		this.animationsAttaque[1] = loadAtkAnimation(spriteAttaque, 1, 2, 0);
		this.animationsAttaque[2] = loadAtkAnimation(spriteAttaque, 2, 3, 0);
		this.animationsAttaque[3] = loadAtkAnimation(spriteAttaque, 3, 4, 0);
		
	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}

	private Animation loadAtkAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 1000);
		}
		return animation;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		float futurX = getFuturX(delta, 1.5);
		float futurY = getFuturY(delta, 1.5);

		switch(action){
		case 0 : break;
		case 1 :
			if (isCollision(futurX, futurY)) {
				suivrePlayer(player, 1, delta, true);
			} else{
				suivrePlayer(player, 1, delta, false);
			}
			break;
		case 2 : 
			
			this.attaquer();
			action=1;
			atkState=0;
			break;
		case 3 : 
			atkTimer++;
			clrCounter++;
			atkState = 1;
			if(atkTimer>=60)
			{
				action=2;
				atkTimer=0;
			}
			break;
		case 4 :
			atkDistState = 1;
			System.out.println("Lancer de fromHache");
			//this.throwTheCheese(delta,1);
//			System.out.println("MadMouse x = "+x);
//			wp.createMadMouseCheese(this,delta, 1);
			action=1;
			break;
		}

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
		if(atkState!=1){
			if(tim==0){
				bossTimer = (int)(Math.random() * (150-50))+50;
			}
			tim ++ ;
			//System.out.println("tim : " + tim);
			if(tim>=bossTimer){
				action=(int)(Math.random() * (5-3))+3;
				tim=0;
			}
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillOval(x - 32, y - 16, 64, 32); // création d'une ombre
		g.drawAnimation(animations[direction + (4)], x - 64, y - 120);
		
		switch(atkStt){
		case 1 :
			g.drawAnimation(animationsAttaque[0], x-264, y-250);
			break;
		case 2 :
			g.drawAnimation(animationsAttaque[1], x-264, y-250);
			break;
		case 3 :
			g.drawAnimation(animationsAttaque[2], x-264, y-250);
			break;
		case 4 :
			g.drawAnimation(animationsAttaque[3], x-264, y-250);
			break;
		}
		
		
		switch(atkState){
		case 0 :
			break;
		case 1 :
			switch(this.clrCounter){
			case 1 :
				atkClr = new Color(Color.yellow);
				break;
			case 20 :
				atkClr = new Color(Color.orange);
				break;
			case 40 :
				atkClr = new Color(Color.transparent);
				atkStt = 1;
				break;
			case 45 :
				atkStt = 2;
				break;
			case 50 :
				atkStt = 3;
				break;
			case 55 :
				atkStt = 4;
				break;
			case 60 :
				clrCounter=0;
				atkStt = 0;
				break;
			}
			
			switch(this.direction)
			{
			case 0 :
				g.setColor(atkClr);
				g.fillRect((int)x - 250 , (int)y-200, 500, 200);
				break;
			case 1 :
				g.setColor(atkClr);
				g.fillRect((int)x-200, (int)y-250, 200, 500);
				break;
			case 2 :
				g.setColor(atkClr);
				g.fillRect((int)x -250, (int)y, 500, 200);
				break;
			case 3 :
				g.setColor(atkClr);
				g.fillRect((int)x, (int)y-250, 200, 500);
				break;
			}
			break;
		}
	}
	
	public boolean isCollision(float x, float y) 
	{
		int tileW = map.getTileWidth();
		int tileH = map.getTileHeight();
		int collisionLayer = this.map.getLayerIndex("collision");
		Image tile = this.map.getTileImage((int) x / tileW, (int) y / tileH, collisionLayer);
		boolean collision = tile != null;
		if (collision)
		{
			Color color = tile.getColor((int) x % tileW, (int) y % tileH);
			collision = color.getAlpha() > 0;
		}
		
		return collision;
	}
	
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
	
	private void attaquer()
	{
		
		Rectangle rect = new Rectangle();
		switch(this.direction)
		{
		case 0 :
			rect.setSize(500, 200);
			rect.setLocation((int)x - 250, (int)y - 200);
			break;
		case 1 :
			rect.setSize(200, 500);
			rect.setLocation((int)x-200, (int)y - 250 );
			break;
		case 2 :
			rect.setSize(500, 200);
			rect.setLocation((int)x - 250, (int)y);
			break;
		case 3 :
			rect.setSize(200, 500);
			rect.setLocation((int)x, (int)y-250);
			break;
		}
		if(rect.contains(this.player.getX(), this.player.getY()))
		{
			this.player.takeDamage(3);
		}
			
	}
	
	public void suivrePlayer(Ramzi player, double vitesse, int delta, boolean collision) {		
		float gX, gY;

		if (player.getX() > this.x)
			gX = player.getX() - this.x;
		else
			gX = this.x - player.getX();

		if (player.getY() > this.y)
			gY = player.getY() - this.y;
		else
			gY = this.y - player.getY();

		if (gX > gY) {
			if (player.getX() > this.x)
				setDirection(3);
			if (player.getX() < this.x)
				setDirection(1);
		} else {
			if (player.getY() > this.y)
				setDirection(2);
			if (player.getY() < this.y)
				setDirection(0);
		}

		float diffX = player.getX() - x;
		float diffY = player.getY() - y;
		float angle = (float) Math.atan2(diffY, diffX);
	  	if(collision)
	  	{
	  		if(isCollision(this.x,this.y-1)){
				x += Math.cos(angle) * vitesse;
	  			y+=1;
	  		}
	  		if(isCollision(this.x,this.y+1)){
				x += Math.cos(angle) * vitesse;
	  			y-=1;
	  		}
	  		if(isCollision(this.x+1,this.y)){
	  			x-=1;
				y += Math.sin(angle) * vitesse;
	  		}
	  		if(isCollision(this.x-1,this.y)){
	  			x+=1;
				y += Math.sin(angle) * vitesse;
	  		}
	  		

	  	} else {
			x += Math.cos(angle) * vitesse;
			y += Math.sin(angle) * vitesse;
		}
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void takeDamage(int dmg){
		this.ptVie-=dmg;
		if(this.ptVie<=0)
			this.death();
	}
	
	private void death(){
		living=false;
	}
	
	public boolean isDead(){
		if(!living){
			System.out.println("MadMouse est mort.");
			return true;
		} else {
			return false;
		}
	}
	
	protected float getFuturX(int delta, double vitesse)
	{

		float futurX = this.x;
		switch (this.direction) {
		case 1:
			futurX = (float) (this.x - .1f * delta * vitesse);
			break;
		case 3:
			futurX = (float) (this.x + .1f * delta * vitesse);
			break;
		}
		return futurX;
	}

	protected float getFuturY(int delta, double vitesse) {
		float futurY = this.y;
		switch (this.direction) {
		case 0:
			futurY = (float) (this.y - .1f * delta * vitesse);
			break;
		case 2:
			futurY = (float) (this.y + .1f * delta * vitesse);
			break;
		}
		return futurY;
	}
	
	
	public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}

}
