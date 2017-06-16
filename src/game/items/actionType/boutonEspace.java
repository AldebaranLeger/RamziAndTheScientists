package game.items.actionType;

import game.items.Objet;
import game.items.Props;

public abstract class boutonEspace implements Objet {
	protected Props props = new Props();
	public boutonEspace(){
		this.props.type = "boutonEspace";
	}
	public String getSrc(){
		return this.props.src;
	}
	public String getType(){
		return this.props.type;
	}
}
