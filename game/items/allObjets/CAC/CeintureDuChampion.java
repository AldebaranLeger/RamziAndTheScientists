package game.items.allObjets.CAC;

import org.newdawn.slick.SlickException;

import game.WorldMap;
import game.items.actionType.clickGauche;

public class CeintureDuChampion extends clickGauche {
	public CeintureDuChampion(){
		definirProprietes();
	}
	
	public void definirProprietes(){
		super.props.name = "ceinture";
		super.props.damage = 4;
		super.props.src = "carte_fromage.png";
	}
	
	public void effet(WorldMap worldMap, int direction, double vitesseX, double vitesseY) throws SlickException{
		// Work in progress
		System.out.println("ceinture");
	}
}
