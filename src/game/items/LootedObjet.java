package game.items;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;

public class LootedObjet {
	private Objet item;
	private float positionX, positionY;
	private Animation[] animations = new Animation[1];
	private boolean justSwitched = false;
	private int timerJustSwitched = 0;
	
	public LootedObjet(Objet item, float positionX, float positionY){
		this.item = item;
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public Animation[] prepareAnimation() throws SlickException {

		SpriteSheet spriteRamzi = new SpriteSheet("ressources/Items/"+this.item.getSrc(), 32, 32);
		this.animations[0] = loadAnimation(spriteRamzi, 0, 1, 0);

		return animations;
	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}
	
	public void init() throws SlickException {	
		prepareAnimation();
	}

	public void render(Graphics g) throws SlickException  {
		g.setColor(new Color(0,0,0, 0.5f));
		g.fillOval(positionX - 16, positionY -8, 32, 16); //création d'une ombre
		g.drawAnimation(animations[0], positionX-16, positionY-16);
		
	}

	public void update(int delta) throws SlickException
	{
		if(this.justSwitched && this.timerJustSwitched == 0){
			this.init();
			this.timerJustSwitched++;
		} else if(this.justSwitched){
			if(this.timerJustSwitched >= 15){
				this.justSwitched = false;
				this.timerJustSwitched = 0;
			} else {
				this.timerJustSwitched++;
			}
		}
	}
	
	public String getType(){
		return this.item.getType();
	}
	
	public Circle calcZoneCollision(){
		return new Circle(this.positionX - 3, this.positionY - 3, 6);
	}
	
	public Objet pickObjet(){
		return this.item;
	}
	
	public Objet switchObjet(Objet item){
		Objet temp = this.item;
		this.item = item;
		this.justSwitched = true;
		return temp;
	}
	
	public boolean isAbleToSwitch(){
		return (!this.justSwitched);
	}
}
