package game;

import java.util.List;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.lwjgl.opengl.Display;

import game.items.AllyFromItem;
import levels.*;
import levels.level1.*;

public class WorldMap extends BasicGameState implements ComponentListener {
	
	public WorldMap (){}
		public int id;
		private StateBasedGame stateBasedGame; //isolated different stages of the game (menu, ingame, hiscores, etc) into different states so they can be easily managed and maintained.
		private GameContainer container; //GameContainer contient le contexte dans lequel notre jeu sera exécuté
		private TiledMap map1; //une map pour chaque niveau
		private Ramzi player;
		private Camera camera;
		public static List<Ennemi> tabEnnemi;
		public static Adn[] coeurs = new Adn[25];
		private List<Bullet> bullet = new ArrayList<Bullet>();
		private List<AllyFromItem> armyOfAlly = new ArrayList<AllyFromItem>();
		public static float cursorX, cursorY;
		public Hud hud;
		private Dialogue dialogue;
		private Dialogue dialogueSave;
		private String[] dialogues = new String[3];
		private String[] dialoguesSave = new String[2];
		//public static MadMouse madMouse = null;
		//menu pause
		private boolean escapeMenu;
		private Image btnResume, btnExit, btnMainMenu, txtGameOver, txtYouWin;
		private MouseOverArea resume, exit, mainMenu;		
		//écran Game Over
		private boolean gameOver = false; 
		private boolean finDePartie = false;
		private int attaqueDistanceCooldown = -1;
		public static boolean difficulte; 
		public boolean afficheDialog = true;
		private boolean boiteDeDialogueDejaAffiche = false;
		
		private Level currentLevel; 
		
		public WorldMap (int id) {
			this.id = id;
			WorldMap.tabEnnemi =  new ArrayList<Ennemi>();
		}
		
		/**
		 * initialise le contenu du jeu, charge les ressources (les graphismes, la musique, etc.)
		 */	
	public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		this.stateBasedGame = stateBasedGame;
		this.container = container;
		this.map1 = new TiledMap("ressources/map/map1.tmx"); //initialisation de la map (niveau 1)
		
		player = new Ramzi(map1, this);	
		player.init();

		this.currentLevel = new Level1(this, map1, player); //niveau 1 au démarrage du jeu (sauvegarde impossible)	
		initLevel();
		
		this.hud = new Hud(player);
		this.hud.init(this);
		camera = new Camera(player);
				
		Controle control = new Controle(this, player);
		this.container.getInput().addKeyListener(control);
		this.container.getInput().addMouseListener(control);
		
		createMenu();
		prepareDialogues();
	
	}

	public void initLevel() throws SlickException 
	{
		tabEnnemi = new ArrayList<Ennemi>();
		this.currentLevel.setTabEnnemi(tabEnnemi);
		this.currentLevel.init(container, stateBasedGame);
	}
	
	/**
	 * affiche le contenu du jeu
	 */
	public void render(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException 
	{		
		g.translate(container.getWidth() / 2 - player.getX(), container.getHeight() / 2 - player.getY());//caméra suiveuse
		this.currentLevel.render(container, stateBasedGame, g); //affiche map, ennemis et boss du niveau courant
		playerBulletRefresh(g);
		playerArmyOfAllyRefresh(g);
		renderMenu(g);		
		if(this.dialogueSave != null)
		{
			this.dialogueSave.render(g);
		}	
	}
	
	/**
	 * met à jour les éléments de la scène en fonction des entrées utilisateurs ou autre
	 */
	public void update(GameContainer container, StateBasedGame stateBasedGame, int delta) throws SlickException
	{
		if(this.dialogueSave != null){
			this.afficheDialog = true;
		}
		if(this.currentLevel.getTotalEnnemisSauves() == 1 && boiteDeDialogueDejaAffiche == false)
		{
			prepareDialoguesSave();
			boiteDeDialogueDejaAffiche = true;
		}
		if(!Display.isActive()){
			escapeMenu = true;
		}
		if(escapeMenu == true || gameOver== true || afficheDialog == true) {
			// on met le jeu en pause
			container.pause();
		} else
		{
			this.player.update(delta);
			hud.update(delta);
			currentLevel.setTabEnnemi(tabEnnemi);
			this.currentLevel.update(container, stateBasedGame, delta);
			tabEnnemi = currentLevel.getTabEnnemi();
			camera.update(container);
			playerBulletUpdate(delta);		
			playerArmyOfAllyUpdate(delta);
			container.resume(); //continue les updates dans le GameContainer
			
			if(!this.player.isAlive()){
				gameOver=true;
			}
		}
	}
	
	public void prepareDialogues() throws SlickException {
		dialogues[0] = "Bienvenue dans le jeu Ramzi & the Scientists ! Tu incarnes \nun petit rat aux multiples capacités déterminé à sauver \ntous les cobayes du laboratoire !";
		dialogues[1] = "Pour avancer, utilise les touches ZQSD ou les flèches \ndirectionnelles. Pour lancer une attaque au corps à corps, \nutilise le clic gauche de ta souris. \nÀ toi de jouer !";
		dialogues[2] = "";
		this.dialogue = new Dialogue(this, dialogues);
		dialogue.init(container);
	}

	public void prepareDialoguesSave() throws SlickException {
		dialoguesSave[0] = "Bien joué ! Tu viens de sauver le premier animal ! \nRegarde le, il s'enfuit dans cette fumée.";
		dialoguesSave[1] = "";
		this.dialogueSave = new Dialogue(this, dialoguesSave);
		dialogueSave.init(container);
	}
	
	public void prepareDescriptionItem(String description) 
	{
		String[] descriptionTab = new String[2];
		descriptionTab[0] = description;
		descriptionTab[1] = "";
		this.dialogue = new Dialogue(this, descriptionTab);
		try {
			dialogue.init(container);
			afficheDialog = true;
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Créer les menus
	 * @throws SlickException
	 */
	public void createMenu() throws SlickException {
		this.escapeMenu=false;
		this.gameOver = false;
		//menu pause
		btnResume = new Image("ressources/boutons/resume.png");
		btnExit = new Image("ressources/boutons/btnExit.png");
		btnMainMenu = new Image ("ressources/boutons/mainmenu.png");
		resume = new MouseOverArea(container, btnResume, 300, container.getHeight() - btnResume.getHeight()*10, this);
		mainMenu = new MouseOverArea(container, btnMainMenu, 300, container.getHeight() - btnMainMenu.getHeight()*10, this);
		exit = new MouseOverArea(container, btnExit, 320, container.getHeight() - btnExit.getHeight() *6, this);
		//écran game over
		txtGameOver = new Image("ressources/boutons/gameover.png");
		//écran Gagné
		txtYouWin = new Image("ressources/boutons/youWin.png");
	}
	
	public void renderHearts(Graphics g) throws SlickException {
		if(this.coeurs!=null){
			for(int i = 0 ; i < coeurs.length; i ++ )
			{
				if(coeurs[i]!=null){
					coeurs[i].render(g);
				}
			}
		}
	}
	
	public void prepareDropHeart(float xHeart, float yHeart) throws SlickException
	{
		
		for(int i=0 ; i < coeurs.length ; i++)
		{
			if(coeurs[i] == null)
			{
				this.currentLevel.dropHeart(player, xHeart, yHeart, i);
				break;
			}
		}
	}
	
	/**
	 * Affiche les attaques à distance du joueur (Ramzi)
	 * @param g
	 * @throws SlickException 
	 */
	public void playerBulletRefresh(Graphics g) throws SlickException {
		if(bullet!=null){
			for(int i = 0; i< this.bullet.size(); i++){
				if(this.bullet.get(i)!=null){
					this.bullet.get(i).render(g);
				}
			}
		}
	}
	
	public void playerArmyOfAllyRefresh(Graphics g) throws SlickException {
		if(this.armyOfAlly!=null){
			for(int i = 0; i< this.armyOfAlly.size(); i++){
				if(this.armyOfAlly.get(i)!=null){
					this.armyOfAlly.get(i).render(g);
				}
			}
		}
	}
	
	/**
	 * affiche le menu en fonction du choix du joueur (touche clavier)
	 * @param g
	 */
	public void renderMenu(Graphics g) throws SlickException {
		if (escapeMenu == true) {
			g.resetTransform(); //fixer le menu en stoppant la caméra
			g.fillRect(0, 0, container.getWidth() + 200, container.getHeight());
			g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.03f));
			resume.render(container, g);
			resume.setMouseOverColor(Color.orange);
			mainMenu.render(container, g);
			mainMenu.setMouseOverColor(Color.orange);
			exit.render(container, g);
			exit.setMouseOverColor(Color.orange);
		}else if(gameOver == true){
			g.resetTransform();
			g.fillRect(0, 0, container.getWidth() + 200, container.getHeight());
			g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.03f));
			txtGameOver.draw(340, container.getHeight() - txtGameOver.getHeight()*10+20);
			mainMenu.render(container, g);
			mainMenu.setMouseOverColor(Color.orange);
			exit.render(container, g);
			exit.setMouseOverColor(Color.orange);
		} else if(this.finDePartie == true){
			g.resetTransform();
			g.fillRect(0, 0, container.getWidth() + 200, container.getHeight());
			g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.03f));
			txtYouWin.draw(340, container.getHeight() - txtYouWin.getHeight()*11);
			mainMenu.render(container, g);
			mainMenu.setMouseOverColor(Color.orange);
			exit.render(container, g);
			exit.setMouseOverColor(Color.orange);				
		} else if(this.afficheDialog == true) {
			if(dialogue != null){
				dialogue.render(g);
			}	
		}else {
			this.hud.render(g);
		}
	}
	
	public void playerBulletUpdate(int delta){
		if(bullet!=null){
			for(int i = 0; i<bullet.size(); i++){
				if(bullet.get(i)!=null){
					bullet.get(i).update(delta);
					if(!bullet.get(i).isAlive()){
						bullet.remove(i);
					}
				}
			}
		}
		if(this.attaqueDistanceCooldown != -1)
		{
			this.attaqueDistanceCooldown++;
			if(this.attaqueDistanceCooldown == 20){
				this.attaqueDistanceCooldown = -1;
			}
		}
	}
	
	public void playerArmyOfAllyUpdate(int delta){
		if(this.armyOfAlly!=null){
			for(int i = 0; i<this.armyOfAlly.size(); i++){
				if(this.armyOfAlly.get(i)!=null){
					this.armyOfAlly.get(i).update(delta);
					if(!this.armyOfAlly.get(i).isAlive()){
						this.armyOfAlly.remove(i);
					}
				}
			}
		}
		if(this.player.getCurrentBoutonEspace()!=null){
			if(this.player.getCurrentBoutonEspace().getCurrentCooldown() != this.player.getCurrentBoutonEspace().getCooldown()+1)
			{
				this.player.getCurrentBoutonEspace().updateCurrentCooldown();
			}
		}
	}

	
	public void keyReleased(int key, char c) {
		//à l'appui sur la touche ECHAP, on quitte le jeu
		if ((Input.KEY_ESCAPE) == key) {
			escapeMenu = true; //menu pause
		}
		if(Input.KEY_P == key) {
			container.exit();
		}
		if(Input.KEY_E == key)
			escapeMenu = false; //on reprend la partie
		
		if(afficheDialog == true) {
			if(Input.KEY_SPACE == key) {
				if(dialogue != null){
					dialogue.changeMessage();
				}
				if(dialogueSave != null){
					dialogueSave.changeMessage();
				}
			}
		}
	}

	public int getID() { return id;}
	
	public void createRamziProjectile(int directionProjectile, double vitesseX, double vitesseY) throws SlickException
	{
		if(this.player.getCurrentClickDroit()!=null){
			if(this.player.getCurrentClickDroit().getName()=="fromages"){
				if(this.attaqueDistanceCooldown == -1){
					if(this.bullet!= null){
						this.bullet.add(new Bullet(this, this.currentLevel.getMap(), player,directionProjectile, vitesseX, vitesseY));
						this.bullet.get(this.bullet.size()-1).init();
					}
					this.attaqueDistanceCooldown = 0;
				}
			}
		}
	}
	
	public void deleteDialog() {
		dialogue = null;
		dialogueSave = null;
	}
	
	public void createArmyOfLittleAlly() throws SlickException
	{

		if(this.player.getCurrentBoutonEspace()!=null){
			if(this.player.getCurrentBoutonEspace().getName()=="chapeau"){
				if(this.player.getCurrentBoutonEspace().getCurrentCooldown() == this.player.getCurrentBoutonEspace().getCooldown()+1){
					while(this.armyOfAlly.size()!=20){
						if(this.bullet!= null){
							this.armyOfAlly.add(new AllyFromItem(this, map1, player.getX(), player.getY()));
						}
					}
					this.player.getCurrentBoutonEspace().updateCurrentCooldown();
				}
			}
		}
	}
	
	public void destroyProjectile()
	{
		//this.projectile = null;
	}
	

//	public void bossAtk(int e){
//		bossAtkState = e;
//	}
	
	public void componentActivated(AbstractComponent source) {
		System.out.println(source);
		if(source == resume) {
			escapeMenu = false;
		} else if(source == mainMenu) {
			stateBasedGame.enterState(0);
		} else {
			container.exit();
		}
	}
	
	public int getEnnemisDebut() { return currentLevel.getNbEnnemisDebut();}
	public int getEnnemisSauves() {return currentLevel.getNbEnnemisSauves();}
	public Boss getBossLevel() {return currentLevel.getBoss();}	
	public void setFinDePartie(boolean b) {this.finDePartie = b;}	
	public void setDifficulte(boolean difficulte) {WorldMap.difficulte = difficulte; }
	
	public void updateCurrentLevel(Level nextLevel) {
		this.currentLevel = nextLevel;
	}
}
