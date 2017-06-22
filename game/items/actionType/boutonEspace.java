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
	public String getName(){
		return this.props.name;
	}
	public int getCooldown(){
		return this.props.cooldown;
	}
	public int getCurrentCooldown(){
		return this.props.currentCooldown;
	}
	public void updateCurrentCooldown(){
		if(this.props.currentCooldown == 0){
			this.props.currentCooldown = this.props.cooldown+1;
		} else {
			this.props.currentCooldown--;
		}
	}
	public String getDescription() {
		return this.props.description;
	}
}
