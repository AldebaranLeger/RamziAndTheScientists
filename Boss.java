import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.tiled.TiledMap;

public class Boss
{
	protected TiledMap map;
	protected Ramzi player;
	protected float x, y;
	protected int direction;
	protected Animation[] animations = new Animation[8];
	protected Animation[] animationsAttaque = new Animation[4];
	protected int maxPv;
	protected int ptVie;
	protected int nbColonne = 9;
	protected boolean living = true;
	protected String srcSpriteBoss;
	
	/*  Collision  */
	protected Circle zoneCollision;
	protected boolean collisionPerso = false;	
	protected String whereIsCollisionPerso = "";
	
	/*  Cooldown  */
	protected int cooldownAttaque = 0;
	protected int bossTimer = 0; // Timer qui permet de déclencher les différentes attaques
	
	public void init() throws SlickException{}
	
	public Animation[] prepareAnimation() throws SlickException {

		SpriteSheet spriteBoss = new SpriteSheet("ressources/sprites/"+ srcSpriteBoss +".png", 128, 128);
		this.animations[0] = loadAnimation(spriteBoss, 0, 1, 0);
		this.animations[1] = loadAnimation(spriteBoss, 0, 1, 1);
		this.animations[2] = loadAnimation(spriteBoss, 0, 1, 2);
		this.animations[3] = loadAnimation(spriteBoss, 0, 1, 3);
		this.animations[4] = loadAnimation(spriteBoss, 1, nbColonne, 0);
		this.animations[5] = loadAnimation(spriteBoss, 1, nbColonne, 1);
		this.animations[6] = loadAnimation(spriteBoss, 1, nbColonne, 2);
		this.animations[7] = loadAnimation(spriteBoss, 1, nbColonne, 3);

		return animations;
	}

	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}

	
	public void render() throws SlickException{}
	
	public void update() throws SlickException{}
	
	protected void canHitRamzi()
	{
	  	if(zoneCollision.intersects(this.player.calcZoneCollision()))
	  	{
	  		this.player.takeDamage(2);
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

	public boolean isCollisionPersos()
	{
		collisionPerso = false;
		calcZoneCollision();
		if(zoneCollision.contains(this.player.calcZoneCollision().getMinX()+3,
					this.player.calcZoneCollision().getMinY()+3))
		{
			collisionPerso = true;
			whereIsCollisionPerso = "topleft";
		} else if(zoneCollision.contains(this.player.calcZoneCollision().getMaxX()-3,
						this.player.calcZoneCollision().getMinY()+3))
		{
			collisionPerso = true;
			whereIsCollisionPerso = "topright";
		} else if(zoneCollision.contains(this.player.calcZoneCollision().getMinX()+3,
						this.player.calcZoneCollision().getMaxY()-3))
		{
			collisionPerso = true;
			whereIsCollisionPerso = "botleft";
		} else if(zoneCollision.contains(this.player.calcZoneCollision().getMaxX()-3,
						this.player.calcZoneCollision().getMaxY()-3))
		{
			collisionPerso = true;
			whereIsCollisionPerso = "botright";
		}
			
		return collisionPerso;
	}
	
	protected void knockBack(String whichCollision)
	{
  		switch(whereIsCollisionPerso) {
  		case "topleft" :
  			x -= 2;
  			y -= 2;
  			break;
  		case "topright" :
  			x += 2;
  			y -= 2;
  			break;
  		case "botleft" :
  			x -= 2;
  			y += 2;
  			break;
  		case "botright" :
  			x += 2;
  			y += 2;
  			break;
  		}
	}
	
	public Circle calcZoneCollision()
	{
		zoneCollision = new Circle(this.x, this.y, 24);
		
		return zoneCollision;

	}
	
	private float getDiffPosition(float playerPosition, float ennemiPosition)
	{
		if (playerPosition > ennemiPosition)
		{
			return playerPosition - ennemiPosition;
		}
		else
		{
			return ennemiPosition - playerPosition;
		}
	}
	
	public void suivrePlayer(Ramzi player, double vitesse, int delta, boolean collision) {
		float diffX, diffY;

		diffX = getDiffPosition(player.getX(), this.x);
		diffY = getDiffPosition(player.getY(), this.y);

		if (diffX > diffY) {
			if (player.getX() > this.x){
				setDirection(3);
			}
			if (player.getX() < this.x){
				setDirection(1);
			}
		} else {
			if (player.getY() > this.y){
				setDirection(2);
			}
			if (player.getY() < this.y){
				setDirection(0);
			}
		}
		seDeplace(player.getX(), player.getY(), collision, vitesse);
	}

	/*
	 * Only used in suivrePLayer()
	 * */
	private void seDeplace(float playerX, float playerY, boolean collision, double vitesse)
	{
		float diffX = playerX - x;
		float diffY = playerY - y;
		float angle = (float) Math.atan2(diffY, diffX);
		
	  	if(collision)
	  	{
	  		if(isCollision(this.x,this.y-2)){
	  			y+=2;
	  		}
	  		if(isCollision(this.x,this.y+2)){
	  			y-=2;
	  		}
	  		if(isCollision(this.x+2,this.y)){
	  			x-=2;
	  		}
	  		if(isCollision(this.x-2,this.y)){
	  			x+=2;
	  		}

	  	}else if (isCollisionPersos()){
	  		knockBack(whereIsCollisionPerso);
	  		
	  	} else {
	 
			x += Math.cos(angle) * vitesse;
			y += Math.sin(angle) * vitesse;
		}
	}	
	
	public void setDirection(int direction) {
		this.direction = direction;
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

	public int getPtVie() {
		return this.ptVie;
	}

	public void takeDamage(int dmg){
		this.ptVie-=dmg;
		if(this.ptVie<=0){
			this.death();
		}
	}

	public float getX(){
		return this.x;
	}
	
	public float getY(){
		return this.y;
	}
	
	protected void death(){
		living=false;
	}
	
	public boolean isSaved(){
		if(!living){
			return true;
		} else {
			return false;
		}
	}
		
	public int getMaxPv(){
		return this.maxPv;
	}
}
