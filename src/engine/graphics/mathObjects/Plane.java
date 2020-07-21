package engine.graphics.mathObjects;
public class Plane{
	private double slopeX = 0,slopeY = 0,slopeZ = 0;
	private double transX = 0,transY = 0,transZ = 0;
	public Plane(double slopex,double slopey,double slopez,double transx,double transy,double transz){
		slopeX = slopex;
		slopeY = slopey;
		slopeZ = slopez;
		transX = transx;
		transY = transy;
		transZ = transz;
	}
	public double getXTrans(){
		return transX;
	}
	public double getYTrans(){
		return transY;
	}
	public double getZTrans(){
		return transZ;
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
}