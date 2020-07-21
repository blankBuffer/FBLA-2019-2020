package game;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import engine.main.KeyBoard;
import engine.main.Mouse;

public class StockMarket {
	
	
	static ArrayList<Stock> theMarket = new ArrayList<Stock>();
	
	StockInvestGui stockInvestGui = null;
	BufferedImage lockTex = null;
	
	int day = 0;
	
	Stock vans = new Stock(Stock.TYPE_RETAIL,"VAN",0.0001,2.1,.10);
	Stock lol = new Stock(Stock.TYPE_RETAIL,"LOL",0.0002,1.5,.07);
	Stock map = new Stock(Stock.TYPE_RETAIL,"MAP",0.00015,5.3,.03);
	Stock moo = new Stock(Stock.TYPE_RETAIL,"MOO",0.00015,5.3,.03);
	Stock cip = new Stock(Stock.TYPE_RETAIL,"CIP",0.00015,5.3,.03);
	
	BankAccount yourAccount = new BankAccount(100.0);
	InvestAccount yourInvestments = new InvestAccount(yourAccount);
	
	public StockMarket(){
		
		theMarket.add(vans);
		theMarket.add(lol);
		theMarket.add(map);
		theMarket.add(moo);
		theMarket.add(cip);
		
		for(int i = 0;i<365*2;i++) {
			newDay();
		}
		
		stockInvestGui = new StockInvestGui();
		
	}
	
	int counter = 0;
	public void update() {
		
		stockInvestGui.update();
		
		counter++;
		if(counter>=10) {
			newDay();
			counter = 0;
		}
		
		if(yourAccount.money>300 && Game.upgradeWindow.bankAccountLocked ) yourAccount.money = 300;
		
		
	}
	
	public void newDay() {
		for(Stock s: theMarket) {
			s.dayStep();
		}
		
		day++;
	}
	public void input(KeyBoard keyBoard,Mouse mouse) {
		stockInvestGui.input(keyBoard, mouse);
	}
	public void render(Graphics2D g) {
		
		stockInvestGui.render(g);
		
	}
	
	class StockInvestGui{
		
		int x = 0,y = 0,wid = 1, hei = 1;
		Button buyButton = null;
		Button sellButton = null;
		
		BufferedImage buyTex = null;
		BufferedImage sellTex = null;
		
		Stock selectedStock = null;
		
		public StockInvestGui() {
			
			
			buyButton = new Button(Button.IMAGE_BUTTON,1,1,1,1);
			sellButton = new Button(Button.IMAGE_BUTTON,1,1,1,1);
			
			importFiles();
			buyButton.setImage(buyTex);
			sellButton.setImage(sellTex);
		}
		
		int yHover = 0;
		int ySelect = 0;
		
		public void input(KeyBoard keyBoard,Mouse mouse) {
			if(!Game.upgradeWindow.stockMarketLocked) {
				if(mouse.y>y&&mouse.y<y+hei/2&&mouse.x>x+wid/2&&mouse.x<x+wid) {
					yHover = (int)((mouse.y-y)/(Settings.GUI_ELEM_SPACING/2));
					try {
						if(mouse.mousePressed) {
							selectedStock = theMarket.get(yHover);
							ySelect = yHover;
						}
					}catch(Exception e) {}
				}
				buyButton.input(keyBoard, mouse);
				sellButton.input(keyBoard, mouse);
			}
		}
		
		public void update() {
			buyButton.update();
			sellButton.update();
			if(stockInvestGui.selectedStock!=null) {
				if(buyButton.isClicked()) {
					yourInvestments.buy(stockInvestGui.selectedStock, 1);
				}
				if(sellButton.isClicked()) {
					yourInvestments.sell(stockInvestGui.selectedStock, 1);
				}
			}
		}
	
		public void render(Graphics2D g) {
			
			//update buttons position and size to screen size ratios
			{
				x = Settings.TOP_CORNER_X;
				y = Settings.TOP_CORNER_Y;
				wid  = Settings.GUI_WIDTH;
				hei = Settings.GUI_HEIGHT;
				buyButton.setView(x+wid/2-Settings.GUI_ELEM_SPACING,y+hei/2,Settings.GUI_ELEM_SPACING,Settings.GUI_ELEM_SPACING);
				sellButton.setView(x+wid/2-Settings.GUI_ELEM_SPACING,y+hei/2+Settings.GUI_ELEM_SPACING,Settings.GUI_ELEM_SPACING,Settings.GUI_ELEM_SPACING);
			}
			//
			
			renderStockGraph(g,x,y,wid/2,hei/2,365/2,30);
			renderStockChart(g,x+wid/2,y,wid/2,hei/2,Settings.GUI_ELEM_SPACING/2);
			renderOwnedStocks(g,x+wid/2,y+hei/2,wid/2,hei/2,Settings.GUI_ELEM_SPACING/2);
			renderYourStatus(g,x,y+hei/2,wid/2,hei/2,Settings.GUI_ELEM_SPACING/2);
			
			buyButton.render(g);
			sellButton.render(g);
			
			if(Game.upgradeWindow.stockMarketLocked) g.drawImage(lockTex, x+(wid/2-hei/4), y+hei/4, hei/2, hei/2,null);
			
		}
		boolean imported = false;
		public void importFiles() {
			if(!imported) {
				buyTex = engine.graphics.Graphics3D.importImage("Gui/Buy.png");
				sellTex = engine.graphics.Graphics3D.importImage("Gui/Sell.png");
				lockTex = engine.graphics.Graphics3D.importImage("Gui/Lock.png");
				imported = true;
			}
		}
		
		double maxRecordedPrice = 0;
		public void renderStockGraph(Graphics2D g,int x,int y,int wid,int hei,int datePeriod,int tickSpacingX){
			g.setStroke(new BasicStroke(1));
			double scaleY = hei/maxRecordedPrice;
			double scaleX = (double)wid/datePeriod;
			int pointWid = 2;
			g.setColor(new Color(50,0,0));
			g.fillRect(x, y, wid, hei);
			
			g.setColor(Color.white);
			for(int i = 0;i<datePeriod;i+=tickSpacingX) {
				g.fillRect(x+wid-(int)(scaleX*i), y, 2, hei);
			}
			
			
			for(Stock s:theMarket) {
				for(int i = day-datePeriod;i<day;i++) {
					if(i<2) continue;
					double price = s.recordedPrice.get(i);
					if(price>maxRecordedPrice) maxRecordedPrice = price;
					g.setColor(s.color);
					g.fillRect((int) (i*scaleX-day*scaleX+wid)+x-pointWid/2,  (int)(hei-(price*scaleY))+y-pointWid/2, pointWid+1,  pointWid);
				}
			}
			
			g.setColor(Color.white);
			g.drawRect(x, y, wid, hei);
			
		}
		
		public void renderStockChart(Graphics2D g,int x,int y,int wid,int hei,int spacing) {
			g.setStroke(new BasicStroke(1));
			g.setColor(new Color(50,0,0));
			g.fillRect(x, y, wid, hei);
			int diff = 14;
			for(int i = 0;i<theMarket.size();i++) {
				Stock tmpStock = theMarket.get(i);
				g.setColor(tmpStock.color);
				g.fillRect(x, i*spacing+y, spacing, spacing);
				g.setFont(new Font(Font.MONOSPACED,0, Settings.FONT_SIZE));
				if(day>diff) {
					if(tmpStock.recordedPrice.get(day-1).doubleValue()>tmpStock.recordedPrice.get(day-diff-1)) {
						g.setColor(Color.green);
					}else {
						g.setColor(Color.red);
					}
				}
				if(tmpStock.currentValue<0.01) {
					g.setColor(Color.red.darker());
				}
				g.drawString(tmpStock.name+" "+(float)tmpStock.currentValue, x+(spacing+10), i*spacing+y+spacing);
				g.setColor(Color.white);
				g.drawLine(x, i*spacing+y, x+wid,i*spacing+y);
				
			}
			
			g.drawLine(x,theMarket.size() *spacing+y, x+wid,theMarket.size()*spacing+y);
			
			//highlight
			g.setColor(new Color(255,255,255,64));
			
			if(yHover<theMarket.size()) {
				g.fillRect(x+spacing, y+yHover*spacing, wid-spacing, spacing);
			}
			//selected stock
			if(selectedStock!=null) {
				g.setColor(new Color(255,255,255,128));
				g.fillRect(x+spacing, y+ySelect*spacing, wid-spacing, spacing);
			}
			
			g.setColor(Color.white);
			g.drawRect(x, y, wid, hei);
			
		}
		
		public void renderOwnedStocks(Graphics2D g,int x,int y,int wid,int hei,int spacing) {
			g.setStroke(new BasicStroke(1));
			g.setColor(new Color(50,0,0));
			g.fillRect(x, y, wid, hei);
			for(int i = 0;i<theMarket.size();i++) {
				Stock tmpStock = theMarket.get(i);
				g.setColor(tmpStock.color);
				g.fillRect(x, i*spacing+y, spacing, spacing);
				g.setFont(new Font(Font.MONOSPACED,0, Settings.FONT_SIZE));
				g.setColor(Color.white);
				g.drawString(tmpStock.name+" "+Collections.frequency(yourInvestments.ownedStocks, tmpStock), x+(spacing+10), i*spacing+y+spacing);
				g.setColor(Color.white);
				g.drawLine(x, i*spacing+y, x+wid,i*spacing+y);
				
			}
			
			g.drawLine(x,theMarket.size() *spacing+y, x+wid,theMarket.size()*spacing+y);
			
			g.setColor(Color.white);
			g.drawRect(x, y, wid, hei);
		}
		
		public void renderYourStatus(Graphics2D g,int x,int y,int wid,int hei,int spacing) {
			g.setStroke(new BasicStroke(1));
			g.setColor(new Color(50,0,0));
			g.fillRect(x, y, wid, hei);
			
			g.setFont(new Font(Font.MONOSPACED,0, Settings.FONT_SIZE));
			
			g.setColor(Color.white);
			
			g.drawString("Bank $ "+yourAccount.money, x+spacing, 1*spacing+y);
			g.drawString("InvestedMoney $ "+yourInvestments.getTotalValue(), x+spacing, 2*spacing+y);
			g.drawString("total Assests $ "+(yourAccount.money+yourInvestments.getTotalValue()) , x+spacing, 3*spacing+y);
			
			g.drawRect(x, y, wid, hei);
		}
		
	}
	
	class InvestAccount{
		
		ArrayList<Stock> ownedStocks = new ArrayList<Stock>();
		BankAccount bc = null;
		
		public InvestAccount(BankAccount acount){
			bc = acount;
		}
		
		public void buy(Stock s,int q) {
			if(s.currentValue*q<bc.money) {
				bc.remove(s.currentValue*q);
				for(int i = 0;i<q;i++) {
					ownedStocks.add(s);
				}
			}
		}
		public void sell(Stock s,int q) {
			if(ownedStocks.contains(s)) bc.add(s.currentValue*q);
			for(int i = 0;i<q;i++) {
				ownedStocks.remove(s);
			}
		}
		
		public double getTotalValue(){
			double money = 0;
			for(Stock s:ownedStocks) {
				money+=s.currentValue;
			}
			return money;
		}
		
	}
	
	static class BankAccount{
		double money = 0;
		public BankAccount(double money){
			this.money = money;
		}
		
		public void remove(double Amount) {
			money-=Amount;
		}
		
		public void add(double Amount) {
			money+=Amount;
		}
	}
	
	static class Stock{
		
		static final int TYPE_RETAIL = 0;
		static final int TYPE_TECH = 1;
		static final int TYPE_STORAGE = 2;
		static final int TYPE_DRUG = 3;
		static final int TYPE_INDUS = 3;
		
		String name = null;
		
		static int daysUntilRecesion = 0;
		static int recesionDuration = 0;
		
		ArrayList<Double> recordedPrice = new ArrayList<Double>();
		
		double currentValue = 0;
		double confidence = 0;
		int durationOfConfidence = 0;
		int daysUntilCrash = 0;
		
		double projectedMonthPrice = 0;
		
		int type = 0;
		double fluc;
		double dailyInterest;
		Color color = null;
		
		public Stock(int type,String name,double dailyInterest,double startingValue,double fluc,Color c) {
			init(type,name,dailyInterest,startingValue,fluc,c);
		}
		
		public Stock(int type,String name,double dailyInterest,double startingValue,double fluc) {
			Color c = new Color((int)(Math.random()*256.0),(int)(Math.random()*256.0),(int)(Math.random()*256.0));
			init(type,name,dailyInterest,startingValue,fluc,c);
		}
		
		private void init(int type,String name,double dailyInterest,double startingValue,double fluc,Color c) {
			this.dailyInterest = dailyInterest;
			currentValue = startingValue;
			this.type = type;
			this.fluc = fluc;
			this.name = name;
			if(type==TYPE_RETAIL) {
				daysUntilCrash = (int)(365.0*random(10.0,300.0));
			}
			color = c;
		}
		
		public void dayStep(){
			
			daysUntilCrash--;
			
			if(daysUntilCrash<=0) dailyInterest = -0.003;
			
			
			if(durationOfConfidence==0) {
				durationOfConfidence = (int)random(3,40);
				if(daysUntilCrash>0) {
					confidence = random(-Math.abs(dailyInterest),Math.abs(dailyInterest)*3);
				}else{
					confidence = random(-Math.abs(dailyInterest)*1.5 ,Math.abs(dailyInterest) );
				}
			}else {
				durationOfConfidence--;
			}
			currentValue=(currentValue)*(dailyInterest+confidence*random(2,8))+currentValue+random(-fluc*currentValue,fluc*currentValue);
			recordedPrice.add(currentValue);
			
		}
		
		private double random(double min,double max) {
			return Math.random()*(max-min)+min;
		}
		
		
	}
	
	
}
