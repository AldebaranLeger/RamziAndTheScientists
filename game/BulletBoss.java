package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

import levels.level1.MadMouse;

/**
 * Projectile des boss
 */
public class BulletBoss {
	
	private Animation[] animationBulletEnnemi = new Animation[1];
	private TiledMap map;
	private WorldMap worldMap;
	private Ramzi player;
	private float xProjectile, yProjectile;
	private int directionProjectile;
	private Ennemi ennemi;
	private MadMouse boss;
	private boolean active = true; //le projectile est visible ou non
	
	public BulletBoss(WorldMap worldMap, TiledMap map, Ramzi player, MadMouse boss) {
		this.map = map;
		this.worldMap = worldMap;
		this.player = player;
		this.boss = boss;
		
		if(boss!= null) {
			setInitCoordonneeProjectileBoss();
		}
		
		try {
			prepareAnimationBullet();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void init() throws SlickException
	{
		prepareAnimationBullet();
	}
	
	private void setInitCoordonneeProjectileBoss()
	{
		this.xProjectile = boss.getX();
		this.yProjectile = boss.getY();
		switch(directionProjectile)
		{
		case 0 : 
			this.xProjectile = boss.getX()-16;
			this.yProjectile = boss.getY()-32;
			break;
		case 1 :
			this.xProjectile = boss.getX()+16;
			this.yProjectile = boss.getY()-16;
			break;
		case 2 : 
			this.xProjectile = boss.getX()-16;
			this.yProjectile = boss.getY();
			break;
		case 3 :
			this.xProjectile = boss.getX()-32;
			this.yProjectile = boss.getY()-16;
			break;
			
		}
	}
	
	public Animation[] prepareAnimationBullet() throws SlickException {

		SpriteSheet spriteBulletEnnemi = new SpriteSheet("ressources/sprites/attaques/AstralFire.png", 32, 32);
		this.animationBulletEnnemi[0] = loadAttaqueAnimation(spriteBulletEnnemi, 0, 5, 0);

		return animationBulletEnnemi;
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
		if(isCollision()){
			this.active = false;
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

}
