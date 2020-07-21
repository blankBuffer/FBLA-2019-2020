package engine.graphics;
import engine.graphics.mathObjects.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
public class Graphics3D {
	private double[][] zBuffer;
	private Color backgroundColor = new Color(255,255,255);
	private final double INFINITY = Math.pow(2, 100);
	private boolean imageReady = false;
	private Worker worker = new Worker();
	private ArrayList<Triangle> trianglesReferece = null;
	private Camera cameraReference = null;
	public Graphics3D(){
			worker.start();
	}
    public static BufferedImage createBlankBufferedImage(int width,int height){
         return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
	public static BufferedImage importImage(String name){
                try{
                        BufferedImage src = ImageIO.read(new File("Resources/"+name));
                        BufferedImage img = new BufferedImage(src.getWidth(), src.getHeight(),BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2d= img.createGraphics();
                        g2d.drawImage(src, 0, 0, null);
                        g2d.dispose();
                        src = null;
                        return img;
                }catch(Exception e){
                        e.printStackTrace();
                        System.err.println("file:"+name);
                        return null;
                }
    }
	public BufferedImage render(Camera camera,BufferedImage imageReference){
        worker.setImageRef(imageReference);
		trianglesReferece = camera.trianglesMemory;
		cameraReference = camera;
		preRender(imageReference);
		boolean loading = true;
		while(loading){
			if(worker.ready){
				loading = false;
			}
		}
		camera.trianglesMemory.clear();
        return imageReference;
	}
	private void preRender(BufferedImage imageReference){
		imageReady = false;
		if(imageReference == null){
			System.err.println("image may not have been initialized");
			return;
		}
		zBuffer = new double[imageReference.getWidth()][imageReference.getHeight()];
		int[] pixels = ((DataBufferInt)imageReference.getRaster().getDataBuffer()).getData();
		for(int i = 0;i<imageReference.getHeight();i++){
			for(int j = 0;j<imageReference.getWidth();j++){
				zBuffer[j][i] = INFINITY;
				pixels[j+imageReference.getWidth()*i] = backgroundColor.getRGB();
			}
		}
		int index = 0;
		cameraReference.setTrigValues();
		if(trianglesReferece.size() == 0){
			return;
		}
		do{
			Triangle triTemp = trianglesReferece.get(index);
			if(triTemp.skip) {
				index++;
				continue;
			}
			triTemp.moveToCamera(cameraReference);
			triTemp.isInFrontOfYou(trianglesReferece ,cameraReference.getClipDistance());
			triTemp.create2dPosition(imageReference.getWidth(),imageReference.getHeight(),cameraReference.getZoom());
			triTemp.isClockWiseOrCounter();
			triTemp.isVisible(imageReference.getWidth()/2,imageReference.getHeight()/2);
			triTemp.create2dBoundBox();
			index++;
		}while(index != trianglesReferece.size());
		try{
			Collections.sort(trianglesReferece);
		}catch(Exception e){
			System.out.println(e);
		}
		worker.reset();
	}
	public void backgroundColor(int r,int g ,int b){
		backgroundColor = new Color(r,g,b);
	}
	private class Worker extends Thread{
		volatile private boolean ready = true;
		BufferedImage ref;
        void setImageRef(BufferedImage r){
            ref = r;
        }
		public void run(){
			while(true){
				if(!ready){
					for(int i = 0;i < trianglesReferece.size();i++){
						trianglesReferece.get(i).drawTriangle(ref,zBuffer,cameraReference.getClipDistance(),cameraReference.getZoom());
					}
					ready = true;
				}
			}
		}
		public synchronized void reset(){
			ready = false;
		}
	}
	public boolean ready(){
		return imageReady;
	}
}