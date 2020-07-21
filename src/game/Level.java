package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

import engine.graphics.Graphics3D;
import engine.main.KeyBoard;
import engine.main.Mouse;
import engine.main.Sound;

public class Level{
	
	double timePlayed = 0;
	NumberDisplay numDisplay = new NumberDisplay();
	
	int spaceing = 50;
	Sound rainSound;
	Sound thunderSound[] = new Sound[2];
	
	static BufferedImage skyTex = null;
	
	TileSheet sheet;
    int tileScreenFit;
    
    public boolean complete = false;
    boolean trashGenerates = true;
    
    double initX = 1;
    double initY = 1;
	
    //weather 
	static final int SUNNY = 0;
	static final int CLOUDY = 1;
	static final int RAINY = 2;
    public int weather = SUNNY;
    final int numberOfDrops = 128;
    final int numberOfClouds = 16;
    
    static BufferedImage completeTex = null;
    static BufferedImage magPlusTex = null;
    static BufferedImage magMinusTex = null;
    
    static Button zoomInButton;
    static Button zoomOutButton;
    static boolean programmerMode;
    static BufferedImage programmerTex = null;
    static Button programmerButton;
    
    static BufferedImage upgradeTex = null;
    static Button upgradeButton;
	
	public Level() {
		
		sheet = new TileSheet(this,100,10);
		
		rainSound = new Sound("Sound/rain.wav");
		rainSound.load();
		thunderSound[0] = new Sound("Sound/thunder0.wav");
		thunderSound[1] = new Sound("Sound/thunder1.wav");
		for(Sound s:thunderSound) {
			s.load();
			s.setVolume(-10.0f);
		}
		
		completeTex = engine.graphics.Graphics3D.importImage("Gui/CompleteText.png");
		skyTex = engine.graphics.Graphics3D.importImage("Background/Sky.png");
		upgradeTex = engine.graphics.Graphics3D.importImage("Gui/Upgrade.png");
		programmerTex = engine.graphics.Graphics3D.importImage("Gui/ProgMode.png");
		programmerButton = new Button(Button.IMAGE_BUTTON);
		programmerButton.setImage(programmerTex);
		
		upgradeButton = new Button(Button.IMAGE_BUTTON);
		upgradeButton.setImage(upgradeTex);
		
		zoomInButton = new Button(Button.IMAGE_BUTTON);
		zoomOutButton = new Button(Button.IMAGE_BUTTON);
		magPlusTex = engine.graphics.Graphics3D.importImage("Gui/ZoomIn.png");
		magMinusTex = engine.graphics.Graphics3D.importImage("Gui/ZoomOut.png");
		zoomInButton.setImage(magPlusTex);
		zoomOutButton.setImage(magMinusTex);
		
	}
	
	
	public void update() {
		timePlayed+=1.0/60.0;
		
		if(weather==RAINY) {
			for(int i = 0;i<2;i++) {
	        	RainDrop dr = new RainDrop();
	        	dr.setPosition(Game.mainChar.getX()+Math.random()*sheet.tileScreenFit*1.5-sheet.tileScreenFit*(1.5/2.0), 0);
	        	dr.setLevel(this);
	        }
		}
		
		upgradeButton.update();
		zoomInButton.update();
		programmerButton.update();
		
		if(upgradeButton.isClicked()) Game.showUpgradeWindow = !Game.showUpgradeWindow;
		
		if(zoomInButton.isClicked()) sheet.zoom--;
		
		if(programmerButton.isClicked()) {
			Game.mainChar.jetPack = !Game.mainChar.jetPack;
			Game.mainChar.fastShoes = !Game.mainChar.fastShoes;
			Game.mainChar.highJump = !Game.mainChar.highJump;
		}
		
		zoomOutButton.update();
		if(zoomOutButton.isClicked()) sheet.zoom++;
		
		if(weather==RAINY) {
			
			if(!rainSound.isPlaying())rainSound.play();
			
				
			
		}
		
		if(trashGenerates) {
			//place trash randomly around player
			if((int)(Math.random()*1000)==1) {
				Trash trash = new Trash();
        		trash.setPosition(Game.mainChar.getX()+(Math.random()*20-10), Math.random()*sheet.height);
        		trash.addToAcceleration(Math.random()*2.0-1.0, 0);
        		trash.setLevel(this);
			}
		}
		
		sheet.update();
		completeTimer--;
		if(completeTimer == 1) {
			Game.backToHome();
		}
	}
	
	public void render (Graphics2D go,double scrollX,int wx,int wy,int ww,int wh) {
		
		
		BufferedImage gameScreen = Graphics3D.createBlankBufferedImage(ww,wh);
		Graphics2D g = gameScreen.createGraphics();
		
		tileScreenFit = ww/sheet.spaceing;
		
		//weather back
		weatherBack(g,ww,wh);
		
		
		sheet.render(g,scrollX,ww,wh);
		//weather
		
		weatherFront(g);
		
		//renders the level complete sign when flag reached
		renderComplete(g);
		
		//draw the gameScreen
		go.drawImage(gameScreen,wx,wy, null);
		
		
		zoomInButton.setView(ww-Settings.GUI_ELEM_SPACING+wx, wh-Settings.GUI_ELEM_SPACING+wy, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING);
		zoomOutButton.setView(ww-Settings.GUI_ELEM_SPACING+wx, wh-Settings.GUI_ELEM_SPACING*2+wy, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING);
		upgradeButton.setView(Settings.WINDOW_WIDTH-Settings.PADDING_SIZE*2-Settings.GUI_ELEM_SPACING, 0, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING);
		programmerButton.setView(Settings.WINDOW_WIDTH-Settings.PADDING_SIZE*2-Settings.GUI_ELEM_SPACING*2, 0, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING);
		zoomInButton.render(go);
		zoomOutButton.render(go);
		upgradeButton.render(go);
		programmerButton.render(go);
		Game.market.stockInvestGui.renderStockGraph(go, Settings.WINDOW_WIDTH-Settings.PADDING_SIZE*2, 0, Settings.PADDING_SIZE*2, Settings.PADDING_SIZE, 60, 14);
		numDisplay.render(go, (int)timePlayed%60, Settings.GUI_ELEM_SPACING/2, Settings.WINDOW_WIDTH-Settings.PADDING_SIZE*4, 0);
		numDisplay.render(go, (int)timePlayed/60, Settings.GUI_ELEM_SPACING/2, Settings.WINDOW_WIDTH-Settings.PADDING_SIZE*4-Settings.GUI_ELEM_SPACING, 0);
	}
	
	//the complete flag that scrolls by screen
	int completeDuration = 60*4;
	int completeTimer = 0;
	public void renderComplete(Graphics2D g) {
		double x = 0;
		if(completeTimer>=completeDuration*2/3) {
			x= (completeTimer-completeDuration*2.0/3.0)/(completeDuration/3.0);
			x*=Game.WIDTH;
			x+=Game.WIDTH/2;
		}else if(completeTimer>=completeDuration/3.0) x = Game.WIDTH/2.0;
		else if(completeTimer>0){
			x = (completeTimer)/(completeDuration/3.0);
			x*=Game.WIDTH;
			x-=Game.WIDTH/2;
		}
		if(completeTimer>0) g.drawImage(completeTex, (int)x-sheet.spaceing*8,Game.HEIGHT/2-sheet.spaceing*2, sheet.spaceing*16,sheet.spaceing*4, null);
	}
	public void complete() {
		complete = true;
		if(completeTimer<0) completeTimer = completeDuration;
	}
	void weatherFront(Graphics2D g) {
	}
	
	Color rainyBackgroundColor = new Color(32,64,128,200);
	void weatherBack(Graphics2D g,int ww,int wh) {
		int lightning = (int)(Math.random()*5000.0);
		g.drawImage(skyTex, 0, 0, ww, wh, null);
		if(weather==RAINY) {
			if(lightning>50) {
				g.setColor(rainyBackgroundColor);
				g.fillRect(0, 0, ww, wh);
			}else {
				//play thunder out of random set
				int chosenSound = (int)(Math.random()*2.0);
				thunderSound[chosenSound].play();
				
				g.setColor(rainyBackgroundColor.brighter());
				g.fillRect(0, 0, ww, wh);
			}
		}
	}
	
	public void saveLevel() {
		
		try {
		  FileWriter myWriter = new FileWriter("save.txt");
		  
		  myWriter.write(Double.toString(initX)+" ");
		  myWriter.write(Double.toString(initY));
		  myWriter.write("\n");
		  myWriter.write(Integer.toString(weather));
		  myWriter.write("\n");
		  myWriter.write(Integer.toString(sheet.length)+" "+Integer.toString(sheet.height));
		  myWriter.write("\n");
		  for(int i = 0;i<sheet.height;i++) {
			  for(int j = 0;j<sheet.length;j++) {
				  Tile temp = sheet.getTile(j,i);
				  int texture = temp.textID;
				  double height = temp.height;
				  int brightness = temp.brightness;
				  int hardness = temp.hardness;
				  boolean coin = temp.coin;
				  int layer = temp.layer;
				  boolean painful = temp.painful;
				  
				  myWriter.write(Integer.toString(hardness)+" "+Boolean.toString(coin)+" "+Integer.toString(layer)+" "+Integer.toString(texture)+" "+Double.toString(height)+" "+Integer.toString(brightness)+" "+Boolean.toString(painful)+" ");
			  }
			  myWriter.write("\n");
		  }
		  /*
		  for(Ent e: sheet.ents) {
			  if(e instanceof Character ) {
				  Character c  = (Character)e;
				  //if(c.type!=Character.MAIN_CHAR)
				   //myWriter.write("c "+Integer.toString(c.type)+" "+Double.toString(c.getX())+" "+Double.toString(c.getY()));
			  	   myWriter.write("\n");
			  }
		  }
		  */
		  
	      myWriter.close();
	      System.out.println("Successfully saved.");
		} catch (IOException e) {
		  System.out.println("An error occurred.");
		  e.printStackTrace();
		}
		
	}
	
	boolean pMousePressed = false;
	public void input(KeyBoard keyBoard,Mouse mouse) {
		zoomInButton.input(keyBoard, mouse);
		zoomOutButton.input(keyBoard, mouse);
		upgradeButton.input(keyBoard, mouse);
		programmerButton.input(keyBoard, mouse);
		
		sheet.input(keyBoard, mouse);
		
		//check if clicking on stock graph in corner to open market window
		if(mouse.x>Settings.WINDOW_WIDTH-Settings.PADDING_SIZE*2&&mouse.y<Settings.PADDING_SIZE&&mouse.mousePressed&&!pMousePressed) {
			Game.marketProgramOpen = !Game.marketProgramOpen;
		}
		pMousePressed = mouse.mousePressed;
	}
	
	public void openLevel(String filename) {
		 int lineNumber = 0;
		 int row = 0;
		 sheet.ents.clear();
		 Game.mainChar.setLevel(this);
		try {
		      File myObj = new File(filename);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String line = myReader.nextLine();
		        String[] parts = line.split(" ");
		        //for(int i = 0;i<parts.length;i++) System.out.println(parts[i]);
		        if(lineNumber==0) {
		        	Game.mainChar.setPosition(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
		        	
		        	initX  = Double.parseDouble(parts[0]);;
		        	initY  = Double.parseDouble(parts[1]);
		        	
		        	Game.mainChar.setVelocityVector(0, 0);
		        }
		        if(lineNumber==1) {
		        	weather = Integer.parseInt(parts[0]);
		        }
		        if(lineNumber==2) {
		        	sheet.reSize(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
		        }
		        if(lineNumber<=sheet.height+2&&lineNumber>2) {
		        	
		        	for(int i = 0;i<sheet.length;i++) {
		        			
		        		int texture = Integer.parseInt(parts[i*7+3]);
		  				double height = Double.parseDouble(parts[i*7+4]);
		  				int brightness = Integer.parseInt(parts[i*7+5]);
		  				int hardness = Integer.parseInt(parts[i*7]);
		  				boolean coin = Boolean.parseBoolean(parts[i*7+1]);
		  				int layer = Integer.parseInt(parts[i*7+2]);
		  				boolean painful = Boolean.parseBoolean(parts[i*7+6]);
		  				
		       			sheet.setTile(i, row, painful, height, coin, texture, hardness, layer, brightness);
		  			    
		       		}
		        	row++;
		        	
		        }
		        if(lineNumber>sheet.height+3) {
		        	int charType = 0;
		        	double xp = 0,yp = 0;
		        	if(parts[0].contains("e")) {
		        		charType = Integer.parseInt(parts[1]);
		        		xp = Double.parseDouble(parts[2]);
		        		yp = Double.parseDouble(parts[3]);
		        		
		        		//Character c = new Character(charType);
		        		//c.setPosition(xp, yp);
		        		//c.setLevel(this);
		        	}
		        	
		        }
		        
		        
		        
		        lineNumber++;
		      }
		      myReader.close();
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		 }
	}
	
}
