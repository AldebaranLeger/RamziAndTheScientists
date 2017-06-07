import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

public abstract class Ennemi {

	protected float x, y;
	protected int direction;
	protected boolean moving = false;
	protected Animation[] animations = new Animation[8], dyingSmoke = new Animation[1], littleMouse = new Animation[3];
	private TiledMap map;
	protected int distanceVue;
	protected int ptVie;
	protected Graphics g;
	protected boolean living=true;
	protected Rectangle hitbox;
	//timer d'animation effet
	private int smokeToken = 0;
	protected int littleMouseDirection;
	private boolean littleMouseRunning = false;
	private float littleMouseX, littleMouseY; 

	public Ennemi(TiledMap map, float x, float y) {
		this.map = map;
		this.x = x;
		this.y = y;
	}
	
	public void init() throws SlickException
	{}
	
	public void update(int delta) throws SlickException
	{}
	
	public Animation[] prepareAnimation(String srcSprite) throws SlickException {

		SpriteSheet spriteSouris = new SpriteSheet("ressources/sprites/" + srcSprite, 64, 64);
		this.animations[0] = loadAnimation(spriteSouris, 0, 1, 0);
		this.animations[1] = loadAnimation(spriteSouris, 0, 1, 1);
		this.animations[2] = loadAnimation(spriteSouris, 0, 1, 2);
		this.animations[3] = loadAnimation(spriteSouris, 0, 1, 3);
		this.animations[4] = loadAnimation(spriteSouris, 1, 9, 0);
		this.animations[5] = loadAnimation(spriteSouris, 1, 9, 1);
		this.animations[6] = loadAnimation(spriteSouris, 1, 9, 2);
		this.animations[7] = loadAnimation(spriteSouris, 1, 9, 3);

		return animations;
	}
	
	public Animation[] prepareSmokeAnimation() throws SlickException {
		SpriteSheet spriteSmoke = new SpriteSheet("ressources/sprites/Effets/Disparition_Ennemis.png", 98, 128);
		this.dyingSmoke[0] = loadAnimation(spriteSmoke, 0, 12, 0);
		return dyingSmoke;
	}
	
	public Animation[] prepareLittleMouseAnimation() throws SlickException {
		SpriteSheet spriteLittle = new SpriteSheet("ressources/sprites/PNJ/Souris_Sauvee.png", 64, 64);
		this.littleMouse[1] = loadAnimation(spriteLittle, 0, 3, 0);
		this.littleMouse[2] = loadAnimation(spriteLittle, 1, 3, 1);
		return littleMouse;
	}

	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX; x < endX; x++) 
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		
		return animation;
	}

	public void render(Graphics g) throws SlickException
	{
		this.hitbox = new Rectangle(this.x - 16, this.y - 32, 32, 32);

		if(!isSaved()) {
			createShadow(g);
		}else {
			this.escapeLittleMouse(g);
		}

	}
	
	private void escapeLittleMouse(Graphics g)
	{
		if(!littleMouseRunning){
			this.popLittleMouse(g);
		} else {
			this.runLittleMouse(g);
		}
		g.drawAnimation(dyingSmoke[0], x-32, y-90);
		smokeToken++;
		moving=false;
	}
	
	private void createShadow(Graphics g)
	{
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillOval(x - 16, y - 8, 32, 16); // création d'une ombre
		g.drawAnimation(animations[direction + (moving ? 4 : 0)], x - 32, y - 60);
	}
	
	private void popLittleMouse(Graphics g)
	{
		littleMouseX = x - 20;
		littleMouseY = y - 50;
		g.drawAnimation(littleMouse[littleMouseDirection], littleMouseX, littleMouseY);		
		littleMouseRunning = true;
	}
	
	private void runLittleMouse(Graphics g)
	{
		switch(littleMouseDirection){
			case 1 :
				littleMouseX += 5;
				break;
			case 2 :
				littleMouseX -= 5;
				break;
		}
		g.drawAnimation(littleMouse[littleMouseDirection], littleMouseX, littleMouseY);
	}
	
	public boolean estAMetamorphoser(){
		if(smokeToken>=40){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCollision(float x, float y) 
	{
		try
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
		catch(ArrayIndexOutOfBoundsException e)
		{
			this.death();
			return true;
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

	public float[] getVueEnnemi(int dist) {
		float[] aireVue = new float[4];
		this.distanceVue = dist;

		aireVue[0] = (this.x - distanceVue);
		aireVue[1] = (this.x + distanceVue);
		aireVue[2] = (this.y - distanceVue);
		aireVue[3] = (this.y + distanceVue);

		return aireVue;

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

	  	} else {
	 
			x += Math.cos(angle) * vitesse;
			y += Math.sin(angle) * vitesse;
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setRandomDirection() {
		this.direction = (int) (Math.random() * 4);
	}

	public boolean getMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public void calcHitBox()
	{
		
		if(this.hitbox!=null)
		{
			if(this.hitbox.contains(WorldMap.cursorX,WorldMap.cursorY))
			{
				this.takeDamage(2);
			}
		}
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
			return true;
		} else {
			return false;
		}
	}
}
