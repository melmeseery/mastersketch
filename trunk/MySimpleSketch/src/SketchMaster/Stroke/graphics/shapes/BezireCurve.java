/**
 * 
 */
package SketchMaster.Stroke.graphics.shapes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.lib.ComputationsGeometry;

/**
 * @author maha
 * 
 */
public class BezireCurve extends Circle {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3510501677272295969L;
	// StartPoint, EndPoint;
	transient Point2D[] Curve = new Point2D[100];
	 PointData[] Cp;
	double ax, bx, cx; // /coffecient of the curve
	double ay, by, cy;//
	static int NO_OF_SEGMENT = 5;

	@Override
	public void paint(Graphics2D g) {
		// g.setColor(Color.BLACK);
		// g.drawRect((int)StartPoint.x, (int)StartPoint.y,3, 3);
		// g.drawRect((int)EndPoint.x, (int)EndPoint.y,3, 3);
		//		
		//		
		// g.fillRect((int)StartPoint.x, (int)StartPoint.y,3, 3);
		// g.fillRect((int)EndPoint.x, (int)EndPoint.y,3, 3);
		//		
		// // 
		// logger.info("this curve is beizer // TODO: drawing of the
		// paint bizire curve. ");
		// super.paint(g);

		drawCurve(g);
		super.paint(g);
	}

	public void drawCurve(Graphics2D g) {
		// logger.info("000000000000000000000000000000000000000000000000000000000000000000000000000");

		Stroke s = g.getStroke();
		g.setStroke(bs);

		g.setColor(Color.ORANGE);
		if (Curve != null)

		{
			for (int i = 0; i < Curve.length - 1; i++) {
				if (Curve[i] != null) {
					// logger.info(Curve[i]);
					if (!Double.isNaN(Curve[i].getX())) {
						if (!Double.isNaN(Curve[i].getY())) {
							// logger.info("The curev e"+Curve[i]);
							g.drawLine((int) Curve[i].getX(), (int) Curve[i]
									.getY(), (int) Curve[i + 1].getX(),
									(int) Curve[i + 1].getY());
						}
					}
				}
			}
		}
		g.setColor(Color.red);
		if (Cp != null)
			for (int i = 0; i < Cp.length; i++) {
				// logger.info(Cp[i]);
				if (!Double.isNaN(Cp[i].getX())) {
					if (!Double.isNaN(Cp[i].getY())) {

						g
								.fillOval((int) Cp[i].getX(), (int) Cp[i]
										.getY(), 4, 4);
					}
				}
			}

		g.setStroke(s);
	}

	/**
	 * 
	 */
	public BezireCurve() {

	}

	public void setCurvParam() {

	}

//	public void setEndPoint(PointData endPoint) {
//		EndPoint = endPoint;
//	}
//
//	public void setStartPoint(PointData startPoint) {
//		StartPoint = startPoint;
//	}

	public static ArrayList<PointData> ApproximateToPoints(PointData[] control) {
		ArrayList<Double> coff = coeff(control);

		ArrayList<PointData> returnArrayList = Approximate(coff, control[0], NO_OF_SEGMENT);
		return returnArrayList;

	}

	static public ArrayList<Double> coeff(PointData[] control) {
		if (control != null) {
			if (control.length == 4)

			{
				double[] temp = calculateCoeffients(control);
				ArrayList<Double> toReturn = new ArrayList<Double>();
				for (int i = 0; i < temp.length; i++) {
					toReturn.add(temp[i]);
				}
				return toReturn;

			}

		}

		// logger.info("compute the coefff of the bezire curve from teh
		// control poins ");
		return null;
	}

	public static ArrayList<PointData> Approximate(ArrayList<Double> coff,
			Point2D cp, int noOfSegments) {
		ArrayList<PointData> temp = new ArrayList<PointData>();

		double dt = 1.0 / (double) (noOfSegments - 1);

		for (int i = 0; i < noOfSegments; i++) {
			PointData point = PointOnCubicBezier(coff, i * dt, cp);

			temp.add(point);

		}
		return temp;
	}

	private static PointData PointOnCubicBezier(ArrayList<Double> Coeff,
			double t, Point2D cp) {
		double tSquared, tCubed;
		PointData result = new PointData();
		double ax, bx, cx; // /coffecient of the curve
		double ay, by, cy;//

		cx = Coeff.get(0);
		bx = Coeff.get(1);
		ax = Coeff.get(2);

		cy = Coeff.get(3);
		by = Coeff.get(4);
		ay = Coeff.get(5);

		/* calculate the curve point at parameter value t */

		tSquared = t * t;
		tCubed = tSquared * t;

		double x = (ax * tCubed) + (bx * tSquared) + (cx * t) + cp.getX();
		double y = (ay * tCubed) + (by * tSquared) + (cy * t) + cp.getY();
		result.setLocation(x, y);
		return result;
		// return null;
	}

	public void CreateCurve(PointData[] control) {
		Cp = control;
		ComputeBezier(control, Curve);
	}

	private static double[] calculateCoeffients(Point2D[] cp) {
		double cx, bx, ax, cy, by, ay;
		double[] temp = new double[6];
		/* calculate the polynomial coefficients */

		cx = 3.0 * (cp[1].getX() - cp[0].getX());
		bx = 3.0 * (cp[2].getX() - cp[1].getX()) - cx;
		ax = cp[3].getX() - cp[0].getX() - cx - bx;

		cy = 3.0 * (cp[1].getY() - cp[0].getY());
		by = 3.0 * (cp[2].getY() - cp[1].getY()) - cy;
		ay = cp[3].getY() - cp[0].getY() - cy - by;

		temp[0] = cx;
		temp[1] = bx;
		temp[2] = ax;

		temp[3] = cy;
		temp[4] = by;
		temp[5] = ay;
		// logger.info("Testing coeeeff -------------");
		// for (int i = 0; i < temp.length; i++) {
		// logger.info("temp "+i+" = "+temp[i]);
		// }
		return temp;
	}

	Point2D PointOnCubicBezier(Point2D[] cp, double t) {
		double tSquared, tCubed;
		Point2D result = new Point2D.Double();

		/* calculate the curve point at parameter value t */

		tSquared = t * t;
		tCubed = tSquared * t;

		double x = (ax * tCubed) + (bx * tSquared) + (cx * t) + cp[0].getX();
		double y = (ay * tCubed) + (by * tSquared) + (cy * t) + cp[0].getY();
		result.setLocation(x, y);
		return result;
	}

	/*
	 * ComputeBezier fills an array of Point2D structs with the curve points
	 * generated from the control points cp. Caller must allocate sufficient
	 * memory for the result, which is <sizeof(Point2D) numberOfPoints>
	 */

	void ComputeBezier(Point2D[] cp, Point2D[] curve) {
		double dt;
		int i;
		int numberOfPoints = curve.length;
		double[] Coeff = calculateCoeffients(cp);
		cx = Coeff[0];
		bx = Coeff[1];
		ax = Coeff[2];

		cy = Coeff[3];
		by = Coeff[4];
		ay = Coeff[5];

		dt = 1.0 / (double) (numberOfPoints - 1);

		for (i = 0; i < numberOfPoints; i++)
			curve[i] = PointOnCubicBezier(cp, i * dt);
	}

	
	public double DifferanceFromPoint(PointData point){
		//distance betweeoin point and center suptracted from teh radius. 
		
		//double distance=ComputationsGeometry.computeLength(point,   centerpoint);
		
		double temp=0;
		
		//temp=Math.sqrt(  (distance-r)* (distance-r)   );
		
	     return temp;	
	
	}

	public double getRadius() {
	
double []radius=new double[1];
radius[0]=0;
int countr=0;
		if (Curve != null)

		{
			if (Cp != null){
				
				radius=new double[Cp.length];
				for (int i = 0; i < Cp.length; i++) {
					// logger.info(Cp[i]);
					if (!Double.isNaN(Cp[i].getX())) {
						if (!Double.isNaN(Cp[i].getY())) {
							
							countr=0;
			for (int k = 0; k < Curve.length - 1; k++) {
				if (Curve[k] != null) {
					// logger.info(Curve[i]);
					if (!Double.isNaN(Curve[k].getX())) {
						if (!Double.isNaN(Curve[k].getY())) {
							// logger.info("The curev e"+Curve[i]);
							
							radius[i]=ComputationsGeometry.length(Curve[k].getX(), Curve[k].getY(), Cp[i].getX(), Cp[i].getY());
							
							countr++;
							 
						}// if y not nan 
					}// if x not nan 
				}// if curve k 
			}//for loop 
			
			radius[i]/=((double)countr);
		}
					}
				}
				
				
				
			}//if cp 
			
			
			
			
			}
		
		double len=0,maxlength=0;
	for (int i = 0; i < radius.length; i++) {
		if (radius[i]>maxlength){
			maxlength=radius[i];
		}
	}
				

		  
		return maxlength;
	}
}
