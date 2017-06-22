package game.items.allObjets.distance;

import org.newdawn.slick.SlickException;

import game.WorldMap;
import game.items.actionType.boutonEspace;

public class Seringue extends boutonEspace {
	public Seringue(){
		definirProprietes();
	}
	
	public void definirProprietes(){
		super.props.name = "seringue";
		super.props.damage = 4;
		super.props.src = "carte_fromage.png";
	}
	
	public void effet(WorldMap worldMap, int direction, double vitesseX, double vitesseY) throws SlickException{
		// Work in progress
		System.out.println("seringue");
	}
}
