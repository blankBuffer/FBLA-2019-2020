package game;

import java.awt.Color;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.main.KeyBoard;
import engine.main.Mouse;

public class TileSheetEditor {
	
	ArrayList<Button> buttonContainer = new ArrayList<Button>();
	
	
	Button weatherSelector = null;
	Button brightnessSlider = null;
	Button hardnessSelector = null;
	Button heightSlider = null;
	Button isCoined = null;
	Button isTextured = null;
	Button pageTurnLeft = null;
	Button pageTurnRight = null;
	Button layerSelector = null;
	Button escapeButton = null;
	Button saveButton = null;
	Button painSelector = null;
	Button addConstWorker = null;
	Button addPoorPerson = null;
	Button undoButton = null;
	Button editOrViewButton = null;
	
	ArrayList<TileSheet> sheetBackups = new ArrayList<TileSheet>();
	
	Button[] page1 = new Button[3];
	Button[] page2 = new Button[3];
	Button[] page3 = new Button[3];
	
	private int currentPage = 1;
	
	private static BufferedImage heartTex;
	private static BufferedImage brokenHeartTex;
	private static BufferedImage cloudyTex;
	private static BufferedImage rainyTex;
	private static BufferedImage sunnyTex;
	private BufferedImage highlighted;
	private BufferedImage leftArrowTex;
	private BufferedImage rightArrowTex;
	private BufferedImage coinTex;
	private BufferedImage frontLayerTex;
	private BufferedImage backLayerTex;
	private BufferedImage escapeTex;
	private BufferedImage saveTex;
	private BufferedImage liquidTex;
	private BufferedImage constHatTex;
	private BufferedImage poorPersonTex;
	private BufferedImage pencilTex;
	private BufferedImage eyeTex;
	
	private static BufferedImage solidTex;
	private static BufferedImage notSolidTex;
	
	private static BufferedImage textureTex;
	private static BufferedImage notTextureTex;
	
	
	double xPan = 1;
	Level level;
	Tile selected = null;
	int indexX = 0,indexY = 0;
	int chosenIndexX = 0,chosenIndexY = 0;
	private boolean mouseInTileRegion = false;
	
	public TileSheetEditor() {
		
		importFiles();
		
		level = new Level();
		level.trashGenerates = false;
		
		
		hardnessSelector = new Button(Button.IMAGE_SELECT);
		isTextured = new Button(Button.IMAGE_SELECT);
		isCoined = new Button(Button.IMAGE_SELECT);
		
		layerSelector = new Button(Button.IMAGE_SELECT);
		
		painSelector = new Button(Button.IMAGE_SELECT);
		
		addConstWorker = new Button(Button.IMAGE_BUTTON);
		addPoorPerson = new Button(Button.IMAGE_BUTTON);
		
		brightnessSlider = new Button(Button.SLIDER);
		brightnessSlider.setMin(0);
		brightnessSlider.setMax(256);
		brightnessSlider.setTickSpacing(32);
		brightnessSlider.setValue(256);
		
		heightSlider = new Button(Button.SLIDER);
		heightSlider.setMin(0);
		heightSlider.setMax(8);
		heightSlider.setTickSpacing(1);
		heightSlider.setValue(8);
		
		editOrViewButton = new Button(Button.IMAGE_SELECT);
		editOrViewButton.addButton(eyeTex);
		editOrViewButton.addButton(pencilTex);
		
		
		pageTurnLeft = new Button(Button.IMAGE_BUTTON);
		pageTurnRight = new Button(Button.IMAGE_BUTTON);
		
		
		page1[0] = new Button(Button.IMAGE_SELECT);
		
		for(int i = 0;i<Tile.tileTex.length;i++) {
			page1[0].addButton(Tile.tileTex[i]);
		}
		page1[0].addButton(Tile.completeFlagTex);
		page1[0].addButton(Tile.lavaTex[0]);
		page1[0].addButton(Tile.waterTex[0]);
		
		escapeButton = new Button(Button.IMAGE_BUTTON);
		
		saveButton = new Button(Button.IMAGE_BUTTON);
		
		undoButton = new Button(Button.IMAGE_BUTTON);
		undoButton.setImage(leftArrowTex);
		
		pageTurnLeft.setImage(leftArrowTex);
		pageTurnRight.setImage(rightArrowTex);
		weatherSelector =  new Button(Button.IMAGE_SELECT);
		weatherSelector.addButton(sunnyTex);
		weatherSelector.addButton(rainyTex);
		weatherSelector.addButton(cloudyTex);
		hardnessSelector.addButton(notSolidTex);
		hardnessSelector.addButton(solidTex);
		hardnessSelector.addButton(liquidTex);
		isTextured.addButton(notTextureTex);
		isTextured.addButton(textureTex);
		isCoined.addButton(notSolidTex);
		isCoined.addButton(coinTex);
		layerSelector.addButton(backLayerTex);
		layerSelector.addButton(frontLayerTex);
		escapeButton.setImage(escapeTex);
		saveButton.setImage(saveTex);
		painSelector.addButton(heartTex);
		painSelector.addButton(brokenHeartTex);
		addConstWorker.setImage(constHatTex);
		addPoorPerson.setImage(poorPersonTex);
		
		buttonContainer.add(weatherSelector);
		buttonContainer.add(brightnessSlider);
		buttonContainer.add(hardnessSelector);
		buttonContainer.add(heightSlider);
		buttonContainer.add(isCoined);
		buttonContainer.add(isTextured);
		buttonContainer.add(pageTurnLeft);
		buttonContainer.add(pageTurnRight);
		buttonContainer.add(layerSelector);
		buttonContainer.add(escapeButton);
		buttonContainer.add(saveButton);
		buttonContainer.add(painSelector);
		buttonContainer.add(addConstWorker);
		buttonContainer.add(addPoorPerson);
		buttonContainer.add(undoButton);
		buttonContainer.add(editOrViewButton);
	}
	public void update() {
		
		level.update();
		Game.mainChar.canCollectCoins = false;
		
		//backup
		int backupJump = 1;
		if(undoButton.isClicked()) {
			if(sheetBackups.size()>backupJump-1) {
				TileSheet s = sheetBackups.get(sheetBackups.size()-backupJump);
				int tmpZoom = level.sheet.zoom;
				//check that you not restoring from now
				if(level.sheet != s) {
					for(int i = 0;i<backupJump;i++) sheetBackups.remove(sheetBackups.size()-1);
					level.sheet = s;
					s.zoom = tmpZoom;
					Game.mainChar.setLevel(level);
				}
			}
			
		}
		//check if if the number of backups is too large and remove old stuff
		if(sheetBackups.size()>40) {
			sheetBackups.remove(0);
		}
		
		for(Button b:buttonContainer) {
			b.update();
		}
		
		if(escapeButton.isClicked()) selected = null;
		if(saveButton.isClicked()) level.saveLevel();
		/*
		if(addConstWorker.isClicked()) {
			Character c = new Character(Character.CONST_WORKER);
			c.setLevel(level);
			c.setPosition(Game.mainChar.getX(), Game.mainChar.getY());
		}
		if(addPoorPerson.isClicked()) {
			Character c = new Character(Character.POOR_PERSON);
			c.setLevel(level);
			c.setPosition(Game.mainChar.getX(), Game.mainChar.getY());
		}
		*/
		
		if(weatherSelector.getChosenSelection()==0) level.weather=Level.SUNNY;
		else if(weatherSelector.getChosenSelection()==1) level.weather=Level.RAINY;
		else if(weatherSelector.getChosenSelection()==2) level.weather=Level.CLOUDY;
		
		
		if(editOrViewButton.getChosenSelection()==1) {
			if(currentPage == 1) {
				page1[0].update();
				if(selected!=null) {
					selected.textID = page1[0].getChosenSelection();
					if(page1[0].getChosenSelection()==page1[0].buttons.size()-3) selected.textID = -4;
					if(page1[0].getChosenSelection()==page1[0].buttons.size()-2) selected.textID = -2;
					if(page1[0].getChosenSelection()==page1[0].buttons.size()-1) selected.textID = -3;
				}
			}
			if(selected!=null) {
				
				selected.coin = isCoined.getChosenSelection()==1;
				selected.height = heightSlider.getNumValue()/8.0;
				selected.brightness = brightnessSlider.getNumValue();
				selected.layer = layerSelector.getChosenSelection();
				selected.painful = painSelector.getChosenSelection()==1;
				
				if(isTextured.getChosenSelection()==0) {
					selected.textID = -1;
				}
				selected.hardness = hardnessSelector.getChosenSelection();
			}
		}else {
			if(selected!=null) {
				hardnessSelector.setChosen(selected.hardness);
				//t for temporary integer
				int t = 0;
				if(selected.isCoin) t = 1;
				isCoined.setChosen(t);
				t = 1;
				if(selected.textID==-1) t = 0;
				isTextured.setChosen(t);
				layerSelector.setChosen(selected.layer);
				t = 0;
				if(selected.painful) t = 1;
				painSelector.setChosen(t);
				brightnessSlider.setValue(selected.brightness);
				//this +1 removes remainder from 255 since does not divide evenly
				if(selected.brightness ==255) {
					brightnessSlider.setValue(256);
				}
				heightSlider.setValue((int)Math.round((selected.height*8.0)));
				
				page1[0].setChosen(selected.textID);
				
				
				if(selected.textID == -4) page1[0].setChosen(page1[0].buttons.size()-3);
				if(selected.textID == -2) page1[0].setChosen(page1[0].buttons.size()-2);
				if(selected.textID == -3) page1[0].setChosen(page1[0].buttons.size()-1);
				
			}
			
			
		}
	}
	public void render(Graphics2D g) {
		
		updateButtonPositions();
		
		level.render(g,xPan,0,Settings.PADDING_SIZE,Settings.WINDOW_WIDTH,Settings.WINDOW_HEIGHT-Settings.PADDING_SIZE*3);
		
		int xp = (int)((indexX+level.sheet.tileScreenFit/2-xPan)*level.sheet.spaceing);
		
		g.drawImage(highlighted,xp,indexY*level.sheet.spaceing+Settings.PADDING_SIZE,level.sheet.spaceing,level.sheet.spaceing,null);
		
		
		if(selected!=null) {
			xp = (int)((chosenIndexX+level.sheet.tileScreenFit/2-xPan)*level.sheet.spaceing);
			g.drawImage(highlighted,xp,chosenIndexY*level.sheet.spaceing+Settings.PADDING_SIZE,level.sheet.spaceing,level.sheet.spaceing,null);
		}
		
		
		//left zone transparent rectangle
		if(!mouseInTileRegion)  g.setColor(new Color(64,64,64,128));
		else g.setColor(new Color(64,64,64,64));
		g.fillRect(0, 0, Settings.GUI_ELEM_SPACING*2, Settings.WINDOW_HEIGHT);
		
		//Bottom zone rectangle (area for choosing texture)
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, Settings.WINDOW_HEIGHT-Settings.PADDING_SIZE*2, Settings.WINDOW_WIDTH, Settings.PADDING_SIZE*2);
		
		//draw buttons
		for(Button b:buttonContainer) {
			b.render(g);
		}
		
		if(currentPage == 1) {
			page1[0].render(g);
		}
		
	}
	
	private void updateButtonPositions() {
		int s = Settings.GUI_ELEM_SPACING/2;
		weatherSelector.setView(0, 0, s, s);
		hardnessSelector.setView(0, s, s, s);
		isTextured.setView(0, s*2, s, s);
		isCoined.setView(0, s*3, s, s);
		layerSelector.setView(0, s*4, s, s);
		painSelector.setView(0, s*5, s, s);
		addConstWorker.setView(0, s*6, s,s);
		addPoorPerson.setView(s, s*6, s, s);
		brightnessSlider.setView(0, s*7, s*3, s);
		heightSlider.setView(0, s*8, s*3, s);
		undoButton.setView(0, s*9, s, s);
		editOrViewButton.setView(0, s*10, s, s);
		page1[0].setView(s*2, Settings.WINDOW_HEIGHT-s*4, s, s);
		pageTurnLeft.setView(0,Settings.WINDOW_HEIGHT-s*4, s, s);
		pageTurnRight.setView(Settings.WINDOW_WIDTH-s,Settings.WINDOW_HEIGHT-s*4, s, s);
		saveButton.setView(0, Settings.WINDOW_HEIGHT-Settings.PADDING_SIZE*2-s, s, s);
		escapeButton.setView(s, Settings.WINDOW_HEIGHT-Settings.PADDING_SIZE*2-s, s, s);
	}
	boolean pMousePressed = false;
	public void input(KeyBoard keyBoard,Mouse mouse) {
		
		if(keyBoard.S) {
			level.initX = Game.mainChar.getX();
			level.initY = Game.mainChar.getY();
		}
		if(keyBoard.O) level.openLevel("save.txt");
		
		for(Button b:buttonContainer) {
			b.input(keyBoard, mouse);
		}
		
		if(currentPage == 1) {
			page1[0].input(keyBoard, mouse);
		}
		
		if(mouse.x>Settings.GUI_ELEM_SPACING*2&&mouse.y<Settings.WINDOW_HEIGHT&&mouse.y<Settings.WINDOW_HEIGHT-Settings.PADDING_SIZE*2&&mouse.y>Settings.PADDING_SIZE) {
			mouseInTileRegion = true;
			indexX = (int)((double)mouse.x/level.sheet.spaceing +xPan-level.sheet.tileScreenFit/2);
			indexY = (mouse.y-Settings.PADDING_SIZE)/level.sheet.spaceing;
			if(indexX<0)indexX = 0;
			if(indexY<0)indexY = 0;
			if(indexX>level.sheet.length-1) indexX = level.sheet.length-1;
			if(indexY>level.sheet.height-1) indexY = level.sheet.height-1;
			if(mouse.mousePressed) { 
				selected = level.sheet.getTile(indexX,indexY);
				chosenIndexX = indexX;
				chosenIndexY = indexY;
				//add backup for every new click on sheet
				if(!pMousePressed)backup();
			}
		}else mouseInTileRegion = false;
		
		pMousePressed = mouse.mousePressed;
	}
	
	public void backup() {
		sheetBackups.add(level.sheet.copy());
	}
	
	
	public void importFiles() {
			sunnyTex = engine.graphics.Graphics3D.importImage("Gui/Clear.png");
			cloudyTex = engine.graphics.Graphics3D.importImage("Gui/Sunny.png");
			rainyTex = engine.graphics.Graphics3D.importImage("Gui/Rainy.png");
			highlighted = engine.graphics.Graphics3D.importImage("Gui/Highlighted.png");
			solidTex = engine.graphics.Graphics3D.importImage("Gui/Solid.png");
			notSolidTex = engine.graphics.Graphics3D.importImage("Gui/NotSolid.png");
			textureTex = engine.graphics.Graphics3D.importImage("Gui/Texture.png");
			notTextureTex = engine.graphics.Graphics3D.importImage("Gui/NotTexture.png");
			leftArrowTex = engine.graphics.Graphics3D.importImage("Gui/PageTurnLeft.png");
			rightArrowTex = engine.graphics.Graphics3D.importImage("Gui/PageTurnRight.png");
			coinTex = engine.graphics.Graphics3D.importImage("Tile/Coin.png");
			frontLayerTex = engine.graphics.Graphics3D.importImage("Gui/FrontLayer.png");
			backLayerTex = engine.graphics.Graphics3D.importImage("Gui/BackLayer.png");
			escapeTex = engine.graphics.Graphics3D.importImage("Gui/Escape.png");
			saveTex = engine.graphics.Graphics3D.importImage("Gui/Save.png");
			heartTex = engine.graphics.Graphics3D.importImage("Char/Heart.png");
			brokenHeartTex = engine.graphics.Graphics3D.importImage("Char/BrokenHeart.png");
			liquidTex = engine.graphics.Graphics3D.importImage("Gui/Liquid.png");
			constHatTex = engine.graphics.Graphics3D.importImage("Gui/ConstHat.png");
			poorPersonTex = engine.graphics.Graphics3D.importImage("Gui/Cup.png");
			eyeTex =  engine.graphics.Graphics3D.importImage("Gui/View.png");
			pencilTex = engine.graphics.Graphics3D.importImage("Gui/Edit.png");
	}
	
}
