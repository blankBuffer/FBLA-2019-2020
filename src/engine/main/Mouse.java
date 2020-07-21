package engine.main;
import java.awt.event.*;
import java.awt.Point;
import java.awt.MouseInfo;
public class Mouse implements MouseListener{
	public boolean mousePressed = false;
	public int x = 0,y = 0;
	public int px = 0,py = 0;
	public void mousePressed(MouseEvent e){
		mousePressed = true;
	}
	public void mouseReleased(MouseEvent e){
		mousePressed = false;
	}
	public void updateMousePosition(Point locactionOfWindow,int windowWidth,int windowHeight){
        try{
			px = x;
			py = y;
			Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
			x = mouseLocation.x-locactionOfWindow.x;
			y = mouseLocation.y-locactionOfWindow.y-22;
			if(x<0||y<0||x>windowWidth||y>windowHeight){
				x=0;
				y=0;
			}
        }catch(Exception e){
        }
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
}