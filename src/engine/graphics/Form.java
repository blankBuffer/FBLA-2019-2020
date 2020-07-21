package engine.graphics;
import java.util.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import engine.graphics.mathObjects.Point;
import engine.graphics.mathObjects.Triangle;
import engine.graphics.mathObjects.Vector;
import engine.graphics.mathObjects.Angle;
public class Form{
    private ArrayList<Triangle> ref;
    private boolean currentTextureMode = false;
    private int currentColor = 0;
    private Point[] currentTextureCords = new Point[3];
    private ArrayList<SimpleTriangle> recallTriData = new ArrayList<SimpleTriangle>();
    private BufferedImage currentTexture = null;
    private byte currentClockDir = 0;
    private double currentTintPercent = 1.0;
    public Form(){
        currentTextureCords[0] = new Point(0,0);
        currentTextureCords[1] = new Point(0,0);
        currentTextureCords[2] = new Point(0,0);
    }
    public void clearStorage() {
    	recallTriData.clear();
    }
    public void reset() {
    	currentTextureMode = false;
        currentColor = 0;
        currentTextureCords[0].x = 0;
        currentTextureCords[0].y = 0;
        currentTextureCords[1].x = 0;
        currentTextureCords[1].y = 0;
        currentTextureCords[2].x = 0;
        currentTextureCords[2].y = 0;
        recallTriData.clear();
        currentTexture = null;
        currentClockDir = 0;
    }
    public void setColor(int r,int g,int b){
        currentTextureMode = false;
        currentColor = new Color(r,g,b).getRGB();
    }
    
    public void clockDirection(byte opt){
        currentClockDir = opt;
    }
    public void setTexture(BufferedImage texture,int x1,int y1,int x2,int y2,int x3,int y3){
        currentTextureMode = true;
        currentTextureCords[0].x = x1;
        currentTextureCords[0].y = y1;
        currentTextureCords[1].x = x2;
        currentTextureCords[1].y = y2;
        currentTextureCords[2].x = x3;
        currentTextureCords[2].y = y3;
        currentTexture = texture;
    }
    public void addTriangle(double x1,double y1,double z1,double x2,double y2,double z2,double x3,double y3,double z3){
        SimpleTriangle tri = new SimpleTriangle();
        tri.textureMode = currentTextureMode;
        tri.tintWeight = currentTintPercent;
        tri.c = currentColor;
        tri.texture = currentTexture;
        tri.points[0] = new Point(x1,y1,z1);
        tri.points[1] = new Point(x2,y2,z2);
        tri.points[2] = new Point(x3,y3,z3);
        tri.textureCords = new Point[]{new Point(currentTextureCords[0].x,currentTextureCords[0].y),new Point(currentTextureCords[1].x,currentTextureCords[1].y),new Point(currentTextureCords[2].x,currentTextureCords[2].y) };
        tri.clockDir = currentClockDir;
        recallTriData.add(tri);
    }
    public void setTintWeight(double percent) {
    	currentTintPercent = percent;
    }
    public void addToScene(Vector v,Angle a,Camera c){
    	ref = c.trianglesMemory;
        for(SimpleTriangle temp : recallTriData){
            Point p1 = new Point(temp.points[0].x,temp.points[0].y,temp.points[0].z);
            Point p2 = new Point(temp.points[1].x,temp.points[1].y,temp.points[1].z);
            Point p3 = new Point(temp.points[2].x,temp.points[2].y,temp.points[2].z);
            p1.rotateAboutOrigin(a);
            p1.translate(v);
            p2.rotateAboutOrigin(a);
            p2.translate(v);
            p3.rotateAboutOrigin(a);
            p3.translate(v);
            if(temp.textureMode){
            	Point tp1 = new Point(temp.textureCords[0].x,temp.textureCords[0].y);
                Point tp2 = new Point(temp.textureCords[1].x,temp.textureCords[1].y);
                Point tp3 = new Point(temp.textureCords[2].x,temp.textureCords[2].y);
                ref.add(new Triangle(new Point[]{p1,p2,p3},temp.texture, new Point[]{tp1,tp2,tp3} , temp.clockDir,temp.tintWeight));
            }else{
                ref.add(new Triangle(new Point[]{p1,p2,p3}, temp.c, temp.clockDir,temp.tintWeight));
            }
        }
    }
    public void addToScene(Vector v,Camera c ){
    	ref = c.trianglesMemory;
        for(SimpleTriangle temp : recallTriData){
            Point p1 = new Point(temp.points[0].x,temp.points[0].y,temp.points[0].z);
            Point p2 = new Point(temp.points[1].x,temp.points[1].y,temp.points[1].z);
            Point p3 = new Point(temp.points[2].x,temp.points[2].y,temp.points[2].z);
            p1.translate(v);
            p2.translate(v);
            p3.translate(v);
            if(temp.textureMode){
            	Point tp1 = new Point(temp.textureCords[0].x,temp.textureCords[0].y);
                Point tp2 = new Point(temp.textureCords[1].x,temp.textureCords[1].y);
                Point tp3 = new Point(temp.textureCords[2].x,temp.textureCords[2].y);
                ref.add(new Triangle(new Point[]{p1,p2,p3},temp.texture, new Point[]{tp1,tp2,tp3} , temp.clockDir,temp.tintWeight));
            }else{
                ref.add(new Triangle(new Point[]{p1,p2,p3}, temp.c, temp.clockDir,temp.tintWeight));
            }
        }
    }
    private class SimpleTriangle{
        public Point[] points = new Point[3];
        public Point[] textureCords = new Point[3];
        public BufferedImage texture;
        public boolean textureMode = false;
        public int c = 0;
        public double tintWeight = 1.0;
        public byte clockDir = 0;
    }
}