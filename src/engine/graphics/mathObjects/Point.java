package engine.graphics.mathObjects;
public class Point {
	public double x = 0,y = 0,z = 0;
	public Point(double xPos,double yPos,double zPos){
		x = xPos;
		y = yPos;
		z = zPos;
	}
	public Point(double xPos,double yPos){
		x = xPos;
		y = yPos;
		z = 0;
	}
	public void translate(Vector v){
		x+=v.getXTrans();
		y+=v.getYTrans();
		z+=v.getZTrans();
	}
	public static Point get2dPosition(Point point,int imageWidth,int imageHeight,double zoom ){
		Point p = new Point(point.x,point.y,point.z);
		double x = p.x;
		double y = p.y;
		double z = p.z;
		x = x/(z/imageWidth*2.0/zoom);
		y = y/(z/imageWidth*2.0/zoom);
		return new Point(x,y);
	}
	public void rotateAboutOrigin(Angle a){
		Point tempPoint;
		tempPoint = new Point(x,y,z);
		x = tempPoint.x*Math.cos(a.getZRotation())-tempPoint.y*Math.sin(a.getZRotation());
		y = tempPoint.y*Math.cos(a.getZRotation())+tempPoint.x*Math.sin(a.getZRotation());
		tempPoint = new Point(x,y,z);
		y = tempPoint.y*Math.cos(a.getYRotation())-tempPoint.z*Math.sin(a.getYRotation());
		z = tempPoint.z*Math.cos(a.getYRotation())+tempPoint.y*Math.sin(a.getYRotation());
		tempPoint = new Point(x,y,z);
		x = tempPoint.x*Math.cos(a.getXRotation())-tempPoint.z*Math.sin(a.getXRotation());
		z = tempPoint.z*Math.cos(a.getXRotation())+tempPoint.x*Math.sin(a.getXRotation());
		
		
		
	}
	public static double dist(Point a,Point b) {
		return Math.sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y)+(a.z-b.z)*(a.z-b.z));
	
	}
}