package game.items.actionType;

import game.items.Objet;
import game.items.Props;

public abstract class clickDroit implements Objet {
	protected Props props = new Props();
	public clickDroit(){
		this.props.type = "clickDroit";
	}
	public String getSrc(){
		return this.props.src;
	}
	public String getType(){
		return this.props.type;
	}
}
