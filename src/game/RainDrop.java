package game;

import java.awt.Color;
import java.awt.Graphics2D;

import engine.main.KeyBoard;
import engine.main.Mouse;

public class RainDrop extends Ent{

	Color dropColor;
	
	public RainDrop() {
		this.tileInteractive(true);
		int br = (int)(Math.random()*128)+128;
		dropColor = new Color(br/2,br/2,br);
		this.canCollectCoins = false;
		this.setSize(0.1, 0.5);
		this.canFeelPain = true;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(dropColor);
		g.fillRect((int)this.getScale()/2,(int)this.getScale()/2 , (int)(this.getScale()/10.0),  (int)(this.getScale()/2.0));
	}

	@Override
	void healthLost() {
		// TODO Auto-generated method stub
		this.destroy();
	}

	@Override
	public void input(KeyBoard keyBoard, Mouse mouse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void update() {
		this.setVelocityVector(-0.1,0.3 );
		this.updatePhysics();
		if(this.touchingBottom) {
			this.destroy();
		}
	}

}
