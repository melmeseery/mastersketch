/**
 * 
 */
package SketchMaster.system.segmentation;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import SketchMaster.system.SystemSettings;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Segment;
import SketchMaster.Stroke.StrokeData.SimpleInkObject;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.shapes.BezireCurve;
import SketchMaster.Stroke.graphics.shapes.GeometricPrimitive;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.Stroke.graphics.shapes.SegmentedShape;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.swarm.polygonApproximations.PolygonSolution;
import SketchMaster.system.SystemSettings;

/**
 * Segment
 * 
 * @author maha
 * 
 */
public class HybirdFitSolution extends PolygonSolution implements
		SegmentedShape {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HybirdFitSolution.class);
	

	public HybirdFitSolution(int size) {
		particlePoints = new int[size];
		polygonVertices = new ArrayList<Point2D>();
	//	super(size);
		 
	}

	private boolean SegmentsErrorComputed=false;
	
	private static final double BezireThreshold = SystemSettings.BEZIRE_ERROR_THRESHOLD;
	private static final double CurveTestThreshold=SystemSettings.CURVE_TEST_THRESHOLD;

	private static final double  HybirdErrorToleranceThreshold=SystemSettings.HYBIRD_ERROR_TOLERANCE_TEST_THRESHOLD;
	protected ArrayList<GeometricPrimitive> SegmentsType;
	boolean segmentUsed = false;

	@Override
	public void refineSolution() {
		if (particlePoints!=null){
			if (particlePoints.length>0){
		particlePoints[0] = 1;
		particlePoints[particlePoints.length - 1] = 1;
		// first and last musst p 1
	//	RefineSolutionSegmentCount();
//		particlePoints[0] = 1;
//		particlePoints[particlePoints.length - 1] = 1;
			}
		}
	}

	/**
	 * @param stroke
	 * 
	 */
	public HybirdFitSolution(Stroke stroke) {

		// super(stroke);

		problemStroke = stroke;
		particlePoints = new int[stroke.getPointsCount()];
		polygonVertices = new ArrayList<Point2D>();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.Stroke.graphics.shapes.GuiShape#paint(java.awt.Graphics2D)
	 */
	public void paint(Graphics2D g) {

		if (segmentUsed) {

			if (SegmentsType != null) {
				
			
				for (int i = 0; i < SegmentsType.size(); i++) {
//					logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   segment count "+SegmentsType.size()+" ("
//							+ this.getClass().getSimpleName()
//							+ "    "
//							+ (new Throwable()).getStackTrace()[0]
//									.getLineNumber() + "  )  ");
					Point2D pointS = SegmentsType.get(i).getStartPoint();
					Point2D pointe=	SegmentsType.get(i).getEndPoint();
					if (	pointS!=null){
					g.setColor(Color.BLUE);
					g.drawRect((int)pointS.getX(),(int)pointS.getY(),4,4);
					g.fillRect((int)pointS.getX(),(int)pointS.getY(),3,3);
					}
					if (pointe!=null)
					{
					g.setColor(Color.RED);
					g.drawRect((int)pointe.getX(),(int)pointe.getY(),5,5);
					g.fillRect((int)pointe.getX(),(int)pointe.getY(),4,4);
					}
					
					SegmentsType.get(i).paint(g);
//					logger.info(" segment type  "	+SegmentsType.get(i).getClass().getSimpleName()
//							+"  from point   "+SegmentsType.get(i).getIStart()+ " to     "+ SegmentsType.get(i).getIEnd() +" ("
//							+ this.getClass().getSimpleName()
//							+ "    "
//							+ (new Throwable()).getStackTrace()[0]
//									.getLineNumber() + "  )  ");
				}
			}

		} else {

			for (int i = 0; i < polygonVertices.size() - 1; i++) {
				g.setColor(Color.blue);
				// draw line between point and next point in polyogn
				g.drawLine((int) (polygonVertices.get(i)).getX(),
						(int) (polygonVertices.get(i)).getY(),
						(int) (polygonVertices.get(i + 1)).getX(),
						(int) (polygonVertices.get(i + 1)).getY());
				g.setColor(Color.blue);
				// draw a small polygon to mark this point as a vertix of
				// polygon.
				g.drawRect((int) polygonVertices.get(i).getX(),
						(int) polygonVertices.get(i).getY(), 5, 5);
				g.fillRect((int) polygonVertices.get(i).getX(),
						(int) polygonVertices.get(i).getY(), 5, 5);

				// draw a small polygon to mark this point as a vertix of
				// polygon.
				g.drawRect((int) polygonVertices.get(i + 1).getX(),
						(int) polygonVertices.get(i + 1).getY(), 5, 5);
				g.fillRect((int) polygonVertices.get(i + 1).getX(),
						(int) polygonVertices.get(i + 1).getY(), 5, 5);

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.Stroke.graphics.shapes.GuiShape#setParam(java.util.ArrayList)
	 */
	public void setParam(ArrayList Param) {

	}

	public void addVertix(int indexInInk) {
		if (indexInInk < particlePoints.length)
			particlePoints[indexInInk] = 1;

	}

	public void clearVertices() {

		for (int i = 0; i < particlePoints.length; i++) {
			particlePoints[i] = 0;
		}
	
		polygonVertices.clear();
	}

	@Override
	public double error() {
		if (SegmentsErrorComputed==false)
		{

		return Error = errorMain();
		}
		else {
			if (logger.isDebugEnabled()) {
				//  logger.debug("error() -  Error =  " + Error + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
			return Error;
		}
	}
	public void computeSegmentError(){
		// nnow check if segment realy exist 
		Error=0.0;
		if (segmentUsed){
			
			if (SegmentsType!=null){
				
				double temperror=0.0;
				for (int i = 0; i < SegmentsType.size(); i++) {
					
					if (SegmentsType.get(i) instanceof BezireCurve) {
						BezireCurve curve = (BezireCurve) SegmentsType.get(i);
					
                         temperror+=curve.getErrorComputed();
//                         logger.info("Error in curve  "+" ("
//								+ this.getClass().getSimpleName()
//								+ "    "
//								+ (new Throwable()).getStackTrace()[0]
//										.getLineNumber() + "  )  ");
                
					}
					if (SegmentsType.get(i) instanceof Line) {
						Line line = (Line) SegmentsType.get(i); 
						
						temperror+=line.getErrorComputed();
						
					}
					
				Error=Math.sqrt(temperror);
				SegmentsErrorComputed=true;
		//		logger.info(" 9999999 sgement error finally it equal to "+Error+" ("
//						+ this.getClass().getSimpleName() + "    "
//						+ (new Throwable()).getStackTrace()[0].getLineNumber()
//						+ "  )  ");
				
				}
				
				
				
				
			}
			
		
		}
		
	} 

	@Override
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
						if (Double.isNaN(temperror)|| Double.isInfinite(temperror))
							temperror = 0.0;
						
						error += temperror;
						break;
					}
				}

			}
		}

		// logger.info("------------------------------Error---------
		// -------------------");

		return Math.sqrt(error);
		// return error;
	}

	@Override
	public Object clone() {

		super.clone();
		HybirdFitSolution tempSolution = null;
		tempSolution = new HybirdFitSolution(this.problemStroke);
		tempSolution.particlePoints = (int[]) particlePoints.clone();

		tempSolution.problemStroke = this.problemStroke;

		return tempSolution;
	}

	public boolean IsCurveSegment(int i) {

		// this mean in the final line if " lenght of arc / lenght of line > 1
		// then it is moslty a curve "
		double lc = 0, ll=0.0;
		int j = this.getNextVertex(i + 1);
		if (j > -1) {
			// get the length of line
			ll = ComputationsGeometry.computeLength(problemStroke.getPoint(i),
					problemStroke.getPoint(j));

			// get the acumelative lenth of the curve
			for (int k = i; k < j - 1; k++) {
				lc += ComputationsGeometry.computeLength(problemStroke
						.getPoint(k), problemStroke.getPoint(k + 1));

			}
			/*****
			 * 9 < l2/l1 *1.1 < 1
			 * ********/
			if (ll==0.0)
			return false;
			// if length of curve > length of line by > curve threshodl 
			//then curve
			if (((lc / ll)) >  CurveTestThreshold)
				return true;
		}
		return false;
	}

	public void CreateBezireSegment(int i) {
//		System.out
//				.println("Segment of vertix 555555555555555555555555555555555555555555555555   "
//						+ i + "     (hybirdfitsolution 245)   ");
		/***********************************************************************
		 * v=first point i , u =second point j c1 = kˆt1 + v ==> first control
		 * c2 = kˆt2 + u ====> second control k =1/3*(Sum(|Sk − Sk+1|))
		 * 
		 * 
		 * where ˆt1 and ˆt2 are the unit length tangent vectors pointing
		 * 
		 * inwards at the curve segment to be approximated. The 1/3 factor in k
		 * controls how much we scale ˆ t1 and ˆ t2 in order to reach the
		 * control points; the summation is simply the length of the chord
		 * between Si and Sj //SegmentsType
		 * 
		 **********************************************************************/

		int j = this.getNextVertex(i + 1);
		if (j > -1) {
			PointData[] control = new PointData[4];
			// logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
			// (hybirdfitsolution 268) ");
			
			
			double e = computeControlPointError(i, j, control);
//               logger.info("bezire error of segment   "+i+"  to  "+j+"   =   "+e+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			if (e < BezireThreshold) {

				BezireCurve temp = new BezireCurve();
				temp.CreateCurve(control);
				temp.setStartPoint((PointData) problemStroke.getPoint(i).clone());
				temp.setEndPoint((PointData) problemStroke.getPoint(j).clone());
				temp.setCurvParam();
				temp.setErrorComputed(e);
				temp.setIStart(i);
				temp.setIEnd(j);

				SegmentsType.add(temp);

			} else {

//				logger.info("divide the curve error is large  " + " ("
//						+ this.getClass().getSimpleName() + "    "
//						+ (new Throwable()).getStackTrace()[0].getLineNumber()
//						+ "  )  ");
				// divide and repeaat the process thill lestha
				int half = (j - i) / 2;
				half += i;
				particlePoints[half] = 1;
				CreateBezireSegment(i);
				CreateBezireSegment(half);

			}

		}
		
		this.calculateSolutionParameters();

	}

	public  double computeControlPointError(int i, int j, PointData[] control) {

		PointData[] controlpoints = computeControlPoint(i, j);

		// ArrayList<Double> coffe=BezireCurve.coeff(controlpoints);

		// approximate the curve using the pecie wise curve
		double errorB = errorBezire(controlpoints, i, j);

		for (int k = 0; k < controlpoints.length; k++) {
			control[k] = controlpoints[k];
		}
		// control=controlpoints;
//		logger.info("  the error is ========== >  " + errorB
//				+
//              " ("
//				+ this.getClass().getSimpleName()
//				+ "    "
//				+ (new Throwable()).getStackTrace()[0]
//						.getLineNumber() + "  )  ");
		return errorB;

	}

	PointData c1, c2, u, V;
	// PointData[] control;
	int ui, vi;

	public PointData[] computeControlPoint(int i, int j) {
		ui = i;
		vi = j;

		int segmentCount=j-i;
		int segment=j-i;
		
		
		if (segmentCount>(2*SystemSettings.STROKE_CONSTANT_NEIGHBOURS))
		{
			segmentCount=(SystemSettings.STROKE_CONSTANT_NEIGHBOURS);
		}
		else {  // they are less than the stroke neighbours // very small
			segmentCount=(segmentCount/2)-1;
			if (segmentCount==0)
				segmentCount=1;
			
		}
//		logger.info("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe");
//		logger.info(" segment count  "+segmentCount+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		double lc = 0, ll, K;
		PointData t1, t2;
		//ll = ComputationsGeometry.computeLength(problemStroke.getPoint(i),
			//	problemStroke.getPoint(j));
		for (int k = i; k < j - 1; k++) {
			lc += ComputationsGeometry.computeLength(problemStroke.getPoint(k),
					problemStroke.getPoint(k + 1));

		}
		K = (1.0 / 3.0) * lc;
		// get tangent to curve
		// get tangent to the curve
//		t1 = ComputationsGeometry.computeTangent(problemStroke.getPoint(i),
//				problemStroke.getPoint(i + segmentCount), problemStroke.getPoint(j));
//		t2 = ComputationsGeometry.computeTangent(problemStroke.getPoint(j - segmentCount),
//				problemStroke.getPoint(j), problemStroke.getPoint(i));
		PointData p1,p2,p3;
		p1=problemStroke.getPoint(i);
		p2=problemStroke.getPoint(i + segmentCount);
		p3=problemStroke.getPoint(i+segment/2);
		// get tangent to curve
		// get tangent to the curve
		t1 = ComputationsGeometry.computeTangent(p1,p2,p3 );
		p1=problemStroke.getPoint(j - segmentCount);
		p2=problemStroke.getPoint(j);
		p3=problemStroke.getPoint(j-segment/2);
		
		t2 = ComputationsGeometry.computeTangent(p1,p2,p3 );
		//g.setColor(Color.cyan);
//		DrawPoint(g, p1);
//		DrawPoint(g, p2);
//		DrawPoint(g, p3);
		// logger.info(" t1 "+t1);
		// logger.info(" t2 "+t2);

		c1 = new PointData(problemStroke.getPoint(i).x, problemStroke
				.getPoint(i).y);
		u = new PointData(problemStroke.getPoint(i).x, problemStroke
				.getPoint(i).y);

		V = new PointData(problemStroke.getPoint(j).x, problemStroke
				.getPoint(j).y);
		c2 = new PointData(problemStroke.getPoint(j).x, problemStroke
				.getPoint(j).y);

		t1.mulScaler(K);
		t2.mulScaler(K);

		c1.addPoint(t1);

		c2.addPoint(t2);

		// now compute the error

		PointData[] controls = new PointData[4];
		controls[0] = u;
		controls[1] = c1;
		controls[2] = c2;
		controls[3] = V;
		// logger.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
		// (hybirdfitsolution 372) ");
		// for (int l = 0; l < controls.length; l++) {
		// logger.info("controls i in the function "+controls[l]);
		// }
		//	
		// logger.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
		// (hybirdfitsolution 377) ");
		//		

		return controls;

	}

	private double errorBezire(PointData[] c, int i, int j) {
		// logger.info(" " +
		// "" +

		// 388) ");
		boolean beginSegment=true;
	
		// this is the approximation to the bezire curve.
		ArrayList<PointData> polygon = BezireCurve.ApproximateToPoints(c);
//		g.setColor(Color.blue);
//		DrawPolygon(g,polygon);
		
//		for (int k = 0; k < ink.getPointsCount(); k++) {
//			DrawPoint(g, ink.getPoint(k));
//		}
		
		
		// this is the part of the stroke that is modeled as a curve
		SimpleInkObject ink =(SimpleInkObject) problemStroke.createSubInkObject(i, j);

		// now i will get the error for the curve
		// used to save last index of the curve point before next segmet.
		int last = 0;
		PointData pi, pi1, p_ink, pIntersection;
		double[] temp;
		double error = 0;
		// for each section i will do the following
		for (i = 0; i < polygon.size() - 1; i++) {
			beginSegment=true;
			//g.setColor(Color.getHSBColor((float) (i ) / polygon.size(),
				//	(float) 0.7, (float) 0.8));
//			logger.info("segment number "+i+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			// get the first and last point in the segment .
			pi = polygon.get(i);
			pi1 = polygon.get(i + 1);
			// for each segment of the curve

	
			
			// compute the length from the line to the curve
			
                int nopoints=ink.getPointsCount();
			for ( j = last; j < nopoints ; j++ ) {
			
				
				// check if the point will have a perpendicular line to this
				// segment is within the line segment
				p_ink = ink.getPoint(j);
				
				// get distance from this point to the approximated line
				// segment.
				temp = ComputationsGeometry.distance2(pi, pi1, p_ink);

				//pIntersection = new PointData(temp[1], temp[2]);
			
				
				double[] intersection = new double[2];
				// test to check if the intersection lies in the approximated
				// segment or not.
				int test = ComputationsGeometry.findLineSegmentIntersection(
						pi.x, pi.y, pi1.x, pi1.y, p_ink.x, p_ink.y, temp[1],
						temp[2], intersection);
		           
				if (test == 0) {
					
					
					if (beginSegment==true){
						// this is the beging of the segment and i must not break
						// i must add the segment to the rest of the segment  
						
						
						
						
					}
					else{
					
					
					// it the segments intersect outside then the point must be
					// computed in the next segment
					last = j+1;
					
//					logger.info("test = "+0+" at the point "+j+" ("
//							+ this.getClass().getSimpleName() + "    "
//							+ (new Throwable()).getStackTrace()[0].getLineNumber()
//							+ "  )  ");
					//g.setColor(Color.orange);
					//DrawPoint(g, ink.getPoint(j+1));
					
					break;
					}
				}// if
				else{
					beginSegment=false;  // i have not got out from the first point on the list
				}
				///DrawLine(g, pIntersection, p_ink);//first one 
				// first check if it not a number or infinite then added it to
				// error
				if (!Double.isNaN(temp[0])) {
					if (!Double.isInfinite(temp[0])) {
						error += temp[0] * temp[0];
					}
				}

			}// for ink

			// get the or the inc dec of the segments

		}// for polygon .

		return Math.sqrt(error);
	}

	public void CreateLineSegment(int i) {

		// SegmentsType
		int j = this.getNextVertex(i + 1);
		if (j > -1) {
			Line temp = new Line(problemStroke.getPoint(i), problemStroke
					.getPoint(j));
	 
			temp.setIStart(i);
			temp.setIEnd(j);
			temp.setErrorComputed( getXiXjError(i, j, problemStroke.getPoint(i), problemStroke
					.getPoint(j)));
			SegmentsType.add(temp);
		}

	}

	public void InitSegments() {

		SegmentsType = new ArrayList<GeometricPrimitive>();
		segmentUsed = true;

	}

	public double getStrokeBasedThreshold() {

		
		setStrokeBasedTolerance();
//		 if (eTolerance> HybirdErrorToleranceThreshold)
//			 eTolerance= eTolerance/2.0;
		
		if (eTolerance> HybirdErrorToleranceThreshold)
			eTolerance= HybirdErrorToleranceThreshold;
		return eTolerance;
	}

	public Segment getSegmentOfIndex(int i) {
		Segment temp=null;
	
		if (segmentUsed) {
			if (SegmentsType != null) {
				
				
				int vi,vj;
				vi=SegmentsType.get(i).getIStart();
				vj=SegmentsType.get(i).getIEnd();
//				logger.info("vi "+vi+"  vj=  "+vj+" ("
//						+ this.getClass().getSimpleName() + "    "
//						+ (new Throwable()).getStackTrace()[0].getLineNumber()
//						+ "  )  ");
				PointData p = (PointData) problemStroke.getPoint(vi).clone();
				 temp = new Segment(problemStroke.createSubInkObject(vi, vj));

				// then addd the vertix

			
				temp.setStartPoint(p);

				p = (PointData) problemStroke.getPoint(vj).clone();
				temp.setEndPoint(p);

				
				// Segpoints.add();
				temp.setSegmentType(SegmentsType.get(i));
				
				
				
			
				

			}
		}
		else 
		{
			 temp = super.getSegmentOfIndex(i);
		}

		return temp;
	}

	public int getSegmentsCount() {
		if (segmentUsed) {
			if (SegmentsType != null) {
				return SegmentsType.size();

			}
		} 
		else
			return polygonVertices.size();

		return 0;
	}
	public int getSegmentLastVertix(){
		if (segmentUsed) {
			if (SegmentsType != null) {
				
				return SegmentsType.get(SegmentsType.size()-1).getIEnd();
				

			}
		} 
		return 0;
	}

	@Override
	public void CheckLineSlopes() {
	    // check all segments of this solution
		
		if (SegmentsType!=null){
//			logger.info(" size of the segments type is     SSSSSSSSSSSSSSSSSSSSs     "+this.SegmentsType.size());
		for (int k = 0; k <this.SegmentsType.size()-1; k++) {
		
		// get segments types 
				GeometricPrimitive T1 = this.SegmentsType.get(k);
				GeometricPrimitive  T2= this.SegmentsType.get(k+1);
				if (isMergableLines(T1, T2))
				{
		
					int i=T1.getIStart();
					int j=T2.getIEnd();
			 			// these lines will be merged 
					Line temp=new Line(problemStroke.getPoint(i), problemStroke
							.getPoint(j));
					temp.setIStart(i);
					temp.setIEnd(j);
					temp.setErrorComputed( getXiXjError(i, j, problemStroke.getPoint(i), problemStroke
							.getPoint(j)));
					
					this.particlePoints[T1.getIEnd()]=0;
					this.particlePoints[T2.getIStart()]=0;
					
					SegmentsType.set(k, temp);
					SegmentsType.remove(k+1);
					k--;
					
					
				}
			
		}
		
		//this.calculateSolutionParameters();
		
		}
		this.calculateSolutionParameters();
		//logger.info(" size of the segments type  AFter  is     SSSSSSSSSSSSSSSSSSSSs     "+this.SegmentsType.size());
		
	}
//    private boolean isMergableLines(GeometricPrimitive  p1, GeometricPrimitive p2)
//    {
//    	if (p1 instanceof Line) {
//			Line l1 = (Line) p1;
//			if (p2 instanceof Line) {
//				Line l2 = (Line) p2;
//				
//				// if true then check if same has the same slope
//				
////				
////				double m1=l1.slope();
////				double m2=l2.slope();
//			//	logger.info( " now check if lines are parallellllllllllllllllllllllllllllllll");
//				boolean temp=l1.isParallel(l2);
//				//logger.info( " result they arey "+temp+"    paraallel");
//				 return temp;
//				
//				
//			}
//			
//			
//		}
//    	
//    	
//    	
//    	return false;
//    	
//    }
	public double getComparableError(){
		return this.error();
	}
}
