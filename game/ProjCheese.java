package game;
import levels.level1.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class ProjCheese
{
	private float xActuel, yActuel;
	private float xArrive, yArrive;
	private float xDiff, yDiff;
	private float xScale, yScale;
	private float dureeVieCheese = 100;
	
	public ProjCheese(Ramzi p, MadMouse mm)
	{
		System.out.println("ProjCheese mm.getX() = "+mm.getX());
		xActuel = mm.getX();
		yActuel = mm.getY();
		xArrive = p.getX();
		yArrive = p.getY();
		xDiff = xActuel - xArrive;
		yDiff = yActuel - yArrive;
		xScale = xDiff / dureeVieCheese;
		yScale = yDiff / dureeVieCheese;
		System.out.println(xActuel + ", " + yActuel);
		
	}
	
	public void update(int delta, int i)
	{
		xActuel += xScale;
		yActuel += yScale;
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		//System.out.println("pos.getX() : "+pos.getX() + "pos.getY() : "+pos.getY());
		g.setColor(new Color(0,0,0, 0.5f));
		//System.out.println("gc "+ xActuel + ", " + yActuel);
		g.fillOval(xActuel, yActuel, 20, 20); //création d'une ombre
		g.setColor(Color.yellow);
		g.fillOval(xActuel, yActuel, 20, 20);
	}
	
	
}
