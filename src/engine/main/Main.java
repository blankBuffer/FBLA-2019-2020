package engine.main;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
public abstract class Main extends Canvas{
	private static final long serialVersionUID = -321280088918514126L;
	public static int WIDTH = 1000,HEIGHT = 600;
	private double UPS = 60,FPS = 60;
	private String PROG_NAME = "Graphics Test";
	private JFrame frame = new JFrame();
	private BufferStrategy bs = null;
	private KeyBoard keyBoard = new KeyBoard();
	private Mouse mouse = new Mouse();
	private boolean interpolation = false;
	private int countedFrames = 0;
	public Main(){
		System.out.println("using \"BcEngine\" version-10.0");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(keyBoard);
		this.addMouseListener(mouse);
		this.setFocusTraversalKeysEnabled(false);
		frame.setFocusable(true);
		frame.add(this);
		frame.setVisible(true);
		this.createBufferStrategy(2);
	}
	private void render(){
		if(bs == null){
			bs = this.getBufferStrategy();
			return;
		}
		Graphics2D g = null;
		do {
			try{
				g =  (Graphics2D)bs.getDrawGraphics();
				g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
				g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
				if(interpolation){
					g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				}
				draw(g);
			} finally {
				   g.dispose();
			}
			bs.show();
		} while (bs.contentsLost());
	}
	protected void size(int wid,int hei){
		WIDTH = wid;
		HEIGHT = hei;
		frame.setSize(wid, hei);
		//22 pixels is height of the x - ^ bar on top
		try {
			Thread.sleep(100);
		}catch(Exception e) {
			System.err.println("oof something realy wrong happend");
		}
        frame.setLocationRelativeTo(null);
	}
	protected void enableInterpolation(){
		interpolation = true;
	}
	protected void name(String name){
		PROG_NAME = name;
	}
	protected void frameRate(double fps){
		FPS = fps;
	}
	protected void updateRate(double ups){
		UPS = ups;
	}
	protected int getFrameRate(){
		return countedFrames;
	}
	public abstract void input(KeyBoard keyIn,Mouse mouIn);
	public abstract void draw(Graphics2D g);
	public abstract void update(long time);
	public void run() {
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			System.err.println("oof something realy wrong happend");
		}
		long initialTime = System.nanoTime();
		double deltaU = 0, deltaF = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		while (true) {
			long currentTime = System.nanoTime();
			deltaU += (currentTime - initialTime) / (1000000000 / UPS);
			deltaF += (currentTime - initialTime) / (1000000000 / FPS);
			initialTime = currentTime;
			while (deltaU >= 1) {
				mouse.updateMousePosition(frame.getLocation(),frame.getWidth(),frame.getHeight());
				input(keyBoard,mouse);
				update(currentTime);
				deltaU--;
			}
			if (deltaF >= 1) {
				render();
				frames++;
				deltaF--;
			}
			if (System.currentTimeMillis() - timer > 1000) {
				System.err.println("FPS: "+frames);
                System.err.println("FREE MEM: "+Runtime.getRuntime().freeMemory());
				countedFrames = frames;
				frames = 0;
				timer += 1000;
				if(deltaF>1){
					deltaF=0;
				}
			}
		}
	}
	public JFrame getWindow(){
		return frame;
	}
}