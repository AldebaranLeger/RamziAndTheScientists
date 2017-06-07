import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class Camera {
	private Ramzi player;
	private float xCamera, yCamera; //coordonnées du point que regarde la caméra (centre de l'écran)
	
	public Camera(Ramzi player) {
		this.player = player;
		this.xCamera = player.getX();
		this.yCamera = player.getY();
	}
	
	public void place(GameContainer container, Graphics g) {
		g.translate(container.getWidth() / 2 - (int) xCamera, container.getHeight() / 2 - (int) yCamera);
	}
	
	//traque le joueur
	public void update(GameContainer container) {
		refreshCamera(container);
	}
	
	public void refreshCamera(GameContainer container) {
		int containerWidth = container.getWidth() / 4;
		this.xCamera = cameraPosition(this.player.getX(), containerWidth);
		
		int containerHeight = container.getHeight() / 4;
		this.yCamera = cameraPosition(this.player.getY(), containerHeight);
	}
	
	public float cameraPosition(float playerPosition, int containerDimension){
		float result = 0;
		if (playerPosition > this.yCamera + containerDimension) {
		    result = playerPosition - containerDimension;
		  } else if (playerPosition < this.yCamera - containerDimension) {
		    result = playerPosition + containerDimension;
		  }
		return result;
	}
}
