package levels.level1;
import levels.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import game.Ennemi;
import game.ProjCheese;
import game.Ramzi;
import game.WorldMap;
import game.items.*;
import game.items.actionType.*;
import game.items.allObjets.distance.*;

public class Level1 extends Level {
	private MadMouse madMouse;
	private List<ProjCheese> tabCheese = new ArrayList<ProjCheese>();
	private boolean bossJustDied = false;
	private LootedObjet lootedObjet = null;
	private float lastMadMouseX, lastMadMouseY;
		
	public Level1(WorldMap worldMap, TiledMap map, Ramzi player)
	{
		super(worldMap, map, player);
		super.levelId = 1;
		super.nbEnnemisDebut = 1;  
	}
	
	public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		addEnnemis();
		for(int i = 0 ; i < nbEnnemisDebut; i ++ )
		{
			tabEnnemi.get(i).init();
		}
	}
	
	public void render(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
		map.render(0,0,0);
		map.render(0,0,1);
		if(this.lootedObjet != null){
			this.lootedObjet.render(g);
		}
		renderRamzi(g);
		//affiche les ennemis dans la map
		if(tabEnnemi!=null) {
			for(int i = 0 ; i < nbEnnemisDebut; i ++ )
			{
				try{
					if(tabEnnemi.get(i)!=null){
						tabEnnemi.get(i).render(g);
					}
				} catch (NullPointerException e){
					System.out.println("relou");
				}
			}
		}
		renderBoss(container, stateBasedGame, g);
	}
	
	public void update(GameContainer container, StateBasedGame stateBasedGame, int delta) throws SlickException {
		if(tabEnnemi!=null){
			nbEnnemisSauves = 0;

			// Permet de gérer la profondeur d'affichage des ennemis, 
			// mais provoque de temps à autre un NullPointerException
			
//			tabEnnemi.sort(new Comparator<Ennemi>() {
//		        @Override
//		        public int compare(Ennemi o1, Ennemi o2) {
//		            final float y1 = o1.getY();
//		            final float y2 = o2.getY();
//		            return y1 < y2 ? -1 : y1 > y2 ? 1 : 0;
//		        }
//		    });

			
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
					madMouse = new MadMouse(map, player, xEnnemiSauve, yEnnemiSauve, this); //le boss MadMouse apparaît aux coordonnées du dernier ennemi sauvé
				}
				
			}
			

			
		} else if(bossArrives) {
			if(madMouse!=null) {
				worldMap.hud.setBossMaxHp(madMouse.getMaxPv());
				worldMap.hud.updateBossHp(madMouse.getPtVie());
				if(!madMouse.isSaved()) {
					madMouse.update(container, stateBasedGame, delta);
					this.lastMadMouseX = madMouse.getX();
					this.lastMadMouseY = madMouse.getY();
					
					if(this.tabCheese.size()!=0){
						for(int i = 0; i<tabCheese.size(); i++){
							if(tabCheese.get(i)!=null){
								tabCheese.get(i).update(delta);
								if(!tabCheese.get(i).isAlive()){
									tabCheese.remove(i);
								}
							}
						}
					}
				} else {
					bossJustDied = true;
				}
				
			} else {
				if(bossJustDied){
					defineMadMouseLoot();
					this.bossJustDied = false;
					this.lootedObjet.init();
				} else if(this.lootedObjet != null) {
					lootedObjet.update(delta);
					if(this.player.calcZoneCollision().intersects(this.lootedObjet.calcZoneCollision())){
						switch(this.lootedObjet.getType()){
						case "clickDroit" :
							if(this.player.getCurrentClickDroit()!=null){
								if(this.lootedObjet.isAbleToSwitch()){
									clickDroit temp = this.player.getCurrentClickDroit();
									this.player.setCurrentClickDroit((clickDroit)this.lootedObjet.pickObjet());
									this.lootedObjet.switchObjet(temp);
								}
							} else {
								this.player.setCurrentClickDroit((clickDroit)this.lootedObjet.pickObjet());
								this.lootedObjet = null;
							}
							break;
						case "clickGauche" :
							if(this.player.getCurrentClickGauche()!=null){
								if(this.lootedObjet.isAbleToSwitch()){
									clickGauche temp = this.player.getCurrentClickGauche();
									this.player.setCurrentClickGauche((clickGauche)this.lootedObjet.pickObjet());
									this.lootedObjet.switchObjet(temp);
								}
							} else {
								this.player.setCurrentClickGauche((clickGauche)this.lootedObjet.pickObjet());
								this.lootedObjet = null;
							}
							break;
						case "boutonEspace" :
							if(this.player.getCurrentBoutonEspace()!=null){
								if(this.lootedObjet.isAbleToSwitch()){
									boutonEspace temp = this.player.getCurrentBoutonEspace();
									this.player.setCurrentBoutonEspace((boutonEspace)this.lootedObjet.pickObjet());
									this.lootedObjet.switchObjet(temp);
								}
							} else {
								this.player.setCurrentBoutonEspace((boutonEspace)this.lootedObjet.pickObjet());
								this.lootedObjet = null;
							}
							break;
						}
					}
				}
				//worldMap.setYouWin(true);
				//passage au niveau suivant
			}			
		}		
	}
	
	private void defineMadMouseLoot(){
		int index  = (int)(Math.random() * (10));
		if(index==0){
			this.lootedObjet = new LootedObjet(new Chapeau(), lastMadMouseX, lastMadMouseY);
		} else {
			this.lootedObjet = new LootedObjet(new Fromages(), lastMadMouseX, lastMadMouseY);
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
						tabEnnemi.add(new Souris1(map,xEnnemi,yEnnemi));
					else
						tabEnnemi.add(new Souris2(map,player,xEnnemi,yEnnemi));
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

	public void createMadMouseCheese(int direction)
	{
		this.tabCheese.add(new ProjCheese(this.madMouse, this.player, this.map, direction, this));
	}
	
	public void createMoreMadMouseCheese(float xPos, float yPos, int direction){
		this.tabCheese.add(new ProjCheese(xPos, yPos, this.map, this.player, direction));
	}
	
	/**
	 * affiche le boss du niveau quand le dernier ennemi est sauvé.
	 * @throws SlickException 
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
					for(int i = 0; i< this.tabCheese.size(); i++){
						if(this.tabCheese.get(i)!=null){
							this.tabCheese.get(i).render(g);
						}
					}
				} else {
					madMouse = null;
				}
			}
		}		
	}
	public MadMouse getBoss() {return this.madMouse;}
}