/**
 * 
 */
package SketchMaster.Stroke.graphics.shapes;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.lib.MathLib;

/**
 * @author maha
 * 
 */
public class Circle extends GeometricPrimitive {


	private static final Logger logger = Logger.getLogger(Circle .class);
	
	@Override
	public String toString() {
		
		return " Center ( "+a+","+b+") Radius ="+r;
	}

	double r, a, b, x, y;
	public int type;
	PointData d1, d2,  centerpoint;
	double start, end;
	int percentDrawnWillDraw;
	int startAngle=0,arcAngle=360;
	
	final static double toTheta = 180.0 / Math.PI;
	Rectangle arcBox = null;
	static protected Stroke bs = new BasicStroke(3);

	public void paint(Graphics2D g) {
		  super.paint(g);
		// logger.info("draw oval ");
		// g.setColor(Color.gray);
		// g.draw(arcBox);

		// g.setColor(Color.gray);
		// g.drawRect((int)x,(int)y , (int)(2*r), (int)(2*r));
		// g.setColor(Color.MAGENTA);
		// g.draw(arcBox);
		// g.drawRect((int)d1.getX(),(int)d1.getY() , (int)(4), (int)(4));
		// g.drawRect((int)d2.getX(),(int)d2.getY() , (int)(4), (int)(4));
 if (arcBox!=null){
		Stroke s = g.getStroke();
		g.setStroke(bs);
		// logger.info("x "+(int)x+" y "+(int)y +" 2r "+ (int)(2*r)+"
		// start "+ (int)start+" end "+end+" angle "+(int)(end-start));

		g.setColor(Color.RED);
		// g.drawArc((int)x,(int)y , (int)(2*r),
		// (int)(2*r),(int)start,(int)(end-start));
	
		//
	
		
		//g.drawArc((int)x, (int)y, (int)r*2,(int) r*2, startAngle, arcAngle);
		
		circleMidpoint(a, b, r, g);
		
		 g.drawRect((int)d1.getX(),(int) d1.getY(), 4, 4);
		 g.fillRect((int)d1.getX(),(int) d1.getY(), 4, 4);
		
		g.setStroke(s);

		// g.setColor(Color.GREEN);
		// // g.drawLine((int)a,(int) b,(int)d1.getX(),(int) d1.getY());
		// g.drawRect((int)d1.getX(),(int) d1.getY(), 4, 4);
		// g.fillRect((int)d1.getX(),(int) d1.getY(), 4, 4);
		// g.setColor(Color.MAGENTA);
		// // g.drawLine((int)a,(int) b,(int)d2.getX(),(int) d2.getY());
		// g.drawRect((int)d2.getX(),(int) d2.getY(), 6, 6);
		// g.fillRect((int)d2.getX(),(int) d2.getY(), 6, 6);
 }
	}
	
	public Circle(double r, double x, double y) {
		super();
		this.r = r;
		this.x = x;
		this.y = y;
	}
	public Circle() {

	}

	public void setParam(ArrayList Param) {

		// this.param = param;
	}

	public void setCircleParams(double r2, double a2, double b2,
			PointData data, PointData data2, Rectangle2D drawBox) {
		// 
		r = r2;
		a = a2;
		b = b2;
		x = a - r;
		y = b - r;

		d1 = data;
		d2 = data2;


	  centerpoint = new 	PointData();
		centerpoint.setLocation(a, b);
		
		
		arcBox = (Rectangle) drawBox;
		
		double L=data.distance(data2)*1.2;
		
		
		arcAngle=(int) Math.ceil(((360.0*L)/(2.0*Math.PI*r)));
		
		int tempArc=arcAngle;
		//double [] p0,p1,p2;
		
//		double [] p0={data.x,data.y,0};
//		double [] p1={a,b,0};
//		double [] p2={data2.x,data2.y,0};
		
 
		
		
		
		//arcAngle=(int) ComputationsGeometry.computeAngle(p0, p1, p2) ;
	//	logger.info(" uisng old way is "+ tempArc+"  using new way is "+arcAngle);
	 // 
		
		double tempx, tempy;
		tempx=d1.x-a;  
		tempy=b-d1.y;
		
		//logger.info(" a = "+a+"   b =  "+b);
		//logger.info("        the temp x "+tempx+"   temp y  "+tempy);
		//double testS=tempx/tempy;
		
	 
		double tempTheta;//
		
       // now i have the angle in radien 		
		 //tempTheta=Math.asin( testS );
		
	//tempTheta=ComputationsGeometry.computeTheta( new Point2D.Double(tempx,tempy));
	tempTheta=Math.atan2(tempy,tempx);//+Math.PI;
	
		// convert to degree 
		// double thetaDegree;
		// thetaDegree=(360.0*tempTheta)/(2.0*Math.PI);
		 
		 startAngle=(int)( tempTheta*toTheta );
		// logger.info("    theta computed is  "+tempTheta+ "   and in degree it is. "+startAngle+ "   then the arc will be dran to "+arcAngle);
		
		// move the center to zero and 
		
		
		
		//drawBox.g
//     double boxSize=drawBox.getHeight()*drawBox.getWidth();
//     double circleSize=a*b*Math.PI;
//     if (circleSize<2.0*boxSize){
//    	 arcBox=null;
//     }
//     
     
     

		// Point p=new Point();
		// p.setLocation(d1.getPointLocation());
		// arcBox=new Rectangle(p);
		// //arcBox.add(d1.getPointLocation());
		// arcBox.add(d2.getPointLocation());
		// //acBox.g
		// // increase the size of the box by 1
		// // arcBox.setBounds(arcBox.x-2, arcBox.y-2, arcBox.width+8,
		// arcBox.height+8);
		// arcBox.grow(2, 2) ;
		// not needed now i was try to compute start and end angle of the arc
		// but failded
		// start=StrokeLib.computeAngle(centerpoint,
		// data.getPointLocation())*toTheta;
		// if (start<0)
		// start+=360;
		// if (start>360){
		// start-=360;
		// }
		//		
		// end=StrokeLib.computeAngle(centerpoint,
		// data2.getPointLocation())*toTheta;
		// if (end<0)
		// end+=360;
		// if (end>360)
		// end-=360;
	}

	private final void circlePoints(int cx, int cy, int x, int y, Graphics2D g) {
		// int act = Color.red.getRGB();

		if (x == 0) {
			g.setColor(Color.RED);

			g.drawLine(cx, cy + y, cx, cy + y);
			g.setColor(Color.GREEN);
			g.drawLine(cx, cy - y, cx, cy - y);
			g.drawLine(cx + y, cy, cx + y, cy);
			g.drawLine(cx - y, cy, cx - y, cy);
		} else if (x == y) {
			g.setColor(Color.RED);
			g.drawLine(cx + x, cy + y, cx + x, cy + y);
			g.setColor(Color.GREEN);
			g.drawLine(cx - x, cy + y, cx - x, cy + y);
			g.drawLine(cx + x, cy - y, cx + x, cy - y);
			g.drawLine(cx - x, cy - y, cx - x, cy - y);
		} else if (x < y) {
			g.setColor(Color.RED);
			g.drawLine(cx + x, cy + y, cx + x, cy + y);
			g.setColor(Color.GREEN);
			g.drawLine(cx - x, cy + y, cx - x, cy + y);
			g.drawLine(cx + x, cy - y, cx + x, cy - y);
			g.drawLine(cx - x, cy - y, cx - x, cy - y);
			g.drawLine(cx + y, cy + x, cx + y, cy + x);
			g.drawLine(cx - y, cy + x, cx - y, cy + x);
			g.drawLine(cx + y, cy - x, cx + y, cy - x);
			g.drawLine(cx - y, cy - x, cx - y, cy - x);
		}
	}

	public void circleMidpoint(double xCenter, double yCenter, double radius,
			Graphics2D g) {
		// int pix = c.getRGB();
		double x = 0;
		double y = radius;
		double p = ((5.0 - radius * 4.0) / 4.0);

		circlePointsSecial(xCenter, yCenter, x, y, g);
		while (x < y) {
			x++;
			if (p < 0) {
				p += 2.0 * x + 1.0;
			} else {
				y--;
				p += 2.0 * (x - y) + 1.0;
			}
			circlePointsSecial(xCenter, yCenter, x, y, g);
		}
	}

	private final void drawPoint(double x, double y, Graphics2D g) {
		if (arcBox != null) {
			// if point exist in the arc neede then it was what i need
			//g.drawLine((int) x, (int) y, (int) x, (int) y);
			if (arcBox.contains(x, y)) {
//
				g.drawLine((int) x, (int) y, (int) x, (int) y);
			} 
			else {
//				if(  arcBox.contains(x, arcBox.getY())&& arcBox.contains(arcBox.getX(), y))
//				{
//					    g.drawLine((int) x, (int) y, (int) x, (int) y);
//					
//				}
//				
//				if( arcBox.contains(x, arcBox.getY()+arcBox.getHeight())&& arcBox.contains(arcBox.getX()+arcBox.getWidth(), y))
//				{
//					g.drawLine((int) x, (int) y, (int) x, (int) y);
//				}

			}
		}
	}

	private final void circlePointsSecial(double cx, double cy, double x,
			double y, Graphics2D g) {
		// int act = Color.red.getRGB();

		if (x == 0) {
			// g.setColor(Color.RED);
			drawPoint(cx, cy + y, g);
			// g.drawLine(, cx, cy + y);
			// g.setColor(Color.GREEN);
			drawPoint(cx, cy - y, g);
			drawPoint(cx + y, cy, g);
			drawPoint(cx - y, cy, g);
		} else if (x == y) {
			// g.setColor(Color.RED);
			drawPoint(cx + x, cy + y, g);
			// g.setColor(Color.GREEN);
			drawPoint(cx - x, cy + y, g);
			drawPoint(cx + x, cy - y, g);
			drawPoint(cx - x, cy - y, g);
		} else if (x < y) {
			// g.setColor(Color.RED);
			drawPoint(cx + x, cy + y, g);
			// g.setColor(Color.GREEN);
			drawPoint(cx - x, cy + y, g);
			drawPoint(cx + x, cy - y, g);
			drawPoint(cx - x, cy - y, g);
			drawPoint(cx + y, cy + x, g);
			drawPoint(cx - y, cy + x, g);
			drawPoint(cx + y, cy - x, g);
			drawPoint(cx - y, cy - x, g);
		}
	}

	
	public double DifferanceFromPoint(PointData point){
		//distance betweeoin point and center suptracted from teh radius. 
		
		double distance=ComputationsGeometry.computeLength(point,centerpoint);
		
		double temp=0;
		
		temp=Math.sqrt(  (distance-r)* (distance-r)   );
	// i am not sure if i can do this but this is best................... 	
	     return temp;	
	
	}

	public double getRadius() {
		  
		return r;
	}

	public Point2D getCenterPoint() {
		  
		return centerpoint;
	}
	
	public double OrthognalError(ArrayList<PointData> points2){
		double error=0;
		double dis;
		PointData pointA;
		// i need to find the 
		for (int i = 0; i < points2.size(); i++) {
			pointA = points2.get(i);
			
			dis=this.centerpoint.distance(pointA)-this.r;
			error+=dis;
			
		}
		
		
		logger.trace("  \\ To Check :  I implement this error OrthognalError ");
		return error;
	}

	public double OrthognalError(ArrayList<PointData> points2, int s, int e){
	
		double error=0;
		double dis;
		PointData pointA;
		// i need to find the 
		for (int i = s; i < points2.size() && i<e; i++) {
			pointA = points2.get(i);
			
			dis=this.centerpoint.distance(pointA)-this.r;
			error+=dis;
			
		}
		
		
	logger.trace("  \\  To Check:  I am going to add  implement this error OrthognalError ");
		return error;
	}
	public double fitError(ArrayList<PointData> points2){
		double error=0;
		double dis;
		PointData pointA;
		// i need to find the 
		for (int i = 0; i < points2.size(); i++) {
			pointA = points2.get(i);
			
			dis=this.centerpoint.distance(pointA)-this.r;
			error+=dis;
			
		}
		
		
		logger.trace("  \\ To Check:  I am going to add  implement this error fitting error  ");
		return error;
	}

	public double fitError(ArrayList<PointData> points2, int s, int e){
		logger.trace("  \\ To Do:  I am going to add  implement this error  fiterror ");
		double error=0;
		double dis;
		PointData pointA;
		// i need to find the 
		for (int i = s; i < points2.size() && i<e; i++) {
			pointA = points2.get(i);
			
			dis=this.centerpoint.distance(pointA)-this.r;
			error+=dis;
			
		}
	
		return error;
	}
	public double fitAreaError(double area) {
		
		//compute the ideal area .... 
		double Idealarea=area();
		
		// get the the points area... 
		double e=MathLib.MeanSquareError(area, Idealarea);
		
		return e;
	}

	 

	public double area(){
		return r*r*Math.PI;
		
	}
}
