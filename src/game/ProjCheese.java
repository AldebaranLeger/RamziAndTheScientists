package game;
import levels.level1.*;

import java.awt.Rectangle;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

public class ProjCheese
{
	private float xPosition, yPosition;
	private TiledMap map;
	private Animation[] animationsBullet = new Animation[1];
	private int lived = 0;
	private static int maxLifetime = 1200;
	private boolean active = true;
	private int direction = 0;
	private Ramzi player;
	private Rectangle projectileArea;
	private boolean enrage;
	private Level1 level;
	private int enrageCheese = 0;
	
	private boolean isTooMuch = false;
	
	public ProjCheese(MadMouse mm, Ramzi player, TiledMap map, int direction, Level1 level)
	{
		xPosition= mm.getX();
		yPosition = mm.getY();
		this.map = map;
		this.enrage = mm.isEnrage();
		this.direction = direction;
		this.player = player;
		this.level = level;

		setInitCoordonneeProjectile(direction);
		try {
			prepareAnimationBullet();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
	public ProjCheese(float xPos, float yPos, TiledMap map, Ramzi player, int direction){
		this.isTooMuch = true;
		
		xPosition= xPos;
		yPosition = yPos;
		this.map = map;
		this.direction = direction;
		this.player = player;

		setInitCoordonneeProjectile(direction);
		try {
			prepareAnimationBullet();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
	public void update(int delta)
	{
		if(this.active){
			deplacementManagement();
			
			toucheJoueur();
			
			if(this.enrage){
				this.enrageCheese+=delta;
				if(this.enrageCheese >= 200){
					throwMoreCheese();
					this.enrageCheese = 0;
				}
			}

			if(isCollision()){
				this.active = false;
			}
			
			lived += delta;
			if (lived > maxLifetime){
				this.active = false;
			}
		}
	}
	
	private void throwMoreCheese(){
		if(this.direction== 0 || this.direction== 2){
			this.level.createMoreMadMouseCheese(this.xPosition, this.yPosition, 1);
			this.level.createMoreMadMouseCheese(this.xPosition, this.yPosition, 3);
		}
		if(this.direction== 1 || this.direction== 3){
			this.level.createMoreMadMouseCheese(this.xPosition, this.yPosition, 2);
			this.level.createMoreMadMouseCheese(this.xPosition, this.yPosition, 0);
		}
	}
	
	public void render(Graphics g) throws SlickException
	{
		if(g != null && projectileArea != null){
			/*
			g.setColor(Color.blue);
			g.fillRect(projectileArea.x, projectileArea.y, projectileArea.width, projectileArea.height);
			*/
			
			Graphics ombre = new Graphics();
			ombre.setColor(new Color(0,0,0, 0.5f));
			ombre.fillOval(xPosition+15, yPosition+18, 25, 25); //création d'une ombre
			
			g.drawAnimation(animationsBullet[0], xPosition, yPosition);
			
			
			/*
			projectile.setColor(Color.red);
			projectile.fillOval(xProjectile, yProjectile-10, 20, 20);
			*/
		}
	}
	
	private void toucheJoueur(){
		projectileArea = new Rectangle((int)xPosition, (int)yPosition-5, 25,30);
		if(peutToucherJoueur(projectileArea)){
			this.player.takeDamage(4);
		}
	}
	
	public boolean peutToucherJoueur(Rectangle damageArea)
	{
		boolean result = false;
		
		if(damageArea.contains(this.player.getX(), this.player.getY()))
		{
			result = true;
		}

		return result;
	}

	private void deplacementManagement(){
		
		if(this.direction== 0 || this.direction== 2){
			deplacerProjectileVerticalement();
		}
		if(this.direction== 1 || this.direction== 3){
			deplacerProjectileHorizontalement();
		}
		
	}
	
	private void deplacerProjectileHorizontalement()
	{
		if(this.direction == 3){
			xPosition += 15;
		}
		else{
			xPosition -= 15;
		}
	}
	
	private void deplacerProjectileVerticalement()
	{
		if(this.direction == 2){
			yPosition += 15;
		}
		else{
			yPosition -= 15;
		}
	}
	
	private void setInitCoordonneeProjectile(int directionProjectile)
	{
		switch(directionProjectile)
		{
		case 0 : 
			this.xPosition -= 16;
			this.yPosition -= 32;
			break;
		case 1 :
			this.xPosition += 16;
			this.yPosition -= 16;
			break;
		case 2 : 
			this.xPosition -= 16;
			break;
		case 3 :
			this.xPosition -= 32;
			this.yPosition -= 16;
			break;
			
		}
	}
	
	public Animation[] prepareAnimationBullet() throws SlickException {

		SpriteSheet spriteBullet = new SpriteSheet("ressources/sprites/Attaques/Bosses/MadMouse/Attaque_Boss_Distance_2.png", 32, 32);
		this.animationsBullet[0] = loadAttaqueAnimation(spriteBullet, 0, 7, 0);

		return animationsBullet;
	}

	private Animation loadAttaqueAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 50);
		}
		return animation;
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		//System.out.println("pos.getX() : "+pos.getX() + "pos.getY() : "+pos.getY());
		g.setColor(new Color(0,0,0, 0.5f));
		//System.out.println("gc "+ xActuel + ", " + yActuel);
		g.fillOval(xPosition, yPosition, 20, 20); //création d'une ombre
	}
	
	private boolean isCollision() 
	{
		int tileW = map.getTileWidth();
		int tileH = map.getTileHeight();
		int collisionLayer = this.map.getLayerIndex("collision");
		
		Image tile = this.map.getTileImage((int) this.xPosition / tileW, (int) this.yPosition / tileH, collisionLayer);
		boolean collision = tile != null;
		if (collision)
		{
			Color color = tile.getColor((int) this.xPosition % tileW, (int) this.yPosition % tileH);
			collision = color.getAlpha() > 0;
		}
		return collision;
		
	}

	public boolean isAlive()
	{
		return active;
	}
	
}
