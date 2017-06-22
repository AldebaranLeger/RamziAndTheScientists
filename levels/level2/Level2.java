package levels.level2;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import game.BulletEnnemi;
import game.Ramzi;
import game.WorldMap;
import levels.Level;
import levels.level1.MadMouse;

public class Level2 extends Level{
	private BunNysterio bunNysterio;
	private List<BulletEnnemi> bulletEnnemi = new ArrayList<BulletEnnemi>();
	
	public Level2(WorldMap worldMap, TiledMap map, Ramzi player) throws SlickException {
		super(worldMap, map, player);
		if(WorldMap.difficulte == true) {
			super.maxEnnemisDebut = 30;
		} else {
			super.maxEnnemisDebut = 20;
		}
	}
	
	public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		nbEnnemisDebut = super.maxEnnemisDebut;
		addEnnemis();
		for(int i = 0 ; i < nbEnnemisDebut; i ++ )
		{
			tabEnnemi.get(i).init();
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
				try{
					if(tabEnnemi.get(i)!=null){
						tabEnnemi.get(i).render(g);
					}
				} catch (NullPointerException e){}
			}
		}
		ennemiBulletRefresh(g);
		renderBoss(container, stateBasedGame, g);
	}
	
	public void update(GameContainer container, StateBasedGame stateBasedGame, int delta) throws SlickException {
		if((tabEnnemi!=null)){
			//Met à jour l'état des ennemis (nombre, apparition du boss) 
			for(int i = 0 ; i < nbEnnemisDebut; i ++ )
			{
				if(tabEnnemi.get(i)!=null){
					tabEnnemi.get(i).update(delta);
					if(tabEnnemi.get(i).isSaved()){
						xEnnemiSauve = tabEnnemi.get(i).getX();
						yEnnemiSauve = tabEnnemi.get(i).getY();
						if(tabEnnemi.get(i).estAMetamorphoser()){
							tabEnnemi.remove(i);
							totalEnnemisSauves++;
							nbEnnemisDebut--;
						}
					}
				}
				
				if(tabEnnemi.size()==0){
					tabEnnemi=null;
					bunNysterio = new BunNysterio(map, player, xEnnemiSauve, yEnnemiSauve); //le boss bunnySterious apparaît aux coordonnées du dernier ennemi sauvé
				}
			}
			for(int j=0; j<this.bulletEnnemi.size(); j++)
			{
				if(this.bulletEnnemi!=null)
				{
					this.bulletEnnemi.get(j).update(delta);
					if(this.bulletEnnemi.get(j).getActive() == false)
					{
						this.bulletEnnemi.remove(j);
					}
				}
			}			
		} else if(bossArrives) {
			if(bunNysterio!=null) {
				worldMap.hud.setBossMaxHp(bunNysterio.getMaxPv());
				worldMap.hud.updateBossHp(bunNysterio.getPtVie());
				if(!bunNysterio.isSaved()) {
					bunNysterio.update(container, stateBasedGame, delta);
				} else {
					youWin=true;
					worldMap.setFinDePartie(true); //fin du jeu quand le joueur a gagné. 
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
						tabEnnemi.add(new Lapin1(this.worldMap, map, player, xEnnemi,yEnnemi, i));
					else
						tabEnnemi.add(new Lapin2(this.worldMap,map,player,xEnnemi,yEnnemi, i, this));
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
		if(bunNysterio!=null && !bossArrives){
			bossArrives=true;
			System.out.println("Bun Nysterious apparait.");
			bunNysterio.init();
		}
		if(bossArrives){
			if(bunNysterio!=null){
				if(!bunNysterio.isSaved()){
					this.bunNysterio.render(container, stateBasedGame, g);
				} else {
					bunNysterio = null;
				}
			}
		}		
	}
	
	private void ennemiBulletRefresh(Graphics g) throws SlickException {
		if(bulletEnnemi!=null){
			for(int i = 0; i< this.bulletEnnemi.size(); i++){
				if(this.bulletEnnemi.get(i)!=null){
					this.bulletEnnemi.get(i).render(g);
				}
			}
		}
	}
	
	public void createEnnemiProjectile(Lapin2 ennemi, int directionProjectile) throws SlickException
	{
		if(this.bulletEnnemi!= null){
			this.bulletEnnemi.add(new BulletEnnemi(worldMap, map, player, directionProjectile, ennemi));
		}		
	}
	
	public BunNysterio getBoss() {return this.bunNysterio;}
}
