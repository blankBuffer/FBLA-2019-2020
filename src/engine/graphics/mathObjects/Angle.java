package engine.graphics.mathObjects;
public class Angle{
	private double xr = 0,yr = 0,zr = 0;
    public Angle(double xRot,double yRot,double zRot){
        xr = xRot;
		yr = yRot;
		zr = zRot;
    }
	public void setAngle(double xRot,double yRot,double zRot){
		xr = xRot;
		yr = yRot;
		zr = zRot;
	}
	public void shiftAngle(double deltaX,double deltaY,double deltaZ){
		xr+=deltaX;
		yr+=deltaY;
		zr+=deltaZ;
	}
	public double getXRotation(){
		return xr;
	}
	public double getYRotation(){
		return yr;
	}
	public double getZRotation(){
		return zr;
	}
}