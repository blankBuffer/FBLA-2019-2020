package game;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import engine.main.KeyBoard;
import engine.main.Mouse;

public class PauseMenu {
	
	boolean imported = false;
	
	int spacing = Settings.GUI_ELEM_SPACING;
	
	boolean showHelpText = false;
	BufferedImage soundTex = null;
	BufferedImage helpTex = null;
	BufferedImage resumeTex = null;
	BufferedImage backTex = null;
	
	BufferedImage[] borderTex = new BufferedImage[4];
	
	BufferedImage arrowKeysTex = null;
	
	Button soundButton = new Button(Button.IMAGE_BUTTON,Game.WIDTH/2-spacing/2,Game.HEIGHT/2-spacing/2,spacing,spacing);
	Button helpButton = new Button(Button.IMAGE_BUTTON,Game.WIDTH/2-spacing/2-spacing*2,Game.HEIGHT/2-spacing/2,spacing,spacing);
	Button resumeButton = new Button(Button.IMAGE_BUTTON,Game.WIDTH/2-spacing/2+spacing*2,Game.HEIGHT/2-spacing/2,spacing,spacing);
	Button backButton = new Button(Button.IMAGE_BUTTON,Settings.PADDING_SIZE,Settings.PADDING_SIZE,spacing,spacing);
	
	ArrayList<String> helpText = new ArrayList<String>();
	
	public void importFiles() {
		
		//images used for pause menu
		soundTex = engine.graphics.Graphics3D.importImage("Gui/Audio.png");
		helpTex = engine.graphics.Graphics3D.importImage("Gui/Help.png");
		resumeTex = engine.graphics.Graphics3D.importImage("Gui/Resume.png");
		backTex = engine.graphics.Graphics3D.importImage("Gui/PageTurnLeft.png");
		arrowKeysTex = engine.graphics.Graphics3D.importImage("Gui/ArrowKeys.png");
		
		borderTex[0] = engine.graphics.Graphics3D.importImage("Gui/TopLeftBorder.png");
		borderTex[1] = engine.graphics.Graphics3D.importImage("Gui/TopRightBorder.png");
		borderTex[2] = engine.graphics.Graphics3D.importImage("Gui/BottomLeftBorder.png");
		borderTex[3] = engine.graphics.Graphics3D.importImage("Gui/BottomRightBorder.png");
		
		
		imported = true;
	}
	
	public PauseMenu() {
		importFiles();
		//settings textures for buttons
		soundButton.setImage(soundTex);
		helpButton.setImage(helpTex);
		resumeButton.setImage(resumeTex);
		backButton.setImage(backTex);
		setText();
	}
	
	public void setText() {
		
		helpText.add( "Welcome to Higher Me");
		helpText.add( "Goal of the game is to grow your wealth and pass levels to become a real business man!");
		helpText.add( "To beat the level simply touch the flag at the end");
		helpText.add("");
		helpText.add( "Replaying levels is acceptable to gain more coins");
		helpText.add("");
		helpText.add( "Controls are simple. The arrow keys are to move the character");
		helpText.add( "Press the Stock Market button to open stock Market to invest collected coins/money");
		helpText.add( "Press the Upgrade button to upgrade and buy items to make passing levels quicker");
		helpText.add( "Press the R key to move player back to spawn");
		helpText.add( "Press the S key in editor to set player spawn in edit mode");
		helpText.add( "Press the escape key to pause the game or if in homescreen to quit");
		helpText.add( "To buy a stock select it from the list on the top right and then press plus button");
		helpText.add( "To sell a stock select it from the list on the top right and then press minus button");
		helpText.add("");
		helpText.add( "Lastly Create Your Own levels with the level Editor and Test your ability");
		helpText.add("");
		helpText.add( "All game content was designed by Benjamin Robert Currie (2019-2020)");
		helpText.add( "Textures designed with Gimp");
		helpText.add( "Programming with the Eclipse IDE");
		helpText.add( "Music written with Mac GarageBand (no pre-made loops used)");
		
	}
	
	public void update() {
		
		soundButton.update();
		helpButton.update();
		resumeButton.update();
		backButton.update();
		
		
		//main pause menu draw
		if(Game.showPauseMenu&&!Game.showSoundControl) {
			if(resumeButton.isClicked()) {
				Game.showPauseMenu = false;
			}
			if(helpButton.isClicked()) showHelpText = true;
			if(backButton.isClicked()) showHelpText = false;
			if(soundButton.isClicked()) {
				Game.showSoundControl = true;
			}
		}
		resumeButton.reset();
	}
	
	public void render(Graphics2D g) {
		
		renderBackground(g);
		
		if(showHelpText) {
			renderHelpText(g);
			backButton.render(g);
		}else {
			soundButton.render(g);
			helpButton.render(g);
			resumeButton.render(g);
		}
		
	}
	
	public void renderHelpText(Graphics2D g) {
		g.setColor(Color.white);
		g.setFont(new Font(Font.MONOSPACED,0, Settings.FONT_SIZE ));
		for(int i = 0;i<helpText.size();i++) {
			String temp = helpText.get(i);
			g.drawString(temp,spacing+Settings.PADDING_SIZE,spacing+Settings.PADDING_SIZE+Settings.FONT_SIZE*(i+1) );
		}
		g.drawImage(arrowKeysTex, Settings.WINDOW_WIDTH-Settings.PADDING_SIZE-Settings.GUI_ELEM_SPACING*2, Settings.WINDOW_HEIGHT-Settings.PADDING_SIZE-Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING*2, Settings.GUI_ELEM_SPACING, null);
	}
	
	Color backgroundColor = new Color(58,14,0);
	private void renderBackground(Graphics2D g) {
		g.setColor(backgroundColor);
		g.fillRect(Settings.TOP_CORNER_X, Settings.TOP_CORNER_Y, Settings.GUI_WIDTH, Settings.GUI_HEIGHT);
		g.drawImage(borderTex[0], Settings.PADDING_SIZE ,  Settings.PADDING_SIZE, spacing*2,spacing*2, null);
		g.drawImage(borderTex[1], Settings.WINDOW_WIDTH-Settings.PADDING_SIZE-spacing*2 ,  Settings.PADDING_SIZE, spacing*2,spacing*2, null);
		g.drawImage(borderTex[2], Settings.PADDING_SIZE ,  Settings.WINDOW_HEIGHT-Settings.PADDING_SIZE-spacing*2, spacing*2,spacing*2, null);
		g.drawImage(borderTex[3], Settings.WINDOW_WIDTH-Settings.PADDING_SIZE-spacing*2 ,  Settings.WINDOW_HEIGHT-Settings.PADDING_SIZE-spacing*2, spacing*2,spacing*2, null);
		
	}
	
	public void input(KeyBoard keyBoard,Mouse mouse){
		if(showHelpText) backButton.input(keyBoard, mouse);
		else {
			backButton.reset();
			soundButton.input(keyBoard, mouse);
			helpButton.input(keyBoard, mouse);
			resumeButton.input(keyBoard, mouse);
		}
	}
	
	
	
	
}
