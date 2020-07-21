package game;

import java.awt.Graphics2D;

import engine.main.KeyBoard;
import engine.main.Mouse;
import engine.main.Sound;

public abstract class Ent {
	private static boolean imported = false;
	static Sound coinSound = null;
	
	private boolean canTouchTiles = false;
	boolean canFeelPain = true;
	boolean canCollectCoins = true;
	double painThreshold = 0.2;//velocity when hitting ground
	
	private double entWid = 0.5;
	private double entHei = 0.95;
	private double xPos = entWid/2.0,yPos=entHei/2.0;
	private double velX = 0,velY = 0,xAcc,yAcc;
	private boolean canCompleteLevel = false;
	
	private int health = 3;
	
	final double kineticF = 0.005;
	final double staticF = 1.5;
	double speedLimit = 0.06;//tiles per tick
	double jumpPower = 0.15;
	private boolean needsToBeDestroyed = false;
	
	private double gravity = 0.006;
	Tile[][] nearTiles = new Tile[(int)Math.ceil(entWid)+3][(int)Math.ceil(entHei)+3];
	public Level level;
	
	public boolean touchingLeft,touchingRight,touchingTop,touchingBottom,touching;
	
	//settings and getting attribute functions
	public void tileInteractive(boolean c) {
		canTouchTiles = c;
	}
	public void setCanCompleteLevels(boolean b) {
		canCompleteLevel = b;
	}
	public void setAccelerationVector(double xcomp,double ycomp) {
		xAcc = xcomp;
		yAcc = ycomp;
	}
	public void addToAcceleration(double xcomp,double ycomp) {
		xAcc+=xcomp;
		yAcc+=ycomp;
	}
	public double getAccelerationX() {
		return xAcc;
	}
	public double getAccelerationY() {
		return yAcc;
	}
	public double getVelocityX() {
		return velX;
	}
	public double getVelocityY() {
		return velY;
	}
	public void setVelocityVector(double xcomp,double ycomp) {
		velX = xcomp;
		velY = ycomp;
	}
	public void addToVelocity(double xcomp,double ycomp) {
		velX+=xcomp;
		velY+=ycomp;
	}
	public int currentHealth() {
		return health;
	}
	public double getGravity() {
		return gravity;
	}
	public void setGravity(double g) {
		gravity = g;
	}
	public void decreaseHealthBy(int num) {
		health-=num;
	}
	public void setHealth(int num) {
		health = num;
	}
	public void setSize(double wid,double hei) {
		entWid = wid;
		entHei = hei;
	}
	public double howWide() {
		return entWid;
	}
	public double howTall() {
		return entHei;
	}
	public double getX() {
		return xPos;
	}
	public double getY() {
		return yPos;
	}
	public void setPosition(double x,double y) {
		xPos = x;
		yPos = y;
	}
	public void destroy() {
		needsToBeDestroyed = true;
	}
	public boolean shouldBeDestroyed() {
		return needsToBeDestroyed;
	}
	//
	public abstract void render(Graphics2D g);
	abstract void healthLost();
	public abstract void input(KeyBoard keyBoard,Mouse mouse);
	public double getScale() {
		return level.sheet.spaceing;
	}
	abstract void update();
	Ent(){
		if(!imported) {
			coinSound = new Sound("Sound/coin.wav");
			coinSound.load();
			coinSound.setVolume(-10.0f);
			imported = true;
		}
	}
	public void updatePhysics() {
		yAcc += gravity;
		touchingBottom = false;
		touchingTop = false;
		touchingLeft = false;
		touchingRight = false;
		touching = false;
		updatePosition();
		if(canTouchTiles) {
			updateTilesAround();
			if(touchingBottom) {
				velY=-Math.abs(velY/4.0);
				if(velX>kineticF) velX-=kineticF;
				else if(velX<-kineticF) velX+=kineticF;
				else velX/=staticF;
			}
			if(touchingTop) velY=Math.abs(velY/4.0);
			if(touchingLeft) velX=Math.abs(velX/4.0);
			if(touchingRight) velX=-Math.abs(velX/4.0);
		}
	}
	private void updateTilesAround() {
		//left shift and top shift for lS and tS
		int lS = (int)Math.ceil(nearTiles.length/2);
		int tS = (int)Math.ceil(nearTiles.length/2);
		for(int j = -tS;j<nearTiles[0].length-tS;j++) {
			for(int i = -lS;i<nearTiles.length-lS;i++) {
				nearTiles[i+lS][j+tS] = level.sheet.getTile((int)xPos+i,(int)yPos+j);
			}
		}
		boolean touchedHarm = false;
		boolean slowDown = false;
		for(int i = 0;i<nearTiles.length;i++) {
			for(int j = 0;j<nearTiles[0].length;j++) {
				Tile t = nearTiles[i][j];
				double p = 1.0-t.height;
				if(t.hardness == Tile.LIQ) {
					if(xPos>t.x-entWid/2&&xPos<t.x+1+entWid/2&&yPos>t.y-entHei/2&&yPos<t.y+1) {
						slowDown = true;
					}
				}
				if(t.painful&&(xPos>t.x-entWid/2&&xPos<t.x+1+entWid/2&&yPos>t.y-entHei/2&&yPos<t.y+1)) touchedHarm = true;
				if(canCollectCoins) {
					if(t.coin) {
						if(xPos>t.x-entWid/2&&xPos<t.x+1+entWid/2&&yPos>t.y-entHei/2&&yPos<t.y+1) {
							t.coin = false;
							Game.market.yourAccount.add(5);
							coinSound.play();
						}
					}
				}
				if(t.textID==-4&&xPos>t.x&&xPos<t.x+1) {
					if(canCompleteLevel == true) level.complete();
				}
				if(xPos+entWid/2+velX>t.x&&xPos+entWid/2-velX<t.x&&yPos+entHei/2.0>t.y+p&&yPos-entHei/2.0<t.y+1.0) {
					if(t.hardness == Tile.SOLID) {
						xPos = t.x-entWid/2.0-0.0001;
						touchingRight = true;
					}
				}
				if(xPos-entWid/2+velX<t.x+1&&xPos-entWid/2-velX>t.x+1&&yPos+entHei/2.0>t.y+p&&yPos-entHei/2.0<t.y+1.0) {
					if(t.hardness == Tile.SOLID) {
						xPos = t.x+1+entWid/2+0.0001;
						touchingLeft = true;
					}
				}
				if(!touchingBottom&&yPos+entHei/2+velY>t.y+p&&xPos>t.x-entWid/2&&xPos-entWid/2<t.x+1&&yPos+entHei/2<t.y+p) {
					if(t.hardness == Tile.SOLID) {
						yPos = t.y-entHei/2.0+p-0.0001;
						touchingBottom = true;
						if(velY>painThreshold&&canFeelPain) healthLost();		
					}
				}
				if(yPos-entHei/2+velY<t.y+1&&xPos>t.x-entWid/2&&xPos-entWid/2<t.x+1&&yPos-entHei/2>t.y+1.0) {
					if(t.hardness == Tile.SOLID) {
						yPos = t.y+1.0+entHei/2.0+0.0001;
						touchingTop = true;
					}
				}		
			}
		}
		if(slowDown) {
			velX/=1.2;
			velY/=1.5;
		}
		if(touchedHarm&&canFeelPain) healthLost();
	}
	private void updatePosition() {
		if(Math.abs(velX)>speedLimit) {
			velX=Math.signum(velX)*speedLimit;
		}
		xPos+=velX;
		velX+=xAcc;
		xAcc = 0;
		yPos+=velY;
		velY+=yAcc;
		yAcc=0;
	}
	public void setLevel(Level parent){
		level = parent;
		level.sheet.addEntity(this);
		this.setHealth(3);
	}
}
