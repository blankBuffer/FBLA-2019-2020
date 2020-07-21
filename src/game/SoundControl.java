package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.main.KeyBoard;
import engine.main.Mouse;

public class SoundControl {
	
	Button musicSelector;
	Button fxSelector;
	Button backButton;
	
	BufferedImage backTex = null;
	BufferedImage musicTex = null;
	BufferedImage noMusicTex = null;
	BufferedImage fxTex = null;
	BufferedImage noFxTex = null;
	
	int spacing = Settings.GUI_ELEM_SPACING;
	
	public SoundControl(){
		musicSelector = new Button(Button.IMAGE_SELECT,Game.WIDTH/2-spacing,Game.HEIGHT/2-spacing,spacing,spacing);
		fxSelector = new Button(Button.IMAGE_SELECT,Game.WIDTH/2-spacing,Game.HEIGHT/2+spacing,spacing,spacing);
		backButton = new Button(Button.IMAGE_BUTTON,Settings.PADDING_SIZE,Settings.PADDING_SIZE,spacing,spacing);
		
		musicTex = engine.graphics.Graphics3D.importImage("Gui/Music.png");
		fxTex = engine.graphics.Graphics3D.importImage("Gui/FX.png");
		noMusicTex = engine.graphics.Graphics3D.importImage("Gui/NoMusic.png");
		noFxTex = engine.graphics.Graphics3D.importImage("Gui/NoFX.png");
		backTex = engine.graphics.Graphics3D.importImage("Gui/PageTurnLeft.png");
		
		musicSelector.addButton(musicTex);
		fxSelector.addButton(fxTex);
		musicSelector.addButton(noMusicTex);
		fxSelector.addButton(noFxTex);
		backButton.setImage(backTex);
	}
	public void render(Graphics2D g) {
		
		musicSelector.render(g);
		fxSelector.render(g);
		backButton.render(g);
	}
	public void update() {
		musicSelector.update();
		fxSelector.update();
		backButton.update();
		
		Game.playMusic = musicSelector.getChosenSelection()==0;
		Game.playFx = fxSelector.getChosenSelection()==0;
		if(backButton.isClicked()) {
			backButton.reset();
			Game.showSoundControl = false;
		}
	}
	
	public void input(KeyBoard keyBoard,Mouse mouse){
		backButton.input(keyBoard, mouse);
		musicSelector.input(keyBoard, mouse);
		fxSelector.input(keyBoard, mouse);
	}
	
}
