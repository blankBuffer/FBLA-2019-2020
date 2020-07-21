package game;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import engine.main.KeyBoard;
import engine.main.Mouse;
import engine.main.Sound;
public class Button {
	
	//media
	private static BufferedImage selectedTex;
	private static BufferedImage highlightedTex;
	private static BufferedImage arrowTexLow;
	private static BufferedImage arrowTexHigh;
	private static BufferedImage arrowTexClick;
	static BufferedImage zeroTex;
	static BufferedImage oneTex;
	static BufferedImage twoTex;
	static BufferedImage threeTex;
	static BufferedImage fourTex;
	static BufferedImage fiveTex;
	static BufferedImage sixTex;
	static BufferedImage sevenTex;
	static BufferedImage eightTex;
	static BufferedImage nineTex;
	
	static Sound clickSound;
	static Sound hoverSound;
	
	private BufferedImage refer = null;
	private static boolean imported = false;
	//state
	public ArrayList<Button> buttons = new ArrayList<Button>();
	private int value;
	private boolean clicked = false;
	private boolean hovering = false;
	private int timer = 0;
	private int chosen = 0;
	private boolean pPressed = false;
	private boolean pressedUp = false;
	private boolean pressedDown = false;
	private boolean pressed = false;
	//attributes
	private int x = 1,y = 1;
	private int wid = 1,hei = 1;
	private int min = 0,max = 1;
	private int tickSpacing = 1;
	public static final int NUMBER_SWITCH = 0;
	public static final int IMAGE_BUTTON = 1;
	public static final int IMAGE_SELECT = 2;
	public static final int SLIDER = 3;
	private int type = 0;
	
	public void setImage(BufferedImage tex) {
		refer = tex;
	}
	
	public Button(int type) {
		if(!imported) {
			clickSound = new Sound("Sound/buttonClick.wav");
			clickSound.load();
			hoverSound = new Sound("Sound/buttonHover.wav");
			hoverSound.load();
			hoverSound.setVolume(-10.0f);
			arrowTexLow = engine.graphics.Graphics3D.importImage("Gui/ArrowBlinkLow.png");
			arrowTexHigh = engine.graphics.Graphics3D.importImage("Gui/ArrowBlinkHigh.png");
			arrowTexClick = engine.graphics.Graphics3D.importImage("Gui/ArrowClick.png");
			zeroTex = engine.graphics.Graphics3D.importImage("Number/Zero.png");
			oneTex = engine.graphics.Graphics3D.importImage("Number/One.png");
			twoTex = engine.graphics.Graphics3D.importImage("Number/Two.png");
			threeTex = engine.graphics.Graphics3D.importImage("Number/Three.png");
			fourTex = engine.graphics.Graphics3D.importImage("Number/Four.png");
			fiveTex = engine.graphics.Graphics3D.importImage("Number/Five.png");
			sixTex = engine.graphics.Graphics3D.importImage("Number/Six.png");
			sevenTex = engine.graphics.Graphics3D.importImage("Number/Seven.png");
			eightTex = engine.graphics.Graphics3D.importImage("Number/Eight.png");
			nineTex = engine.graphics.Graphics3D.importImage("Number/Nine.png");
			selectedTex = engine.graphics.Graphics3D.importImage("Gui/Selected.png");
			highlightedTex = engine.graphics.Graphics3D.importImage("Gui/Highlighted.png");
			imported = true;
		}
		this.type = type;
	}
	public Button(int type,int x,int y,int wid,int hei) {
		if(!imported) {
			clickSound = new Sound("Sound/buttonClick.wav");
			clickSound.load();
			hoverSound = new Sound("Sound/buttonHover.wav");
			hoverSound.load();
			hoverSound.setVolume(-10.0f);
			arrowTexLow = engine.graphics.Graphics3D.importImage("Gui/ArrowBlinkLow.png");
			arrowTexHigh = engine.graphics.Graphics3D.importImage("Gui/ArrowBlinkHigh.png");
			arrowTexClick = engine.graphics.Graphics3D.importImage("Gui/ArrowClick.png");
			zeroTex = engine.graphics.Graphics3D.importImage("Number/Zero.png");
			oneTex = engine.graphics.Graphics3D.importImage("Number/One.png");
			twoTex = engine.graphics.Graphics3D.importImage("Number/Two.png");
			threeTex = engine.graphics.Graphics3D.importImage("Number/Three.png");
			fourTex = engine.graphics.Graphics3D.importImage("Number/Four.png");
			fiveTex = engine.graphics.Graphics3D.importImage("Number/Five.png");
			sixTex = engine.graphics.Graphics3D.importImage("Number/Six.png");
			sevenTex = engine.graphics.Graphics3D.importImage("Number/Seven.png");
			eightTex = engine.graphics.Graphics3D.importImage("Number/Eight.png");
			nineTex = engine.graphics.Graphics3D.importImage("Number/Nine.png");
			selectedTex = engine.graphics.Graphics3D.importImage("Gui/Selected.png");
			highlightedTex = engine.graphics.Graphics3D.importImage("Gui/Highlighted.png");
			imported = true;
		}
		this.type = type;
		this.x = x;
		this.y = y;
		this.wid = wid;
		this.hei = hei;
	}
	
	public void setView(int x,int y,int wid,int hei) {
		this.x = x;
		this.y = y;
		this.wid = wid;
		this.hei = hei;
		for(int i = 0;i<buttons.size();i++) {
			Button b = buttons.get(i);
			b.setView(x+i*wid, y, wid, hei);
		}
	}
	
	public void addButton(BufferedImage tex) {
		Button toBeAdded = new Button(IMAGE_BUTTON);
		toBeAdded.setImage(tex);
		buttons.add(toBeAdded);
	}
	public void render(Graphics2D g) {
		if(type == NUMBER_SWITCH) {
			g.setColor(Color.gray);
			if(pressedUp) {
				g.drawImage(arrowTexClick,x,y+hei/3,wid,-hei/3,null);
			}else if(timer<10) {
				g.drawImage(arrowTexHigh,x,y+hei/3,wid,-hei/3,null);
			}else {
				g.drawImage(arrowTexLow,x,y+hei/3,wid,-hei/3,null);
			}
			if(pressedDown) {
				g.drawImage(arrowTexClick,x,y+(hei/3)*2,wid,hei/3,null);
			}else if(timer<10) {
				g.drawImage(arrowTexHigh,x,y+(hei/3)*2,wid,hei/3,null);
			}else {
				g.drawImage(arrowTexLow,x,y+(hei/3)*2,wid,hei/3,null);
			}
			
			if(value==0) g.drawImage(zeroTex,x,y+hei/3,wid,hei/3,null);
			if(value==1) g.drawImage(oneTex,x,y+hei/3,wid,hei/3,null);
			if(value==2) g.drawImage(twoTex,x,y+hei/3,wid,hei/3,null);
			if(value==3) g.drawImage(threeTex,x,y+hei/3,wid,hei/3,null);
			if(value==4) g.drawImage(fourTex,x,y+hei/3,wid,hei/3,null);
			if(value==5) g.drawImage(fiveTex,x,y+hei/3,wid,hei/3,null);
			if(value==6) g.drawImage(sixTex,x,y+hei/3,wid,hei/3,null);
			if(value==7) g.drawImage(sevenTex,x,y+hei/3,wid,hei/3,null);
			if(value==8) g.drawImage(eightTex,x,y+hei/3,wid,hei/3,null);
			if(value==9) g.drawImage(nineTex,x,y+hei/3,wid,hei/3,null);
			return;
		}else if(type == IMAGE_BUTTON) {
			g.drawImage(refer, x, y, wid, hei, null);
			if(hovering) g.drawImage(highlightedTex, x, y, wid, hei, null);
			g.setColor(Color.gray);
			if(pressed) g.fillRect( x+wid/4, y+hei/4, wid/2, hei/2);
		}else if(type == IMAGE_SELECT) {
			for(Button b:buttons) b.render(g);
			if(timer<10) g.drawImage(selectedTex, x+chosen*wid, y, wid, hei, null);
			else g.drawImage(selectedTex, x+chosen*wid, y+hei/8, wid, hei, null);
		}else if(type == SLIDER) {
			g.setColor(Color.gray);
			g.fillRect(x, y+hei/4, wid, hei/2);
			g.setColor(Color.black);
			g.fillRect(x-wid/40,y, wid/20, hei);
			g.fillRect(wid+x-wid/40,y, wid/20, hei);
			double xp = x+((double)wid/(max-min))*(value-min)-hei/2.0;
			if(timer<10) g.drawImage(selectedTex,(int)Math.round(xp), y, hei, hei,null);
			else g.drawImage(selectedTex,(int)Math.round(xp), y-hei/8, hei, hei,null);
		}
	}
	
	boolean pHover = false;
	public void input(KeyBoard keyBoard,Mouse mouse) {
		if(type == NUMBER_SWITCH) {
			if(mouse.x>x+wid*2/12&&mouse.y>y&&mouse.x<x+wid*2/12+wid*2/3&&mouse.y<y+hei/3&&mouse.mousePressed&&!pPressed) {
				pressedUp = true;
				value++;
			}
			if(mouse.x>x+wid*2/12&&mouse.y>y+(hei/3)*2&&mouse.x<x+wid*2/12+wid*2/3&&mouse.y<y+(hei/3)*2+hei/3&&mouse.mousePressed&&!pPressed) {
				pressedDown = true;
				value--;
			}
			if(!mouse.mousePressed) {
				pressedUp = false;
				pressedDown = false;
			}
			if(value<0) value = 9;
			if(value>9) value = 0;
		}else if(type == IMAGE_BUTTON) {
			clicked = false;
			if(mouse.x>x&&mouse.x<x+wid&&mouse.y>y&&mouse.y<y+hei) {
				hovering = true;
				if(!pHover)hoverSound.play();
				if(mouse.mousePressed&&!pPressed) {
					clickSound.play();
					clicked = true;
					pressed = true;
				}
			}else hovering = false;
			if(!mouse.mousePressed) pressed = false;
		}else if(type == IMAGE_SELECT) {
			for(int i = 0;i<buttons.size();i++) {
				Button b = buttons.get(i);
				b.input(keyBoard, mouse);
				if(b.clicked) chosen = i;
			}
		}else if(type == SLIDER) {
			if(mouse.mousePressed&&mouse.x<x+wid+wid/40&&mouse.x>x-wid/40&&mouse.y>y&&mouse.y<y+hei)
			value = (int)Math.ceil(((double)(mouse.x-x)/wid)*(max-min))+min;
		}
		pPressed = mouse.mousePressed;
		pHover = hovering;
	}
	public void reset() {
		pressed = false;
		clicked = false;
	}
	public int getNumValue() {
		return value;
	}
	public int getChosenSelection() {
		return chosen;
	}
	public boolean isClicked() {
		return clicked;
	}
	public boolean isMouseHovering() {
		return hovering;
	}
	public void setMax(int m) {
		max = m;
	}
	public void setMin(int m) {
		min = m;
	}
	public void setTickSpacing(int s) {
		tickSpacing = s;
	}
	public void setChosen(int c) {
		chosen = c;
		if(c>buttons.size()-1) c = buttons.size()-1;
		else if(c<0) c = 0;
	}
	public void setValue(int v) {
		value = v;
		if(v>max) v = max;
		else if(v<min) v = min;
	}
	public void update() {
		timer++;
		if(timer>20) timer = 0;
		for(Button b:buttons) b.update();
		if(type == SLIDER) {
			if(value%tickSpacing!=0) {
				value-=value%tickSpacing;
			}
			if(value<min) value = min;
			if(value>max) value = max;
		}
	}
}
