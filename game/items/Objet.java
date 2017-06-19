package game.items;

import org.newdawn.slick.SlickException;

import game.WorldMap;

public interface Objet {
	public void definirProprietes();
	public void effet(WorldMap worldMap, int direction, double vitesseX, double vitesseY) throws SlickException;
	public String getSrc();
	public String getType();
}
