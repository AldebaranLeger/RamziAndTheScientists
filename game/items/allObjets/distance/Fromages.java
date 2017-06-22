package game.items.allObjets.distance;

import org.newdawn.slick.SlickException;

import game.WorldMap;
import game.items.actionType.clickDroit;

public class Fromages extends clickDroit {
	
	public Fromages(){
		definirProprietes();
	}
	
	public void definirProprietes(){
		super.props.name = "fromages";
		super.props.damage = 2;
		super.props.src = "carte_fromage.png";
	}
	
	public void effet(WorldMap worldMap, int direction, double vitesseX, double vitesseY) throws SlickException{
		worldMap.createRamziProjectile(direction, vitesseX, vitesseY);
	}
}
