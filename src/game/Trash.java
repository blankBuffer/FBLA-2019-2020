package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.main.KeyBoard;
import engine.main.Mouse;
import engine.main.Sound;

public class Trash extends Ent{
	
	private static BufferedImage[] textures = new BufferedImage[4];
	int chosenTex = (int)(Math.random()*4.0);
	private static boolean imported = false;
	private static MainCharacter refToChar = null;
	
	private static Sound collectionSound;
	
	public Trash(){
		this.setSize(0.75, 0.5);
		if(!imported) {
			for(int i = 0;i<textures.length;i++) textures[i] = engine.graphics.Graphics3D.importImage("Ent/Trash"+i+".png");
			collectionSound = new Sound("Sound/trash.wav");
			collectionSound.load();
			imported = true;
		}
		this.tileInteractive(true);
		this.canCollectCoins = false;
		this.canFeelPain = false;
		this.setGravity(0.002*Math.random()+0.002);
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(textures[chosenTex], 0,-(int)this.getScale()/4 ,(int)this.getScale(),(int)this.getScale(), null);
	}

	@Override
	void healthLost() {}
	@Override
	public void input(KeyBoard keyBoard, Mouse mouse) {}

	boolean startTerminalCountdown = false;
	//15 ticks for 1/4 second of collection time
	int terminalCountdown = 15;
	
	@Override
	void update() {
		// TODO Auto-generated method stub
		this.updatePhysics();
		//check for neer player
		
		//get reference character entity if it does not exits
		if(refToChar==null) {
			refToChar = Game.mainChar;
		}
		
		//remove if outside world
		if(getX()<0) this.destroy();
		
		//check if its 1 units away, if so start count down timer for deletion and collection of money
		if(Math.abs(refToChar.getX()-this.getX())<1.0) {
			if(Math.abs(refToChar.getY()-this.getY())<1.0) {
				startTerminalCountdown = true;
				
			}
		}
		if(startTerminalCountdown) {
			terminalCountdown--;
			this.addToAcceleration(0, -0.008);
		}
		//playSound and add money
		if(terminalCountdown==7) {
			Game.market.yourAccount.add(0.5);
			collectionSound.play();
		}
		
		//fully collected needs to be destroyed
		if(terminalCountdown<0) this.destroy();
		
		
	}
	
}
