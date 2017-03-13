import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Projectile
{

	private int xInit,yInit;
	private int xArrive, yArrive;
	private int x, y;
	private Image image;
	
	public Projectile(int xRamzi, int yRamzi, int xClic, int yClic)
	{
		this.xInit = xRamzi;
		this.yInit = yRamzi;
		this.xArrive = xClic;
		this.yArrive = yClic;
		
	}
	
	public void init() throws SlickException
	{
		image = new Image("ressources/sprites/projectile1.png");
	}
	
	
	public void render(Graphics g) throws SlickException  {
		g.setColor(new Color(0,0,0, 0.5f));
		g.fillOval(x - 16, y -8, 32, 16); //création d'une ombre
	
	}
	
	public void update(int delta) throws SlickException
	{

	}
		
	
}
