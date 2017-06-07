package game;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Bullet 
{
	private Vector2f pos;
	private Vector2f speed;
	private int lived = 0;
	public static boolean active = true;
	private static int maxLifetime = 2000;
	
	public Bullet(Vector2f pos, Vector2f speed)
	{
		this.pos = pos;
		this.speed = speed;
//		System.out.println("Bullet, Vecteur pos : "+pos);
//		System.out.println("Bullet, Vecteur pos X : "+pos.getX());
//		System.out.println("Bullet, Vecteur pos Y : "+pos.getY());
		
	}
	
	public void update(int delta)
	{
		if(active)
		{
			Vector2f realSpeed = speed.copy();
			realSpeed.scale(delta/1000.0f);
			pos.add(realSpeed);
			
			lived += delta;
			if (lived > maxLifetime) active = false; 
		}
		
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException
	{

		//g.setColor(new Color(0,0,0, 0.5f));
		//g.fillOval(pos.getX(), pos.getY(), 20, 20); //création d'une ombre
		g.setColor(Color.red);
		g.fillOval(gc.getWidth() / 2, gc.getHeight() / 2, 20, 20);
		//System.out.println("Bullet Render val x : "+(gc.getWidth() / 2 - (int) pos.getX())+" Bullet Render val y :"+(gc.getHeight() / 2 - (int) pos.getY()));
	}
	
	public void toucheEnnemi(int i, int dmg)
	{
		WorldMap.tabEnnemi[i].takeDamage(dmg);
	}
	
	public boolean isAlive()
	{
		return active;
	}
}
