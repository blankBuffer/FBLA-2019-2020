package engine.graphics;
import engine.graphics.mathObjects.Point;
import java.util.ArrayList;
import engine.graphics.mathObjects.Triangle;
import engine.graphics.mathObjects.Angle;
public class Camera {
    public ArrayList<Triangle> trianglesMemory = new ArrayList<Triangle>();
	private final static double errFix = 0.000001;
	private Point cameraPosition = new Point(0,0,0);
	private double rotationX = errFix,rotationY = errFix,rotationZ = errFix;
	public double sinRotX = 0,cosRotX = 1,sinRotY = 0,cosRotY = 1,sinRotZ = 0,cosRotZ = 1;
	private double zoomFactor = 1;
	private double clipD = 0.1;
	public double getClipDistance(){
		return clipD;
	}
	public void setClipDistance(double amount){
		clipD = amount;
	}
	public double getZoom(){
		return zoomFactor;
	}
	public void setZoom(double amount){
		zoomFactor = amount;
	}
	public void setPosition(Point position){
		cameraPosition.x = position.x;
		cameraPosition.y = position.y;
		cameraPosition.z = position.z;
	}
	public void setAngle(Angle s){
		rotationX = s.getXRotation();
		rotationY = s.getYRotation();
		rotationZ = s.getZRotation();
        if(Math.abs(rotationX)<errFix)rotationX=errFix;
        if(Math.abs(rotationY)<errFix)rotationY=errFix;
        if(Math.abs(rotationZ)<errFix)rotationZ=errFix;
        if(rotationX>Math.PI*2) {
        	rotationX-=Math.PI*2;
        }
        if(rotationY>Math.PI*2) {
        	rotationY-=Math.PI*2;
        }
        if(rotationZ>Math.PI*2) {
        	rotationZ-=Math.PI*2;
        }
        if(rotationX<0) {
        	rotationX+=Math.PI*2;
        }
        if(rotationY<0) {
        	rotationY+=Math.PI*2;
        }
        if(rotationZ<0) {
        	rotationZ+=Math.PI*2;
        }
	}
	public Angle getAngle(){
		return new Angle(rotationX,rotationY,rotationZ);
	}
	public void setTrigValues(){
		sinRotX = Math.sin(rotationX);
		cosRotX = Math.cos(rotationX);
		sinRotY = Math.sin(rotationY);
		cosRotY = Math.cos(rotationY);
		sinRotZ = Math.sin(rotationZ);
		cosRotZ = Math.cos(rotationZ);
	}
	public Point getPosition(){
		return new Point(cameraPosition.x,cameraPosition.y,cameraPosition.z);
	}
}