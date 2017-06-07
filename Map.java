import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;


public class Map {
	
	private TiledMap tiledMap;
	
	public Map() {}

	public void init() throws SlickException {
		this.tiledMap = new TiledMap("ressources/map/map_test3.tmx");
	}
	
	public void render() {
		tiledMap.render(0,0,0);
		tiledMap.render(0,0,1);
		tiledMap.render(0,0,2);
		tiledMap.render(0,0,3);
	}

}
