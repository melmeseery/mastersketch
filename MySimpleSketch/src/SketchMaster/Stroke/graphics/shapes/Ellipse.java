package SketchMaster.Stroke.graphics.shapes;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.lib.ComputationsGeometry;

public class Ellipse extends GeometricPrimitive implements GuiShape {
	private static final Logger logger = Logger.getLogger(GeometricPrimitive.class);
	
	double x, y, xc, yc, a, b;
	Line MinorAxes,MajorAxes;
	@Deprecated
	public Ellipse(){
		//TODO : MUST REMOVE ALL REFRENCE TO THIS FUNCTION
	}
	public Ellipse(double cx, double cy, Line l, Line l2) {
		setEllipseParam(cx, cy,l.length()/2.0, l2.length()/2.0);
		MajorAxes=l;
		MinorAxes=l2;
		
	}

	public void paint(Graphics2D g) {
		g.drawOval((int) x, (int) y, (int) a, (int) b);
		super.paint(g);
	}

	@Deprecated
	public void setParam(ArrayList Param) {

		// this.param = param;
	}

	public double area(){
	return a*b*Math.PI;	
	}	
	
	public double DifferanceFromPoint(PointData point){
		
		//x y is the intersection and xx,yy is the point 
		////x = a*b*xx/sqrt((b*xx)2 + (a*yy)2)
		///y = a*b*yy/sqrt((b*xx)2 + (a*yy)2)|
		//distance betweeoin point and center suptracted from teh radius. 
		
		//double distance=ComputationsGeometry.computeLength(point,   centerpoint);
		
		double temp=0;
		
	//	temp=Math.sqrt(  (distance-r)* (distance-r)   );
		
	     return temp;	
	
	}
	
	public void setEllipseParam(double x0, double y0, double ellipse_a,
			double ellipse_b) {
		xc = x0;
		yc = y0;
		a = ellipse_a;
		b = ellipse_b;
		x = xc - a;
		y = yc - b;
	}

	public double  getLargestRadius() {
		  
		if (a>=b)
			return a;
		else return b;
	}
	
	/*
	* This functions returns an array containing 36 points to draw an
	* ellipse.
	*
	* @param x {double} X coordinate
	* @param y {double} Y coordinate
	* @param a {double} Semimajor axis
	* @param b {double} Semiminor axis
	* @param angle {double} Angle of the ellipse
	*/
public ArrayList<Point2D>  calculateEllipse(double  x, double y, double a,double  b, int  angle, int steps) 
	{
	  if (steps == -1)
	    steps = 36;
	  ArrayList<Point2D> points = new 	  ArrayList<Point2D> () ;
	 
	  // Angle is given by Degree Value
	 double beta = -angle * (Math.PI / 180); //(Math.PI/180) converts Degree Value into Radians
	 double sinbeta = Math.sin(beta);
	 double cosbeta = Math.cos(beta);
	 
	  for (int i = 0; i < 360; i += 360 / steps) 
	  {
		  double alpha = i * (Math.PI / 180) ;
		  double  sinalpha = Math.sin(alpha);
		  double cosalpha = Math.cos(alpha);
	 
		  double X = x + (a * cosalpha * cosbeta - b * sinalpha * sinbeta);
		  double Y = y + (a * cosalpha * sinbeta + b * sinalpha * cosbeta);
	 
	    points.add(new Point2D.Double(X, Y));
	   }
	 
	  return points;
	}
public double OrthognalError(ArrayList<PointData> points2){
	logger.warn("  \\ To Do:  I am going to add  implement this error OrthognalError ");
	return 0;
}

public double OrthognalError(ArrayList<PointData> points2, int s, int e){
	logger.warn("  \\ To Do:  I am going to add  implement this error OrthognalError ");
return 0;	
}
public double fitError(ArrayList<PointData> points2){
	logger.warn("  \\ To Do:  I am going to add  implement this error  fiterror ");
	return 5;
}

public double fitError(ArrayList<PointData> points2, int s, int e){
	logger.warn("  \\ To Do:  I am going to add  implement this error  fiterror ");
	return 0;
}



}
