package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.main.KeyBoard;
import engine.main.Mouse;

public class Upgrade {
	
	Button jumpingUpgrades;
	Button[] investmentUpgrades = new Button[5];
	Button speedUpgrades;
	Button flyingUpgrades;
	
	BufferedImage upgradesLogoTex;
	BufferedImage stockMarketTex;
	BufferedImage bankTex;
	BufferedImage lockTex;
	BufferedImage windTex;
	BufferedImage higherJumpTex;
	BufferedImage jetPackTex;
	
	public boolean stockMarketLocked = true;
	public boolean bankAccountLocked = true;
	public boolean higherSpeedLocked = true;
	public boolean higherJumpLocked = true;
	public boolean jetPackLocked = true;
	
	
	ExplenationWindow bankAccountEx = new ExplenationWindow();
	ExplenationWindow stockMarketEx = new ExplenationWindow();
	ExplenationWindow higherSpeedEx = new ExplenationWindow();
	ExplenationWindow higherJumpEx = new ExplenationWindow();
	ExplenationWindow jetPackEx = new ExplenationWindow();
	
	int mouseX,mouseY;
	
	public Upgrade() {
		//explanations of upgrades
		bankAccountEx.addLine("Lets you keep");
		bankAccountEx.addLine("more than 300");
		bankAccountEx.addLine("coins. You can");
		bankAccountEx.addLine("next buy the");
		bankAccountEx.addLine("ability to trade");
		bankAccountEx.addLine("stocks");
		bankAccountEx.addLine("Cost: $250");
		
		stockMarketEx.addLine("Lets you trade");
		stockMarketEx.addLine("stocks as you");
		stockMarketEx.addLine("choose. Unlocks");
		stockMarketEx.addLine("ability to buy");
		stockMarketEx.addLine("run faster");
		stockMarketEx.addLine("upgrade");
		stockMarketEx.addLine("Cost: $500");
		
		higherSpeedEx.addLine("Lets you run");
		higherSpeedEx.addLine("to higher speed.");
		higherSpeedEx.addLine("Unlocks the");
		higherSpeedEx.addLine("ability to buy");
		higherSpeedEx.addLine("higher jump");
		higherSpeedEx.addLine("Cost: $1000");
		
		higherJumpEx.addLine("Lets you jump");
		higherJumpEx.addLine("with higher power.");
		higherJumpEx.addLine("Unlocks the");
		higherJumpEx.addLine("ability to buy");
		higherJumpEx.addLine("jetpack");
		higherJumpEx.addLine("Cost: $5000");
		
		jetPackEx.addLine("Lets you fly");
		jetPackEx.addLine("Press the Space");
		jetPackEx.addLine("Bar and hold");
		jetPackEx.addLine("to activate");
		jetPackEx.addLine("Cost: $10000");
		
		//Button textures
		upgradesLogoTex = engine.graphics.Graphics3D.importImage("Gui/Upgrade.png");
		stockMarketTex = engine.graphics.Graphics3D.importImage("Gui/StockMarket.png");
		bankTex = engine.graphics.Graphics3D.importImage("Gui/Bank.png");
		windTex = engine.graphics.Graphics3D.importImage("Gui/Wind.png");
		higherJumpTex = engine.graphics.Graphics3D.importImage("Gui/HigherJump.png");
		jetPackTex = engine.graphics.Graphics3D.importImage("Gui/JetPack.png");
		lockTex = engine.graphics.Graphics3D.importImage("Gui/Lock.png");
		
		//initializing buttons
		for(int i = 0;i<5;i++) {
			investmentUpgrades[i] = new Button(Button.IMAGE_BUTTON);
		}
		
		//setting button textures
		investmentUpgrades[0].setImage(bankTex);
		investmentUpgrades[1].setImage(stockMarketTex);
		investmentUpgrades[2].setImage(windTex);
		investmentUpgrades[3].setImage(higherJumpTex);
		investmentUpgrades[4].setImage(jetPackTex);
		
	}
	Color backgroundColor = new Color(50,0,0,200);
	public void render(Graphics2D g) {
		
		for(int i = 0;i<investmentUpgrades.length;i++) {
			investmentUpgrades[i].setView(Settings.TOP_CORNER_X+ Settings.GUI_ELEM_SPACING*2*i, Settings.TOP_CORNER_Y, Settings.GUI_ELEM_SPACING*2,  Settings.GUI_ELEM_SPACING*2);
		}
		
		//drawing upgrade window
		g.setColor(backgroundColor);
		g.fillRect(Settings.TOP_CORNER_X, Settings.TOP_CORNER_Y, Settings.GUI_WIDTH, Settings.GUI_HEIGHT);
		
		
		for(int i = 0;i<investmentUpgrades.length;i++) investmentUpgrades[i].render(g);
		
		if(bankAccountLocked) g.drawImage(lockTex, Settings.PADDING_SIZE ,Settings.PADDING_SIZE+Settings.GUI_ELEM_SPACING*2, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING,null);
		if(stockMarketLocked) g.drawImage(lockTex, Settings.PADDING_SIZE+Settings.GUI_ELEM_SPACING*2 ,Settings.PADDING_SIZE+Settings.GUI_ELEM_SPACING*2, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING,null);
		if(higherSpeedLocked) g.drawImage(lockTex, Settings.PADDING_SIZE+Settings.GUI_ELEM_SPACING*4 ,Settings.PADDING_SIZE+Settings.GUI_ELEM_SPACING*2, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING,null);
		if(higherJumpLocked) g.drawImage(lockTex, Settings.PADDING_SIZE+Settings.GUI_ELEM_SPACING*6 ,Settings.PADDING_SIZE+Settings.GUI_ELEM_SPACING*2, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING,null);
		if(jetPackLocked) g.drawImage(lockTex, Settings.PADDING_SIZE+Settings.GUI_ELEM_SPACING*8 ,Settings.PADDING_SIZE+Settings.GUI_ELEM_SPACING*2, Settings.GUI_ELEM_SPACING, Settings.GUI_ELEM_SPACING,null);
		
		if(investmentUpgrades[0].isMouseHovering()) bankAccountEx.render(g, mouseX, mouseY);
		if(investmentUpgrades[1].isMouseHovering()) stockMarketEx.render(g, mouseX, mouseY);
		if(investmentUpgrades[2].isMouseHovering()) higherSpeedEx.render(g, mouseX, mouseY);
		if(investmentUpgrades[3].isMouseHovering()) higherJumpEx.render(g, mouseX, mouseY);
		if(investmentUpgrades[4].isMouseHovering()) jetPackEx.render(g, mouseX, mouseY);
		
		
		//lock textures on upgrades
		
	}
	public void update() {
		if(investmentUpgrades[0].isClicked()&&Game.market.yourAccount.money>=250) {
			if(bankAccountLocked) Game.market.yourAccount.money-=250;
			bankAccountLocked = false;
		}
		if(investmentUpgrades[1].isClicked()&&Game.market.yourAccount.money>=500) {
			if(stockMarketLocked)Game.market.yourAccount.money-=500;
			stockMarketLocked = false;
		}
		if(investmentUpgrades[2].isClicked()&&Game.market.yourAccount.money>=1000&&!stockMarketLocked) {
			if(higherSpeedLocked)Game.market.yourAccount.money-=1000;
			Game.mainChar.fastShoes=true;
			higherSpeedLocked = false;
		}
		if(investmentUpgrades[3].isClicked()&&Game.market.yourAccount.money>=5000&&!higherSpeedLocked) {
			if(higherJumpLocked)Game.market.yourAccount.money-=5000;
			Game.mainChar.highJump=true;
			higherJumpLocked = false;
		}
		if(investmentUpgrades[4].isClicked()&&Game.market.yourAccount.money>=10000&&!higherJumpLocked) {
			if(jetPackLocked)Game.market.yourAccount.money-=10000;
			Game.mainChar.jetPack=true;
			jetPackLocked = false;
		}
	}
	public void input(KeyBoard keyBoard,Mouse mouse){
		mouseX = mouse.x;
		mouseY = mouse.y;
		for(int i = 0;i<investmentUpgrades.length;i++) investmentUpgrades[i].input(keyBoard,mouse);
	}
	
	class ExplenationWindow{
		
		ArrayList<String> text = new ArrayList<String>();
		
		public void render(Graphics2D g,int x,int y) {
			g.setColor(Color.black);
			g.fillRect(x,y,Game.WIDTH/6,Game.WIDTH/6);
			g.setColor(Color.white);
			g.setFont(new Font(Font.MONOSPACED,0, Game.HEIGHT/50));
			for(int i = 0;i < text.size();i++) {
				String s = text.get(i);
				g.drawString(s, x+Game.HEIGHT/50, y+Game.HEIGHT/50*(i+2));
			}
			g.drawRect(x,y,Game.WIDTH/6,Game.WIDTH/6);
		}
		
		public void addLine(String s) {
			text.add(s);
		}
		
	}
	
}
