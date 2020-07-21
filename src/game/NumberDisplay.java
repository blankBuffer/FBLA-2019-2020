package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class NumberDisplay {
	private static BufferedImage[] digitTex = new BufferedImage[10];
	private static BufferedImage decimalPointTex = null;
	private static BufferedImage exponentTex = null;
	private static boolean imported = false;
	
	public NumberDisplay() {
		
		if(!imported) {
			for(int i = 0;i<digitTex.length;i++) digitTex[i] = engine.graphics.Graphics3D.importImage("Number/digit"+i+".png");
			decimalPointTex = engine.graphics.Graphics3D.importImage("Number/digitDP.png");
			exponentTex = engine.graphics.Graphics3D.importImage("Number/digitE.png");
		}
		
	}
	
	public void render(Graphics2D g,double number,int height,int xp,int yp) {
		BufferedImage curIm = null;
		char charArray[] = String.valueOf((float)number).toCharArray();
		for(int i = 0;i<charArray.length;i++) {
			char c = charArray[i];
			if(c=='0') curIm = digitTex[0];
			if(c=='1') curIm = digitTex[1];
			if(c=='2') curIm = digitTex[2];
			if(c=='3') curIm = digitTex[3];
			if(c=='4') curIm = digitTex[4];
			if(c=='5') curIm = digitTex[5];
			if(c=='6') curIm = digitTex[6];
			if(c=='7') curIm = digitTex[7];
			if(c=='8') curIm = digitTex[8];
			if(c=='9') curIm = digitTex[9];
			if(c=='.') curIm = decimalPointTex;
			g.drawImage(curIm, xp+i*height, yp, height,height,null);
		}
		
	}
	public void render(Graphics2D g,int number,int height, int xp,int yp) {
		BufferedImage curIm = null;
		char charArray[] = String.valueOf(number).toCharArray();
		for(int i = 0;i<charArray.length;i++) {
			char c = charArray[i];
			if(c=='0') curIm = digitTex[0];
			if(c=='1') curIm = digitTex[1];
			if(c=='2') curIm = digitTex[2];
			if(c=='3') curIm = digitTex[3];
			if(c=='4') curIm = digitTex[4];
			if(c=='5') curIm = digitTex[5];
			if(c=='6') curIm = digitTex[6];
			if(c=='7') curIm = digitTex[7];
			if(c=='8') curIm = digitTex[8];
			if(c=='9') curIm = digitTex[9];
			g.drawImage(curIm, xp+i*height, yp, height,height,null);
		}
		
	}
	
}
