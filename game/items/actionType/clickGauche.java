package game.items.actionType;

import game.items.Objet;
import game.items.Props;

public abstract class clickGauche implements Objet {
	protected Props props = new Props();
	public clickGauche(){
		this.props.type = "clickGauche";
	}
	public String getSrc(){
		return this.props.src;
	}
	public String getType(){
		return this.props.type;
	}
	public String getName(){
		return this.props.name;
	}
}
