package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Intro extends BasicGameState implements ComponentListener  {

	private StateBasedGame stateBasedGame;
	private Animation animationsIntro[]= new Animation[2];
	private int compteurAnim = 0;
	private int id;
	private String dialogues[] = new String[5];
	private Dialogue dialogue;
	private GameContainer container;
	public boolean afficheDialog = true;
	private int numMessage = 0;
	
	public Intro(int id){
		this.id = id;
	}

	public void init(GameContainer gc, StateBasedGame stateBasedGame) throws SlickException {
		prepareAnimationIntro();
		prepareDialogues();
		this.container = gc;
		this.stateBasedGame = stateBasedGame;
	}
	
	public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
		if(compteurAnim > 10){
			g.drawAnimation(animationsIntro[0], 0, 0);
			if(compteurAnim > 20){
				compteurAnim = 0;
			}
		}else{
			g.drawAnimation(animationsIntro[1], 0, 0);
		}
		
		if(this.afficheDialog == true) {
			dialogue.render(g);
			g.setColor(Color.white);
			g.drawString("Lire : espace | Passer : echap", 600, 580);
		}
	}
	
	public void update(GameContainer gc, StateBasedGame stateBasedGame, int delta) throws SlickException {
		compteurAnim ++;
	}
	
	private void afficherMenu()
	{
		stateBasedGame.enterState(0);
	}

	public void prepareDialogues() throws SlickException {
		dialogues[0] = "Bun Nysterio : Parfois quand je ferme les yeux, je revois \nleurs grosses mains gantées m attraper par les oreilles et \nme jeter sur la table froide. La suite, je ne m en souviens \nque vaguement. La douleur infligée par leurs expériences";
		dialogues[1] = "me rendait hystérique et totalement vide d esprit... \nJamais je n aurai espérer que quelqu’un vienne un jour nous \nlibérer de cette prison.";
		dialogues[2] = "Mad Mouse : Ces scientifiques n ont rien pu faire face à \ncelui qu on surnomme Ramzi. Ce petit rat doué d un génie \net d une force inégalée nous a tous sauvé, mes petites \nsouris et moi-même. Jamais je ne saurais être assez";
		dialogues[3] = "reconnaissant pour ce qu il a fait. Te souviens-tu lorsqu \nil a ...";
		dialogues[4] = "";
		this.dialogue = new Dialogue(this, dialogues);
		dialogue.init(container);
	}
	
	public Animation[] prepareAnimationIntro() throws SlickException {
		SpriteSheet image1 = new SpriteSheet("ressources/Cinematiques/Intro.png", 877, 600);
		SpriteSheet image2 = new SpriteSheet("ressources/Cinematiques/Intro2.png", 877, 600);
		this.animationsIntro[0] = loadAnimation(image1, 0, 1, 0);
		this.animationsIntro[1] = loadAnimation(image2, 0, 1, 0);

		return animationsIntro;
	}

	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX ; x < endX ; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}

	public int getID() {
		return id;
	}
	
	public void deleteDialog() {
		dialogue = null;
	}
	
	@Override
	public void componentActivated(AbstractComponent arg0)
	{}
	
	public void keyReleased(int key, char c) {
		  switch (key) {
			  case Input.KEY_SPACE:
				  if(numMessage < 3)
				  {
				  	this.dialogue.changeMessage();
				  	numMessage ++;
			  	  }
				  else{
					  afficherMenu();
				  }
				  break;
			  case Input.KEY_ESCAPE: 
				  afficherMenu();
				  break;
		  }
	  }
}
