/**
 * 
 */
package SketchMaster.swarm.polygonApproximations;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.system.SystemSettings;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Segment;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.shapes.Circle;
import SketchMaster.Stroke.graphics.shapes.GeometricPrimitive;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.Stroke.graphics.shapes.SegmentedShape;

/**
 * @author maha
 * 
 */
public class DigitalCurveDivideSolution extends PolygonSolution implements
		SegmentedShape {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DigitalCurveDivideSolution.class);

	private static final double MaxSegmentERROR = 10;

	/**
	 * A list of the final vertices of the solution [only the vertics not all
	 * the point of stroke.]
	 * 
	 * @uml.property name="polygonVertices"
	 */
	protected ArrayList<GeometricPrimitive> SegmentsType;
	private static double K =5;// 0.05;// was 0.01

	static protected boolean storeShape = false;

	// /**
	// *
	// */
	// public DigitalCurveDivideSolution() {
	//		
	// }
	//
	// /**
	// * @param size
	// */
	// public DigitalCurveDivideSolution(int size) {
	// super(size);
	//		
	// }

	/**
	 * @param problemStroke
	 */
	public DigitalCurveDivideSolution(Stroke problemStroke) {
		super(problemStroke);
		refineSolution();

	}

	// final static double T=0.7;
	// public static double eTolerance= 200.0;

	@Override
	public double eval() {
		double func = 0.0;
		Error = error();
//		if (Error>this.eTolerance){
//	func=Error;
//}
		
		func = 1.0 / (Error * (Math.pow(polygonVertices.size(), K)));

		return func;
	}

	@Override
	public double error() {
		SegmentsType = new ArrayList<GeometricPrimitive>();

		return errorMain();

	}

	@Override
	public void refineSolution() {

		
//		RefineSolutionMerge();
//		RefineSolutionSegmentCount();
//		// logger.info("Final points");
//		// for (int i = 0; i < particlePoints.length; i++) {
//		// System.out.print(" "+particlePoints[i]+" ");
//		// }
//		// logger.info(" ");
//		// super.refineSolution();
//		particlePoints[0] = 1;
//		particlePoints[particlePoints.length - 1] = 1;
		
		
		
		if (SystemSettings.POLYGON_ADJUST_Default != POLYGON_ADJUST_NONE ){
			
			
			if (SystemSettings.POLYGON_ADJUST_Default == POLYGON_ADJUST_MERGE
					|| SystemSettings.POLYGON_ADJUST_Default == POLYGON_ADJUST_BOTH) {
				int count=0;
				while (RefineSolutionMerge()&& count<10){
					count++;
				}
			}
			if (SystemSettings.POLYGON_ADJUST_Default == POLYGON_ADJUST_DOMINATE_POINT
					|| SystemSettings.POLYGON_ADJUST_Default == POLYGON_ADJUST_BOTH) {
				int count=0;
				while (RefineSolutionDomintate()&& count<10){
					count++;
				}
				//simpleRefineSolutionDominate();
			}
					int count=0;
			while (RefineSolutionSegmentCount()&& count<10){
				count++;
			}
			
			}
//		}
			if (SystemSettings.POLYGON_ADJUST_Default ==POLYGON_ADJUST_ONLY_SEGMENTS)
				RefineSolutionSegmentCount();
			// first and last musst p 1
			if (particlePoints.length>0){
				
			particlePoints[0] = 1;
			particlePoints[particlePoints.length - 1] = 1;
			}

		// // try to refine the solution .
	}
	
	@Override
	protected boolean isErrorMenimized(int First, int second, int next) {
		  
		
		double e1 = getSegmentError(First, second, false);
		double e2 =  getSegmentError(second,next, false);
		 

		double e3 = getSegmentError(First, next, false);
//		getXiXjError(First, next, problemStroke
//				.getPoint(First).getPointLocation(), problemStroke
//				.getPoint(next).getPointLocation());

		if (e3 < (e1 + e2))
			return true;
		
		else 
			return false;
		 
		
	}

	private 	boolean RefineSolutionMerge() {
		
		boolean merged=false;
		// do th elagorith min page 247
		int count = 0;
		int j = 0;
		int next = 0;

		// first adjust
		for (int i = 0; i < particlePoints.length; i++) {
			if (particlePoints[i] == 1) {
				// get the count of vertix one for prev and current and next
				// beter calcuate prameter and use the veritix dat. strucute r
				// try to adjust it by moving it along the arc from prev to next
				// to min error of two adjacent arcs

				j = getNextVertex(i + 1);
				if (j != -1) {

					// if this point is a vertix now

					// now i have the i for vertix and j for next vertix

					// try to find th enext vertix
					next = getNextVertex(j + 1);
					if (next != -1) { // now i i will try to merge them

						// frist compute the erro rf the first system i j next
//						double e1 = getSegmentError(i, j,false);
//						double e2 = getSegmentError(j, next,false);
//
//						double e3 = getSegmentError(i, next,false);

						// logger.info(" e1 "+e1 + " e2 "+e2 + " e3"+e3+"
						// ------- "+i+" " + j+" " +next);
						if (isErrorMenimized(i, j, next)){
				//		if (e3 <= (e1 + e2)) {// if error after merge is less
												// than current then merge them
												// by letting j =0
							 merged=true;
							// logger.info(" Merge "+i+" " + j+" "
							// +next);
							// then
							particlePoints[j] = 0;
							i = i - 1;
						}

					}

					// merg every tow arc if total error > error final

				}

			}
		}

		// getXiXjError()
		particlePoints[0] = 1;
		particlePoints[particlePoints.length - 1] = 1;
		return merged;
		// will need to compute the error
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.swarm.polygonApproximations.polygonSolution#setStrokeBasedTolerance()
	 */
	@Override
	protected void setStrokeBasedTolerance() {

		super.setStrokeBasedTolerance();

		if (eTolerance >SystemSettings.DIGITAL_CURVE_DIVIDE_THRESHOLD)
			eTolerance = SystemSettings.DIGITAL_CURVE_DIVIDE_THRESHOLD;
		// logger.info(eTolerance);
		// super.setStrokeBasedTolerance();
	}

	@Override
	public Object clone() {
		DigitalCurveDivideSolution tempSolution = null;
		tempSolution = (DigitalCurveDivideSolution) super.clone();
		// tempSolution.SegmentsType=(ArrayList<GuiShape>)SegmentsType.clone();

		return tempSolution; // finished
	}

	public void paint(Graphics2D g) {

		// logger.info("in paint ");
		// logger.info("segments count"+SegmentsType.size());
		// logger.info( " the color is "+linecolor.toString());
		for (int i = 0; i < SegmentsType.size(); i++) {
			// if (i>1)
			// return ;
			SegmentsType.get(i).paint(g);
			// g.setColor(Color.BLUE);
			// //draw line between point and next point in polyogn
			// g.drawLine((int) ( polygonVertices.get(i)).getX(),
			// (int) ( polygonVertices.get(i)).getY(),
			// (int) ( polygonVertices.get(i + 1)).getX(),
			// (int) ( polygonVertices.get(i + 1)).getY());
			// g.setColor(Color.BLACK);
			// //draw a small polygon to mark this point as a vertix of polygon.
			// g.drawRect((int) polygonVertices.get(i).getX(), (int)
			// polygonVertices.get(i).getY(), 3, 3);
			// g.fillRect((int) polygonVertices.get(i).getX(), (int)
			// polygonVertices.get(i).getY(), 3, 3);

		}

	}

	public double errorMain() {
		double error = 0.0;
		double temperror;
		int currentType;
		Point2D pi, pj;
		int vj = 0;
		int v1index = 0, v2index = 0, pjindex = 0, vcount = 0;

		for (int i = 0; i < polygonVertices.size() - 1; i++) {
			// now i
			pi = polygonVertices.get(i);
			pj = polygonVertices.get(i + 1);

			vcount = 0;
			// get the index of the consective vertics and send it to the error
			// segment.
			for (int j = v2index; j < particlePoints.length; j++) {

				if (particlePoints[j] == 1) {

					// this is a vertix
					if (vcount == 0)// check if first vertix
					{
						// logger.info("found first vertix "+j);
						v1index = j; // vertix is the index
						vcount++; // add vertix count

					} else if (vcount == 1) // if second vertix make set xj
											// vertix
					{

						v2index = j;// set the second vertix
						// now call error and add it to the current error
						// currentType=getSegmentType(v1index, v2index, pi, pj);
						temperror = getSegmentError(v1index, v2index,false);
                        
						error += temperror;
						break;
					}
				}

			}
		}

		// logger.info("------------------------------Error---------
		// -------------------");

		return error;

	}

	private double getSegmentError(int indexxi, int indexxj,boolean store) {
		if (SystemSettings.DIGITAL_CURVE_MULTI_PREMETIVE)
		{
			return getTypeMultiPrimitive(indexxi, indexxj,store);
		}
		
		return getType(indexxi, indexxj,store);

	}

	public void calculateSolutionParameters() {

		super.calculateSolutionParameters();

	}

	private Circle getCircleParameters(int indexxi,int indexxj){
		
		PointData pointk;
		double k, c, tempxy, tempx, tempy, tempx2, tempy2 = 0, tempx2y, tempxy2;
		tempxy = tempx = tempy = tempx2 = tempx2y = tempxy2 = 0.0;
		double tempx3 = 0, tempy3 = 0;

		double xi, yi;

		int N = 0;
		N = indexxj - indexxi;

		Point pr = new Point();
		pr.setLocation(problemStroke.getPoints().get(indexxi));
		Rectangle2D tempBox = new Rectangle(pr);

		// counts of points in the segments
		// logger.info(" i "+indexxi+" j ="+indexxj);
		// firstly get the first point in curve.
		for (int i = indexxi; i < indexxj; i++) {

			pointk = (PointData) problemStroke.getPoints().get(i);
			tempBox.add(pointk);
			xi = pointk.getX();
			yi = pointk.getY();
			tempx += xi;
			tempy += yi;
			tempxy += xi * yi;
			tempx2 += xi * xi;
			tempy2 += yi * yi;
			tempx2y += (xi * xi) * yi;
			tempxy2 += xi * (yi * yi);
			tempx3 += xi * xi * xi;
			tempy3 += yi * yi * yi;

		}

		// ///////////// now this is the formal of the line
		double term1, term2, term3;
		term1 = N * tempxy - tempx * tempy;
		term2 = N * tempx2 - (tempx * tempx);
		term3 = tempx2 * tempy - tempx * tempxy;
		// logger.info("N "+ N);
		// logger.info("term1 "+ term1 +" term2 "+term2+"term3 "+term3);
		k = term1 / term2;
		c = term3 / term2;
		// logger.info("K "+k+" c "+c);

		// / now find the circular arc assitiomation
		double term4, term5;
		double a11, a12, a21, a22, b1, b2, a, b, R;
		a11 = 2.0 * (tempx * tempx - (N * tempx2));

		a12 = a21 = 2.0 * (tempx * tempy - (N * tempxy));
		a22 = 2.0 * (tempy * tempy - N * tempy2);

		b1 = tempx2 * tempx - N * tempx3 + tempx * tempy2 - N * tempxy2;
		b2 = tempx2 * tempy - N * tempy3 + tempy * tempy2 - N * tempx2y;

		// if (a12==0){
		// logger.info("x "+tempx+" y "+tempy + " xy "+ tempxy +" y2" +
		// tempy2);
		// logger.info(" tempx*tempy "+(tempx*tempy));
		// logger.info("N*tempxy "+(N*tempxy));
		// logger.info("a11 " +a11+" a12 "+a12 +" a22 "+ a22);
		// logger.info("b1 "+b1+" b2 "+b2);
		//	
		// }

		double delta = 0;

		delta = a11 * a22 - a21 * a12;
		if (delta==0||Double.isNaN(delta)){
			delta=SystemSettings.ZERO;
		}
		
		a = (b1 * a22 - b2 * a12) / delta;
		b = (b2 * a11 - b1 * a21) / delta;
		// if (a12==0){
		// logger.info("delta "+delta+" a "+a+" b "+b);
		// }

		double tempxa = 0, tempyb = 0;
		for (int i = indexxi; i < indexxj; i++) {

			pointk = (PointData) problemStroke.getPoints().get(i);
			xi = pointk.getX();
			yi = pointk.getY();
			tempxa += xi * a;
			tempyb += yi * b;

		}
		double term6 = -2.0 * tempxa;
		double term7, term8;
		term7 = -2.0 * tempyb;
		term8 = N * a * a + N * b * b + tempx2 + tempy2;

		R = Math.sqrt((term6 + term7 + term8) * (1.0 / N));
		// now get the ds and dc
		double ds = 0, dc = 0;
		double ds1 = 0, dsterm, dc1 = 0;
		dsterm = N * Math.sqrt(k * k + 1.0);
		double sqrttemp = 0;
		for (int i = indexxi; i < indexxj; i++) {

			pointk = (PointData) problemStroke.getPoints().get(i);
			xi = pointk.getX();
			yi = pointk.getY();
			ds1 += Math.abs((k * xi + c) - yi);
			sqrttemp = ((xi - a) * (xi - a)) + ((yi - b) * (yi - b));
			dc1 += Math.abs(Math.sqrt(sqrttemp) - R);
		}

	//	ds = ds1 / dsterm;
		// logger.info("dsterm "+dsterm);
		// logger.info("N "+ N);
		dc = dc1 / N;
//		int type = 0;
//		double error = 0;
	// 
			 
		Circle	segment = new Circle();
				segment.type = SystemSettings.SEGMENT_CIRCLE;
				segment.setCircleParams(R, a, b,
						(PointData) problemStroke.getPoints().get(indexxi),
						(PointData) problemStroke.getPoints().get(indexxj),
						tempBox);
				segment.setStartPoint( problemStroke.getPoints().get(indexxi));
				segment.setEndPoint(problemStroke.getPoints().get(indexxj));
	 
				segment.setErrorComputed(dc);
			//	this.SegmentsType.add(segment);
				
				return segment;
		 
		}
	
	private double getTypeMultiPrimitive(int indexxi, int indexxj,boolean store){
		int N = 0;
	  	//TODO: IMPLEMENT THIS FUNCTION 28 JAN
		 logger.info(" getTypeMultiPrimitive	//TODO: IMPLEMENT THIS FUNCTION 28 JAN  ");
		// get number of points in this segments 
	//	N = indexxj - indexxi;
        // get the sub stroke of this segment 
		Point pr = new Point();
		pr.setLocation(problemStroke.getPoints().get(indexxi));
	//	Rectangle2D tempBox = new Rectangle(pr);
		// run the line test 
		
		// run the curve test 
		
		
		// run the spiral test. 
		
		// run the polygon test. 
		
		// run helix test. 
		
		// run complete circcle test. 
		
		// run rectangle test. 
		
		// run wave test ...
		
		
		// add segment to list of segments...
		
		
		//return error ... 
		return 0;
	}
	private double getType(int indexxi, int indexxj,boolean store) {
		// logger.info("------------------------get type of
		// index-------------------");
		PointData pointk;
		double k, c, tempxy, tempx, tempy, tempx2, tempy2 = 0, tempx2y, tempxy2;
		tempxy = tempx = tempy = tempx2 = tempx2y = tempxy2 = 0.0;
		double tempx3 = 0, tempy3 = 0;

		double xi, yi;

		int N = 0;
		N = indexxj - indexxi;

		Point pr = new Point();
		pr.setLocation(problemStroke.getPoints().get(indexxi));
		Rectangle2D tempBox = new Rectangle(pr);

		// counts of points in the segments
		// logger.info(" i "+indexxi+" j ="+indexxj);
		// firstly get the first point in curve.
		for (int i = indexxi; i < indexxj; i++) {

			pointk = (PointData) problemStroke.getPoints().get(i);
			tempBox.add(pointk);
			xi = pointk.getX();
			yi = pointk.getY();
			tempx += xi;
			tempy += yi;
			tempxy += xi * yi;
			tempx2 += xi * xi;
			tempy2 += yi * yi;
			tempx2y += (xi * xi) * yi;
			tempxy2 += xi * (yi * yi);
			tempx3 += xi * xi * xi;
			tempy3 += yi * yi * yi;

		}

		// ///////////// now this is the formal of the line
		double term1, term2, term3;
		term1 = N * tempxy - tempx * tempy;
		term2 = N * tempx2 - (tempx * tempx);
		term3 = tempx2 * tempy - tempx * tempxy;
		// logger.info("N "+ N);
		// logger.info("term1 "+ term1 +" term2 "+term2+"term3 "+term3);
		k = term1 / term2;
		c = term3 / term2;
		// logger.info("K "+k+" c "+c);

		// / now find the circular arc assitiomation
		//double term4, term5;
		double a11, a12, a21, a22, b1, b2, a, b, R;
		a11 = 2.0 * (tempx * tempx - (N * tempx2));

		a12 = a21 = 2.0 * (tempx * tempy - (N * tempxy));
		a22 = 2.0 * (tempy * tempy - N * tempy2);

		b1 = tempx2 * tempx - N * tempx3 + tempx * tempy2 - N * tempxy2;
		b2 = tempx2 * tempy - N * tempy3 + tempy * tempy2 - N * tempx2y;

//		 if (a12==0){
//		 logger.info("x "+tempx+" y "+tempy + " xy "+ tempxy +" y2" +
//		 tempy2);
//		 logger.info(" tempx*tempy "+(tempx*tempy));
//		 logger.info("N*tempxy "+(N*tempxy));
//		 logger.info("a11 " +a11+" a12 "+a12 +" a22 "+ a22);
//		 logger.info("b1 "+b1+" b2 "+b2);
//			
//		 }

		double delta = 0;

		delta = a11 * a22 - a21 * a12;
		if (delta==0||Double.isNaN(delta)){
			
			
			
				
			delta=SystemSettings.ZERO;
			
			a = (b1 * a22 - b2 * a12) / delta;
			b = (b2 * a11 - b1 * a21) / delta;
//			if (Double.isNaN(a)||Double.isNaN(b)){
//			 logger.info("x "+tempx+" y "+tempy + " xy "+ tempxy +" y2" +
//					 tempy2);
//					 logger.info(" tempx*tempy "+(tempx*tempy));
//					 logger.info("N*tempxy "+(N*tempxy));
//					 logger.info("a11 " +a11+" a12 "+a12 +" a22 "+ a22);
//					 logger.info("b1 "+b1+" b2 "+b2);
//			logger.info("   now delta is  "+delta+"   a = "+a+"   b= "+b);
//			
//			 logger.info("---------------------------------------");
//			}
			//delta=1;
		}
		else {
		a = (b1 * a22 - b2 * a12) / delta;
		b = (b2 * a11 - b1 * a21) / delta;
		}
		// if (a12==0){
		// logger.info("delta "+delta+" a "+a+" b "+b);
		// }

		double tempxa = 0, tempyb = 0;
		for (int i = indexxi; i < indexxj; i++) {

			pointk = (PointData) problemStroke.getPoints().get(i);
			xi = pointk.getX();
			yi = pointk.getY();
			tempxa += xi * a;
			tempyb += yi * b;

		}
		double term6 = -2.0 * tempxa;
		double term7, term8;
		term7 = -2.0 * tempyb;
		term8 = N * a * a + N * b * b + tempx2 + tempy2;

		R = Math.sqrt((term6 + term7 + term8) * (1.0 / N));
		// now get the ds and dc
		double ds = 0, dc = 0;
		double ds1 = 0, dsterm, dc1 = 0;
		dsterm = N * Math.sqrt(k * k + 1.0);
		double sqrttemp = 0;
		for (int i = indexxi; i < indexxj; i++) {

			pointk = (PointData) problemStroke.getPoints().get(i);
			xi = pointk.getX();
			yi = pointk.getY();
			ds1 += Math.abs((k * xi + c) - yi);
			sqrttemp = ((xi - a) * (xi - a)) + ((yi - b) * (yi - b));
			dc1 += Math.abs(Math.sqrt(sqrttemp) - R);
		}

		ds = ds1 / dsterm;
		// logger.info("dsterm "+dsterm);
		// logger.info("N "+ N);
		dc = dc1 / N;
		int type = 0;
		double error = 0;
	// 
		
		
		
	
			GeometricPrimitive segment = null;
			
			int typeCircle=GetSegmentMinError(dc,ds);
			
			if (typeCircle==1){
				error = dc;
				
			}
			else if (typeCircle==0){
				error=ds;
			}
			else {
			//	logger.error(" error in the  segmenation error they are both are nan... ");
				//error=MaxSegmentERROR;
				error=0.0;
			}
			
			
			if (store){
			if (typeCircle==1){
				segment = new Circle();
				((Circle) segment).type = SystemSettings.SEGMENT_CIRCLE;
				((Circle) segment).setCircleParams(R, a, b,
						(PointData) problemStroke.getPoints().get(indexxi),
						(PointData) problemStroke.getPoints().get(indexxj),
						tempBox);
				segment.setStartPoint( problemStroke.getPoints().get(indexxi));
				segment.setEndPoint(problemStroke.getPoints().get(indexxj));
				error = dc;
				
				
			}
			else {
				
				if (!Double.isNaN(ds))
				error = ds;
				else 
					error =0;
				segment = new Line(k, c,
						(PointData) problemStroke.getPoints().get(indexxi),
						(PointData) problemStroke.getPoints().get(indexxj));
				((Line) segment).type = SystemSettings.SEGMENT_LINE;
			 
				((Line) segment).setIStart(indexxi);
				((Line) segment).setIEnd(indexxj);
				segment.setStartPoint( problemStroke.getPoints().get(indexxi));
				segment.setEndPoint(problemStroke.getPoints().get(indexxj));
			}
		 
				this.SegmentsType.add(segment);
		 
		}
	
		return error;
	}
	
	private int GetSegmentMinError(double dc,double ds){
		

		// store shape only at the last calcuation when solution found // at
		// the end
		GeometricPrimitive segment = null;
		if ((Double.isNaN(ds)) || (Double.isNaN(dc))
				|| (Double.isInfinite(ds)) || (Double.isInfinite(dc))) {
			// logger.info("one of dc or dce is nan or infinty ");
//			if (logger.isDebugEnabled()) {
//				 logger.debug("getType(int, int) -   ds   " + ds); //$NON-NLS-1$
//			
//				  logger.debug("getType(int, int) -   dc  " + dc); //$NON-NLS-1$
//			}

			if ((Double.isNaN(ds)) && !Double.isNaN(dc)) {
			return 0;//curve 
				// logger.info("type circe with error "+error);
			} else if (Double.isNaN(dc) && !Double.isNaN(ds)) {
			 return 0;
				
				// logger.info("type line with error "+error + "
				// where is circle is "+dc);
			} else if (Double.isNaN(dc) && Double.isNaN(ds)) {
				// error = Double.MAX_VALUE;
				//if (logger.isDebugEnabled()) {
				// logger.debug("getType(int, int) -    both are nans "); //$NON-NLS-1$
				 return -1;
				//}
			} else {
				//if (logger.isDebugEnabled()) {
				//  logger.debug("getType(int, int) - other must not be ever the case !!!! "); //$NON-NLS-1$
				  return 0;
				//}
			}

		} else {
			// logger.info(" noo nans or infinithy ");
			 //logger.info("ds "+ds);
			// logger.info("dc * 3 "+dc*3.0);

			if ((dc*2.5)< ds) {// the dc is the min then it is more cirlce than
							// line
				return 1;//curve 
				 
				// logger.info("type circe with error "+error);
			} else  // (dc>ds)
			{
				return 0;// line 
			 
			 
			}
		}

	
	}
	
	private double GetMinErrorSegments(double dc,double ds , double a,double b, double R, int indexxi,int indexxj,	Rectangle2D tempBox) {
//		double error = 0;
//		if (storeShape) {
//			// store shape only at the last calcuation when solution found // at
//			// the end
//			GeometricPrimitive segment = null;
//			if ((Double.isNaN(ds)) || (Double.isNaN(dc))
//					|| (Double.isInfinite(ds)) || (Double.isInfinite(dc))) {
//				// logger.info("one of dc or dce is nan or infinty ");
//				if (logger.isDebugEnabled()) {
//					 logger.debug("getType(int, int) -   ds   " + ds); //$NON-NLS-1$
//				
//					  logger.debug("getType(int, int) -   dc  " + dc); //$NON-NLS-1$
//				}
//
//				if ((Double.isNaN(ds)) && !Double.isNaN(dc)) {
//					segment = new Curve();
//					((Curve) segment).type = SystemSettings.SEGMENT_CIRCLE;
//					((Curve) segment).setCircleParams(R, a, b,
//							(PointData) problemStroke.getPoints().get(indexxi),
//							(PointData) problemStroke.getPoints().get(indexxj),
//							tempBox);
//					error = dc;
//					// logger.info("type circe with error "+error);
//				} else if (Double.isNaN(dc) && !Double.isNaN(ds)) {
//					error = ds;
//					segment = new Line();
//					((Line) segment).type = SystemSettings.SEGMENT_LINE;
//					((Line) segment).setLineParams(k, c,
//							(PointData) problemStroke.getPoints().get(indexxi),
//							(PointData) problemStroke.getPoints().get(indexxj));
//					((Line) segment).setIStart(indexxi);
//					((Line) segment).setIEnd(indexxj);
//					
//					// logger.info("type line with error "+error + "
//					// where is circle is "+dc);
//				} else if (Double.isNaN(dc) && Double.isNaN(ds)) {
//					// error = Double.MAX_VALUE;
//					if (logger.isDebugEnabled()) {
//						//  logger.debug("getType(int, int) -    both are nans "); //$NON-NLS-1$
//					}
//				} else {
//					if (logger.isDebugEnabled()) {
//						//  logger.debug("getType(int, int) - other "); //$NON-NLS-1$
//					}
//				}
//
//			} else {
//				// logger.info(" noo nans or infinithy ");
//				// logger.info("ds"+ds);
//				// logger.info("dc "+dc);
//
//				if (ds > dc) {// the dc is the min then it is more cirlce than
//								// line
//
//					segment = new Curve();
//					((Curve) segment).type = SystemSettings.SEGMENT_CIRCLE;
//
//					((Curve) segment).setCircleParams(R, a, b,
//							(PointData) problemStroke.getPoints().get(indexxi),
//							(PointData) problemStroke.getPoints().get(indexxj),
//							tempBox);
//					error = dc;
//					// logger.info("type circe with error "+error);
//				} else // (dc>ds)
//				{
//					error = ds;
//					segment = new Line();
//					((Line) segment).type = SystemSettings.SEGMENT_LINE;
//					((Line) segment).setLineParams(k, c,
//							(PointData) problemStroke.getPoints().get(indexxi),
//							(PointData) problemStroke.getPoints().get(indexxj));
//					((Line) segment).setIStart(indexxi);
//					((Line) segment).setIEnd(indexxj);
//					// logger.info("type line with error "+error+ " where
//					// is circle is "+dc);
//				}
//			}
//
//			if (this.SegmentsType != null && segment != null) {
//				this.SegmentsType.add(segment);
//			} else {
//				// let the segmet be line form xi to xj with bold slope
//				segment = new Line();
//				((Line) segment).type = SystemSettings.SEGMENT_LINE;
//				// get slope between xi and xj
//
//				((Line) segment).setLineParams(k, c, (PointData) problemStroke
//						.getPoints().get(indexxi), (PointData) problemStroke
//						.getPoints().get(indexxj));
//                        
//				((Line) segment).setIStart(indexxi);
//				((Line) segment).setIEnd(indexxj);
//				if (logger.isDebugEnabled()) {
//					//  logger.debug("getType(int, int) -   No segment to add   " + indexxi + "  to   " + indexxj); //$NON-NLS-1$ //$NON-NLS-2$
//				}
//			}
//
//		} /// store shape  
//		
//		
//		else { // / while calculating the particles
//
//			// logger.info("++++++++++++++++++++++++++++++++++++++++");
//			if ((Double.isNaN(ds)) || (Double.isNaN(dc))
//					|| (Double.isInfinite(ds)) || (Double.isInfinite(dc))) {
//
//				if ((Double.isNaN(ds)) && !Double.isNaN(dc)) {
//					error = dc;
//					// logger.info("type circe with error "+error);
//				}
//				if (Double.isNaN(dc) && !Double.isNaN(ds)) {
//					error = ds;
//					// logger.info("type line with error "+error + "
//					// where is circle is "+dc);
//				}
//				if (Double.isNaN(dc) && Double.isNaN(ds)) {
//					error = Double.MAX_VALUE;
//					// logger.info("both are nans ");
//				}
//
//			} else {
//				if (ds > dc) {// the dc is the min then it is more cirlce than
//								// line
//					error = dc;
//				logger.debug("type circe with error "+error);
//				} else // (dc>ds)
//				{
//					error = ds;
//				 logger.debug("type line with error "+error+ " where is circle is "+dc);
//				}
//			}
//
//			// logger.info(" Error "+error);
//
//		}// do not store 
//           return error;
		return 0;
	} 

	public void setStoreShape(boolean storeShape) {
		this.storeShape = storeShape;
	}

	public Segment getSegmentOfIndex(int index) {

		Segment temp = super.getSegmentOfIndex(index);

		if (SegmentsType != null)
			// Segpoints.add();
		{
			if (temp!=null)
			temp.setSegmentType(SegmentsType.get(index));
		}

		return temp;
	}

	public int getSegmentsCount() {
		
		if (SegmentsType != null)
			return SegmentsType.size();
		else
			return super.getSegmentsCount();
	}
	
	
	
    private void correctParticles() {
    	
    	if (SegmentsType!=null){
			
    	logger.trace( "     correcting particles.............."+SegmentsType.size());
		for (int i = 0; i < particlePoints.length; i++) {
			particlePoints[i]=0;
		}
	//	ArrayList< PointData>  points=this.problemStroke.getPoints();
	//	int j=0;
		if (SegmentsType.size()<2){
			if (particlePoints.length>0){
			 particlePoints[0]=1;
			
			particlePoints[particlePoints.length-1]=1;
			}
		}
		else {
			 for (int j2 = 0; j2 < SegmentsType.size(); j2++) {
				
				 particlePoints[SegmentsType.get(j2).getIStart()]=1;
				 particlePoints[SegmentsType.get(j2).getIEnd()]=1;
				 
			}
				if (particlePoints.length>0){
					 particlePoints[0]=1;
					
					particlePoints[particlePoints.length-1]=1;
					}
				// now i reached the point in polygon. 
				
			 
		 
	
		}
    	}
		//regenerate the polygon vertices. 
    this.calculateSolutionParameters();
		
	}
	public String getSegmentsString() {
		StringBuilder s=new StringBuilder();
		 s.append(" Segments = <[< ");
			for (int k = 0; k <this.SegmentsType.size()-1; k++) {
				
				GeometricPrimitive T1 = this.SegmentsType.get(k);
				PointData p1 =  (PointData)T1.getStartPoint();
				PointData  p2= (PointData) T1.getEndPoint();
				if (T1 instanceof Line) {
					 //Curve t1 = ( Curve) t1;
				 s.append("  S" ).append(k).append(
						" Line{P(").append(p1.x).append(",").append(p1.y).append(") to P(")
						.append(p2.x).append(",").append(p2.y).append(")},    ");
				}
				else {
					
					if (T1 instanceof Circle) {
						Circle c = (Circle) T1;
						s.append("  S").append(k+" Curve{P(").append(p1.x).append(",").append(p1.y).append(") to P(").
						append(p2.x).append(",").append(p2.y).append("), ").append( c ).append(" },");
				
					}
					
						
				}
				
			}
//		 for (int k = 0; k <this.polygonVertices.size()-2; k++) {
//			 
//				PointData p1 =  (PointData) polygonVertices.get(k);
//				PointData  p2= (PointData) polygonVertices.get(k+1);
//		s+=" Segment L("+k+")[("+p1.x+","+p1.y+") to ("	+p2.x+","+p2.y+"),      ";
//				// get segments types 
//				
//			
//		 }
		 s.append(">]>  ");
		 
			return s.toString();
		}
	public void CheckLineSlopes() {
		logger.trace(" in check for line slope...... ");
	    // check all segments of this solution
		
		if (SegmentsType!=null){
			
			boolean MergeFound=false;
			
			do {
				
				logger.trace( "   inside the slop with sizes = "+SegmentsType.size());
				MergeFound=false;
//			logger.info(" size of the segments type is     SSSSSSSSSSSSSSSSSSSSs     "+this.SegmentsType.size());
		for (int k = 0; k <this.SegmentsType.size()-1; k++) {
		
		// get segments types 
				GeometricPrimitive T1 = this.SegmentsType.get(k);
				GeometricPrimitive  T2= this.SegmentsType.get(k+1);
				
				
				
				if (isMergableCurves(T1,T2)){
					
					logger.trace( " removing curve "+k+1);
					MergeFound=true;
					// these circles ARE TO BE MERGED	
					int i=T1.getIStart();
					int j=T2.getIEnd();
					Circle segment = getCircleParameters(i,j);
					segment.setErrorComputed( getSegmentDistanceError(segment,i,j));
					 
					
					//this.particlePoints[T1.getIEnd()]=0;
					//this.particlePoints[T2.getIStart()]=0;
					
					SegmentsType.set(k, segment);
					SegmentsType.remove(k+1);
					//k--;
				}
				
				else if (isMergableLines(T1, T2))
				{
					
				//	logger.info( " removingline  "+k+1);
					MergeFound=true;
					// these lines will be merged 
					
					int i=T1.getIStart();
					int j=T2.getIEnd();
			 Line temp=new Line(problemStroke.getPoint(i), problemStroke
							.getPoint(j));
					temp.setIStart(i);
					temp.setIEnd(j);
					temp.setErrorComputed( getXiXjError(i, j, problemStroke.getPoint(i), problemStroke
							.getPoint(j)));
					
//					this.particlePoints[T1.getIEnd()]=0;
//					this.particlePoints[T2.getIStart()]=0;
					
					SegmentsType.set(k, temp);
					SegmentsType.remove(k+1);
					//k--;
					
					
				}
			
		}
			}while (MergeFound);
		
			
			correctParticles();
		//this.calculateSolutionParameters();
		
		}
	//	this.calculateSolutionParameters();
		//logger.info(" size of the segments type  AFter  is     SSSSSSSSSSSSSSSSSSSSs     "+this.SegmentsType.size());
		
	}
	private boolean isMergableCurves(GeometricPrimitive t1,
			GeometricPrimitive t2) {
		if (t1 instanceof Circle) {
			 //Curve t1 = ( Curve) t1;
			if (t2 instanceof  Circle) {
				// Curve t2 = ( Curve) t2;
	
		
		Circle Seg1 = getCircleParameters(t1.getIStart(),t1.getIEnd());
		Circle Seg2=getCircleParameters(t1.getIStart(),t1.getIEnd());
		Circle newSeg = getCircleParameters(t1.getIStart(),t2.getIEnd());
		double e1,e2;
		e1=Seg1.getErrorComputed();
		e2=Seg2.getErrorComputed();
		boolean distanceError=false;
//		double distanceBetweenCenters=Seg1.getCenterPoint().distance(Seg2.getCenterPoint());
//		double 
//		
//		if (distanceError){
//			return true;
//		}
		
		if (newSeg.getErrorComputed()<=(e1+e2)){
		//	logger.info( " the error of curves is "+ newSeg.getErrorComputed()+" and e1=  "+e1+" e2 "+e2);
			
		  return true;
		}
			}
		}
	 //logger.info( "return ing errrrrrrrrrrror false..........");
		return false;
	}

	public double getComparableError(){
		this.storeShape=true;
		return TotalErrorCompare() ;
		 
	}
	public double TotalErrorCompare() {
		this.SegmentsType=new ArrayList<GeometricPrimitive>();
		double error = 0.0;
		double temperror;
		int currentType;
		Point2D pi, pj;
		int vj = 0;
		int v1index = 0, v2index = 0, pjindex = 0, vcount = 0;

		for (int i = 0; i < polygonVertices.size() - 1; i++) {
			// now i
			pi = polygonVertices.get(i);
			pj = polygonVertices.get(i + 1);

			vcount = 0;
			// get the index of the consective vertics and send it to the error
			// segment.
			for (int j = v2index; j < particlePoints.length; j++) {

				if (particlePoints[j] == 1) {

					// this is a vertix
					if (vcount == 0)// check if first vertix
					{
						// logger.info("found first vertix "+j);
						v1index = j; // vertix is the index
						vcount++; // add vertix count

					} else if (vcount == 1) // if second vertix make set xj
											// vertix
					{

						v2index = j;// set the second vertix
						// now call error and add it to the current error
						temperror = getSegmentError(v1index, v2index,true);
						
					 // get the error of the type of the final segments 
						//  
						
						boolean lineSegment,circleSegment;
						
						double ErrorSegment=0;
						
						GeometricPrimitive CurrentSegment = SegmentsType.get(SegmentsType.size()-1);
						
					
						
						ErrorSegment=getSegmentDistanceError(CurrentSegment,v1index,v2index); 
						//double ErrorLine=getXiXjError(v1index,v2index, CurrentSegment.getStartPoint(), CurrentSegment.getEndPoint());
						
						
						
							//logger.debug(" if line then "+ErrorLine+" this segment is a "+CurrentSegment+"   with error "+ErrorSegment);
							
						
						
						error += ErrorSegment;
						break;
					}
				}

			}
		}

		// logger.info("------------------------------Error---------
		// -------------------");

		return Math.sqrt(error);

	}
	
	
	
	
	private double getSegmentDistanceError(GeometricPrimitive currentSegment,
			int vi, int vj) {
	
		
		double error=0;
		for (int i = vi ; i <= vj; i++) {
			
			error+=currentSegment.DifferanceFromPoint(problemStroke.getPoint(i));
		}
	 
		return error*error;
	}
	
	

	public double errorWithStore() {
		this.SegmentsType=new ArrayList<GeometricPrimitive>();
		double error = 0.0;
		double temperror;
//		int currentType;
		Point2D pi, pj;
//		int vj = 0;
		int v1index = 0, v2index = 0; 
		//pjindex = 0,
		int  vcount = 0;

		for (int i = 0; i < polygonVertices.size() - 1; i++) {
			// now i
			pi = polygonVertices.get(i);
			pj = polygonVertices.get(i + 1);

			vcount = 0;
			// get the index of the consective vertics and send it to the error
			// segment.
			for (int j = v2index; j < particlePoints.length; j++) {

				if (particlePoints[j] == 1) {

					// this is a vertix
					if (vcount == 0)// check if first vertix
					{
						// logger.info("found first vertix "+j);
						v1index = j; // vertix is the index
						vcount++; // add vertix count

					} else if (vcount == 1) // if second vertix make set xj
											// vertix
					{

						v2index = j;// set the second vertix
						// now call error and add it to the current error
						// currentType=getSegmentType(v1index, v2index, pi, pj);
						temperror = getSegmentError(v1index, v2index,true);
                        
						error += temperror;
						break;
					}
				}

			}
		}

		// logger.info("------------------------------Error---------
		// -------------------");

		return error;
	}

	
}
