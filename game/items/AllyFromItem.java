package game.items;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.geom.Rectangle;

import game.Ramzi;
import game.WorldMap;

public class AllyFromItem {
	private WorldMap worldMap;
	private float x, y;
	private double vitesseX, vitesseY;
	private int direction;
	private Animation[] littleEnnemi = new Animation[3];
	private TiledMap map;
	private boolean active = true;
	private int lived = 0;
	private int maxLifetime = 1000;
	private Rectangle hitbox;
	
	public AllyFromItem(WorldMap worldMap, TiledMap map, float initX, float initY)
	{
		this.map = map;
		this.worldMap = worldMap;
		this.x = initX;
		this.y = initY;
		this.vitesseX = Math.random()*(15-1)+1;
		this.vitesseY = Math.random()*(15-1)+1;
		if((int)(Math.random()*(3)+1) == 1){
			this.vitesseY = -this.vitesseY;
		}
		if((int)(Math.random()*(3)+1) == 1){
			this.vitesseX = -this.vitesseX;
		}
		this.direction = this.vitesseX > 0 ? 1 : 2;
		try {
			prepareLittleAllyAnimation();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public Animation[] prepareLittleAllyAnimation() throws SlickException {
		SpriteSheet spriteLittle = new SpriteSheet("ressources/sprites/PNJ/Souris_Sauvee.png", 64, 64);
		this.littleEnnemi[1] = loadAnimation(spriteLittle, 0, 3, 0);
		this.littleEnnemi[2] = loadAnimation(spriteLittle, 1, 3, 1);
		return littleEnnemi;
	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
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
		boolean collision = false;
		
		try{
			Image tile = this.map.getTileImage((int) this.x / tileW, (int) this.y / tileH, collisionLayer);
			collision = tile != null;
		if (collision)
		{
			Color color = tile.getColor((int) this.x % tileW, (int) this.y % tileH);
			collision = color.getAlpha() > 0;
		}
		} catch(ArrayIndexOutOfBoundsException e){}
		return collision;
		
	}
	
	private void touchEnnemis()
	{
		hitbox = new Rectangle((int)x-5, (int)y-5, 10,10);

		for(int i = 0; i<this.worldMap.getEnnemisDebut();i++){
			if(this.canTouchEnnemis(hitbox, i))
			{
				if(WorldMap.tabEnnemi != null){
					WorldMap.tabEnnemi.get(i).takeDamage(6);
				} else if (this.worldMap.getBossLevel() != null){
					this.worldMap.getBossLevel().takeDamage(6);
				}
				this.active = false;
			}
		}
	}
	
	public boolean canTouchEnnemis(Rectangle damageArea, int i)
	{
		boolean result = false;
		if(WorldMap.tabEnnemi!=null){
			try{
				if(WorldMap.tabEnnemi.get(i)!=null){
					if(damageArea.contains(WorldMap.tabEnnemi.get(i).getX() +10, WorldMap.tabEnnemi.get(i).getY() -10) ||
					   damageArea.contains(WorldMap.tabEnnemi.get(i).getX() -10, WorldMap.tabEnnemi.get(i).getY() -10) ||
					   damageArea.contains(WorldMap.tabEnnemi.get(i).getX(), WorldMap.tabEnnemi.get(i).getY() -30) ||
					   damageArea.contains(WorldMap.tabEnnemi.get(i).getX(), WorldMap.tabEnnemi.get(i).getY() +5)){
						
						result = true;
					}
				}
			} catch(IndexOutOfBoundsException e){}
		} else {
			try
			{
				if(damageArea.contains(this.worldMap.getBossLevel().getX(), this.worldMap.getBossLevel().getY()))
				{
					result = true;
				}
			}
			catch(Exception e){}
		}
		return result;
	}
	
	private void deplacementManagement(){
		this.y += this.vitesseY;
		this.x += this.vitesseX;
	}

	public void render(Graphics g) throws SlickException
	{
		if(g != null && this.hitbox != null){
			/*
			g.setColor(Color.blue);
			g.fillRect(projectileArea.x, projectileArea.y, projectileArea.width, projectileArea.height);
			*/
			
			Graphics ombre = new Graphics();
			ombre.setColor(new Color(0,0,0, 0.4f));
			ombre.fillOval(this.x-12, this.y, 20, 20); //création d'une ombre
			
			if(this.lived <= this.maxLifetime){
				g.drawAnimation(littleEnnemi[this.direction], this.x - 34, this.y - 46);
			}
			
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
}

