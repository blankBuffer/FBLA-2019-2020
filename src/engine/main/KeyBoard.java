package engine.main;
import java.awt.event.*;
public class KeyBoard implements KeyListener{
	public boolean UP,DOWN,LEFT,RIGHT,SHIFT,SPACE,ESCAPE;
	public boolean A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z;
	public boolean PRESSED;
	public KeyBoard(){
		System.out.println("Press F-10 to quit!");
	}
	public void keyPressed(KeyEvent e){
		PRESSED = true;
		setKeyChar(true, e);
		setKeyCode(true, e);
		if(e.getKeyCode()==KeyEvent.VK_F10){
			System.exit(0);
		}
		if(SHIFT){
			A = false;B = false;C = false;D = false;E = false;F = false;
			G = false;H = false;I = false;J = false;K = false;L = false;
			M = false;N = false;O = false;P = false;Q = false;R = false;
			S = false;T = false;U = false;V = false;W = false;X = false;
			Y = false;Z = false;UP = false;DOWN = false;LEFT = false;RIGHT = false;
			SPACE = false;
		}
	}
	public void keyReleased(KeyEvent e){
		setKeyChar(false, e);
		setKeyCode(false, e);
		if(!(A||B||C||D||E||F||G||H||I||J||K||L||M||N||O||P||Q||R||S||T||U||V||W||X||Y||Z||UP||DOWN||LEFT||RIGHT||SHIFT||SPACE||ESCAPE)){
			PRESSED = false;
		}
	}
	public void setKeyCode(boolean onOff,KeyEvent e){
		int KP = e.getKeyCode();
		if(KP == KeyEvent.VK_UP )
			UP = onOff;
		if(KP == KeyEvent.VK_DOWN )
			DOWN = onOff;
		if(KP == KeyEvent.VK_LEFT )
			LEFT = onOff;
		if(KP == KeyEvent.VK_RIGHT )
			RIGHT = onOff;
		if(KP == KeyEvent.VK_SHIFT )
			SHIFT = onOff;
		if(KP == KeyEvent.VK_SPACE )
			SPACE = onOff;
		if(KP == KeyEvent.VK_ESCAPE )
			ESCAPE = onOff;
	}
	private void setKeyChar(boolean onOff,KeyEvent e){
		char LP = e.getKeyChar();
		if (LP == 'a')
			A = onOff;
		if (LP == 'b')
			B = onOff;
		if (LP == 'c')
			C = onOff;
		if (LP == 'd')
			D = onOff;
		if (LP == 'e')
			E = onOff;
		if (LP == 'f')
			F = onOff;
		if (LP == 'g')
			G = onOff;
		if (LP == 'h')
			H = onOff;
		if (LP == 'i')
			I = onOff;
		if (LP == 'j')
			J = onOff;
		if (LP == 'k')
			K = onOff;
		if (LP == 'l')
			L = onOff;
		if (LP == 'm')
			M = onOff;
		if (LP == 'n')
			N = onOff;
		if (LP == 'o')
			O = onOff;
		if (LP == 'p')
			P = onOff;
		if (LP == 'q')
			Q = onOff;
		if (LP == 'r')
			R = onOff;
		if (LP == 's')
			S = onOff;
		if (LP == 't')
			T = onOff;
		if (LP == 'u')
			U = onOff;
		if (LP == 'v')
			V = onOff;
		if (LP == 'w')
			W = onOff;
		if (LP == 'x')
			X = onOff;
		if (LP == 'y')
			Y = onOff;
		if (LP == 'z')
			Z = onOff;
	}
	public void keyTyped(KeyEvent e){};
}