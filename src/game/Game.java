package game;
import engine.main.*;

import java.awt.Color;
import java.awt.Graphics2D;

public class Game extends Main{
	
	private static final long serialVersionUID = 1524306043384221391L;
    
    public static Level currentLevel;
    static TileSheetEditor edit = null;
    Sound music = new Sound("Sound/boop moog.wav");
    static Upgrade upgradeWindow = null;
    static PauseMenu pauseMenu = null;
    
    static MainCharacter mainChar = null;
    
    static HomeScreen home = null;
    static SoundControl soundControl;
    
    static boolean editMode = false;
    static boolean showPauseMenu = false;
    static boolean showUpgradeWindow = false;
    static boolean showSoundControl = false;
    static boolean marketProgramOpen = false;
    public static boolean showHomeScreen = true;
    public static boolean gameOver = false;
    static boolean playMusic = true;
    static boolean playFx = true;
    
    static StockMarket market = null;
    
    
	
    public Game(){
    	size(Settings.WINDOW_WIDTH,Settings.WINDOW_HEIGHT);
    	//GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow( getWindow() );
        updateRate(60);
		frameRate(60);
		init();
		name("Template");
        run();
    }
    public void init() {
    	pauseMenu = new PauseMenu();
    	currentLevel = new Level();
    	mainChar = new MainCharacter();
    	music.load();
    	if(!editMode) mainChar.setLevel(currentLevel);
    	
    	if(editMode) currentLevel = edit.level;
    	
    	market = new StockMarket();
    	home = new HomeScreen();
    	upgradeWindow = new Upgrade();
    	soundControl = new SoundControl();
    }
	public void update(long currentTime){
		
		Settings.updateWindowDimesions(getWindow());
		
		if(gameOver) {
			
			fullReset();
			return;
			
		}
		
		if(playMusic) {
			if(!music.isPlaying()) music.play();
		}else {
			music.stop();
		}
		
		if(!showPauseMenu&&!showHomeScreen) {
			if(editMode) {
				edit.update();
				edit.xPan = mainChar.getX();
			}else {
				currentLevel.update();
			}
			//else l1.update();
			if(showUpgradeWindow) upgradeWindow.update();
		
			market.update();
		}else {
			pauseMenu.update();
		}
		if(showSoundControl) soundControl.update();
		if(showHomeScreen) home.update();
		
	}
	double smoothPan = 0;
	public void draw(Graphics2D g){
		//black background
		g.setColor(Color.black);
		g.fillRect(0, 0, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
		
		
		if(gameOver) return;
		if(editMode) edit.render(g);
		smoothPan -= mainChar.getVelocityX()*0.5;
		if(!editMode) currentLevel.render(g,mainChar.getX()+smoothPan ,0,Settings.PADDING_SIZE,Settings.WINDOW_WIDTH,Settings.WINDOW_HEIGHT-Settings.PADDING_SIZE);
		smoothPan/=1.1;
		
		mainChar.renderHealth(g,Settings.GUI_ELEM_SPACING*3,0);
		mainChar.renderMoney(g,Settings.GUI_ELEM_SPACING*3,Settings.PADDING_SIZE/2 );
		
		//else l1.render(g,mainChar.xPos);
		if(showUpgradeWindow) upgradeWindow.render(g);
		if(marketProgramOpen) market.render(g);
		
		if(showPauseMenu&&!showSoundControl) {
			pauseMenu.render(g);
		}
		
		if(showSoundControl) soundControl.render(g);
		
		if(showHomeScreen) home.render(g);
	}
	
	boolean prevEscape = false;
	
	public void input(KeyBoard keyBoard,Mouse mouse){
		
		if(currentLevel!=null)currentLevel.input(keyBoard, mouse);
		if(editMode) edit.input(keyBoard, mouse);
		
		if(gameOver) return;
		if(showUpgradeWindow&&!showPauseMenu) upgradeWindow.input(keyBoard, mouse);
		if(marketProgramOpen&&!showPauseMenu) market.input(keyBoard, mouse);
		
		if(showPauseMenu) pauseMenu.input(keyBoard, mouse);
		
		if(showSoundControl) soundControl.input(keyBoard, mouse);;
		
		if(keyBoard.ESCAPE&&!prevEscape&&!showSoundControl) showPauseMenu = !showPauseMenu;
		prevEscape = keyBoard.ESCAPE;
		
		if(showHomeScreen) home.input(keyBoard, mouse);
	}
	
	public void fullReset(){
		
		editMode = false;
	    showPauseMenu = false;
	    showUpgradeWindow = false;
	    showSoundControl = false;
	    marketProgramOpen = false;
	    showHomeScreen = true;
	    gameOver = false;
		
    	currentLevel = new Level();
    	edit = new TileSheetEditor();
    	mainChar = new MainCharacter();
    	
    	mainChar.setLevel(currentLevel);
    	
    	market = new StockMarket();
    	home = new HomeScreen();
    	upgradeWindow = new Upgrade();
    	soundControl = new SoundControl();
	}
	
	public static void backToHome() {
		
		currentLevel = new Level();
		
		
		editMode = false;
	    showPauseMenu = false;
	    showUpgradeWindow = false;
	    showSoundControl = false;
	    marketProgramOpen = false;
	    showHomeScreen = true;
		
	    home = new HomeScreen();
	    
	}
	
	
	public static void main(String[] args) {
		new Game();
	}
	
}