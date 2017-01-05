import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;


public class Map {
	
	private TiledMap tiledMap;
	
	public Map() {
		
	}

	public void init() throws SlickException {
		this.tiledMap = new TiledMap("ressources/map/map_niv1.tmx");
	}
	
	public boolean isCollision(float x, float y) {
	    int tileW = this.tiledMap.getTileWidth();
	    int tileH = this.tiledMap.getTileHeight();
	    int collisionLayer = this.tiledMap.getLayerIndex("collision");
	    Image tile = this.tiledMap.getTileImage((int) x / tileW, (int) y / tileH, collisionLayer);
	    boolean collision = tile != null;
	    if (collision) {
	      Color color = tile.getColor((int) x % tileW, (int) y % tileH);
	      collision = color.getAlpha() > 0;
	    }
	    return collision;
	  }
	
	public void render() {
		tiledMap.render(0,0,0);
		tiledMap.render(0,0,1);
		tiledMap.render(0,0,2);
		tiledMap.render(0,0,3);
	}

}
