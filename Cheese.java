import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Cheese
{
	int livedCheese = 0;
	Vector2f pos;
	Vector2f dir;
	MadMouse madMouse;
	WorldMap wp;
	
	public Cheese(WorldMap wp, Ramzi player, MadMouse mm, int delta){
		
		float xDirection, yDirection;
		this.wp = wp;
		this.madMouse = mm;
		
		
		if(player.getX() > madMouse.getX())
			xDirection = player.getX();
		else
			xDirection = -player.getX();
		if(player.getY() > madMouse.getY())
			yDirection = player.getY();
		else
			yDirection = -player.getY();
		
		
		
		//System.out.println("xDirection = " + xDirection + ", yDirection = "+ yDirection);
		pos = new Vector2f();
		dir = new Vector2f(xDirection, yDirection);
		//System.out.println("Cheese : " + pos );
		//dir = new Vector2f(player.getX(), player.getY());

	}
	
	
	public void update(int delta, int i)
	{
		Vector2f realSpeed = dir.copy();
		realSpeed.scale(delta/1000.0f);
		pos.add(realSpeed);
		livedCheese += delta;
		if (livedCheese > 2000)
			wp.destroyMadMouseCheese(i);
		
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException
	{

		//System.out.println("pos.getX() : "+pos.getX() + "pos.getY() : "+pos.getY());
		g.setColor(new Color(0,0,0, 0.5f));
		g.fillOval(pos.getX(), pos.getY(), 20, 20); //création d'une ombre
		g.setColor(Color.yellow);
		g.fillOval(pos.getX(), pos.getY(), 20, 20);
	}
	
}
