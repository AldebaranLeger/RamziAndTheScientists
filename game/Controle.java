package game;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;

public class Controle implements KeyListener, MouseListener{
	private Ramzi ramzi;
	private WorldMap worldMap;
	private int xKeyCounter = 0;
	private int yKeyCounter = 0;
	
	public Controle(WorldMap worldMap, Ramzi player){
		this.worldMap = worldMap;
		this.ramzi=player;
	}
	
	  public void setInput(Input input) { }
	  public boolean isAcceptingInput() { return true; }
	  public void inputEnded() { }
	  public void inputStarted() { }
	  public void keyPressed(int key, char c) {
		  if(worldMap.afficheDialog == false)
		  {
			  switch (key) {
			  case Input.KEY_Z:
			    this.ramzi.setDy(-1);
			    this.ramzi.furtherY(-1.05);
			    this.yKeyCounter++;
			    break;
			  case Input.KEY_Q:
			    this.ramzi.setDx(-1);
			    this.ramzi.furtherX(-1.05);
			    this.xKeyCounter++;
			    break;
			  case Input.KEY_S:
			    this.ramzi.setDy(1);
			    this.ramzi.furtherY(1.05);
			    this.yKeyCounter++;
			    break;
			  case Input.KEY_D:
			    this.ramzi.setDx(1);
			    this.ramzi.furtherX(1.05);
			    this.xKeyCounter++;
			    break;
			  case Input.KEY_UP:
			    this.ramzi.setDy(-1);
			    this.ramzi.furtherY(-1.05);
			    this.yKeyCounter++;
			    break;
			  case Input.KEY_LEFT:
			    this.ramzi.setDx(-1);
			    this.ramzi.furtherX(-1.05);
			    this.xKeyCounter++;
			    break;
			  case Input.KEY_DOWN:
				    this.ramzi.setDy(1);
			    this.ramzi.furtherY(1.05);
			    this.yKeyCounter++;
			    break;
			  case Input.KEY_RIGHT:
			    this.ramzi.setDx(1);
			    this.ramzi.furtherX(1.05);
			    this.xKeyCounter++;
			    break;
			  case Input.KEY_SPACE:
				  try {
					this.ramzi.boutonEspacePressed();
				  } catch (SlickException e) {}
				  break;
			  }
		  } else {
			  this.ramzi.stopFurtherY();
			  ramzi.setDy(0); 

			  this.ramzi.stopFurtherX();
			  ramzi.setDx(0); 
			  
			  this.yKeyCounter = 0;
			  this.xKeyCounter = 0;
		  }
	  }
	  public void keyReleased(int key, char c) {
		  if(worldMap.afficheDialog == false)
		  {
			  switch (key) {
				  case Input.KEY_Z: 
					  if(this.yKeyCounter!=0){
					    this.yKeyCounter--;
						if(this.yKeyCounter==0){
							  this.ramzi.stopFurtherY();
								ramzi.setDy(0);
						}
					}
					  break;
				  case Input.KEY_S:  
					  if(this.yKeyCounter!=0){
						    this.yKeyCounter--;
							if(this.yKeyCounter==0){
								  this.ramzi.stopFurtherY();
								  ramzi.setDy(0); 
							}
						}
					  break;
				  case Input.KEY_Q:
					  if(this.xKeyCounter!=0){
						    this.xKeyCounter--;
							if(this.xKeyCounter==0){
								  this.ramzi.stopFurtherX();
								  ramzi.setDx(0); 
							}
						}
					  break;
				  case Input.KEY_D:  
					  if(this.xKeyCounter!=0){
						    this.xKeyCounter--;
							if(this.xKeyCounter==0){
								  this.ramzi.stopFurtherX();
								  ramzi.setDx(0);
							}
						}
					  break;
				  case Input.KEY_UP: 
					  if(this.yKeyCounter!=0){
						    this.yKeyCounter--;
							if(this.yKeyCounter==0){
								  this.ramzi.stopFurtherY();
								  ramzi.setDy(0); 
							}
						}
					  break;
				  case Input.KEY_DOWN:  
					  if(this.yKeyCounter!=0){
						    this.yKeyCounter--;
							if(this.yKeyCounter==0){
								  this.ramzi.stopFurtherY();
								  ramzi.setDy(0); 
							}
						}
					  break;
				  case Input.KEY_LEFT: 
					  if(this.xKeyCounter!=0){
						    this.xKeyCounter--;
							if(this.xKeyCounter==0){
								  this.ramzi.stopFurtherX();
								  ramzi.setDx(0); 
							}
						}
					  break;
				  case Input.KEY_RIGHT: 
					  if(this.xKeyCounter!=0){
						    this.xKeyCounter--;
							if(this.xKeyCounter==0){
								  this.ramzi.stopFurtherX();
								  ramzi.setDx(0); 
							}
						}
					  break;
			  }
		  } else {
			  this.ramzi.stopFurtherY();
			  ramzi.setDy(0); 

			  this.ramzi.stopFurtherX();
			  ramzi.setDx(0); 
			  
			  this.yKeyCounter = 0;
			  this.xKeyCounter = 0;
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
				if(worldMap.afficheDialog == false)
					this.ramzi.prepareAttaqueCAC(mX, mY);
		    break;
			case 1:
			try {
				this.ramzi.attackADistance(mX, mY);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		    break;
		}
	}
	public void mouseReleased(int arg0, int arg1, int arg2) {}
	public void mouseWheelMoved(int arg0) {}
	
	}