package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.main.KeyBoard;
import engine.main.Mouse;

public class HomeScreen {
	
	
	BufferedImage playUnTex = null;
	BufferedImage playPrTex = null;
	BufferedImage plusTex = null;
	BufferedImage backgroundTex = null;
	BufferedImage backTex = null;
	
	BufferedImage higherMeTex = null;
	
	Button createMapButton = null;
	Button helpButton;
	BufferedImage helpTex = null;
	boolean showHelp = false;
	
	int spaceing = 100;
	
	Button backButton = new Button(Button.IMAGE_BUTTON);
	
	boolean imported = false;
	
	int launchFadeTimer = 180;
	
	boolean playButtonH = false;
	boolean showMainScreen = true;
	boolean showSelectionScreen = false;
	
	int playPressDelay = 0;
	
	LevelSelect levelSelector = new LevelSelect();
	
	public HomeScreen(){
		importFiles();
		createMapButton = new Button(Button.IMAGE_BUTTON);
		backButton.setImage(backTex);
		createMapButton.setImage(plusTex);
		helpButton = new Button(Button.IMAGE_BUTTON);
		helpButton.setImage(helpTex);
	}
	
	public void update() {
		helpButton.update();
		if(helpButton.isClicked()) {
			showHelp = true;
		}
		backButton.setView(0, 0, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING);
		spaceing = Settings.GUI_ELEM_SPACING;
		backButton.update();
		createMapButton.update();
		launchFadeTimer --;
		playPressDelay --;
		if(playPressDelay==1) {
			showMainScreen = false;
			showSelectionScreen = true;
		}
		if(showSelectionScreen) {
			levelSelector.update();
		}
		if(createMapButton.isClicked()) {
			
			Game.showHomeScreen = false;
			Game.editMode = true;
			Game.edit = new TileSheetEditor();
			
			Game.mainChar.canFeelPain = false;
			Game.mainChar.setLevel(Game.edit.level);
			
		}
		if(backButton.isClicked()) {
			if(showSelectionScreen||showHelp) {
				showSelectionScreen = false;
				showMainScreen = true;
				showHelp = false;
			}
			else if(!showSelectionScreen) System.exit(0);
		}
		
	}
	
	public void render(Graphics2D g) {
		
		g.setColor(Color.black);
		g.fillRect(0, 0, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
		int imageHeight = Settings.WINDOW_WIDTH/2;
		g.drawImage(backgroundTex, 0, Settings.WINDOW_HEIGHT/2-imageHeight/2, Settings.WINDOW_WIDTH, imageHeight, null);
		
		if(showMainScreen) {
		
			
			if(!playButtonH) g.drawImage(playUnTex, Settings.WINDOW_WIDTH/2-spaceing*2, Settings.WINDOW_HEIGHT/2-spaceing, spaceing*4, spaceing*2, null);
			else {
				g.drawImage(playPrTex, Settings.WINDOW_WIDTH/2-spaceing*2, Settings.WINDOW_HEIGHT/2-spaceing, spaceing*4, spaceing*2, null);
				playPressDelay = 10;
			}
			
			createMapButton.setView(Settings.WINDOW_WIDTH-Settings.GUI_ELEM_SPACING*2, Settings.WINDOW_HEIGHT/2-Settings.GUI_ELEM_SPACING/2, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING);
			createMapButton.render(g);
			helpButton.setView(0, Settings.WINDOW_HEIGHT-Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING);
			helpButton.render(g);
			if(showHelp) {
				g.setColor(Color.DARK_GRAY.darker());
				g.fillRect(Settings.TOP_CORNER_X, Settings.TOP_CORNER_Y, Settings.GUI_WIDTH, Settings.GUI_HEIGHT);
				Game.pauseMenu.renderHelpText(g);
			}
		}
		if(showSelectionScreen) {
			
			levelSelector.render(g);
			
			
		}
		backButton.render(g);
		if(launchFadeTimer>0&&launchFadeTimer<120) {
			g.setColor(new Color(0,0,0,(int)(launchFadeTimer/120.0*255.0)));
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		}else if(launchFadeTimer>=120) {
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
			double ratio = 32.0/170.0;
			double scaleAni = (1-((    (launchFadeTimer-120)/120.0*3.0    ))+1)*0.80;
		
			g.drawImage(higherMeTex,(int)(Game.WIDTH/2-spaceing*4*scaleAni), (int)(Game.HEIGHT/2-(int)(spaceing*ratio)*4*scaleAni), (int)(spaceing*8*scaleAni), (int)(spaceing*ratio*8*scaleAni),null);
			g.setColor(new Color(0,0,0, (int)((launchFadeTimer-120)/120.0*255.0)   ));
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		}
	}
	public void input(KeyBoard keyBoard,Mouse mouse){
		if(showMainScreen) {
			if(keyBoard.ESCAPE) System.exit(0);
			if(mouse.x> Settings.WINDOW_WIDTH/2-spaceing*2&&mouse.y>Settings.WINDOW_HEIGHT/2-spaceing&&mouse.x<Settings.WINDOW_WIDTH/2+spaceing*2&&mouse.y<Settings.WINDOW_HEIGHT/2+spaceing&&mouse.mousePressed) {
				playButtonH = true;
			}else {
				playButtonH = false;
			}
			createMapButton.input(keyBoard, mouse);
			helpButton.input(keyBoard, mouse);
		}else createMapButton.reset();
		if(showSelectionScreen) {
			levelSelector.input(keyBoard, mouse);
			
		}
		backButton.input(keyBoard, mouse);
	}
	
	class LevelSelect{
		
		Button[] easyLButton = new Button[5];
		boolean[] easyUnlock = new boolean[5];
		Button[] medLButton = new Button[5];
		boolean[] medUnlock = new boolean[5];
		Button[] hardLButton = new Button[5];
		boolean[] hardUnlock = new boolean[5];
		Color tintDark = new Color(64,64,64,200);
		
		int scale = 0;
		
		public LevelSelect(){
			
			for(int i = 0;i<5;i++) {
				easyLButton[i] = new Button(Button.IMAGE_BUTTON);	
				easyUnlock[i] = false;
			}
			
			easyLButton[0].setImage(Button.oneTex);
			easyLButton[1].setImage(Button.twoTex);
			easyLButton[2].setImage(Button.threeTex);
			easyLButton[3].setImage(Button.fourTex);
			easyLButton[4].setImage(Button.fiveTex);
			
			for(int i = 0;i<5;i++) {
				medLButton[i] = new Button(Button.IMAGE_BUTTON);	
				medUnlock[i] = false;
			}
			
			medLButton[0].setImage(Button.oneTex);
			medLButton[1].setImage(Button.twoTex);
			medLButton[2].setImage(Button.threeTex);
			medLButton[3].setImage(Button.fourTex);
			medLButton[4].setImage(Button.fiveTex);
			
			for(int i = 0;i<5;i++) {
				hardLButton[i] = new Button(Button.IMAGE_BUTTON);
				hardUnlock[i] = false;
			}
			
			hardLButton[0].setImage(Button.oneTex);
			hardLButton[1].setImage(Button.twoTex);
			hardLButton[2].setImage(Button.threeTex);
			hardLButton[3].setImage(Button.fourTex);
			hardLButton[4].setImage(Button.fiveTex);
			
			easyUnlock[0] = true;
			
		}
		
		void render(Graphics2D g) {
			
			g.setColor(tintDark);
			
			g.fillRect(Settings.TOP_CORNER_X,Settings.TOP_CORNER_Y, Settings.GUI_WIDTH,Settings.GUI_HEIGHT );
			
			for(int i = 0;i<5;i++) {
				easyLButton[i].setView(Settings.TOP_CORNER_X+i*Settings.GUI_ELEM_SPACING,Settings.TOP_CORNER_Y , Settings.GUI_ELEM_SPACING,Settings.GUI_ELEM_SPACING );
				medLButton[i].setView(Settings.TOP_CORNER_X+i*Settings.GUI_ELEM_SPACING,Settings.TOP_CORNER_Y+Settings.GUI_ELEM_SPACING , Settings.GUI_ELEM_SPACING,Settings.GUI_ELEM_SPACING );
				hardLButton[i].setView(Settings.TOP_CORNER_X+i*Settings.GUI_ELEM_SPACING,Settings.TOP_CORNER_Y+Settings.GUI_ELEM_SPACING*2 , Settings.GUI_ELEM_SPACING,Settings.GUI_ELEM_SPACING );
			}
			
			
			for(Button b:easyLButton) b.render(g);
			for(Button b:medLButton) b.render(g);
			for(Button b:hardLButton) b.render(g);
			
			
			g.setColor(Color.white);
			g.setStroke(new BasicStroke(5));
			g.drawRect(Settings.TOP_CORNER_X,Settings.TOP_CORNER_Y, Settings.GUI_WIDTH,Settings.GUI_HEIGHT );
			
		}
		
		void update() {
			for(Button b:easyLButton) b.update();
			for(Button b:medLButton) b.update();
			for(Button b:hardLButton) b.update();
			
			if(easyLButton[0].isClicked()) {
				Game.editMode = false;
				Game.currentLevel.openLevel("Levels/level1.txt");
				Game.mainChar.canCollectCoins = true;
				Game.showHomeScreen = false;
			}
			if(easyLButton[1].isClicked()) {
				Game.editMode = false;
				Game.currentLevel.openLevel("Levels/level2.txt");
				Game.mainChar.canCollectCoins = true;
				Game.showHomeScreen = false;
			}
			if(easyLButton[2].isClicked()) {
				Game.editMode = false;
				Game.currentLevel.openLevel("Levels/level3.txt");
				Game.mainChar.canCollectCoins = true;
				Game.showHomeScreen = false;
			}
			if(easyLButton[3].isClicked()) {
				Game.editMode = false;
				Game.currentLevel.openLevel("Levels/level4.txt");
				Game.mainChar.canCollectCoins = true;
				Game.showHomeScreen = false;
			}
			if(easyLButton[4].isClicked()) {
				Game.editMode = false;
				Game.currentLevel.openLevel("Levels/level5.txt");
				Game.mainChar.canCollectCoins = true;
				Game.showHomeScreen = false;
			}
		}
		
		void input(KeyBoard keyBoard,Mouse mouse){
			
			for(Button b:easyLButton) b.input(keyBoard, mouse);
			for(Button b:medLButton) b.input(keyBoard, mouse);
			for(Button b:hardLButton) b.input(keyBoard, mouse);
			
		}
		
	}
	
	public void importFiles() {
		if(!imported) {
			
			playUnTex = engine.graphics.Graphics3D.importImage("Gui/PlayUnPressed.png");
			playPrTex = engine.graphics.Graphics3D.importImage("Gui/PlayPressed.png");
			plusTex = engine.graphics.Graphics3D.importImage("Gui/Buy.png");
			backgroundTex = engine.graphics.Graphics3D.importImage("Gui/HomeScreenBackground.png");
			higherMeTex = engine.graphics.Graphics3D.importImage("Gui/HigherMe.png");
			backTex = engine.graphics.Graphics3D.importImage("Gui/PageTurnLeft.png");
			helpTex = engine.graphics.Graphics3D.importImage("Gui/Help.png");
			
			imported = true;
		}
	}
	
}
