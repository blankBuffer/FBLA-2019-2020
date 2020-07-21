package engine.graphics.mathObjects;
public class Vector {
	private double xTrans = 0,yTrans = 0,zTrans = 0;
	public Vector(double xtrans,double ytrans,double ztrans){
		xTrans = xtrans;
		yTrans = ytrans;
		zTrans = ztrans;
	}
	public double getXTrans(){
		return xTrans;
	}
	public double getYTrans(){
		return yTrans;
	}
	public double getZTrans(){
		return zTrans;
	}
	public static Vector crossProduct(Vector a,Vector b){
		Vector c = new Vector(a.yTrans*b.zTrans-a.zTrans*b.yTrans,a.zTrans*b.xTrans-a.xTrans*b.zTrans,a.xTrans*b.yTrans-a.yTrans*b.xTrans);
		return c;
	}
	public void add(Vector b) {
		xTrans+=b.getXTrans();
		yTrans+=b.getYTrans();
		zTrans+=b.getZTrans();
	}
	public double getMagnitude() {
		return Math.sqrt(xTrans*xTrans+yTrans*yTrans+zTrans*zTrans);
	}
	public void scaler(double d) {
		xTrans*=d;
		yTrans*=d;
		zTrans*=d;
		
	}
	public static Vector add(Vector a,Vector b) {
		return new Vector(a.getXTrans()+b.getXTrans(),a.getYTrans()+b.getYTrans(),a.getZTrans()+b.getZTrans());
	}
	public static double AngleBetween(Vector a,Vector b) {
		double dot = a.getXTrans()*b.getXTrans()+a.getYTrans()*b.getYTrans()+a.getZTrans()*b.getZTrans();
		double distA = Math.sqrt(a.getXTrans()*a.getXTrans()+a.getYTrans()*a.getYTrans()+a.getZTrans()*a.getZTrans());
		double distB = Math.sqrt(b.getXTrans()*b.getXTrans()+b.getYTrans()*b.getYTrans()+b.getZTrans()*b.getZTrans());
		double val = dot/(distA*distB);
		return Math.acos(val);
	}
	public void setTrans(double x,double y,double z) {
		xTrans = x;
		yTrans = y;
		zTrans = z;
	}
	public void rotate(Angle a){
		Vector tempVector = new Vector(xTrans,yTrans,zTrans);
		xTrans = tempVector.xTrans*Math.cos(a.getXRotation())-tempVector.zTrans*Math.sin(a.getXRotation());
		zTrans = tempVector.zTrans*Math.cos(a.getXRotation())+tempVector.xTrans*Math.sin(a.getXRotation());
		tempVector = new Vector(xTrans,tempVector.yTrans,zTrans);
		yTrans = tempVector.yTrans*Math.cos(a.getYRotation())-tempVector.zTrans*Math.sin(a.getYRotation());
		zTrans = tempVector.zTrans*Math.cos(a.getYRotation())+tempVector.yTrans*Math.sin(a.getYRotation());
		tempVector = new Vector(tempVector.xTrans,yTrans,zTrans);
		xTrans = tempVector.xTrans*Math.cos(a.getZRotation())-tempVector.yTrans*Math.sin(a.getZRotation());
		yTrans = tempVector.yTrans*Math.cos(a.getZRotation())+tempVector.xTrans*Math.sin(a.getZRotation());
	}
}