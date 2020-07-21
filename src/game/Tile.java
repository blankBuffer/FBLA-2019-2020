package game;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

class Tile{
	
	//interaction
	static final int AIR = 0;
	static final int SOLID = 1;
	static final int LIQ = 2;
	
	//coin
	
	public int coinRiseTimer = 0;
	
	//layer
	static final int BACK = 0;
	static final int FRONT = 1;
	
	//media
	private static final int numberOfTileTex = 8;
	public static BufferedImage[] tileTex = new BufferedImage[numberOfTileTex];
	public static BufferedImage[] lavaTex = new BufferedImage[3];
	public static BufferedImage[] waterTex = new BufferedImage[3];
	public static BufferedImage completeFlagTex = null;
	
	
	
	public static BufferedImage coinTex;
	static boolean imported = false;
	
	private static ArrayList<cacheTile> cachedTiles = new ArrayList<cacheTile>();
	
	//attributes
	boolean painful = false;
	double height = 1;
	int x,y;
	boolean coin = false;
	int textID = -1;
	int hardness = 0;
	int layer = 0;
	int brightness = 255;
	
	//background
	int backTestID = -1;
	int backbrightness = 255;
	
	//owner
	TileSheet sheet;
	
	public Tile(int isSolid,int x,int y,TileSheet parent) {
		sheet = parent;
		hardness = isSolid;
		this.x = x;
		this.y = y;
		if(!imported) {
			coinTex = engine.graphics.Graphics3D.importImage("Tile/Coin.png");
			for(int i = 0;i<numberOfTileTex;i++) tileTex[i] = engine.graphics.Graphics3D.importImage("Tile/Tile"+i+".png");
			for(int i = 0;i<3;i++) lavaTex[i] = engine.graphics.Graphics3D.importImage("Tile/Lava"+i+".png");
			for(int i = 0;i<3;i++) waterTex[i] = engine.graphics.Graphics3D.importImage("Tile/Water"+i+".png");
			completeFlagTex = engine.graphics.Graphics3D.importImage("Tile/Flag.png");
			imported = true;
		}
	}
	
	public void render(Graphics2D g,double rightShift) {
		
		if(textID != -1) {
			if(height == 1) {
				if(textID>-1) g.drawImage(tileTex[textID], (int)rightShift-1, sheet.spaceing-(int)(sheet.spaceing),sheet.spaceing,sheet.spaceing, null);
				else if(textID == -2){
					if(textureTimerLava<10) g.drawImage(lavaTex[0], (int)rightShift-1, sheet.spaceing-(int)(sheet.spaceing),sheet.spaceing,sheet.spaceing, null);
					else if(textureTimerLava<20) g.drawImage(lavaTex[1], (int)rightShift-1, sheet.spaceing-(int)(sheet.spaceing),sheet.spaceing,sheet.spaceing, null);
					else if(textureTimerLava<30) g.drawImage(lavaTex[2], (int)rightShift-1, sheet.spaceing-(int)(sheet.spaceing),sheet.spaceing,sheet.spaceing, null);
					else g.drawImage(lavaTex[1], (int)rightShift-1, sheet.spaceing-(int)(sheet.spaceing),sheet.spaceing,sheet.spaceing, null);
				} else if(textID == -3){
					if(textureTimerWater<10) g.drawImage(waterTex[0], (int)rightShift-1, sheet.spaceing-(int)(sheet.spaceing),sheet.spaceing,sheet.spaceing, null);
					else if(textureTimerWater<20) g.drawImage(waterTex[1], (int)rightShift-1, sheet.spaceing-(int)(sheet.spaceing),sheet.spaceing,sheet.spaceing, null);
					else if(textureTimerWater<30) g.drawImage(waterTex[2], (int)rightShift-1, sheet.spaceing-(int)(sheet.spaceing),sheet.spaceing,sheet.spaceing, null);
					else g.drawImage(waterTex[1], (int)rightShift-1, sheet.spaceing-(int)(sheet.spaceing),sheet.spaceing,sheet.spaceing, null);
				}else if(textID == -4) {
					g.drawImage(completeFlagTex, (int)rightShift-1, sheet.spaceing-(int)(sheet.spaceing),sheet.spaceing,sheet.spaceing, null);
				}
			}else if(height != 0&&textID>-1){
				boolean available = false;
				BufferedImage crop = null;
				for(cacheTile ct: cachedTiles) {
					if(ct.height == height&&ct.textID == textID) {
						available = true;
						crop = ct.cacheImage;
					}
				}
				if(!available) {
					crop = tileTex[textID].getSubimage(0, 0, tileTex[textID].getWidth(),(int) (height*(double) tileTex[textID].getHeight()));
					cachedTiles.add(new cacheTile(crop,textID,height));
				}
				g.drawImage(crop,(int)rightShift-1, sheet.spaceing-(int)(height*sheet.spaceing), sheet.spaceing, (int)(height*sheet.spaceing) , null);	
				if(cachedTiles.size()>128) cachedTiles.remove(0);
			}
		}
		
		if(brightness>255) brightness = 255;
		if(brightness<0) brightness = 0;
		
		if(brightness!=255) {
			g.setColor(new Color(0,0,0,255-brightness));
			g.fillRect((int)rightShift-1, sheet.spaceing-(int)(height*sheet.spaceing), sheet.spaceing, (int)(height*sheet.spaceing)  );
		}
		renderCoin(g,rightShift);
		
	}
	boolean isCoin = false;
	public void renderCoin(Graphics2D g,double rightShift) {
		if(coin==true) {
			isCoin = true;
			int x = (int)rightShift-1+sheet.spaceing/2-sheet.spaceing/8;
			int y = sheet.spaceing-(int)sheet.spaceing+sheet.spaceing/2-sheet.spaceing/8;
			g.drawImage(coinTex,x, y, sheet.spaceing/4, sheet.spaceing/4 , null);
		}else {
			if(isCoin) {
				coinRiseTimer++;
				if(coinRiseTimer%10<5&&coinRiseTimer<30) {
					int x = (int)rightShift-1+sheet.spaceing/2-sheet.spaceing/8;
					int y = sheet.spaceing-(int)sheet.spaceing+sheet.spaceing/2-sheet.spaceing/8-(coinRiseTimer*sheet.spaceing)/40;
					g.drawImage(coinTex,x, y, sheet.spaceing/4, sheet.spaceing/4 , null);
					
				}
			}
		}
	}
	
	private class cacheTile{
		
		cacheTile(BufferedImage i,int t,double h){
			cacheImage = i;
			textID = t;
			height  = h;
		}
		BufferedImage cacheImage = null;
		int textID = 0;
		double height = 1;
		
	}
	static int textureTimerWater = 0;
	static int textureTimerLava = 0;
	static public void updateTex() {
		textureTimerLava++;
		textureTimerWater+=(int)(Math.random()*5.0);
		if(textureTimerLava>40) textureTimerLava = 0;
		if(textureTimerWater>40) textureTimerWater = 0;
	}
	
	public void setTile(boolean painful,double height,boolean coin,int textID,int hardness,int layer,int brightness) {
		this.height = height;
		this.coin = coin;
		this.textID = textID;
		this.hardness = hardness;
		this.layer = layer;
		this.brightness = brightness;
		this.painful = painful;
	}
	
}