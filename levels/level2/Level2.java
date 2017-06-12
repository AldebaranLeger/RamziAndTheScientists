package levels.level2;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import game.Ramzi;
import game.WorldMap;
import levels.Level;
import levels.level1.MadMouse;
import levels.level1.Souris1;
import levels.level1.Souris2;

public class Level2 extends Level{
	private MadMouse bunnySterious; //changer classe MadMouse par celle de Bun Nysterious

	public Level2(WorldMap worldMap, TiledMap map, Ramzi player) {
		super(worldMap, map, player);
		super.levelId = 2;
		super.nbEnnemisDebut = 15;
	}
	
	public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		addEnnemis();
		for(int i = 0 ; i < nbEnnemisDebut; i ++ )
		{
			tabEnnemi[i].init();
		}
	}
	
	public void render(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
		map.render(0,0,0);
		map.render(0,0,1);
		renderRamzi(g);
		//affiche les ennemis dans la map
		if(tabEnnemi!=null) {
			for(int i = 0 ; i < nbEnnemisDebut; i ++ )
			{
				if(tabEnnemi[i]!=null){
					tabEnnemi[i].render(g);
				}
			}
		}
		renderBoss(container, stateBasedGame, g);
	}
	
	public void update(GameContainer container, StateBasedGame stateBasedGame, int delta) throws SlickException {
		if((tabEnnemi!=null)){
			nbEnnemisSauves = 0;
			//Met à jour l'état des ennemis (nombre, apparition du boss) 
			for(int i = 0 ; i < nbEnnemisDebut; i ++ )
			{
				if(tabEnnemi[i]!=null){
					tabEnnemi[i].update(delta);
					if(tabEnnemi[i].isSaved()){
						xEnnemiSauve = tabEnnemi[i].getX();
						yEnnemiSauve = tabEnnemi[i].getY();
						if(tabEnnemi[i].estAMetamorphoser()){
							tabEnnemi[i]=null;
							totalEnnemisSauves++;
						}
					}
				}
				else {
					nbEnnemisSauves++;
				}
				if(nbEnnemisSauves==nbEnnemisDebut){
					tabEnnemi=null;
					bunnySterious = new MadMouse(map, player, xEnnemiSauve, yEnnemiSauve); //le boss bunnySterious apparaît aux coordonnées du dernier ennemi sauvé
				}
			}
		} else if(bossArrives) {
			if(bunnySterious!=null) {
				worldMap.hud.setBossMaxHp(bunnySterious.getMaxPv());
				worldMap.hud.updateBossHp(bunnySterious.getPtVie());
				if(!bunnySterious.isSaved()) {
					bunnySterious.update(container, stateBasedGame, delta);
				} else {
					worldMap.setYouWin(true);
					//passage au niveau suivant
				}
				
			}			
		}		
	}
	
	private void renderRamzi(Graphics g) throws SlickException {
		if(player.getImmuniteCooldown() == 0){
			player.render(g);
		}
		else {
			if(player.getImmuniteCooldown()%6 == 1){
				player.render(g);
			}
		}
	}
	
	/**
	 * Placement des ennemis de manière aléatoire sur la carte
	 * @throws SlickException 
	 */
	public void addEnnemis() throws SlickException {
		int tileW = map.getTileWidth();
		int tileH = map.getTileHeight();
		int collisionLayer = map.getLayerIndex("collision");
		int mapLayer = map.getLayerIndex("sol");
		float yEnnemi = 0;
		float xEnnemi = 0;
		for(int i = 0 ; i < nbEnnemisDebut; i ++ )
		{
			boolean inMap = false;
			boolean inCollision = false;
			boolean inRamziRayon;
			do
			{
				inRamziRayon  = false;
				//coordonnées de l'ennemi calculé aléatoirement
				yEnnemi = (float)(Math.random() * (map.getHeight() * map.getTileHeight() - 0)); 
				xEnnemi = (float)(Math.random() * (map.getWidth() * map.getTileWidth() - 0));
				
				
				Image tileMap = map.getTileImage((int) xEnnemi / tileW, (int) yEnnemi / tileH, mapLayer);
				Image tileCol = map.getTileImage((int) xEnnemi / tileW, (int) yEnnemi / tileH, collisionLayer);
				inMap = tileMap != null;
				inCollision = tileCol != null;
				//si les coordonnées de l'ennemi font partie intégrante de la map, alors on le crée.
				if(inMap)
				{
					int random = (int)(Math.random() * 2 +1);
					if(random == 1)
						tabEnnemi[i] = new Souris1(map,xEnnemi,yEnnemi);
					else
						tabEnnemi[i] = new Lapin1(map,player,xEnnemi,yEnnemi);
				}
				// si l'ennemi est dans la rayon autour de Ramzi, alors on refait la boucle pour le placer ailleurs. 
				if (( xEnnemi > (player.getX() - 200) && xEnnemi < (player.getX() + 200))
						&& ( yEnnemi > (player.getY() - 200) && ( yEnnemi < player.getY() + 200))) 
				{
					inRamziRayon = true;
				}
			}while(!inMap || inRamziRayon);
			
		}
	}
	
	/**
	 * affiche le boss du niveau quand le dernier ennemi est sauvé.
	 * @throws SlickException 
	 */
	public void renderBoss(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
		if(bunnySterious!=null && !bossArrives){
			bossArrives=true;
			System.out.println("Bun Nysterious apparait.");
			bunnySterious.init();
		}
		if(bossArrives){
			if(bunnySterious!=null){
				if(!bunnySterious.isSaved()){
					this.bunnySterious.render(container, stateBasedGame, g);
				} else {
					bunnySterious = null;
				}
			}
		}		
	}
	public MadMouse getBoss() {return this.bunnySterious;}
}
