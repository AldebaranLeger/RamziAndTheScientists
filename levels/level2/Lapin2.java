package levels.level2;

import org.newdawn.slick.tiled.TiledMap;

import game.Ennemi;

/*Lapins qui se d�place al�atoirement et qui lance des projectiles sur le joueur d�s que celui ci est dans son champ de vision*/
public class Lapin2 extends Ennemi {

	public Lapin2(TiledMap map, float x, float y) {
		super(map, x, y);
	}

}
