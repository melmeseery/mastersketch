package SketchMaster.Stroke.graphics.shapes;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.lib.MathLib;

public class Ellipse extends GeometricPrimitive implements GuiShape {
	private static final Logger logger = Logger.getLogger(GeometricPrimitive.class);
	
	double x, y, xc, yc, a, b;
	Line MinorAxes,MajorAxes;
	@Deprecated
	public Ellipse(){
		//TODO : MUST REMOVE ALL REFRENCE TO THIS FUNCTION
	}
	public Ellipse(double cx, double cy, Line l, Line l2) {
		setEllipseParam(cx, cy,l.length(), l2.length());
		MajorAxes=l;
		MinorAxes=l2;
		
	}

	public Ellipse(Line l, Line l2) {
		double cx,cy ;
		cx=l.getMidpoint().getX();
		cy=l.getMidpoint().getY();
		
		
		setEllipseParam(cx, cy,l.length(), l2.length());
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
		x = xc - a/2.0;
		y = yc -b/2.0;
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

	double sum=0, er, xx,yy;
	for (int i = 0; i <  points2.size() ; i++) {
//	
PointData p = points2.get(i);	
	 xx=p.getX();
	 yy=p.getY();
	er=getDistanceFromEllipse(xx, yy);
	sum+=er;

//	

	}	
	return Math.sqrt(sum*sum);
}

public double OrthognalError(ArrayList<PointData> points2, int s, int e){
	logger.warn("  \\ To Do:  I am going to add  implement this error OrthognalError ");
	
	
	double sum=0, er, xx,yy;
	for (int i = s; i <  points2.size()&& i<e; i++) {
//	
PointData p = points2.get(i);	
	 xx=p.getX();
	 yy=p.getY();
	er=getDistanceFromEllipse(xx, yy);
	sum+=er;

//	

	}	
	return Math.sqrt(sum*sum);
	
	
//return 0;	
}
public double fitError(ArrayList<PointData> points2){
	logger.warn("  \\ To Do:  I am going to add  implement this error  fiterror ");
//	// The ellipse is (x/2)^2 + (y/1)^2 = 1. Compute distances for points
//	// (u,v) with |u| <= 3 and |v| <= 3.
//	double dA = this.a, dB =this.b;
//	
//	final double dEpsilon = 1e-06;
//	final  int iMax = 10;
//	double dUMin = -30.0, dUMax = 30.0;
//	double dVMin = -30.0, dVMax = 30.0;
//	double dU, dV, dDistance;
//	int iX, iY, iMaxIFinal = 0;
//	final int iXBound = 256, iYBound = 256;// can be the boundries of the stroke ... 
//	 Integer iIFinal=new Integer(0);
//	Double dX,dY;
//	dX=new Double(0);
//	dY=new Double(0);
//	
////	for (iY = 0; iY < iYBound; iY++)  // for alll the points in the strokke .. 
////	{
////
////	for (iX = 0; iX < iXBound; iX++)
////	{
//		for (int i = 0; i <  points2.size(); i++) {
//			
//        PointData p = points2.get(i);
//		
//	dV = dVMin + (dVMax-dVMin)*p.getY()/iYBound;
//	dU = dUMin + (dUMax-dUMin)*p.getX()/iXBound;
//
//	
//	dDistance = DistancePointEllipse(dU,dV,dA,dB,dEpsilon,iMax,
//	 iIFinal,dX,dY);
//	double dist = dDistance;
//	int IterationFound= iIFinal;
//	if (iIFinal > iMaxIFinal)
//	{
//	iMaxIFinal = iIFinal;
//	}
//	}
//	}
//	
	double sum=0, er, xx,yy;
	for (int i = 0; i <  points2.size(); i++) {
//	
PointData p = points2.get(i);	
	 xx=p.getX();
	 yy=p.getY();
	er=getDistanceFromEllipse(xx, yy);
	sum+=er;

//	

	}	
	return (Math.sqrt(sum*sum) );
	 
}
	
	

private double getDistanceFromEllipse(double xx, double yy){
	Point p=new Point();
	//	double x = (a*b*xx)/Math.sqrt((b*xx)*2 + (a*yy)*2);
//	double y = a*b*yy/Math.sqrt((b*xx)*2 + (a*yy)*2);
	// simple...........
	double bx=(b*xx);
	double ay=a*yy;
	double  dem=Math.sqrt((bx*bx) + (ay*ay));
	
	double x = (a*bx)/dem;
	double y = (b*ay)/dem;
	  
//	  // then distance from x
//	 return PointData.distance(xx, yy, x, y);
	
	/// complex displaced center
	double m=(yc-yy)/(xc-xx);
   double temp=m*xx+yy;
   double ab=a*a*b*b;
   // const =a2b2+b2xc-a2temp2 +a2yc
   double cont=a*a*b*b+b*b*xc-a*a*temp*temp+a*a*yc;

   // now eq pramtesr
   double eqa=b*b+a*a*m;
   double eqb=-2*temp*a*a;
   double eqc=-cont;
   

	
  Point2D xxLocations = MathLib.solveQuadratic(eqa,eqb,eqc);
	
  
  if (xxLocations!=null){
  // check the dircarded...
	  //now i either have one solution or twoo,
	  
	  if (xxLocations.getX()==xxLocations.getY()){  //one root...
		  x=xxLocations.getX();
		  y=m*(x-xx)+yy;
		  
		  // then distance from x
		 return PointData.distance(xx, yy, x, y);
	  }
	  else {// two roots is found 
		  double temp1 =xxLocations.getX()*xxLocations.getY();
		  if (temp1> 0){ // if both the same sign (-ve or +ve)
			  if (xxLocations.getX()>xxLocations.getY())
			  { 
				  xx=xxLocations.getX();
			  }
			  
			  if (xxLocations.getY()>xxLocations.getX())
			  {
				  xx=xxLocations.getY();
			  
			  }
			  
			  
		  }
		  // check if one of the roots is -ve
		  if (xxLocations.getX()<0){ // x is -ve then y is +ve;.
			  xx=xxLocations.getY();
			  
		  }
		  if (xxLocations.getY()<0){ // x is -ve then y is +ve;.
			  xx=xxLocations.getX();
			  
		  }
		  
		  
		  
		  y=m*(x-xx)+yy;
		  
		  // then distance from x
		 return PointData.distance(xx, yy, x, y);
	  }
	  
	  
  }
  else {
	  // no roots for this equation and that meeans no distance 
	  return 0;
	  
  }
  
	
	// return 0; 
}

public double fitError(ArrayList<PointData> points2, int s, int e){
	logger.warn("  \\ To Do:  I am going to add  implement this error  fiterror ");

	double sum=0, er, xx,yy;
	for (int i = s; i <  points2.size()&& i<e; i++) {
//	
PointData p = points2.get(i);	
	 xx=p.getX();
	 yy=p.getY();
	er=getDistanceFromEllipse(xx, yy);
	sum+=er;

//	

	}	
	return (Math.sqrt(sum*sum) );
}
public double fitAreaError(double area) {
	
	//compute the ideal area .... 
	double Idealarea=getEllipsearea();
	
	// get the the points area... 
	double e=Math.abs( area/Idealarea);
	//double e=MathLib.MeanPercentSquareError(area, Idealarea);
	
	return e;
}

private double getEllipsearea() {
 
	
	// i will change this line to correct error caclculation from 
	//		double area = Math.PI * (this.ellipse_a / 2.0) * (this.ellipse_b / 2.0);
	double area = Math.PI * (this.a) * (this.b);
	//  logger.trace("   ellipse area =    " + area);
	return area;
}

double DistancePointEllipseSpecial (double dU, double dV, double dA,
		double dB, final double dEpsilon,final  int iMax, Integer riIFinal,
		Double rdX, Double rdY)
		{
		// initial guess
		double dT = dB*(dV - dB);
		// Newton’s method
		int i;
		for (i = 0; i < iMax; i++)
		{
		double dTpASqr = dT + dA*dA;
		double dTpBSqr = dT + dB*dB;
		double dInvTpASqr = 1.0/dTpASqr;
		double dInvTpBSqr = 1.0/dTpBSqr;
		double dXDivA = dA*dU*dInvTpASqr;
		double dYDivB = dB*dV*dInvTpBSqr;
		double dXDivASqr = dXDivA*dXDivA;
		double dYDivBSqr = dYDivB*dYDivB;
		double dF = dXDivASqr + dYDivBSqr - 1.0;
		if (dF < dEpsilon)
		{
		// F(t0) is close enough to zero, terminate the iteration
		rdX = dXDivA*dA;
		rdY = dYDivB*dB;
		riIFinal = i;
		break;
		}
		double dFDer = 2.0*(dXDivASqr*dInvTpASqr + dYDivBSqr*dInvTpBSqr);
		double dRatio = dF/dFDer;
		if (dRatio < dEpsilon)
		{
		// t1-t0 is close enough to zero, terminate the iteration
		rdX = dXDivA*dA;
		rdY = dYDivB*dB;
		riIFinal = i;
		break;
		}
		dT += dRatio;
		}
		if (i == iMax)
		{
		// method failed to converge, let caller know
		riIFinal = -1;
		return -Double.MAX_VALUE;
		}
		double dDelta0 = rdX - dU, dDelta1 = rdY - dV;
		return Math.sqrt(dDelta0*dDelta0 + dDelta1*dDelta1);
		}
		//----------------------------------------------------------------------------
		double DistancePointEllipse (
		double dU, double dV, // test point (u,v)
		double dA, double dB, // ellipse is (x/a)^2 + (y/b)^2 = 1
		final double dEpsilon, // zero tolerance for Newton’s method
		final int iMax, // maximum iterations in Newton’s method
		Integer  riIFinal, // number of iterations used
		Double rdX, Double rdY) // a closest point (x,y)
		{
		// special case of circle
		
		if (Math.abs(dA-dB) < dEpsilon)
		{
		double dLength = Math.sqrt(dU*dU+dV*dV);
		return Math.abs(dLength - dA);
		}
		// reflect U = -U if necessary, clamp to zero if necessary
		boolean bXReflect;
		if (dU > dEpsilon)
		{
		bXReflect = false;
		}
		else if (dU < -dEpsilon)
		{
		bXReflect = true;
		dU = -dU;
		}
		else
		{
		bXReflect = false;
		dU = 0.0;
		}
		// reflect V = -V if necessary, clamp to zero if necessary
		boolean bYReflect;
		if (dV > dEpsilon)
		{
		bYReflect = false;
		}
		else if (dV < -dEpsilon)
		{
		bYReflect = true;
		dV = -dV;
		}
		else
		{
		bYReflect = false;
		dV = 0.0;
		}
		// transpose if necessary
		double dSave;
		boolean bTranspose;
		if (dA >= dB)
		{
		bTranspose = false;
		}
		else
		{
		bTranspose = true;
		dSave = dA;
		dA = dB;
		dB = dSave;
		dSave = dU;
		dU = dV;
		dV = dSave;
		}
		double dDistance;
		if (dU != 0.0)
		{
		if (dV != 0.0)
		{
		dDistance = DistancePointEllipseSpecial(dU,dV,dA,dB,dEpsilon,iMax,
		riIFinal,rdX,rdY);
		}
		else
		{
		double dBSqr = dB*dB;
		if (dU < dA - dBSqr/dA)
		{
		double dASqr = dA*dA;
		rdX = dASqr*dU/(dASqr-dBSqr);
		double dXDivA = rdX/dA;
		rdY = dB*Math.sqrt(Math.abs(1.0-dXDivA*dXDivA));
		double dXDelta = rdX - dU;
		dDistance = Math.sqrt(dXDelta*dXDelta+rdY*rdY);
		riIFinal = 0;
		}
		else
		{
		dDistance = Math.abs(dU - dA);
		rdX = dA;
		rdY = 0.0;
		riIFinal = 0;
		}
		}
		}
		else
		{
		dDistance =Math.abs(dV - dB);
		rdX = 0.0;
		rdY = dB;
		riIFinal = 0;
		}
		if (bTranspose)
		{
		dSave = rdX;
		rdX = rdY;
		rdY = dSave;
		}
		if (bYReflect)
		{
		rdY = -rdY;
		}
		if (bXReflect)
		{
		rdX = -rdX;
		}
		return dDistance;
		}
		//----------------------------------------------------------------------------
//		int main ()
//		{
//		// The ellipse is (x/2)^2 + (y/1)^2 = 1. Compute distances for points
//		// (u,v) with |u| <= 3 and |v| <= 3.
//		double dA = 2.0, dB = 1.0;
//		const double dEpsilon = 1e-08;
//		const int iMax = 32;
//		double dUMin = -3.0, dUMax = 3.0;
//		double dVMin = -3.0, dVMax = 3.0;
//		double dU, dV, dX, dY, dDistance;
//		int iX, iY, iIFinal, iMaxIFinal = 0;
//		const int iXBound = 256, iYBound = 256;
//		ImageDouble2D kImage(iXBound,iYBound);
//		ImageInt2D kIndex(iXBound,iYBound);
//		for (iY = 0; iY < iYBound; iY++)
//		{
//		dV = dVMin + (dVMax-dVMin)*iY/iYBound;
//		for (iX = 0; iX < iXBound; iX++)
//		{
//		dU = dUMin + (dUMax-dUMin)*iX/iXBound;
//		dDistance = DistancePointEllipse(dU,dV,dA,dB,dEpsilon,iMax,
//		iIFinal,dX,dY);
//		kImage(iX,iY) = dDistance;
//		kIndex(iX,iY) = iIFinal;
//		if (iIFinal > iMaxIFinal)
//		{
//		iMaxIFinal = iIFinal;
//		}
//		}
//		}
//		// The point (umax,vmax) has maximal distance. Color the image so that
//		// points inside the ellipse are blue with intensity proportional to
//		// distance. Color points outside red with intensity proportional to
//		// distance.
//		double dMaxDistance = kImage(255,0);
//		ImageRGB82D kColor(iXBound,iYBound);
//		ImageDouble2D kTest(iXBound,iYBound);
//		for (iY = 0; iY < iYBound; iY++)
//		{
//		dV = dVMin + (dVMax-dVMin)*iY/iYBound;
//		for (iX = 0; iX < iXBound; iX++)
//		{
//		dU = dUMin + (dUMax-dUMin)*iX/iXBound;
//		dDistance = kImage(iX,iY);
//		double dURatio = dU/dA;
//		double dVRatio = dV/dB;
//		double dTest = dURatio*dURatio + dVRatio*dVRatio - 1.0;
//		kTest(iX,iYBound-1-iY) = dTest;
//		unsigned char ucGray;
//		if (dTest > 0.0)
//		{
//		ucGray = (unsigned char)(255.0f*dDistance/dMaxDistance);
//		kColor(iX,iY) = GetColor24(ucGray,0,0);
//		}
//		else
//		{
//		ucGray = (unsigned char)(255.0f*dDistance);
//		kColor(iX,iY) = GetColor24(0,0,ucGray);
//		}
//		}
//		}
//		// draw ellipse
//		iMaxIFinal++;
//		for (iY = 1; iY < iYBound-1; iY++)
//		{
//		for (iX = 1; iX < iXBound-1; iX++)
//		{
//		if (kTest(iX,iY) <= 0.0)
//		{
//		if (kTest(iX-1,iY-1) > 0.0
//		|| kTest(iX ,iY-1) > 0.0
//		|| kTest(iX+1,iY-1) > 0.0
//		|| kTest(iX-1,iY ) > 0.0
//		|| kTest(iX+1,iY ) > 0.0
//		|| kTest(iX-1,iY+1) > 0.0
//		|| kTest(iX ,iY+1) > 0.0
//		|| kTest(iX+1,iY+1) > 0.0)
//		{
//		kColor(iX,iY) = GetColor24(128,128,128);
//		kIndex(iX,iY) = iMaxIFinal;
//		}
//		}
//		}
//		}
//		kImage.Save("distance.im");
//		kIndex.Save("index.im");
//		kColor.Save("color.im");
//		return 0;
//		}
		public Point2D getCenter() {
	          return new Point2D.Double(xc,yc);
			//return null;
		}

}
