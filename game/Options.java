package game;

import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Options extends BasicGameState implements ComponentListener {

	private int id;
	private WindowGame windowGame;
	private StateBasedGame sbg;
	private Image back, difficulte, noDifficulte, backMenu;
	private MouseOverArea btnDifficulte, btnDifficulte2, btnBack;
	private boolean difficile = false;
	
	public Options(WindowGame windowGame, int id) {
		this.id = id;
		this.windowGame = windowGame;
	}
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.sbg = sbg;
		back = new Image("ressources/background/back.jpg");
		difficulte = new Image("ressources/boutons/difficulte.png");
		noDifficulte = new Image("ressources/boutons/nodifficulte.png");
		btnDifficulte = new MouseOverArea(gc, difficulte, 550, 200, this);
		btnDifficulte2 = new MouseOverArea(gc, noDifficulte, 550,200, this);
		backMenu = new Image("ressources/boutons/retourMenu.png");
		btnBack = new MouseOverArea(gc, backMenu, 550, 520, this);
	}

	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.resetTransform();
		back.draw(0, 0, gc.getWidth(), gc.getHeight());
		g.drawString("Choisis ton mode de jeu préféré !", 500, 150);
		if(difficile == true) {
			btnDifficulte.setNormalColor(Color.green);
			btnDifficulte.render(gc, g);
			btnDifficulte.setMouseOverImage(noDifficulte);
		} else {
			btnDifficulte2.setNormalColor(Color.red);
			btnDifficulte2.render(gc,g);
			btnDifficulte2.setMouseOverImage(difficulte);
		}		
		g.setColor(Color.white);
		g.drawString("CREDITS", 600, 300);
		g.drawString("Thanh-Thao HOANG", 560, 350);
		g.drawString("Erwan LE CORVIC", 560, 380);
		g.drawString("Valentin LAMPRIERE", 560, 410);
		g.drawString("Aldébaran LEGER", 560, 440);
		g.drawString("Romain NEGRI", 560, 470);
		btnBack.render(gc, g);
		btnBack.setMouseOverColor(Color.black);
	}

	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {}
	
	public void keyReleased(int key, char c) {
		if(Input.KEY_R == key) {
			sbg.enterState(WindowGame.STARTMENU);
		}
	}
	
	public int getID() {
		return id;
	}
	
	public void componentActivated(AbstractComponent source) {
		if(source == btnDifficulte) {		
			difficile = false;
			windowGame.setDifficulte(difficile);
			System.out.println("btn1 "+difficile);
		} else if (source == btnDifficulte2) {
			difficile = true;
			windowGame.setDifficulte(difficile);
			System.out.println("btn2 " +difficile);
		} else if(source == btnBack) {
			sbg.enterState(0);
		}
	}
	
	public boolean getDifficulte() {
		return this.difficile;
	}

}
