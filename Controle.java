import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public class Controle implements KeyListener {
	private SourisEnnemie ramzi;
	
	public Controle(SourisEnnemie player){
		this.ramzi=player;
	}
	
	  @Override
	  public void setInput(Input input) { }

	  @Override
	  public boolean isAcceptingInput() { return true; }
	 
	  @Override
	  public void inputEnded() { }

	  @Override
	  public void inputStarted() { }

	  @Override
	  public void keyPressed(int key, char c) {
		  switch (key) {
		  case Input.KEY_Z:
		    this.ramzi.setDy(-1);
		    break;
		  case Input.KEY_Q:
		    this.ramzi.setDx(-1);
		    break;
		  case Input.KEY_S:
		    this.ramzi.setDy(1);
		    break;
		  case Input.KEY_D:
		    this.ramzi.setDx(1);
		    break;
		  case Input.KEY_UP:
		    this.ramzi.setDy(-1);
		    break;
		  case Input.KEY_LEFT:
		    this.ramzi.setDx(-1);
		    break;
		  case Input.KEY_DOWN:
		    this.ramzi.setDy(1);
		    break;
		  case Input.KEY_RIGHT:
		    this.ramzi.setDx(1);
		    break;
		  }
	  }

	  @Override
	  public void keyReleased(int key, char c) {
		  switch (key) {
			  case Input.KEY_Z: ramzi.setDy(0); break;
			  case Input.KEY_S:  ramzi.setDy(0); break;
			  case Input.KEY_Q: ramzi.setDx(0); break;
			  case Input.KEY_D: ramzi.setDx(0); break;
			  case Input.KEY_UP: ramzi.setDy(0); break;
			  case Input.KEY_DOWN:  ramzi.setDy(0); break;
			  case Input.KEY_LEFT: ramzi.setDx(0); break;
			  case Input.KEY_RIGHT: ramzi.setDx(0); break;
		  }
	  }
	}