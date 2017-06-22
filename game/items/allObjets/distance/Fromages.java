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
		super.props.description = "Utilise le clic droit de ta souris pour lancer des fromages ! \nMaintenant diriges toi vers l’ échelle et poursuis ta quête !";
	}
	
	public void effet(WorldMap worldMap, int direction, double vitesseX, double vitesseY) throws SlickException{
		worldMap.createRamziProjectile(direction, vitesseX, vitesseY);
	}
}
