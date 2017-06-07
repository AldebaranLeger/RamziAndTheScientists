import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;


public class Camera {
	private Ramzi souris;
	private float xCamera, yCamera;
	
	public Camera(Ramzi souris) {
		this.souris = souris;
		this.xCamera = souris.getX();
		this.yCamera = souris.getY();
	}
	
	public void place(GameContainer container, Graphics g) {
		g.translate(container.getWidth() / 2 - (int) xCamera, container.getHeight() / 2 - (int) yCamera);
	}
	
	//traque le joueur
	public void update(GameContainer container) {
		int w = container.getWidth() / 4;
		  if (this.souris.getX() > this.xCamera + w) {
		    this.xCamera = this.souris.getX() - w;
		  } else if (this.souris.getX() < this.xCamera - w) {
		    this.xCamera = this.souris.getX() + w;
		  }
		  int h = container.getHeight() / 4;
		  if (this.souris.getY() > this.yCamera + h) {
		    this.yCamera = this.souris.getY() - h;
		  } else if (this.souris.getY() < this.yCamera - h) {
		    this.yCamera = this.souris.getY() + h;
		  }
	}
}
