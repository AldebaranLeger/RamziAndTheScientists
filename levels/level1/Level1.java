package levels.level1;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import game.Ramzi;
import game.WorldMap;
import levels.Level;
import levels.level2.Level2;

public class Level1 extends Level {
	private MadMouse madMouse; 
		
	public Level1(WorldMap worldMap, TiledMap map, Ramzi player)
	{
		super(worldMap, map, player);
		super.nbEnnemisDebut = 1;  
	}
	
	public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		tileW = map.getTileWidth();
		tileH = map.getTileHeight();
		collisionLayer = map.getLayerIndex("collision");
		mapLayer = map.getLayerIndex("sol");
		super.prepareAnimationTreasure("treasure.png");
		
		addEnnemis();
		for(int i = 0 ; i < nbEnnemisDebut; i ++ )
		{
			tabEnnemi[i].init();
		}
		super.closedTreasure = new Image("ressources/img/closedTreasure.png"); //image du trésor fermé (avant que le joueur ne l'ouvre)
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
		if(youWin==true && treasureSquareVisible == true) {
			if(openTreasure == false)
			{
				g.setColor(Color.red);
				closedTreasure.draw(bossLastX-32, bossLastY-32);
			}
			if(createLadder && openTreasure) {
				g.drawAnimation(super.animations[0], bossLastX-32, bossLastY-32);
			}
			if(ladderSquareVisible == true) {
				g.setColor(Color.blue);
				g.fillRect(xLadder, yLadder , 10, 10);// échelle pour passer au second niveau
			}
		}
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
					madMouse = new MadMouse(map, player, xEnnemiSauve, yEnnemiSauve); //le boss MadMouse apparaît aux coordonnées du dernier ennemi sauvé
				}
			}
		} else if(bossArrives) {
			if(madMouse!=null) {
				worldMap.hud.setBossMaxHp(madMouse.getMaxPv());
				worldMap.hud.updateBossHp(madMouse.getPtVie());
				if(!madMouse.isSaved()) {
					madMouse.update(container, stateBasedGame, delta);
				} else {
					youWin = true;
					bossLastX = madMouse.getX();
					bossLastY = madMouse.getY();
					treasureSquare = new Rectangle (bossLastX, bossLastY, 32,32);			
				}
			}			
		}
		if(treasureSquare != null)
		{
			timerLadder++;
			this.prepareTreasure();			
		}
		if(createLadder == true) 
		{
			this.prepareNextLevel();
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
	
	private void prepareTreasure()
	{
		if(timerLadder > 50) {
			treasureSquareVisible = true;
			if(treasureSquare.intersects(player.calcZoneCollision())) 
			{
				openTreasure = true; // déclenche l'animation
				createLadder = true; //créer l'échelle après l'ouverture du trésor contenant les items
			}
			timerLadder = 0;
		}
	}
	
	private void prepareNextLevel()
	{
		//si l'échelle n'est pas crée alors on la crée.
		if(yLadder == 0 && xLadder == 0)
		{
			addLadder();
		}
		//si le joueur passe sur l'échelle, on passe au niveau suivant
		if(ladderSquare.intersects(player.calcZoneCollision())) 
		{
			try {
				nextLevel();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void nextLevel() throws SlickException
	{		
		TiledMap map2 = new TiledMap("ressources/map/map2.tmx"); //map niveau 2
		Level nextLevel = new Level2(worldMap, map2, player);
		worldMap.updateCurrentLevel(nextLevel);
		worldMap.initLevel();
		player.setMap(map2);
		player.setX(560); //on réinitialise la position du joueur sur la nouvelle map
		player.setY(180);
	}
	
	private void addLadder() 
	{
		do {
			inRamziRayon  = false;
			yLadder = (float)(Math.random() * ((bossLastY +60) - (bossLastY -60)) + (bossLastY -60)); 
			xLadder = (float)(Math.random() * ((bossLastX +60) - (bossLastX-60)) + (bossLastX-60));		
			Image tileMap = map.getTileImage((int) xLadder / tileW, (int) yLadder / tileH, mapLayer);
			Image tileCol = map.getTileImage((int) xLadder / tileW, (int) yLadder / tileH, collisionLayer);
			inMap = tileMap != null;
			inCollision = tileCol != null;
			if(inMap)
			{
				ladderSquare = new Rectangle(xLadder , yLadder, 10, 10); //zone de collision de l'échelle (passage au niveau suivant)
				ladderSquareVisible = true;
			}
			// si l'ennemi est dans la rayon autour de Ramzi, alors on refait la boucle pour le placer ailleurs. 
			if (( xLadder > (player.getX() - 60) && xLadder < (player.getX() + 60))
					&& ( yLadder > (player.getY() - 60) && ( yLadder < player.getY() + 60))) 
			{
				inRamziRayon = true;
			}
		}while(!inMap || inRamziRayon);
	}
	
	/**
	 * Placement des ennemis de manière aléatoire sur la carte
	 * @throws SlickException 
	 */
	public void addEnnemis() throws SlickException {
		float yEnnemi = 0;
		float xEnnemi = 0;
		for(int i = 0 ; i < nbEnnemisDebut; i ++ )
		{
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
						tabEnnemi[i] = new Souris2(map,player,xEnnemi,yEnnemi);
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
	 */
	public void renderBoss(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
		if(madMouse!=null && !bossArrives){
			bossArrives=true;
			System.out.println("MadMouse apparait.");
			madMouse.init();
		}
		if(bossArrives){
			if(madMouse!=null){
				if(!madMouse.isSaved()){
					this.madMouse.render(container, stateBasedGame, g);
				} else {
					madMouse = null;
				}
			}
		}		
	}
	public MadMouse getBoss() {return this.madMouse;}
}
