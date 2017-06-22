package game;
import java.awt.Rectangle;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.tiled.TiledMap;

public class Bullet 
{
	private TiledMap map;
	private Ramzi player;
	private WorldMap worldMap;
	private float xProjectile, yProjectile;
	
	// Permettent de donner l'illusion que le projectile tombe
	private float yProjectileRender;
	private int falling = 0;
	
	private int directionProjectile;
	private int lived = 0;
	private boolean active = true;
	private static int maxLifetime = 500;
	private Graphics projectile;
	private Circle projectileArea;
	private Animation[] animationsBullet = new Animation[1];
	
	
	private double vitesseX = 0;
	private double vitesseY = 0;

	public Bullet(WorldMap worldMap, TiledMap map, Ramzi player, int directionProjectile, double vitesseX, double vitesseY)
	{
		this.map = map;
		this.worldMap = worldMap;
		this.player = player;
		this.directionProjectile = directionProjectile;
		this.vitesseX = vitesseX;
		this.vitesseY = vitesseY;

		setInitCoordonneeProjectile();
		try {
			prepareAnimationBullet();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private void setInitCoordonneeProjectile()
	{
		switch(directionProjectile)
		{
		case 0 : 
			this.xProjectile = player.getX();
			this.yProjectile = player.getY() + 16;
			break;
		case 1 :
			this.xProjectile = player.getX() + 16;
			this.yProjectile = player.getY();
			break;
		case 2 : 
			this.xProjectile = player.getX();
			this.yProjectile = player.getY() + 16;
			break;
		case 3 :
			this.xProjectile = player.getX() - 16;
			this.yProjectile = player.getY();
			break;
			
		}
	}
	
	public void init() throws SlickException
	{
		prepareAnimationBullet();
	}
	
	public Animation[] prepareAnimationBullet() throws SlickException {

		SpriteSheet spriteBullet = new SpriteSheet("ressources/sprites/Attaques/Ramzi/lootMadMouse/fromage2.png", 32, 32);
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
	
	public void update(int delta)
	{
		if(active)
		{
			deplacementManagement();
			
			touchEnnemis();
			
			if(isCollision()){
				this.active = false;
			}
			
			lived += delta;
			if (lived > maxLifetime){
				this.active = false;
			}
		}
		
	}
	
	private boolean isCollision() 
	{
		int tileW = map.getTileWidth();
		int tileH = map.getTileHeight();
		int collisionLayer = this.map.getLayerIndex("collision");
		
		Image tile = this.map.getTileImage((int) this.xProjectile / tileW, (int) this.yProjectile / tileH, collisionLayer);
		boolean collision = tile != null;
		if (collision)
		{
			Color color = tile.getColor((int) this.xProjectile % tileW, (int) this.yProjectile % tileH);
			collision = color.getAlpha() > 0;
		}
		return collision;
		
	}
	
	private void touchEnnemis()
	{
		projectileArea = new Circle((int)xProjectile, (int)yProjectile-10, 20);

		for(int i = 0; i<this.worldMap.getEnnemisDebut();i++){
			if(this.canTouchEnnemis(projectileArea, i))
			{
				if(WorldMap.tabEnnemi != null){
					this.worldMap.tabEnnemi.get(i).takeDamage(1);
				}
				this.active = false;
			}
			
		}
		if(this.worldMap.getBossLevel() != null){
			if(this.canTouchBoss(projectileArea)){
				if (this.worldMap.getBossLevel() != null){
					this.worldMap.getBossLevel().takeDamage(1);
					this.active = false;
				}
			}
		}
	}

	public boolean canTouchBoss(Circle damageArea)
	{
		if(damageArea.intersects(this.worldMap.getBossLevel().calcZoneCollision())){
			return true;
		}else{
			return false;
		}
	}

	public boolean canTouchEnnemis(Circle damageArea, int i)
	{
		boolean result = false;
		try{
			if(WorldMap.tabEnnemi!=null){
				if(WorldMap.tabEnnemi.size() > 0){
						if(WorldMap.tabEnnemi.get(i)!=null)
						{
							if(damageArea.contains(WorldMap.tabEnnemi.get(i).getX() +10, WorldMap.tabEnnemi.get(i).getY() -10) ||
							   damageArea.contains(WorldMap.tabEnnemi.get(i).getX() -10, WorldMap.tabEnnemi.get(i).getY() -10) ||
							   damageArea.contains(WorldMap.tabEnnemi.get(i).getX(), WorldMap.tabEnnemi.get(i).getY() -30) ||
							   damageArea.contains(WorldMap.tabEnnemi.get(i).getX(), WorldMap.tabEnnemi.get(i).getY() +5)){
								
								result = true;
							}
						}
				}
			}
		}catch(ArrayIndexOutOfBoundsException e){}
		return result;
	}
	
	private void deplacementManagement(){
		
		if(this.directionProjectile == 0 || this.directionProjectile == 2){
			deplacerProjectileVerticalement();
		}
		if(this.directionProjectile == 1 || this.directionProjectile == 3){
			deplacerProjectileHorizontalement();
		}		
		xProjectile += this.vitesseX*6;
		yProjectile += this.vitesseY*6;
		
	}
	
	private void deplacerProjectileHorizontalement()
	{
		if(this.directionProjectile == 1){
			xProjectile += 20;
		}
		else{
			xProjectile -= 20;
		}
	}
	
	private void deplacerProjectileVerticalement()
	{
		if(this.directionProjectile == 2){
			yProjectile += 20;
		}
		else{
			yProjectile -= 20;
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
			ombre.fillOval(xProjectile-12, yProjectile, 20, 20); //création d'une ombre
			
			this.yProjectileRender = this.yProjectile;
			if(this.lived >= 350){
				this.falling+=2;
				g.drawAnimation(animationsBullet[0], xProjectile-16, yProjectileRender+this.falling-16);
			} else {
				g.drawAnimation(animationsBullet[0], xProjectile-16, yProjectile-16);
			}
			projectile = g;
			
			/*
			projectile.setColor(Color.red);
			projectile.fillOval(xProjectile, yProjectile-10, 20, 20);
			*/
		}
	}
	
	public boolean isAlive()
	{
		return active;
	}
	
	public void changerBulletNiveau(TiledMap newmap)
	{
		this.map = newmap;
	}
}
