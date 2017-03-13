import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

public class Controle implements KeyListener, MouseListener{
	private Ramzi ramzi;
	
	public Controle(Ramzi player){
		this.ramzi=player;
	}
	
	  public void setInput(Input input) { }
	  public boolean isAcceptingInput() { return true; }
	  public void inputEnded() { }
	  public void inputStarted() { }
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
		  case Input.KEY_A:
			 this.ramzi.parle();
			 break;
		  }
	  }
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

	public void mouseClicked(int arg0, int arg1, int arg2, int arg3) {}
	public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {}
	public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {}
	public void mousePressed(int mouse, int mX, int mY)
	{
		switch(mouse)
		{
			case 0:
				this.ramzi.attack1(mX, mY);
		    break;
			case 1:
				this.ramzi.attackADistance(mX, mY);
		    break;
		}
	}
	public void mouseReleased(int arg0, int arg1, int arg2) {}
	public void mouseWheelMoved(int arg0) {}
	
	}