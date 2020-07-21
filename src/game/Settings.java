package game;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Settings {
	
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	/*
	 * for non fullscreen
	 * 
	 * WINDOW WIDTH is 75% of display width
	 * 
	 * for fullScreen
	 * 
	 * WINDOW WIDTH is display width
	 * 
	 * HEIGHT of WINDOW is 75% of window Width
	 * GUI PADDING is 10% of WINDOW width
	 * element size is 7% of WIDTH
	 */
	
	static int WINDOW_WIDTH = (int)(screenSize.getWidth()*.75);
	static int WINDOW_HEIGHT = WINDOW_WIDTH*75/100;
	static int PADDING_SIZE = WINDOW_WIDTH*10/100;
	static int GUI_ELEM_SPACING = WINDOW_WIDTH*7/100;
	static int FONT_SIZE = GUI_ELEM_SPACING/5;
	
	//for sub windows
	static int GUI_WIDTH = WINDOW_WIDTH-PADDING_SIZE*2;
	static int GUI_HEIGHT = WINDOW_HEIGHT-PADDING_SIZE*2;
	static int TOP_CORNER_X = PADDING_SIZE;
	static int TOP_CORNER_Y = PADDING_SIZE;
	
	static void updateWindowDimesions(JFrame frame) {
		Settings.WINDOW_WIDTH = frame.getWidth();
		Settings.WINDOW_HEIGHT = frame.getHeight()-22;
		PADDING_SIZE = WINDOW_WIDTH*10/100;
		GUI_ELEM_SPACING = WINDOW_WIDTH*7/100;
		FONT_SIZE = GUI_ELEM_SPACING/5;
		
		GUI_WIDTH = WINDOW_WIDTH-PADDING_SIZE*2;
		GUI_HEIGHT = WINDOW_HEIGHT-PADDING_SIZE*2;
		TOP_CORNER_X = PADDING_SIZE;
		TOP_CORNER_Y = PADDING_SIZE;
		
	}
	
	
}
