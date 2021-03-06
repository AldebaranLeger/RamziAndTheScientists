package game;
import levels.level1.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Hud {
	// position de la barre (coin sup�rieur gauche)
	private static final int P_BAR_X = 10;
	private static final int P_BAR_Y = 10;
		
	private static final int I_BAR_X = 350;
	private static final int I_BAR_Y = 530;
	private static final int LIFE_PLAYER__BAR_X = -206 + P_BAR_X;
	private static final int LIFE_PLAYER__BAR_Y = -57 - P_BAR_Y;
	private static final int ATQ_BAR_Y = 24 + P_BAR_Y;
	private static final int CPT_ENNEMI_BAR_X = 30 + P_BAR_X;
	private static final int CPT_ENNEMI_BAR_Y = 60 + P_BAR_Y;
	private static final int LIFE_BOSS_BAR_X = 200;
	private static final int LIFE_BOSS_BAR_Y = 460;

	private static final Color LIFE_COLOR = new Color(210, 0, 0);
	private static final Color ATQ_COLOR = new Color(0, 0, 255);
	private static final Color CPT_ENNEMI_COLOR = new Color(31, 31, 109);
	private static final int BAR_WIDTH = 175;
	private static final int BAR_HEIGHT = 31;
	private Image playerBars, playerItems, bossBar, bossBar2;
	private Ramzi player;
	private int playerMaxHp, bossMaxHp, currentBossHp;
	private WorldMap worldMap;
	private int maxEnnemis, nbEnnemisSauves;
		
	public Hud(Ramzi player){
		this.player = player;
	}

	public void init(WorldMap worldMap) throws SlickException {	
		this.worldMap = worldMap;
		//nb ennemi au d�but du niveau
		this.maxEnnemis = worldMap.getEnnemisDebut();
		playerManagement();
	} 
	  
	public void render(Graphics g) throws SlickException {
		//annule la cam�ra car le hud est fixe
		g.resetTransform();
		if(worldMap.getBossLevel() == null){
			renderEnnemiBar(g);
		}
		if(this.currentBossHp!=0 && this.currentBossHp > 0) {
			if(worldMap.getBossLevel() != null){
				renderLifeBossBar(g);
			}
			if(worldMap.getBossLevel() != null){
				if(worldMap.getBossLevel().getBossName().equals("MadMouse")){
					g.drawImage(this.bossBar, 180, 340);
				}else if(worldMap.getBossLevel().getBossName().equals("BunNysterio")){
					g.drawImage(this.bossBar2, 180, 340);
				}
			}
		}
		renderLifePlayerBar(g);
		drawHud(g);
		//renderAtqBar(g);
	}
	
	public void update(int delta)
	{
		this.maxEnnemis = worldMap.getEnnemisDebut();
		this.nbEnnemisSauves = worldMap.getEnnemisSauves();
	}
  
	private void playerManagement() throws SlickException{
		this.playerMaxHp = player.getHp();
		this.playerBars = new Image("ressources/hud/hud_2.png");
		this.playerItems = new Image("ressources/hud/HUD_Items.png");
		this.bossBar = new Image("ressources/hud/Lifebar_Boss_V2.png");
		this.bossBar2 = new Image("ressources/hud/Lifebar_Boss_BunnySterio.png");
	}
	
	private void renderLifeBossBar(Graphics g) {
		g.setColor(LIFE_COLOR);
		g.fillRect(LIFE_BOSS_BAR_X, LIFE_BOSS_BAR_Y, ((float)this.bossMaxHp - ((float)this.bossMaxHp - (float)this.currentBossHp))* 510 / (float)this.bossMaxHp, 28);
	}
  
	private void renderLifePlayerBar(Graphics g) {
		g.rotate(0, 0, 180);
		g.setColor(LIFE_COLOR);		
		g.fillRect(LIFE_PLAYER__BAR_X, LIFE_PLAYER__BAR_Y, ((float)this.playerMaxHp - ((float)this.playerMaxHp - (float)this.player.getCurrentHp()))* BAR_WIDTH / (float)this.playerMaxHp, BAR_HEIGHT);		
		g.resetTransform();
	}
  
	/*private void renderAtqBar(Graphics g) {
		g.setColor(ATQ_COLOR);
		g.fillRect(BAR_X, ATQ_BAR_Y, .8f * BAR_WIDTH, BAR_HEIGHT);
	}	*/
  
	private void renderEnnemiBar(Graphics g) {
		g.setColor(CPT_ENNEMI_COLOR);
		//jauge qui diminue � chaque ennemi sauv�
		g.fillRect(CPT_ENNEMI_BAR_X, CPT_ENNEMI_BAR_Y, ((float)this.maxEnnemis - (float)this.nbEnnemisSauves) * BAR_WIDTH / (float)this.maxEnnemis , BAR_HEIGHT);
	}
  
	private void drawHud(Graphics g) throws SlickException {
		g.drawImage(this.playerBars, P_BAR_X, P_BAR_Y); //dessine les jauges
		g.drawImage(this.playerItems, I_BAR_X, I_BAR_Y); //dessine le bloc des items
		this.itemsManagement(g);
		g.setColor(Color.white);
		g.drawString("ClicL", I_BAR_X+5, I_BAR_Y +45 );
		g.drawString("ClicR", I_BAR_X + 60, I_BAR_Y + 45);
	}
	
	private void itemsManagement(Graphics g) throws SlickException{
		if(this.player.getCurrentClickGauche()!=null){
			g.drawImage(new Image("ressources/items/"+this.player.getCurrentClickGauche().getSrc()), I_BAR_X, I_BAR_Y);
		}
		if(this.player.getCurrentClickDroit()!=null){
			g.drawImage(new Image("ressources/items/"+this.player.getCurrentClickDroit().getSrc()), I_BAR_X+63, I_BAR_Y+8);
		}
		if(this.player.getCurrentBoutonEspace()!=null){
			g.drawImage(new Image("ressources/items/"+this.player.getCurrentBoutonEspace().getSrc()), I_BAR_X+108, I_BAR_Y+8);
			if(this.player.getCurrentBoutonEspace().getCurrentCooldown()!=this.player.getCurrentBoutonEspace().getCooldown()+1){
				g.setColor(new Color(68,68,68,0.5f));
				g.fillRect( I_BAR_X+108, I_BAR_Y+40, 32, 
						-((float)this.player.getCurrentBoutonEspace().getCurrentCooldown()*32 / (float)this.player.getCurrentBoutonEspace().getCooldown()));
			}
		}
	}
	
	public void updateBossHp(int hp){
		this.currentBossHp = hp;
	}
	
	public void setBossMaxHp(int hp){
		this.bossMaxHp = hp;
	}
	  	  
}