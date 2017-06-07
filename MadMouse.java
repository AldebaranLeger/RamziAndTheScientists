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
	private TiledMap map;
	private Ramzi player;
	private float x, y;
	private int direction;
	private Animation[] animations = new Animation[8];
	private Animation[] animationsAttaque = new Animation[4];
	private int action = 0;
	private int cooldownAttaqueCAC = 0;
	private int bossTimer = 0; // Timer qui permet de déclencher les différentes attaques
	private int attaqueCACTimer = 0; // Timer qui permet de déclencher au bout d'un certain temps l'attaque CàC
	private int attaqueCACState = 0; // Etat de l'attaque au CàC
	private int ColorState = 0;
	private Color atkClr;
	private int atkDistState=0;
	private int atkDistUpdate=0;
	private Rectangle fromage = null;
	private int timerFromage = 0;
	private float xDiff, yDiff, xScale, yScale;
	Vector2f pos;
	Vector2f dir;
	private int maxPv;
	private int ptVie;
	private boolean living = true;
	
	public MadMouse(TiledMap map, Ramzi player, float xMadMouseSpawn, float yMadMouseSpawn)
	{
		this.map = map;
		this.player = player;
		this.x = xMadMouseSpawn;
		this.y = yMadMouseSpawn;
		this.ptVie = 25;
		this.maxPv = 25;
	}
	
	public int getMaxPv(){
		return this.maxPv;
	}
	
	public Animation[] prepareAnimation() throws SlickException {

		SpriteSheet spriteMadMouse = new SpriteSheet("ressources/sprites/madMouse.png", 128, 128);
		this.animations[0] = loadAnimation(spriteMadMouse, 0, 1, 0);
		this.animations[1] = loadAnimation(spriteMadMouse, 0, 1, 1);
		this.animations[2] = loadAnimation(spriteMadMouse, 0, 1, 2);
		this.animations[3] = loadAnimation(spriteMadMouse, 0, 1, 3);
		this.animations[4] = loadAnimation(spriteMadMouse, 1, 9, 0);
		this.animations[5] = loadAnimation(spriteMadMouse, 1, 9, 1);
		this.animations[6] = loadAnimation(spriteMadMouse, 1, 9, 2);
		this.animations[7] = loadAnimation(spriteMadMouse, 1, 9, 3);

		return animations;
	}
	
	public Animation[] prepareAnimationAttaque() throws SlickException {

		SpriteSheet spriteMadMouseAttaque = new SpriteSheet("ressources/sprites/attaques/Boss/SpriteAttaqueBoss.png", 200, 500);
		this.animationsAttaque[0] = loadAttaqueAnimation(spriteMadMouseAttaque, 0, 1, 0);
		this.animationsAttaque[1] = loadAttaqueAnimation(spriteMadMouseAttaque, 1, 2, 0);
		this.animationsAttaque[2] = loadAttaqueAnimation(spriteMadMouseAttaque, 2, 3, 0);
		this.animationsAttaque[3] = loadAttaqueAnimation(spriteMadMouseAttaque, 3, 4, 0);

		return animations;
	}
	
	public void init() throws SlickException
	{
		this.direction = 2;
		prepareAnimation();
		prepareAnimationAttaque();
		
		this.action = 1; // Action : suivre le joueur

	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}

	private Animation loadAttaqueAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 1000);
		}
		return animation;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		realiserAction(delta);
		
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
			if(cooldownAttaqueCAC==0){
				bossTimer = (int)(Math.random() * (150-50))+50;
			}
			cooldownAttaqueCAC ++ ;
			//System.out.println("cooldownAttaqueCAC : " + cooldownAttaqueCAC);
			if(cooldownAttaqueCAC>=bossTimer){
				action=(int)(Math.random() * (4-2))+2;
				cooldownAttaqueCAC=0;
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
			switchColorState(g);
			switchDirection(g);
			break;
		}
	}
	
	private void switchColorState(Graphics g)
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
		switch(this.ColorState){
		case 1 :
			atkClr = new Color(Color.yellow);
			break;
		case 20 :
			atkClr = new Color(Color.orange);
			break;
		case 40 :
			atkClr = new Color(Color.transparent);
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
			ColorState=0;
			break;
		}
	}
	
	private void switchDirection(Graphics g)
	{
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
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillOval(x - 32, y - 16, 64, 32); // création d'une ombre
		g.drawAnimation(animations[direction + (4)], x - 64, y - 120);
		
		animateAttaqueCACbyTimer(g);
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
		ColorState++;
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
	
	private float getDiffXetY(float playerCoordonnee, float bossCoordonnee)
	{
		if (playerCoordonnee > bossCoordonnee)
			return playerCoordonnee - bossCoordonnee;
		else
			return  bossCoordonnee - playerCoordonnee;
	}
	
	private void ifCollision(float angle, double vitesse)
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
	}
	
	public void suivrePlayer(Ramzi player, double vitesse, int delta, boolean collision) {		
		float diffX, diffY;

		diffX = getDiffXetY(player.getX(), this.x);
		diffY = getDiffXetY(player.getY(), this.y);

		if (diffX > diffY) {
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

		diffX = player.getX() - x;
		diffY = player.getY() - y;
		float angle = (float) Math.atan2(diffY, diffX);
	  	if(collision)
	  	{
	  		ifCollision(angle, vitesse);
	  	} else {
			x += Math.cos(angle) * vitesse;
			y += Math.sin(angle) * vitesse;
		}
	}
	
	public int getPtVie() {
		return this.ptVie;
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
	
	public boolean isSaved(){
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
