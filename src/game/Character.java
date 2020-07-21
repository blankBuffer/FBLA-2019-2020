package game;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import engine.main.Sound;

abstract class Character extends Ent{
	
		//media
		static Sound jumpSound = null;
		static Sound frictionSound = null;
		
		boolean recieved = false;
		
		BufferedImage heartTex = null;
		BufferedImage brokenHeartTex = null;
		
		BufferedImage constWorkerTex = null;
		BufferedImage poorPersonTex = null;
		
		public boolean fastShoes = false;
		public boolean jetPack = false;
		public boolean highJump = false;
		
		static final int MAIN_CHAR = 0;
		static final int CONST_WORKER = 1;
		static final int POOR_PERSON = 2;
		private boolean imported = false;
		
		
		public Character(){
			this.tileInteractive(true);
			if(!imported) {
				
				heartTex = engine.graphics.Graphics3D.importImage("Char/Heart.png");
				brokenHeartTex = engine.graphics.Graphics3D.importImage("Char/BrokenHeart.png");
				
				jumpSound = new Sound("Sound/jump.wav");
				frictionSound = new Sound("Sound/friction.wav");
				frictionSound.load();
				jumpSound.load();
					
				
				constWorkerTex = engine.graphics.Graphics3D.importImage("Char/ConstWorker.png");
				poorPersonTex = engine.graphics.Graphics3D.importImage("Char/PoorPerson.png");
				imported = true;
				canFeelPain = true;
			}
		}
		
		int healthLostTimer = 0;
		
		public void healthLost() {
			if(healthLostTimer<0) {
				this.decreaseHealthBy(1);
				healthLostTimer = 120;
			}
		}
		
		public abstract void render(Graphics2D g);		
		public abstract void update();
	}