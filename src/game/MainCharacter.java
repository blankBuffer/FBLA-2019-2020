package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.main.KeyBoard;
import engine.main.Mouse;

public class MainCharacter extends Character{
	
	NumberDisplay display = new NumberDisplay();
	BufferedImage[] stillRightTex = new BufferedImage[4];
	BufferedImage[] stillLeftTex = new BufferedImage[4];
	BufferedImage rightStepRightTex = null;
	BufferedImage leftStepRightTex = null;
	BufferedImage rightStepRightStillTex = null;
	BufferedImage rightStepLeftTex = null;
	BufferedImage leftStepLeftTex = null;
	BufferedImage leftStepLeftStillTex = null;
	
	BufferedImage[] landAnimation = new BufferedImage[9];
	BufferedImage[] shoesAnimation = new BufferedImage[5];
	
	
	ArrayList<JetParticle> jetParts = new ArrayList<JetParticle>();
	private final boolean LEFT = false;
	private final boolean RIGHT = true;
	private boolean faceing = RIGHT;
	
	int walkTimer = 0;
	
	public MainCharacter(){
		fastShoes = false;
		jetPack = false;
		for(int i = 0;i<4;i++) stillRightTex[i] = engine.graphics.Graphics3D.importImage("Char/stillRight"+i+".png");
		for(int i = 0;i<4;i++) stillLeftTex[i] = engine.graphics.Graphics3D.importImage("Char/stillLeft"+i+".png");
		rightStepRightTex = engine.graphics.Graphics3D.importImage("Char/rightStepRight.png");
		leftStepRightTex = engine.graphics.Graphics3D.importImage("Char/leftStepRight.png");
		rightStepRightStillTex = engine.graphics.Graphics3D.importImage("Char/rightStepRightStill.png");
		rightStepLeftTex = engine.graphics.Graphics3D.importImage("Char/rightStepLeft.png");
		leftStepLeftTex = engine.graphics.Graphics3D.importImage("Char/leftStepLeft.png");
		leftStepLeftStillTex = engine.graphics.Graphics3D.importImage("Char/leftStepLeftStill.png");
		for(int i = 0;i<landAnimation.length;i++) landAnimation[i] = engine.graphics.Graphics3D.importImage("Char/land"+i+".png");
		for(int i = 0;i<shoesAnimation.length;i++) shoesAnimation[i] = engine.graphics.Graphics3D.importImage("Char/shoes"+i+".png");
		this.setCanCompleteLevels(true);
	}
	
	double landAnimationFrameCounter = 0;
	double shoesAnimationFrameCounter = 0;
	@Override
	public void render(Graphics2D g) {
		renderJetPack(g);
		BufferedImage currentTex = null;
		if(Math.abs(this.getVelocityX())<0.01) {
			if(faceing==RIGHT) currentTex = stillRightTex[(int)walkTimer/10];
			else if(faceing==LEFT) currentTex = stillLeftTex[(int)walkTimer/10];
		}else {
			if(faceing==RIGHT) {
				if(walkTimer<35/4) {
					currentTex = rightStepRightTex;
				}
				else if(walkTimer < 35/4*2 ) currentTex = rightStepRightStillTex;
				else if(walkTimer < 35/4*3) {
					currentTex = leftStepRightTex;
				}
				else currentTex = rightStepRightStillTex;
			}else if(faceing==LEFT) {
				if(walkTimer<35/4) {
					currentTex = rightStepLeftTex;
				}
				else if(walkTimer < 35/4*2 ) currentTex = leftStepLeftStillTex;
			
				else if(walkTimer < 35/4*3) {
					currentTex = leftStepLeftTex;
				}
				else currentTex = leftStepLeftStillTex;
			}
		}
		if(healthLostTimer%6<3) {
			g.drawImage(currentTex,0, 0, (int)(getScale()), (int)(getScale()),null);
			if((int)landAnimationFrameCounter<9) g.drawImage(landAnimation[(int)landAnimationFrameCounter],0, 0, (int)(getScale()), (int)(getScale()),null);
			if(fastShoes)g.drawImage(shoesAnimation[(int)shoesAnimationFrameCounter],0, 0, (int)(getScale()), (int)(getScale()),null);
		}
	}
	
	public void renderJetPack(Graphics2D g) {
		for(JetParticle j:jetParts ) j.render(g);
		if(jetPack&&healthLostTimer%6<3) {
			if(faceing==RIGHT) {
				g.setColor(Color.DARK_GRAY);
				g.fillRect(0,level.sheet.spaceing/4 , level.sheet.spaceing/3,level.sheet.spaceing/2 );
			}else {
				g.setColor(Color.DARK_GRAY);
				g.fillRect(level.sheet.spaceing-level.sheet.spaceing/3,level.sheet.spaceing/4 , level.sheet.spaceing/3,level.sheet.spaceing/2 );
			}
		}
	}
	
	private double pVelY;
	public void update() {
		healthLostTimer--;
		walkTimer+=Math.abs(this.getVelocityX()*20.0)+1;
		if(walkTimer>35) walkTimer = 0;
		
		
		updatePhysics();
		
		//hitting ground sound
		{
			if(touchingBottom&&pVelY>0) {
				frictionSound.play();
				if(landAnimationFrameCounter>10) landAnimationFrameCounter = -1;
			}
			pVelY = this.getVelocityY();
		}
		
		//update and remove particles accordingly
		for(int i = 0;i<jetParts.size();i++) {
			JetParticle temp = jetParts.get(i);
			temp.update();
			if(!temp.isAlive()) {
				jetParts.remove(temp);
			}
		}
		
		//set running speed limit
		if(fastShoes)speedLimit = 0.1;
		else speedLimit = 0.06;
		if(this.highJump) {
			jumpPower = 0.2;
			this.painThreshold = 2;
		}else {
			jumpPower = 0.15;
			this.painThreshold = 0.2;
		}
		
		landAnimationFrameCounter+=0.30;
		shoesAnimationFrameCounter+=0.5;
		if((int)shoesAnimationFrameCounter>4) {
			shoesAnimationFrameCounter = 0;
		}
	}
	
	public void renderHealth(Graphics2D g,int x,int y) {
		if((currentHealth()<0)) return;
		for(int i = 0;i<currentHealth();i++) {
			g.drawImage(heartTex, x+i* Settings.GUI_ELEM_SPACING*3/4, y,  Settings.GUI_ELEM_SPACING/2,  Settings.GUI_ELEM_SPACING/2, null);
		}
		if(healthLostTimer%15<10&&healthLostTimer>0) g.drawImage(brokenHeartTex, x+(currentHealth())*Settings.GUI_ELEM_SPACING*3/4, y, Settings.GUI_ELEM_SPACING/2, Settings.GUI_ELEM_SPACING/2, null);
		
	}
	public void renderMoney(Graphics2D g,int x,int y) {
		g.drawImage(Tile.coinTex, x, y, Settings.GUI_ELEM_SPACING/2, Settings.GUI_ELEM_SPACING/2, null);
		g.setColor(Color.white);
		display.render(g, Game.market.yourAccount.money,Settings.GUI_ELEM_SPACING/2,x+Settings.GUI_ELEM_SPACING, y);
	}
	
	public void input(KeyBoard keyBoard,Mouse mouse) {
		if(keyBoard.R) {
			setPosition(level.initX,level.initY);
			this.setVelocityVector(0, 0);
		}
		if(keyBoard.LEFT) {
			this.addToAcceleration(-0.01, 0);
			faceing = LEFT;
		}
		if(keyBoard.RIGHT) {
			this.addToAcceleration(0.01, 0);
			faceing = RIGHT;
		}
        if(keyBoard.SPACE&&jetPack) {
        	this.setAccelerationVector(this.getAccelerationX(), -getGravity()*1.2);
        	//add jet pack particles
        	for(int c = 0;c<2;c++) jetParts.add(new JetParticle());
        }
        if(keyBoard.UP&&touchingBottom) {
        	this.setVelocityVector(this.getVelocityX(), 0);
        	this.addToAcceleration(0, -jumpPower);
        	if(Game.playFx) jumpSound.play();
        }
        //if(keyBoard.T) {
        
        //}
	}
	
	class JetParticle{
		public int lifeTime = 20;
		double x,y;
		double vX,vY;
		Color c;
		int brightness;
		
		public JetParticle() {
			y = getY()+howTall()/2+1.0/3.0;
			if(faceing==RIGHT) x = getX();
			else x = getX()+1.0;
			vX = (Math.random()*2.0-1.0)*Math.random()*(level.sheet.spaceing/2000.0);
			vY = Math.random()*(level.sheet.spaceing/500.0);
			brightness = (int)(   Math.random()*128   )+64;
		}
		public boolean isAlive() {
			return lifeTime>0;
		}
		public void render(Graphics2D g) {
			c = new Color(brightness,brightness-lifeTime*3,brightness-lifeTime*3);
			g.setColor(c);
			g.fillRect( (int)((x-getX())*level.sheet.spaceing)-level.sheet.spaceing/16, (int)((y-getY())*level.sheet.spaceing)-level.sheet.spaceing/16, level.sheet.spaceing/8, level.sheet.spaceing/8);
		}
		public void update() {
			//random number is used to Giggle particle
			double rn = (Math.random()*2.0-1.0)*(level.sheet.spaceing/500.0);
			x+=vX+rn;
			rn = (Math.random()*2.0-1.0)*(level.sheet.spaceing/500.0);
			y+=vY+rn;
			lifeTime--;
		}
	}
	
}
