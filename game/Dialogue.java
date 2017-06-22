package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Dialogue{
	
	private WorldMap worldMap;
	private Image dialogBox, fleche;	
	private String[] messages; 
	private String message;
	private int cptMsg=1;
	
	public Dialogue(WorldMap worldMap, String[] messages) 
	{
		this.worldMap = worldMap;
		this.messages = messages;
		this.message = messages[0];
	}
	
	public void init() throws SlickException {
		this.dialogBox = new Image("ressources/hud/Boite_dialogue.png");
		this.fleche = new Image("ressources/hud/fleche.png");
	}
	
	public void render(Graphics g) {
		g.resetTransform();
		this.dialogBox.draw(140, 400);
		this.fleche.draw(620, 510);
		g.setColor(Color.black);
		g.drawString(message, 200, 465);
	}

	public void changeMessage() {
		message = messages[cptMsg];
		if(cptMsg<messages.length-1)
		{
			cptMsg++;
		} else {
			worldMap.afficheDialog = false;
			worldMap.deleteDialog();		
		}		
	}
}
