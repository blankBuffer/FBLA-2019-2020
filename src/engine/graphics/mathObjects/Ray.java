package engine.graphics.mathObjects;

public class Ray{
	private double slopeX = 0,slopeY = 0,slopeZ = 0;
	public Ray(double slopex,double slopey,double slopez){
		slopeX = slopex;
		slopeY = slopey;
		slopeZ = slopez;
	}
	public void planeIntersection(Plane p,Point output){
		double a=p.getXSlope()*p.getXTrans()+p.getYSlope()*p.getYTrans()+p.getZSlope()*p.getZTrans();
	 	double b=p.getXSlope()*getXSlope()+p.getYSlope()*getYSlope()+p.getZSlope()*getZSlope();
		double t = a/b;
		output.x = slopeX*t;
		output.y = slopeY*t;
		output.z = slopeZ*t;
	}
	public double getXSlope(){
		return slopeX;
	}
	public double getYSlope(){
		return slopeY;
	}
	public double getZSlope(){
		return slopeZ;
	}
	public void setSlope(double slopex,double slopey,double slopez){
		slopeX = slopex;
		slopeY = slopey;
		slopeZ = slopez;
	}
}