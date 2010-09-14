package SketchMaster.swarm.polygonApproximations;

import org.apache.log4j.Logger;

import SketchMaster.system.SystemSettings;
import SketchMaster.Stroke.StrokeData.DominatePointStructure;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Segment;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.shapes.GeometricPrimitive;
//import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.Stroke.graphics.shapes.SegmentedShape;
import SketchMaster.collection.SortedValueMap;
import SketchMaster.gui.DrawingDebugUtils;
//import SketchMaster.io.log.FileLog;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.swarm.Solution;
import SketchMaster.swarm.SwarmSystem;
//import SketchMaster.swarm.curvefit.StrokeCurveSolution;
//import SketchMaster.system.SystemSettings;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
//import java.util.Random;

/**
 * @author maha It representa solution location in the search space for the
 *         agents in the problem of the polygon approximation
 */
public class PolygonSolution extends Solution implements SegmentedShape {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PolygonSolution.class);

	public final static int POLYGON_ADJUST_MERGE = 1;
	public final static int POLYGON_ADJUST_DOMINATE_POINT = 2;
	public final static int POLYGON_ADJUST_BOTH = 3;
	public static final int POLYGON_ADJUST_NONE = 4;
	public static final int POLYGON_ADJUST_ONLY_SEGMENTS = 5;
	private static int minSegment = 20;
    protected static boolean debugS=SwarmSystem.DEBUG_SWARM;
	final static double T = SystemSettings.SOLUTION_INITIAL_TOLERANCE;
     Color  SolutionColor=Color.getHSBColor(.5f, 0.1f, 0.5f);
	 double  colorValue=0.1;
 
	 
	 
	public static double eTolerance = SystemSettings.SOLUTION_AlgS1_ERROR_TOLERANCE;

	@Override
	public double eval() {
		double func = 0.0;
		double error = error();
		// logger.info("Error = "+ error);

		if (error > eTolerance) {

			func = -(error / (eTolerance * particlePoints.length));
			// logger.info("size="+polygonVertices.size()+"-ve eroror "+
			// error+" func"+func);
		} else {
			
			//DominateCount  i need to maximize 
			//DominateCount
		//DominateCount
			func = ((double)DominateCount / (double) polygonVertices.size());
			
			// func=particlePoints.length-(double)polygonVertices.size();
			//func = ((double)1.0 / (double) polygonVertices.size());
			// logger.info(" func"+func+" plygon size
			// "+polygonVertices.size());
			// logger.info("func "+func);
		}

		return func;
	}

	/**
	 * the stroke the agents are segmenting and getting the shapes.
	 * 
	 * @uml.property name="ProblemStroke"
	 * @uml.associationEnd multiplicity="(1 1)" aggregation="shared"
	 *                     inverse="polygonSolution:SketchMaster.Stroke.StrokeData.Stroke"
	 * @uml.association name="strokeProblem"
	 */
	protected Stroke problemStroke = null;

	/**
	 * Getter of the property <tt>ProblemStroke</tt>
	 * 
	 * @return Returns the problemStroke.
	 * @uml.property name="ProblemStroke"
	 */
	public Stroke getProblemStroke() {
		return problemStroke;
	}

	/**
	 * Setter of the property <tt>ProblemStroke</tt>
	 * 
	 * @param ProblemStroke
	 *            The problemStroke to set.
	 * @uml.property name="ProblemStroke"
	 */
	public void setProblemStroke(Stroke problemStroke) {
		this.problemStroke = problemStroke;
	}

	/**
	 * A list of the final vertices of the solution [only the vertics not all
	 * the point of stroke.]
	 * 
	 * @uml.property name="polygonVertices"
	 */
	protected ArrayList<Point2D> polygonVertices;

	/**
	 * Getter of the property <tt>polygonVertices</tt>
	 * 
	 * @return Returns the polygonVertices.
	 * @uml.property name="polygonVertices"
	 */
	public ArrayList<Point2D> getPolygonVertices() {
		return polygonVertices;
	}

	/**
	 * calculate and return the nubmer of vertices that is currently in the
	 * solution array.
	 */
	private int getNumberOfVertices() {
		// int count=0;
		// for (int i = 0; i < particlePoints.length; i++) {
		// if(particlePoints[i]==1)
		// {
		// count++;
		// }
		// }
		if (polygonVertices != null)
			return polygonVertices.size();
		else
			return 0;

	}

	/**
	 * @uml.property name="ParticlePoints" multiplicity="(0 -1)" dimension="1"
	 */
	protected int[] particlePoints;

	private int[] particleDominate;

	private int DominateCount;

	/**
	 * Getter of the property <tt>ParticlePoints</tt>
	 * 
	 * @return Returns the particlePoints.
	 * @uml.property name="ParticlePoints"
	 */
	public int[] getParticlePoints() {
		return particlePoints;
	}

	/**
	 * Setter of the property <tt>ParticlePoints</tt>
	 * 
	 * @param ParticlePoints
	 *            The particlePoints to set.
	 * @uml.property name="ParticlePoints"
	 */
	public void setParticlePoints(int[] particlePoints) {
		this.particlePoints = particlePoints;
	}

	/**
	 * Am empty constructor
	 */
	public PolygonSolution() {
	}

	/**
	 * Constructure create a random solution
	 */
	public PolygonSolution(int size) {
		super(size);
		particlePoints = new int[size];
		particleDominate = new int[size];

	 
		// now itialize the values with random parameter

		for (int i = 0; i < particlePoints.length; i++) {
			if (r1Random.nextDouble() > T)
				particlePoints[i] = 1;
			else
				particlePoints[i] = 0;
			
			
			particleDominate[i]=0;
		if (i==0)
		{
					particlePoints[0] = 1;
					particleDominate[0]=1;
					
		}
		if (i==particlePoints.length - 1)
		{
					particlePoints[particlePoints.length - 1] = 1;
					particleDominate[particlePoints.length - 1]=1;
		}

		}
		
		
	}

	/**
	 * Constructure create a random solution
	 */
	public PolygonSolution(Stroke problemStroke) {
		this(problemStroke.getPointsCount());
		this.problemStroke = problemStroke;
		setStrokeBasedTolerance();
		calculateSolutionParameters();
	}

	static boolean firstime = true;

	protected void setStrokeBasedTolerance() {

//		minSegment = (int) ((5.0 / 100.0) * (problemStroke.getPoints().size()));
//      logger.info( " min segment based on the stroke "+minSegment);
      
  	minSegment = (int) ((5.0 / 100.0) * (problemStroke.getLength()));
    //logger.info( " min segment based on the stroke "+minSegment);
      
        if (minSegment<  SystemSettings.MinSegmentCountDefault){
        	 minSegment=  SystemSettings.MinSegmentCountDefault;
         }
//		Rectangle2D temp = this.problemStroke.getStatisticalInfo().getBox();
//		double area ;
//		if (temp!=null)
//			area = temp.getWidth() * temp.getHeight();
//		else 
//			area=1;
		// logger.info("area is ="+area);
		// this.eTolerance=((area*0.125)+(problemStroke.getStatisticalInfo().getArea()*0.125))*0.5;

//		double strokearea = Math.abs(problemStroke.getStatisticalInfo()
//				.getArea());
		//1
	
//		if (strokearea < (SystemSettings.SOLUTION_ERROR_TOLERANCE*10)) {
//			this.eTolerance = 0.1 * strokearea;
//			//this.eTolerance =1e2;
//
//		}
//
//		else {
			// if (strokearea>1e4)
			this.eTolerance =SystemSettings.SOLUTION_AlgS1_ERROR_TOLERANCE;
//			if(strokearea <1e5){
//			//	this.eTolerance =1e3;
//				this.eTolerance = strokearea*0.05;
//			}
//			else{ 
//				this.eTolerance =SystemSettings.SOLUTION_ERROR_TOLERANCE;
////				if (strokearea<1e6){
////					this.eTolerance =1e4;
////				}
////				else {
////					this.eTolerance =1e5;
////				}				
//			}
		//	this.eTolerance = area*0.1;
//		}
		// logger.info("storke area "+strokearea +" (
		// "+this.getClass().getSimpleName()+ " 204 )");
		// logger.info("e tolerance is "+this.eTolerance+" (
		// "+this.getClass().getSimpleName()+ " 205 )");
		// if (eTolerance>5e3)
		// eTolerance=5e3;
		// this.eTolerance=((area*0.25)+(problemStroke.getStatisticalInfo().getArea()*0.25))*0.5;
		// this.eTolerance=(area*this.problemStroke.getPointsCount())*1e-5;
		if (firstime) {
			
//			if (strokearea < 1e4) {
//		
//				logger.info("11111111111111111111111111111111"+" (" + this.getClass().getSimpleName()
//						+ "    "
//						+ (new Throwable()).getStackTrace()[0].getLineNumber()
//						+ "  )  ");
//			}
//
//			else {// if (strokearea>1e4)
//			
//					if(strokearea <1e5){
//					//	this.eTolerance =5e3;
//						
//						logger.info("22222222222222222222222222222222222"+" (" + this.getClass().getSimpleName()
//								+ "    "
//								+ (new Throwable()).getStackTrace()[0].getLineNumber()
//								+ "  )  ");
//					}
//					else{ 
//						
//						if (strokearea<1e6){
//							
//							logger.info("33333333333333333333333333333333333333"+" (" + this.getClass().getSimpleName()
//									+ "    "
//									+ (new Throwable()).getStackTrace()[0].getLineNumber()
//									+ "  )  ");
//							//this.eTolerance =5e4;
//						}
//						else {
//							
//							logger.info("44444444444444444444444444444444444444"+" (" + this.getClass().getSimpleName()
//									+ "    "
//									+ (new Throwable()).getStackTrace()[0].getLineNumber()
//									+ "  )  ");
//							//this.eTolerance =5e5;
//						}
//						
//					}
//				//	this.eTolerance = area*0.1;
//			
//			}
			if (logger.isDebugEnabled()) {
				//  logger.debug("setStrokeBasedTolerance() - area of bounding box is " + area + "the area of the storke " + strokearea + "min segment = " + minSegment + " the error is == " + this.eTolerance + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			}
			// logger.info(((area*0.25)+(problemStroke.getStatisticalInfo().getArea()*0.25))*0.5);
//			  logger.trace("then etolerance will be " + eTolerance);
//			  logger.trace("area of the   stroke "	+ problemStroke.getStatisticalInfo().getArea());
//			  logger.trace("area is =" + area);
//			  logger.trace("number of ponts in stroke is "+ problemStroke.getPointsCount());
//			  logger.trace("----------------------------------------------------------------");
//			logger.info("storke are a    " + strokearea + "   (  "
	//				+ this.getClass().getSimpleName() + "   218  )");
//			   logger.info("minsegment "+minSegment +  " ("
//						+ this.getClass().getSimpleName()
//						+ "    "
//						+ (new Throwable()).getStackTrace()[0]
//								.getLineNumber() + "  )  ");
//			logger.info("etolerance   is " + this.eTolerance +
//                            " ("
//					+ this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0]
//							.getLineNumber() + "  )  ");
			firstime = false;
		}
	}

	/**
	 * after moving into new location the solution parameter and data must be
	 * adjusted to the new values
	 */
	public void calculateSolutionParameters() {

		PointData temp;
		DominateCount =0;
		// construct the vertices table from stroke and computer number of
		// vertices
		// initalize the vertices
		polygonVertices = new ArrayList<Point2D>();
		
//logger.info("createing a new vertices "+" (" + this.getClass().getSimpleName() + "    "
//		+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");
		for (int i = 0; i < particlePoints.length; i++) {
			
			if (particlePoints[i] == 1) {
				temp = (PointData) problemStroke.getPoints().get(i);
				polygonVertices.add(temp.getPointLocation());
				if (particleDominate!=null)
				DominateCount+=particleDominate[i];
			}
		}
		
         if (DominateCount==0)
        	 DominateCount=1;
	}

	private double getPolygongArea() {
		// // calculateSolutionParameters();
		// double area =0.0;
		// double temperror;
		// double a =0.0;
		//
		// Point2D pi,pj;
		// int vj=0;
		// int v1index=0, v2index=0,pjindex=0,vcount=0;
		//		  		
		// for (int i = 0; i < polygonVertices.size()-1; i++) {
		// //now i
		// pi=polygonVertices.get(i);
		// // {
		// pj=polygonVertices.get(i+1);
		// a = (pi.getX()*pj.getY())-(pj.getX()*pi.getY());
		// area+=a;
		//              
		//  		
		// }
		//  			 
		//  		
		//  		
		//  	
		// area =area/2.0;
		double area = ComputationsGeometry.computeArea(polygonVertices);

		// logger.info("------------------------------Error---------
		// -------------------");

		return area;
	}

	public double getErrorFromArea() {

		// try equlidaina distance
		double polyerr =Math.abs( getPolygongArea());
		
		
		double serror = Math.abs(problemStroke.getStatisticalInfo().getArea());
//		logger.info("polygon error " +polyerr+"    stroke area   "+serror+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");

		// logger.info("p="+polyerr);
		double error = 0.0;

		error = (serror - polyerr) * (serror - polyerr);

		return Math.sqrt(error);
	}

	public double errorMain() {
		double error = 0.0;
		double temperror;

		Point2D pi, pj;
		int vj = 0;
		int v1index = 0, v2index = 0, pjindex = 0, vcount = 0;
		// logger.info("------------------------------Error---------
		// -------------------");
		// logger.info(this.toString());
		// logger.info("nubmer of polygong in this
		// solution"+polygonVertices.size());
		for (int i = 0; i < polygonVertices.size() - 1; i++) {
			// now i
			pi = polygonVertices.get(i);

			// if ((i+1)==polygonVertices.size())
			// {
			// pj=polygonVertices.get(0);
			// pjindex=0;
			//	  				
			// }
			// else
			// {
			pj = polygonVertices.get(i + 1);
			// logger.info("In vertix "+i+" with error "+error);
			// pjindex=i+1;
			// }
			// initalize the vertix count
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
						// logger.info("found second vertix "+j);
						v2index = j;// set the second vertix
						// now call error and add it to the current error
						//
						// try with box
						// temperror=getXiXjBox(v1index, v2index, pi, pj);
						temperror = getXiXjError(v1index, v2index, pi, pj);
						// logger.info("error of segment is "+temperror);
						// logger.info("compuative errro ris "+error);
						// if
						// (Double.isNaN(temperror)||Double.isInfinite(temperror))
						// temperror=0.0;
						error += temperror;
						break;
					}
				}

			}
		}

		// logger.info("------------------------------Error---------
		// -------------------");

		return Math.sqrt(error);

	}

	@Override
	public double error() {
		firstime = true;
	   double  Error1= errorMain();
	    
	//	Error = getErrorFromArea();
          
//	  if (debugS)
//	  {
//		  logger.debug("------------Error  from area " +Error);
//		  logger.debug("----------Error from  long method "+Error1);
//		//drawSolutions();
//		
//	  }
	  Error=Error1;
		return Error;
		//

	}
   private void drawSolutions(){
		 
	 
		if (DrawingDebugUtils.DEBUG_GRAPHICALLY ){
		
			//  DrawingDebugUtils.getDebugFrame().paintAll( DrawingDebugUtils.getGraphics())
			colorValue+=0.1;
	
			 SolutionColor=Color.getHSBColor(0.4f, (float)colorValue, 0.5f);
				DrawingDebugUtils.getGraphics().setBackground(Color.white);
			 DrawingDebugUtils.getGraphics().setColor(SolutionColor);
			 
				
			// 
			 DrawingDebugUtils.drawThickPointPath( 	DrawingDebugUtils.getGraphics()  , SolutionColor, SolutionColor, polygonVertices);
			 DrawingDebugUtils.getGraphics().drawString(" color value = "+colorValue, 12,  (int)( (colorValue*100)+30));
				
	}
		
   }
   @Deprecated
	private double getXiXjBox(int indexxi, int indexxj, Point2D xi, Point2D xj) {
		PointData pointk;

//		double length;
//		double error = 0.0;
		Rectangle segmentBox = new Rectangle();
		for (int i = indexxi; i < indexxj; i++) {

			pointk = (PointData) problemStroke.getPoints().get(i);
			segmentBox.add(pointk.getPointLocation());
		}
		//double distance = xi.distance(xj);
		// now i want to get the width
		// distnce from pont to a corner

		// get the slope of the line
	//	double angle = ComputationsGeometry.computeAngle(xi, xj);

		return (segmentBox.getWidth() * segmentBox.getHeight() * 0.5);

	}

	/**
	 * calculate the error from approximating the curve from point xi to xj by
	 * the segment xi,xj
	 * 
	 * @param indexxi
	 *            the index of point xi in the stroke curve
	 * @param indexxj
	 *            the index of point xj in the stroke curve
	 * @param xi
	 *            the vertix point xi
	 * @param xj
	 *            the vertix point xj
	 * @return the approximating error of segment xixj
	 */
	protected double getXiXjError(int indexxi, int indexxj, Point2D xi,
			Point2D xj) {
		// get the slope of xixj line
	//	double m, termM2, termIm, x, y;
		double distance;
		// double deltax,deltay;
		// deltax=xj.getX()-xi.getX();
		// deltay=xj.getY()-(xi.getY());
		// double sqrtDelta=Math.sqrt((deltax*deltax)+(deltay*deltay));
		// if (sqrtDelta==0)
		// {
		// // sqrtDelta=1.0;
		// }

		distance = xi.distance(xj);
		// logger.info("delta x "+deltax);
		// logger.info("delta y "+deltay);
		// m=deltay/deltax;//slope computation
		//				 
		// Vector v = S.P1 - S.P0;
		// Vector w = P - S.P0;
		//
		// double c1 = dot(w,v);
		// if ( c1 <= 0 )
		// return d(P, S.P0);
		//
		// double c2 = dot(v,v);
		// if ( c2 <= c1 )
		// return d(P, S.P1);
		//
		// double b = c1 / c2;
		// Point Pb = S.P0 + b * v;
		// return d(P, Pb);
		//				 
		// // now using m we compute the line equation
		//				 
		// // deltay*(x-xi)= (y-yi)*deltax
		// //which is deltay(x-xi)-(deltax(y-yi)
		//				 
		// termM2=1.0/((m*m)+1.0); // term m2+1
		//				
		// termIm= xi.getY()-(m*xi.getX());//term im
		//				
		// double termIm2=-(m*xi.getY()-(m*m*xi.getX()));
		//				
		// double termSecondX=termIm2*termM2;

		PointData pointk;

		double length;
		double error = 0.0;

		// firstly get the first point in curve.
		for (int i = indexxi; i < indexxj; i++) {

			pointk = (PointData) problemStroke.getPoints().get(i);
			// //first get crosspoinding point in line
			// x=termSecondX+((m*pointk.getY()+pointk.getX())*termM2);
			// y=x*m+termIm;
			//					
			// //now get length between xk and xi
			// length=
			// ComputationsGeometry.length(x,pointk.getX(),y,pointk.getY());
			//				
			// //
			// if (sqrtDelta!=0)
			// {
			//					
			length = ComputationsGeometry.DistancePointLine(pointk.getPointLocation(), xi, xj,
					distance);
		//	logger.debug("  point   "+pointk.getPointLocation()+"   xi "+xi+"  xj "+xj +"   distance =   "+length);
			// length=((pointk.getX()-xi.getX())*deltay)-((pointk.getY()-xi.getY())*deltax);
			// another formula
			// length=(((deltay)*pointk.getX())+(deltax*(pointk.getY()))+((xi.getX()*(xj.getY()))-(xi.getY()*xj.getX())))/(sqrtDelta);
			// }
			// else
			// length=0;

			// antoher methods is to compute lenthg dirctly
			// which is deltay(x-xi)-(deltax(y-yi)
			// length=((pointk.getX()-xi.getX())*deltay)-((pointk.getY()-xi.getY())*deltax);
			// length=Math.abs(length);
			// logger.info("length "+ length);
			// now add the lenght square to the compulative error
			if (!(Double.isNaN(length))) {
				error += length;
				
				// logger.info("legnth"+length);
			}
			else {
			//	logger.error("  ERRROR ON THE lenth o f te error "+length);
				
			}

		}
		// if (Double.isNaN(error)){
		// logger.info(" m = "+m+" termM2 " +termM2 +" termSecondX
		// "+termSecondX+" termIm "+termIm);
		// }

		return (error * error);
	}

	@Deprecated
	private double Magnitude(Point x1, Point x2)
	// ( XYZ *Point1, XYZ *Point2 )
	{
		// XYZ Vector;

		double X = x2.getX() - x1.getX();
		double Y = x2.getY() - x1.getY();
		// double Z = Point2->Z - Point1->Z;

		return Math.sqrt(X * X + Y * Y);
	}
       // use the one in the computational goemtry.............
	@Deprecated
	private double DistancePointLine(Point2D point, Point2D x1, Point2D x2,
			double LineMag)
	// XYZ *Point, XYZ *LineStart, XYZ *LineEnd, float *Distance )
	{

		if (debugS){
			if (DrawingDebugUtils.DEBUG_GRAPHICALLY )
			{
				//DrawingDebugUtils.SetColor(DrawingDebugUtils.getGraphics(),);
				DrawingDebugUtils.drawLine(DrawingDebugUtils.getGraphics(),Color.orange, x1, x2);
			}
			
		}
		//
		// double LineMag;
		double U;

		// XYZ Intersection;

		// LineMag = Magnitude( LineEnd, LineStart );

		if (LineMag==0){
			LineMag=1;
			// the points x1 and x2 are nearly over each other or they may be over each other
		}
		
		U = (double) (((point.getX() - x1.getX()) * (x2.getX() - x1.getX())) + ((point
				.getY() - x1.getY()) * (x2.getY() - x1.getY())))
				/ (LineMag * LineMag);

//		if (U < 0.0f || U > 1.0f) {
//			//logger.info("-----------------------------------------------problem");
//			return 0.0; // closest point does not fall within the line
//								// segment
//
//		}
         
		
		double X = (double) x1.getX() + U * (x2.getX() - x1.getX());
		double Y = (double) x1.getY() + U * (x2.getY() - x1.getY());
		Point2D intersection = new Point2D.Double();
		intersection.setLocation(X, Y);
		// Intersection.Z = LineStart->Z + U * ( LineEnd->Z - LineStart->Z );

		
		double Distance = point.distance(intersection);
		// double Distance = Magnitude( point, intersection );
		if (debugS){
			if (DrawingDebugUtils.DEBUG_GRAPHICALLY)
			{
//				DrawingDebugUtils.drawPoint(DrawingDebugUtils.getGraphics(),Color.red, point);
			//	DrawingDebugUtils.getGraphics().drawString("  Intersection "+intersection, (int)point.getX(), (int) point.getY());

				DrawingDebugUtils.drawLine(DrawingDebugUtils.getGraphics(),Color.GREEN, point, intersection);
				DrawingDebugUtils.drawPoint(DrawingDebugUtils.getGraphics(),Color.red, point);
			//	logger.info(" point is "+point+"   and intersection is "+intersection);
//				if (point.getY()<0||point.getX()<0){
//					
//					logger.info( "Intersecton  --------------> "+intersection); 
//					
//				}
				//DrawingDebugUtils.drawPoint(DrawingDebugUtils.getGraphics(),Color.black, intersection);
			}
			
		}

		// logger.info("the line lenthg is "+LineMag+" the final distance
		// is"+Distance);
		return Distance;
	}

	public int compareTo(Object obj) {
		if (this == obj)
			return 0;
		if (obj == null)
			throw new ClassCastException("Polygon Solution expected");
		if (getClass() != obj.getClass())
			throw new ClassCastException("Polygon Solution expected");
		
		PolygonSolution other = (PolygonSolution) obj;
		
	if (Arrays.equals(particlePoints, other.particlePoints))
			return 0;
	else {
		Integer test;//=new Integer(0);
          for (int i = 0; i < particlePoints.length; i++) {
        	  test=particlePoints[i];
        	int result=test.compareTo(other.particlePoints[i]);
        	if (result!=0){
        		return result;
        	}
			
		}
          return 0;
	}
	
}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + DominateCount;
		result = prime * result + Arrays.hashCode(particleDominate);
		result = prime * result + Arrays.hashCode(particlePoints);
		result = prime * result
				+ ((polygonVertices == null) ? 0 : polygonVertices.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolygonSolution other = (PolygonSolution) obj;
//		if (DominateCount != other.DominateCount)
//			return false;
//		if (!Arrays.equals(particleDominate, other.particleDominate))
//			return false;
		if (!Arrays.equals(particlePoints, other.particlePoints))
			return false;
//		if (polygonVertices == null) {
//			if (other.polygonVertices != null)
//				return false;
//		} else if (!polygonVertices.equals(other.polygonVertices))
//			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder temp=new StringBuilder();
		temp.append( " Particles =  [ ");
		for (int i = 0; i < particlePoints.length; i++) {
			temp.append( " ");
			temp.append( particlePoints[i]);

		}
		temp.append(" ] ");
		return temp.toString();
	}

	public void refineSolution() {
		// /first remove adjasent lit it segment > minsegment =15 piint as in
		// devide curve
		if (SystemSettings.POLYGON_ADJUST_Default != POLYGON_ADJUST_NONE ){
			
	//	logger.trace("inside the refine solution  ");
		if (SystemSettings.POLYGON_ADJUST_Default == POLYGON_ADJUST_MERGE
				|| SystemSettings.POLYGON_ADJUST_Default == POLYGON_ADJUST_BOTH) {
			//logger.trace(" the merge ");
			int count=0;
			while (RefineSolutionAdjustMerge()&& count<10){
				count++;
			}
		}	
		if (SystemSettings.POLYGON_ADJUST_Default == POLYGON_ADJUST_DOMINATE_POINT
				|| SystemSettings.POLYGON_ADJUST_Default == POLYGON_ADJUST_BOTH) {
//			logger.trace(" the dominate ");
			int count=0;
			while (RefineSolutionDomintate()&& count<10){
				count++;
			}
			//simpleRefineSolutionDominate();
		}
	
		logger.trace("the segment cound ");
		int count=0;
		while (RefineSolutionSegmentCount()&& count<10){
			count++;
		}
		//logger.trace(" trying alll........... ");
		
		}
//	}
		if (SystemSettings.POLYGON_ADJUST_Default ==POLYGON_ADJUST_ONLY_SEGMENTS)
			RefineSolutionSegmentCount();
		// first and last musst p 1
		if (particlePoints.length>0){
			
		particlePoints[0] = 1;
		particlePoints[particlePoints.length - 1] = 1;
		}

		// // try to refine the solution .
	}


	
		protected boolean RefineSolutionSegmentCount() {

                 boolean merged=false;
			if (particlePoints.length==0)
				return  merged;
			particlePoints[0] = 1;
			
			particlePoints[particlePoints.length - 1] = 1;
			//logger.info();
			int lastAcceptableEdge = 0;
			
			double lengTemp=0;
			for (int i = 0; i < particlePoints.length; i++) {
				// / let index equal
				if (particlePoints[i] == 1) {
					lengTemp= problemStroke.getPoint(i).distance(problemStroke.getPoint( lastAcceptableEdge ));
					if (i == 0) {
						lastAcceptableEdge = i;
					}
					else if ((lengTemp) >= minSegment) {
						// set the new index to last edge
						lastAcceptableEdge = i;//Graphics2D g, Color linColor, Color pointColor , ArrayList<Point2D> polygonVertices
					} else if ((lengTemp) < minSegment) {
						
                        logger.trace("  the segment is less than acceptable min segment "+minSegment+"   because has length of "+lengTemp+" this is for edge "+i+"  from the "+particlePoints.length );
						particlePoints[i] = 0;
						merged=true;
					}
				}
			}
		

//			// now check the last 4 points
			for (int i = particlePoints.length - 2; i > 0; i--) {
				if (particlePoints[i] == 1) {
					lengTemp= problemStroke.getPoint(i).distance(problemStroke.getPoint( particlePoints.length - 1));
				if ((lengTemp) < minSegment) {
						
                       // logger.info("  the segment is less than acceptable min segment "+minSegment+"   because has length of "+lengTemp+" this is for edge "+i+"  from the "+particlePoints.length );
						particlePoints[i] = 0;
						merged=true;
					}
					 
				break;

				}
			}
			
			particlePoints[0] = 1;
			particlePoints[particlePoints.length - 1] = 1;
			
			if (particlePoints.length<minSegment)
			{
				merged=false;
			}
//			particlePoints[0] = 1;
//			particlePoints[particlePoints.length - 1] = 1;
			
//			
//			for (int i = 0; i < particlePoints.length; i++) {
//				System.out.print("   "+particlePoints[i]);
//			}
//			logger.info();
//			logger.info("After refine "+" (" + this.getClass().getSimpleName()
//					+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			return merged;
		}
		


	protected void simpleRefineSolutionDominate(){
		// get stroke stistical data.

		// get the data
		// first get the points in the currents solution
		// get dominate point of the stroke
		// loop on the points from stroke
		// go to the first point set to 1
		// if a point is set to 1 is not located in the dominate points set it
		// to 0
		// get the index of the domiante point and set it to 1
		// go to the next point that is set to 1
		// calculateSolutionParameters again
		ArrayList temp;
		int PreviousIndex = 0;
		// first check if dominat points is calcuated if not calcate them again
		if (problemStroke.getStatisticalInfo() != null) {
			if ((temp = problemStroke.getStatisticalInfo()
					.getDominatePointsIndeces()) != null) {
				// there is one dominate pointn now i need to check if the
				// vertics
				for (int i = 0; i < particlePoints.length; i++) {
					if (particlePoints[i] == 1) {

						// if the particle is one

						// get the nearst index
						for (int j = PreviousIndex; j < temp.size(); j++) {

							{
								int test = ((DominatePointStructure) temp
										.get(j)).getIndexInInk();
								if (test == i) {// found it 
									PreviousIndex = j;
									// then this index exist in the list of
									// dominat epoint and skip to th enext
									break;
								}
								else if (test<i){
									// continue to search to check if dominate points 
									
									// do nothing just continue to loop 
								 
								}
								else if (test >i){
									
									// reached to the next edje. 
									// this is not a dominat epoint so set it to 0
									particlePoints[i] =0;
									if (j>1)
										PreviousIndex=j-1;
								}
							
							}// instance of integer

						} // end for

					}// / if point is vertix check if dominate

				}// / for particle point
			}

		}
	}

	protected boolean RefineSolutionDomintate() {
		// get stroke stistical data.
 boolean merged=false;
		// get the data
		// first get the points in the currents solution
		// get dominate point of the stroke
		// loop on the points from stroke
		// go to the first point set to 1
		// if a point is set to 1 is not located in the dominate points set it
		// to 0
		// get the index of the domiante point and set it to 1
		// go to the next point that is set to 1
		// calculateSolutionParameters again
		ArrayList temp;
		int PreviousIndex = 0;
		// first check if dominat points is calcuated if not calcate them again
		if (problemStroke.getStatisticalInfo() != null) {
			if ((temp = problemStroke.getStatisticalInfo()
					.getDominatePointsIndeces()) != null) {
				//logger.trace( " inside refine dominate ");
				// there is one dominate pointn now i need to check if the
				// vertics
				//logger.info(" number of domiante points = "+temp.size());
				for (int i = 0; i < particlePoints.length; i++) {
					if (particlePoints[i] == 1) {

						// if the particle is one
                            
						// get the nearst index
						for (int j = PreviousIndex; j < temp.size(); j++) {

							{
								int test = ((DominatePointStructure) temp
										.get(j)).getIndexInInk();
								if (test == i) {
									PreviousIndex = j;
									// then this index exist in the list of
									// dominat epoint and skip to th enext
									break;
								}
								if (test > i) {
									//// i first want to check if this movement need to minize error. 
									// commpute current error. 
								
									
									// now i have crossed the avalible index and
									// this pooint is not a dominate
									// search for the nearest dominatte point
									
									int after =0, before = 0;

									int prev = 0;

									if (j > 0)
										prev = ((DominatePointStructure) temp
												.get(j - 1)).getIndexInInk();
									else
										prev = test;
									// example 5 9 17
									if ((test - i) > (i - prev)) // the prev
																	// is
																	// nearest
									{
										// now let the particle of prev ==1
										 if (isErrorMenimized(prev,i,j))
										 {
										
										merged=true;
										particlePoints[prev] = 1;
										particlePoints[i] = 0;
										 }
										
										// check if test is labled 0 then set it
										// to 1

										// check if prvious lable index is
										// labled 0 and set nerest 1

									} else // the next is nearest or equal
									{
										 if (isErrorMenimized(i,j,test))
										 {
										
											 merged=true;
										particlePoints[test] = 1;
										particlePoints[i] = 0;
										 }
										//particlePoints[test] = 1;
									}

									PreviousIndex = j;
									
									
									
									
									
									
									break;
							 
								
								} // index not found in domiante point
							}// instance of integer

						} // end for

					}// / if point is vertix check if dominate

				}// / for particle point
			}

		}
		//logger.trace(" returning "+merged);
		return merged;
	}

	protected boolean isErrorMenimized(int First, int second, int next) {
		  
		
		double e1 = getXiXjError(First, second, problemStroke
				.getPoint(First).getPointLocation(), problemStroke
				.getPoint(second).getPointLocation());
		double e2 = getXiXjError(second, next, problemStroke
				.getPoint(second).getPointLocation(), problemStroke
				.getPoint(next).getPointLocation());

		double e3 = getXiXjError(First, next, problemStroke
				.getPoint(First).getPointLocation(), problemStroke
				.getPoint(next).getPointLocation());

		if (e3 < (e1 + e2))
			return true;
		
		else 
			return false;
		 
		
	}

	private boolean RefineSolutionAdjustMerge() {
		boolean merged=false;
		// do th elagorith min page 247
		int count = 0;
		int j = 0;
		int next = 0;
		if (particlePoints.length==0)
			return merged;

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

//						// frist compute the erro rf the first system i j next
//						double e1 = getXiXjError(i, j, problemStroke
//								.getPoint(i).getPointLocation(), problemStroke
//								.getPoint(j).getPointLocation());
//						double e2 = getXiXjError(j, next, problemStroke
//								.getPoint(j).getPointLocation(), problemStroke
//								.getPoint(next).getPointLocation());
//
//						double e3 = getXiXjError(i, next, problemStroke
//								.getPoint(i).getPointLocation(), problemStroke
//								.getPoint(next).getPointLocation());

						// logger.info(" e1 "+e1 + " e2 "+e2 + " e3"+e3+"
						// ------- "+i+" " + j+" " +next);
						 if (isErrorMenimized(i,j,next)){
						//if (e3 < (e1 + e2)) {// if error after merge is less
												// than current then merge them
												// by letting j =0

							// logger.info(" Merge "+i+" " + j+" "
							// +next);
							// then
						//	 merged=true;
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
		// will need to compute the error
		return merged;
	}

	/**
	 * @param j
	 * @return -1 in case not found a next edge.
	 */
	protected int getNextVertex(int j) {
		// first adjust
		for (int i = j; i < particlePoints.length; i++) {
			if (particlePoints[i] == 1) {

				return i;
			}
		}
		return -1;// this is the last vertix of the
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		PolygonSolution tempSolution = null;
		tempSolution = (PolygonSolution) super.clone();
		tempSolution.particlePoints = (int[]) particlePoints.clone();
		tempSolution.particleDominate=(int[])particleDominate.clone();
		tempSolution.velocities = velocities.clone();
		
		// tempSolution.polygonVertices=(ArrayList<Point2D>)this.polygonVertices.clone();
		tempSolution.problemStroke = this.problemStroke;
		tempSolution.eTolerance = this.eTolerance;
		tempSolution.calculateSolutionParameters();
		return tempSolution;
	}

	public void setParam(ArrayList Param) {
		// 
		// do noting i don't need it here
	}

	public void paint(Graphics2D g) {
		// 
//		logger.info(" is interpolated "+problemStroke.isInterpolated()+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");

		
		java.awt.Stroke s = g.getStroke();
		java.awt.BasicStroke bs=new BasicStroke(3);
		g.setStroke(bs);
	//	g.setColor(Color.yellow);
		// logger.info( " the color is "+linecolor.toString());
		for (int i = 0; i < polygonVertices.size() - 1; i++) {
			g.setColor(Color.GRAY);
			// draw line between point and next point in polyogn
			g.drawLine((int) (polygonVertices.get(i)).getX(),
					(int) (polygonVertices.get(i)).getY(),
					(int) (polygonVertices.get(i + 1)).getX(),
					(int) (polygonVertices.get(i + 1)).getY());
			g.setColor(Color.MAGENTA);
			// draw a small polygon to mark this point as a vertix of polygon.
			g.drawRect((int) polygonVertices.get(i).getX(),
					(int) polygonVertices.get(i).getY(), 5, 5);
			
			g.fillRect((int) polygonVertices.get(i).getX(),
					(int) polygonVertices.get(i).getY(), 5, 5);
			
			g.fillRect((int) polygonVertices.get(i+1).getX(),
					(int) polygonVertices.get(i+1).getY(), 5, 5);

		}
		g.setStroke(s);

	}

	public Segment getSegmentOfIndex(int index) {

		int count = 0, vi = 0, vj = 0;
		boolean SegmentFound = false;
		// get the first vertix fo the segmetn
		for (int j = 0; j < particlePoints.length; j++) {

			if (particlePoints[j] == 1) {
				count++;

				if (count >= index) {
					vi = j;
					vj = getNextVertex(j + 1);
					if (vj != -1)
						SegmentFound = true;

					break;
				}

			}

		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("getSegmentOfIndex(int) -  The index is  " + index + "  and from vertix  " + vi + "   till vertix " + vj + "  which meand that found state =  " + SegmentFound + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		}
		// now if segment not found i will return null
		if (!SegmentFound)
			return null;
		// else i start creating the segment.

		Segment temp = new Segment(problemStroke.createSubInkObject(vi, vj));

		// then addd the vertix

		PointData p = (PointData) problemStroke.getPoint(vi).clone();
		temp.setStartPoint(p);

		p = (PointData) problemStroke.getPoint(vj).clone();
		temp.setEndPoint(p);

		Line linPrim = new Line(problemStroke.getPoint(vi), problemStroke
				.getPoint(vj));
	
		linPrim.setIStart(vi);
		linPrim.setIEnd(vj);
		// Segpoints.add();
		temp.setSegmentType(linPrim);

		return temp;
	}

	public int getSegmentsCount() {
             this.calculateSolutionParameters();
		return this.polygonVertices.size()-1;
	}

	public void CheckLineSlopes() {
	    // check all segments of this solution
		
		//if (SegmentsType!=null){
	
		
		boolean MergeFound=false;
		
		do {
			MergeFound=false;
			
			ArrayList<Integer> pointsToRemove=new ArrayList<Integer>();
			ArrayList<Point2D> TemppolygonVertices=new ArrayList<Point2D>();
	//	logger.info(" size of the segments type is     SSSSSSSSSSSSSSSSSSSSs     "+this.polygonVertices.size());
		for (int k = 0; k <this.polygonVertices.size()-2; k++) {
		
		// get segments types 
				PointData p1 =  (PointData) polygonVertices.get(k);
				PointData  p2= (PointData) polygonVertices.get(k+1);
				PointData  p3=(PointData)polygonVertices.get(k+2);
				
				Line  l1=new Line(p1, p2);
			
				
				Line  l2=new Line(p2, p3);
			
	
				
				
				//logger.info("comparing the line of "+l1+"   and "+l2);
				if (isMergableLines(l1, l2))
				{
				
				//	logger.info("remoivng the point number "+k);
					// polygonVertices.remove(k+1);
					 pointsToRemove.add(Integer.valueOf(k+1));
				//	this.particlePoints[k+1]=0;
					//this.particlePoints[T2.getIStart()]=0;
					 MergeFound=true;
					
					
				}
			
		}
//	 logger.info("remving  ............................."+pointsToRemove.size());
		int k=0;
		for (int i = 0; i < polygonVertices.size(); i++) {
			 if (k<pointsToRemove.size()){
			
			if (i!=pointsToRemove.get(k))
			{ 
			
				TemppolygonVertices.add(polygonVertices.get(i));
			  //k++;
			  
			  
			}
			else {
				k++;
			}
			
			 }
			 else {
				 
				 TemppolygonVertices.add(polygonVertices.get(i));
			 }
		}
		
	 
		
		polygonVertices=TemppolygonVertices;
//	for (int i = 0; i < pointsToRemove.size(); i++) {
//		logger.info("remving the point number   "+pointsToRemove.get(i));
//			 polygonVertices.remove(pointsToRemove.get(i));//pointsToRemove.add(k+1);
//			
//		}


	
		//logger.info(" after the removal now the  pints has ...... "+ polygonVertices.size());
		}while (MergeFound);
		//if (polygonVertices.)
		
		//this.calculateSolutionParameters();
		correctParticles();
		
		
		//this.calculateSolutionParameters();
		//logger.info(" size of the segments type  AFter  is     SSSSSSSSSSSSSSSSSSSSs     "+this.SegmentsType.size());
		
	}
    private void correctParticles() {
		for (int i = 0; i < particlePoints.length; i++) {
			particlePoints[i]=0;
		}
		ArrayList< PointData>  points=this.problemStroke.getPoints();
		int j=0;
		if (polygonVertices.size()<2){
			if (particlePoints.length>0){
			 particlePoints[0]=1;
			
			particlePoints[particlePoints.length-1]=1;
			}
		}
		else {
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).equals(polygonVertices.get(j)))
			{
				// now i reached the point in polygon. 
				
				particlePoints[i]=1;
				j++;
				if (j>=polygonVertices.size())
					break;
				
			}
		}
		}
		//regenerate the polygon vertices. 
    this.calculateSolutionParameters();
		
	}

	protected boolean isMergableLines(GeometricPrimitive  p1, GeometricPrimitive p2)
    {
    	if (p1 instanceof Line) {
			Line l1 = (Line) p1;
			if (p2 instanceof Line) {
				Line l2 = (Line) p2;
				
				// if true then check if same has the same slope
				
//				
//				double m1=l1.slope();
//				double m2=l2.slope();
			//	logger.info( " now check if lines are parallellllllllllllllllllllllllllllllll");
				boolean temp=l1.isCollinear(l2);
				//logger.info( " result they arey "+temp+"    paraallel");
				 return temp;
				
				
			}
			
			
		}
    	
    	
    	
    	return false;
    	
    }

	public void setDominatePointMap(SortedValueMap pointmap) {
	 	logger.trace(" setting the point length of the dominate points ");
		if (pointmap.getSortedList()!=null){
		
			for (Iterator iter  = pointmap.getSortedList().iterator(); iter .hasNext();) {
				
				 DominatePointStructure point =(DominatePointStructure) ((Map.Entry)iter.next()).getKey();
				 if (point.getIndexInInk()>0 && point.getIndexInInk() < particleDominate.length)
				 {
					 particleDominate[point.getIndexInInk()]=1;
					 	//particleDominate[point.getIndexInInk()]++;
					 logger.trace(" dominate point................");
				 }
			}
		}
		
		
	}
	public double getComparableError(){
		return this.error();
	}

	public String getSegmentsString() {
	 StringBuilder s=new StringBuilder ("<[<");
	 
	 for (int k = 0; k <this.polygonVertices.size()-2; k++) {
		 
			PointData p1 =  (PointData) polygonVertices.get(k);
			PointData  p2= (PointData) polygonVertices.get(k+1);
	//s+="  S"+k+" Line[P("+p1.x+","+p1.y+") to P("+p2.x+","+p2.y+")]  ,    ";
			// get segments types 
	 s.append("  S" ).append(k).append(
		" Line{P(").append(p1.x).append(",").append(p1.y).append(") to P(")
		.append(p2.x).append(",").append(p2.y).append(")},    ");
		
	 }
	 s.append(">]>  ");
	 
		return s.toString();
	}
	
	
}