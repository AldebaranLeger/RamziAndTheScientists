package game.items.allObjets.distance;

import org.newdawn.slick.SlickException;

import game.WorldMap;
import game.items.actionType.boutonEspace;

public class Chapeau extends boutonEspace {

	public Chapeau(){
		definirProprietes();
	}
	
	public void definirProprietes(){
		super.props.name = "chapeau";
		super.props.damage = 2;
		super.props.src = "Mad_Mouse_Hat.png";
		super.props.description = "Appuie sur la touche espace pour libérer une vague de super \nsouris ! \nMaintenant diriges toi vers l’ échelle et poursuis ta quête !";
	}
	
	public void effet(WorldMap worldMap, int direction, double vitesseX, double vitesseY) throws SlickException{
		System.out.println("Le chapeaaaauuuuuu");
	}
}
