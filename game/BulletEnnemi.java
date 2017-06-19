package game;

import java.awt.Rectangle;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Projectiles des ennemis
 */
public class BulletEnnemi {
	
	private Animation[] animationBulletEnnemi = new Animation[1];
	private TiledMap map;
	private WorldMap worldMap;
	private Ramzi player;
	private float xProjectile, yProjectile;
	private int directionProjectile;
	private Ennemi ennemi;
	private Rectangle projectileArea;
	private boolean active = true; //le projectile est visible ou non
	
	public BulletEnnemi(WorldMap worldMap, TiledMap map, Ramzi player, int directionProjectile, Ennemi ennemi) {
		this.map = map;
		this.directionProjectile = directionProjectile;
		this.worldMap = worldMap;
		this.player = player;
		this.ennemi = ennemi;
		setInitCoordonneeProjectileEnnemi();
		try {
			prepareAnimationBulletCarrot();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private void setInitCoordonneeProjectileEnnemi()
	{
		this.xProjectile = ennemi.getX();
		this.yProjectile = ennemi.getY();
		switch(directionProjectile)
		{
		case 0 : 
			this.xProjectile = ennemi.getX()-16;
			this.yProjectile = ennemi.getY()-32;
			break;
		case 1 :
			this.xProjectile = ennemi.getX()+16;
			this.yProjectile = ennemi.getY()-16;
			break;
		case 2 : 
			this.xProjectile = ennemi.getX()-16;
			this.yProjectile = ennemi.getY();
			break;
		case 3 :
			this.xProjectile = ennemi.getX()-32;
			this.yProjectile = ennemi.getY()-16;
			break;			
		}
	}
	
	public Animation[] prepareAnimationBulletCarrot() throws SlickException {

		SpriteSheet spriteBullet = new SpriteSheet("ressources/sprites/Attaques/Ramzi/lootMadMouse/fromage2.png", 32, 32);
		this.animationBulletEnnemi[0] = loadAttaqueAnimation(spriteBullet, 0, 7, 0);

		return animationBulletEnnemi;
	}
	
	private Animation loadAttaqueAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 50);
		}
		return animation;
	}
	
	public void render(Graphics g) throws SlickException
	{
		if(g!= null && projectileArea != null){			
			Graphics ombre = new Graphics();
			ombre.setColor(new Color(0,0,0, 0.5f));
			ombre.fillOval(xProjectile+12, yProjectile+16, 20, 20); //création d'une ombre
			
			g.drawAnimation(animationBulletEnnemi[0], xProjectile, yProjectile);
		}
	}
	
	public void update(int delta)
	{
		if(active) {
			deplacementManagement();
			touchPlayer();			
			if(isCollision()){
				this.active = false;
			}
		}
		
	}
	
	private void deplacementManagement(){		
		if(this.directionProjectile== 0 || this.directionProjectile== 2){
			deplacerProjectileVerticalement();
		}
		if(this.directionProjectile== 1 || this.directionProjectile== 3){
			deplacerProjectileHorizontalement();
		}		
	}
	
	private void deplacerProjectileHorizontalement()
	{
		if(this.directionProjectile == 3){
			xProjectile += 15;
		}
		else{
			xProjectile -= 15;
		}
	}
	
	private void deplacerProjectileVerticalement()
	{
		if(this.directionProjectile == 2){
			yProjectile += 15;
		}
		else{
			yProjectile -= 15;
		}
	}
	
	private void touchPlayer()
	{
		projectileArea = new Rectangle((int)xProjectile, (int)yProjectile-10, 20,20);
		for(int i = 0; i<this.worldMap.getEnnemisDebut();i++){
			if(this.canTouchPlayer(projectileArea, i))
			{
				if(player != null){
					player.takeDamage(2);
				}
				this.active = false;
			}
		}
	}
	
	public boolean canTouchPlayer(Rectangle damageArea, int i)
	{
		boolean result = false;
		if(player!=null){
			if(damageArea.contains(player.getX() +10, player.getY() -10) ||
			   damageArea.contains(player.getX() -10, player.getY() -10) ||
			   damageArea.contains(player.getX(), player.getY() -30) ||
			   damageArea.contains(player.getX(), player.getY() +5))
			{
				result = true;
			}			
		}
		return result;
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
	
	public boolean getActive() { return active; }
}
