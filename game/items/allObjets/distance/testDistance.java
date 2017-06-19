package game.items.allObjets.distance;

import org.newdawn.slick.SlickException;

import game.WorldMap;
import game.items.actionType.clickDroit;

public class testDistance extends clickDroit {

	public testDistance(){
		definirProprietes();
	}
	
	public void definirProprietes(){
		super.props.name = "test";
		super.props.damage = 2;
		super.props.src = "Mad_Mouse_Hat.png";
	}
	
	public void effet(WorldMap worldMap, int direction, double vitesseX, double vitesseY) throws SlickException{
		System.out.println("pouf pouf");
	}
}
