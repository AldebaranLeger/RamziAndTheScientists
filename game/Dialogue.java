package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import levels.level1.Level1;

public class Dialogue{
	
	private WorldMap worldMap;
	private Intro intro;
	private Image dialogBox, fleche;	
	private String[] messages; 
	private String message;
	private int cptMsg=1;
	private Level1 level1;
	
	public Dialogue(WorldMap worldMap, String[] messages) 
	{
		this.worldMap = worldMap;
		this.messages = messages;
		this.message = messages[0];
	}
	
	public Dialogue(Intro intro, String[] messages) 
	{
		this.intro = intro;
		this.messages = messages;
		this.message = messages[0];
	}
	
	public Dialogue(Level1 level1, String[] messages) 
	{
		this.level1 = level1;
		this.messages = messages;
		this.message = messages[0];
	}
	
	public void init(GameContainer gameContainer) throws SlickException {
		this.dialogBox = new Image("ressources/hud/Boite_dialogue.png");
		this.fleche = new Image("ressources/hud/fleche.png");
	}
	
	public void render(Graphics g) {
		g.resetTransform();
		this.dialogBox.draw(140, 400);
		if(intro == null){
			g.setColor(Color.white);
			g.drawString("Lire : espace", 750, 580);
		}
		g.setColor(Color.black);
		g.drawString(message, 200, 465);
	}

	public void changeMessage() {
		message = messages[cptMsg];
		if(cptMsg<messages.length-1)
		{
			cptMsg++;
		} else {
			if(intro == null){
				worldMap.afficheDialog = false;
				worldMap.deleteDialog();
			}else{
				intro.afficheDialog = false;
				intro.deleteDialog();
			}	
		}		
	}
}
