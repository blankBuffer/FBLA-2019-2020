package engine.graphics.mathObjects;
import engine.graphics.Camera;
import java.util.ArrayList;
import java.awt.image.*;
public class Triangle implements Comparable<Triangle>{
	public Point[] points = new Point[3];
	public Point[] points2d = new Point[3];
	private int color = 0;
	private int xMin = 0,xMax = 0,yMin = 0,yMax = 0;
	public double zMin = 0;
	public double zAvg = 0;
	private boolean rotatedToCamera = false;
	public boolean skip = false;
	private boolean textureMode = false;
	private Point[] texturePoints = new Point[]{new Point(0,0),new Point(0,0),new Point(0,0)};
	private BufferedImage texture = null;
	private byte clockDir = 1;
	private double tint = 1.0;
	public Triangle(Point[] triPoints,int col,byte clockDir,double tint){
		for(int i = 0;i < 3;i ++){
			points[i] = new Point(triPoints[i].x, triPoints[i].y, triPoints[i].z);
		}
		color = col;
		this.clockDir = clockDir;
		this.tint = tint;
	}
	public Triangle(Point[] triPoints,BufferedImage tex,Point[] texPoints,byte clockDir,double tint){
		textureMode = true;
		for(int i = 0;i < 3;i ++){
			points[i] = new Point(triPoints[i].x, triPoints[i].y, triPoints[i].z);
		}
		texture = tex;
		texturePoints = texPoints;
		this.clockDir = clockDir;
		this.tint = tint;
	}
	public void moveToCamera(Camera localCamera){
		if(rotatedToCamera){
			return;
		}
		for(int i = 0;i < 3;i ++){
			Point cameraPoint = localCamera.getPosition();
			points[i].translate(new Vector(-cameraPoint.x, -cameraPoint.y, -cameraPoint.z));
			Point tempPoint = new Point(points[i].x,points[i].y,points[i].z);
			points[i].x = tempPoint.x*localCamera.cosRotX-tempPoint.z*localCamera.sinRotX;
			points[i].z = tempPoint.z*localCamera.cosRotX+tempPoint.x*localCamera.sinRotX;
			tempPoint = new Point(points[i].x,points[i].y,points[i].z);
			points[i].y = tempPoint.y*localCamera.cosRotY-tempPoint.z*localCamera.sinRotY;
			points[i].z = tempPoint.z*localCamera.cosRotY+tempPoint.y*localCamera.sinRotY;
			tempPoint = new Point(points[i].x,points[i].y,points[i].z);
			points[i].x = tempPoint.x*localCamera.cosRotZ-tempPoint.y*localCamera.sinRotZ;
			points[i].y = tempPoint.y*localCamera.cosRotZ+tempPoint.x*localCamera.sinRotZ;
		}
		zAvg = points[0].z+points[1].z+points[2].z;
		rotatedToCamera = true;
	}
	private Point intersection = new Point(0,0,0);
	private Point pixLocation = new Point(0,0);
	private int pixX = 0,pixY = 0;
	public void drawTriangle (BufferedImage image,double[][] zBuffer,double clipLevel,double zoom){
		if(skip){
			return;
		}
		int imageMinHeight = -image.getHeight()/2;
		int imageMaxHeight = image.getHeight()/2;
		int imageMinWidth = -image.getWidth()/2;
		int imageMaxWidth = image.getWidth()/2;
		int leftBound = (int)xMin,rightBound = (int)xMax,bottomBound = (int)yMin,topBound = (int)yMax;
		int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		if(leftBound<imageMinWidth){
			leftBound = imageMinWidth;
		}
		if(bottomBound<imageMinHeight){
			bottomBound = imageMinHeight;
		}
		if(rightBound>imageMaxWidth){
			rightBound = imageMaxWidth;
		}
		if(topBound>imageMaxHeight){
			topBound = imageMaxHeight;
		}
		int imageWidth = image.getWidth();
		Ray ray = new Ray(0,0,(image.getWidth()*zoom)/2);
		Plane plane;
		Vector a = new Vector(points[1].x-points[0].x, points[1].y-points[0].y, points[1].z-points[0].z);
		Vector b = new Vector(points[2].x-points[0].x, points[2].y-points[0].y, points[2].z-points[0].z);
		Vector c = Vector.crossProduct(a, b);
		plane = new Plane(c.getXTrans(), c.getYTrans(), c.getZTrans(), points[0].x,  points[0].y,  points[0].z);
		if(textureMode){
			int[] texturePixels = ((DataBufferInt)texture.getRaster().getDataBuffer()).getData();
			int textureWidth = texture.getWidth();
			int textureHeight = texture.getHeight();
			int color = 0;
			int alpha = 0;
			int red = 0;
			int green = 0;
			int blue = 0;
			int afterColor = 0;
			reMapSetup(points, texturePoints);
			for(int i = bottomBound;i<topBound;i++){
				for(int j = leftBound;j<rightBound;j++){
					if(inTriangle2d(this,j,i )){
						if(zMin<zBuffer[j+imageMaxWidth][i+imageMaxHeight]){
							ray.setSlope(j, i, ray.getZSlope());
							ray.planeIntersection(plane,intersection);
							if(intersection.z<zBuffer[j+imageMaxWidth][i+imageMaxHeight]){
								reMapTrianglePoint(points,texturePoints, intersection ,pixLocation);
								pixX = (int)pixLocation.x;
								pixY = (int)pixLocation.y;
								if(pixX<0||pixX>=textureWidth||pixY<0||pixY>=textureHeight){
									continue;
								}
								color = texturePixels[pixX+textureWidth*pixY];
								red = (color>>16)&0x00FF;
								green = (color>>8)&0x0000FF;
								blue = (color)&0x000000FF;
								alpha = (color>>24)&0xFF;
								if(alpha<127){
									continue;
								}
								red*=tint;
								green*=tint;
								blue*=tint;
								if(red>255)red = 255;
								if(green>255)green = 255;
								if(blue>255)blue = 255;
								if(red<0)red = 0;
								if(green<0)green = 0;
								if(blue<0)blue = 0;
								red = (red << 16) & 0x00FF0000;
								green = (green << 8) & 0x0000FF00;
								blue = blue & 0x000000FF;
								afterColor =  0xFF000000 | red | green | blue;
								pixels[(j+imageMaxWidth)+imageWidth*(i+imageMaxHeight)] = afterColor;
								zBuffer[j+imageMaxWidth][i+imageMaxHeight] = intersection.z;
							}
						}
					}
				}
			}
		}else{
			int afterColor = color;
			if(tint!=1.0) {
				int red = (color>>16)&0x00FF;
				int green = (color>>8)&0x0000FF;
				int blue = (color)&0x000000FF;
				red*=tint;
				green*=tint;
				blue*=tint;
				if(red>255)red = 255;
				if(green>255)green = 255;
				if(blue>255)blue = 255;
				if(red<0)red = 0;
				if(green<0)green = 0;
				if(blue<0)blue = 0;
				red = (red << 16) & 0x00FF0000;
				green = (green << 8) & 0x0000FF00;
				blue = (blue) & 0x000000FF;
				afterColor =  0xFF000000 | red | green | blue;
			}
			for(int i = bottomBound;i<topBound;i++){
				for(int j = leftBound;j<rightBound;j++){
					if(inTriangle2d(this,j,i )){
						if(zMin<zBuffer[j+imageMaxWidth][i+imageMaxHeight]){
							ray.setSlope(j, i, ray.getZSlope());
							ray.planeIntersection(plane,intersection);
							if(intersection.z<zBuffer[j+imageMaxWidth][i+imageMaxHeight]){
								pixels[(j+imageMaxWidth)+imageWidth*(i+imageMaxHeight)] = afterColor;
								zBuffer[j+imageMaxWidth][i+imageMaxHeight] = intersection.z;
							}
						}
					}
				}
			}
			
		}
	}
	private static double f1 = 0,f2 = 0,c1 = 0,c2 = 0,c = 0,d = 0,dif1x = 0,dif2x = 0,dif1y,dif2y;
	private void reMapSetup(Point[] T1,Point[] T2){
		f1 = (T1[1].y-T1[0].y)/(T1[1].x-T1[0].x);
		f2 = (T1[2].y-T1[1].y)/(T1[2].x-T1[1].x);
		c1 = 1.0/((T1[2].x-T1[1].x)*(f2-f1));
		c2 = 1.0/((T1[0].x-T1[1].x)*(f1-f2));
		dif1x = T2[2].x-T2[1].x;
		dif2x = T2[1].x-T2[0].x;
		dif1y = T2[2].y-T2[1].y;
		dif2y = T2[1].y-T2[0].y;
	}
	private static double m  = 0;
	private void reMapTrianglePoint(Point[] T1,Point[] T2,Point in,Point out){
		m = -in.x+T1[1].x;
		c = (f1*m+in.y-T1[1].y)*c1;
		d = (f2*m+in.y-T1[1].y)*c2;
		out.x = c*dif1x-d*dif2x+T2[1].x;
		out.y = c*dif1y-d*dif2y+T2[1].y;
	}
	public void create2dBoundBox(){
		if(skip){
			return;
		}
		if(points2d[0].x<points2d[1].x&&points2d[0].x<points2d[2].x){
			xMin = (int)points2d[0].x;
		}
		if(points2d[1].x<points2d[0].x&&points2d[1].x<points2d[2].x){
			xMin = (int)points2d[1].x;
		}
		if(points2d[2].x<points2d[1].x&&points2d[2].x<points2d[0].x){
			xMin = (int)points2d[2].x;
		}
		if(points2d[0].x>points2d[1].x&&points2d[0].x>points2d[2].x){
			xMax = (int)points2d[0].x+1;
		}
		if(points2d[1].x>points2d[0].x&&points2d[1].x>points2d[2].x){
			xMax = (int)points2d[1].x+1;
		}
		if(points2d[2].x>points2d[1].x&&points2d[2].x>points2d[0].x){
			xMax = (int)points2d[2].x+1;
		}
		if(points2d[0].y<points2d[1].y&&points2d[0].y<points2d[2].y){
			yMin = (int)points2d[0].y;
		}
		if(points2d[1].y<points2d[0].y&&points2d[1].y<points2d[2].y){
			yMin = (int)points2d[1].y;
		}
		if(points2d[2].y<points2d[1].y&&points2d[2].y<points2d[0].y){
			yMin = (int)points2d[2].y;
		}
		if(points2d[0].y>points2d[1].y&&points2d[0].y>points2d[2].y){
			yMax = (int)points2d[0].y+1;
		}
		if(points2d[1].y>points2d[0].y&&points2d[1].y>points2d[2].y){
			yMax = (int)points2d[1].y+1;
		}
		if(points2d[2].y>points2d[1].y&&points2d[2].y>points2d[0].y){
			yMax = (int)points2d[2].y+1;
		}
	}
	public void isInFrontOfYou(ArrayList<Triangle> tris,double clipD){
		if(skip){
			return;
		}
		int sum = 0;
		for(int i = 0;i<3;i++){
			if(points[i].z<clipD){
				sum++;
			}
		}
		if(sum == 3){
			skip = true;
			return;
		}else{
			if(sum != 0){
				specialCases( tris,clipD);
				skip = true;
				return;
			}else{
				for(int i = 0;i < 3;i++){
					if(i == 0){
						zMin = points[i].z;
						continue;
					}
					if(points[i].z < zMin){
						zMin = points[i].z;
					}
				}
			}
		}
	}
	public void isVisible(int halfWidth,int halfHeight){
		if(skip){
			return;
		}
		int sum = 0;
		for(int i = 0;i < 3;i ++){
			if(points2d[i].x < -halfWidth){
				sum++;
			}
		}
		if(sum == 3){
			skip = true;
			return;
		}
		sum = 0;
		for(int i = 0;i < 3;i ++){
			if(points2d[i].x > halfWidth){
				sum++;
			}
		}
		if(sum == 3){
			skip = true;
			return;
		}
		sum = 0;
		for(int i = 0;i < 3;i ++){
			if(points2d[i].y < -halfHeight){
				sum++;
			}
		}
		if(sum == 3){
			skip = true;
			return;
		}
		sum = 0;
		for(int i = 0;i < 3;i ++){
			if(points2d[i].y > halfHeight){
				sum++;
			}
		}
		if(sum == 3){
			skip = true;
			return;
		}
	}
	private static double subArea1 = 0,subArea2 = 0,subArea3 = 0,wholeArea = 0;
	public static boolean inTriangle2d(Triangle tri,double x,double y){
				subArea1 = triangleArea(x, y, tri.points2d[1].x, tri.points2d[1].y,tri.points2d[2].x, tri.points2d[2].y);
				subArea2 = triangleArea(tri.points2d[0].x, tri.points2d[0].y, x, y,tri.points2d[2].x, tri.points2d[2].y);
				subArea3 = triangleArea(tri.points2d[0].x,tri.points2d[0].y,tri.points2d[1].x, 	tri.points2d[1].y, x, y);
				wholeArea = triangleArea(tri.points2d[0].x,tri.points2d[0].y,tri.points2d[1].x,tri.points2d[1].y, tri.points2d[2].x, tri.points2d[2].y);
				if(subArea1+subArea2+subArea3-0.1<wholeArea){
					return true;
		}else{
			return false;
		}
	}
	public void specialCases(ArrayList<Triangle> tris,double clipLevel){
		if((points[0].z<clipLevel&&points[1].z>=clipLevel&&points[2].z>=clipLevel) || (points[1].z<clipLevel&&points[0].z>=clipLevel&&points[2].z>=clipLevel) || (points[2].z<clipLevel&&points[0].z>=clipLevel&&points[1].z>clipLevel) ){
			while(points[0].z>=clipLevel){
				Point[] temp = new Point[3];
				Point[] tempTex = new Point[3];
				temp[0] = new Point(points[0].x,points[0].y,points[0].z);
				temp[1] = new Point(points[1].x,points[1].y,points[1].z);
				temp[2] = new Point(points[2].x,points[2].y,points[2].z);
				tempTex[0] = new Point(texturePoints[0].x,texturePoints[0].y,texturePoints[0].z);
				tempTex[1] = new Point(texturePoints[1].x,texturePoints[1].y,texturePoints[1].z);
				tempTex[2] = new Point(texturePoints[2].x,texturePoints[2].y,texturePoints[2].z);
				points[0] = temp[1];
				points[1] = temp[2];
				points[2] = temp[0];
				texturePoints[0] = tempTex[1];
				texturePoints[1] = tempTex[2];
				texturePoints[2] = tempTex[0];
			}
			Point intersection1 = new Point(( clipLevel-points[0].z)/( ( points[1].z-points[0].z )/( points[1].x-points[0].x ) ) +points[0].x ,( clipLevel-points[0].z)/( ( points[1].z-points[0].z )/( points[1].y-points[0].y ) ) +points[0].y,clipLevel);
			Point intersection2 = new Point(( clipLevel-points[0].z)/( ( points[2].z-points[0].z )/( points[2].x-points[0].x ) ) +points[0].x ,( clipLevel-points[0].z)/( ( points[2].z-points[0].z )/( points[2].y-points[0].y ) ) +points[0].y,clipLevel);
			if(textureMode){
				Point intersection12d = new Point(0,0);
				reMapSetup(points, texturePoints);
				reMapTrianglePoint(points, texturePoints, intersection1, intersection12d);
				Point intersection22d = new Point(0,0);
				reMapSetup(points, texturePoints);
				reMapTrianglePoint(points, texturePoints, intersection2, intersection22d);
				Triangle t1 = new Triangle( new Point[]{ intersection1,points[1],intersection2},texture,new Point[]{ intersection12d,texturePoints[1],intersection22d },clockDir  ,tint);
				Triangle t2 = new Triangle( new Point[]{ points[2],intersection2,points[1]},texture,new Point[]{ texturePoints[2],intersection22d,texturePoints[1] },clockDir,tint  );
				t1.rotatedToCamera = true;
				t2.rotatedToCamera = true;
				tris.add(t1);
				tris.add(t2);
			}else{
				Triangle t1 = new Triangle(new Point[]{ intersection1,points[1],intersection2} ,color,clockDir,tint);
				Triangle t2 = new Triangle( new Point[]{ points[2],intersection2,points[1]},color,clockDir,tint);
				t1.rotatedToCamera = true;
				t2.rotatedToCamera = true;
				tris.add(t1);
				tris.add(t2);
			}
		}else if((points[0].z>=clipLevel&&points[1].z<clipLevel&&points[2].z<clipLevel)||(points[1].z>=clipLevel&&points[2].z<clipLevel&&points[0].z<clipLevel)||(points[2].z>=clipLevel&&points[0].z<clipLevel&&points[1].z<clipLevel) ){
			while(points[0].z<clipLevel){
				Point[] temp = new Point[3];
				Point[] tempTex = new Point[3];
				temp[0] = new Point(points[0].x,points[0].y,points[0].z);
				temp[1] = new Point(points[1].x,points[1].y,points[1].z);
				temp[2] = new Point(points[2].x,points[2].y,points[2].z);
				tempTex[0] = new Point(texturePoints[0].x,texturePoints[0].y,texturePoints[0].z);
				tempTex[1] = new Point(texturePoints[1].x,texturePoints[1].y,texturePoints[1].z);
				tempTex[2] = new Point(texturePoints[2].x,texturePoints[2].y,texturePoints[2].z);
				points[0] = temp[2];
				points[1] = temp[0];
				points[2] = temp[1];
				texturePoints[0] = tempTex[2];
				texturePoints[1] = tempTex[0];
				texturePoints[2] = tempTex[1];
			}
			Point intersection1 = new Point(( clipLevel-points[0].z)/( ( points[1].z-points[0].z )/( points[1].x-points[0].x ) ) +points[0].x ,( clipLevel-points[0].z)/( ( points[1].z-points[0].z )/( points[1].y-points[0].y ) ) +points[0].y,clipLevel);
			Point intersection2 = new Point(( clipLevel-points[0].z)/( ( points[2].z-points[0].z )/( points[2].x-points[0].x ) ) +points[0].x ,( clipLevel-points[0].z)/( ( points[2].z-points[0].z )/( points[2].y-points[0].y ) ) +points[0].y,clipLevel);
			if(textureMode){
				Point intersection12d = new Point(0,0);
				reMapSetup(points, texturePoints);
				reMapTrianglePoint(points, texturePoints, intersection1, intersection12d);
				Point intersection22d = new Point(0,0);
				reMapSetup(points, texturePoints);
				reMapTrianglePoint(points, texturePoints, intersection2, intersection22d);
				Triangle t1 = new Triangle( new Point[]{ intersection2,points[0],intersection1},texture,new Point[]{ intersection22d,texturePoints[0],intersection12d },clockDir,tint);
				t1.rotatedToCamera = true;
				tris.add(t1);
			}else{
				Triangle t1 = new Triangle(new Point[]{ intersection2,points[0],intersection1} ,color,clockDir,tint);
				t1.rotatedToCamera = true;
				tris.add(t1);
			}
		}
	}
	public void isClockWiseOrCounter(){
		if(clockDir==0){
			return;
		}else{
			double val = (points2d[1].y-points2d[0].y)*(points2d[2].x-points2d[1].x)-(points2d[2].y-points2d[1].y)*(points2d[1].x-points2d[0].x);
			if(val>=0&&clockDir<0){
				skip = true;
			}
			if(val<=0&&clockDir>0){
				skip = true;
			}
		}
	}
	public void create2dPosition(int imageWidth,int imageHeight,double zoom){
		for(int i = 0;i < 3;i ++){
			points2d[i] = Point.get2dPosition(points[i], imageWidth, imageHeight, zoom);
		}
	}
	private static double rectangleArea = 0,extraArea1 = 0,extraArea2 = 0,extraArea3 = 0;
	private static double triangleArea(double x1,double y1,double x2,double y2,double x3,double y3){
		rectangleArea = (x3-x1)*(y3-y2);
		extraArea1 = ( (x2-x1)*(y1-y2) )*.5;
		extraArea2 = ( (x3-x1)*(y3-y1) )*.5;
		extraArea3 = ( (x3-x2)*(y3-y2) )*.5;
		return Math.abs(rectangleArea-extraArea1-extraArea2-extraArea3);
	}
	public int compareTo(Triangle another) {
        if (zAvg<another.zAvg){
            return -1;
        }else if(zAvg>another.zAvg){
            return 1;
        }else{
			return 0;
		}
    }
}