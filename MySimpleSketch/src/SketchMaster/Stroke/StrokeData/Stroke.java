package SketchMaster.Stroke.StrokeData;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;




import SketchMaster.system.SystemSettings;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.SegmentedShape;
import SketchMaster.gui.DrawingDebugUtils;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.system.SystemSettings;

/**
 * @author Mahi
 */
public class Stroke extends SimpleInkObject implements Serializable, GuiShape {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Stroke.class);
    private static boolean debug=false;
	// private PointData[] points;

	public static double RectangleLoopThreshold = 50;
	private transient static boolean onLine = SystemSettings.OnLineComputations;
    private boolean interpolated=false;
	/**
	 * 
	 */
   
	private static final long serialVersionUID = -4866211701068294061L;

	private PointData StartPoint = null;

	private PointData EndPoint = null;

	private boolean dirty = true;// dirty mean not finish editing.

	private transient StrokeStatisticalData StatisticalInfo = null; // stroke


	// computed
																	// StatisticalInfo

	public Stroke() {
		StatisticalInfo = new StrokeStatisticalData();
		StatisticalInfo.initAll();
		points = new ArrayList<PointData>();

	}

	public   Stroke(SimpleInkObject ink) {
		// get all info from theh simple ink to the stroke 
		
		this.points=new ArrayList<PointData>();
		int noPoints=ink.getPointsCount();
		for (int i = 0; i < noPoints; i++) {
			points.add((PointData)ink.getPoint(i).clone());
			
		}
		StatisticalInfo = null;
		this.setStartPoint((PointData)points.get(0).clone());
		this.setEndPoint((PointData)ink.getPoint(noPoints-1).clone());
	
		//StatisticalInfo.initAll();
		
		
	}

	public Stroke InterpolatePoints(){
		if (this.points.size()>0){
		Stroke NewInterploated=new Stroke();
		  ArrayList pointsa = new ArrayList<PointData>();
		 
	        PointData prev =this.points.get(0);
	        PointData point=null;
	        NewInterploated.addPoint(prev);
	        NewInterploated.setStartPoint(prev);
	        
	        if (debug)
	        {
	        	if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
	        		
	        		DrawingDebugUtils.drawPointPath(DrawingDebugUtils.getGraphics(), DrawingDebugUtils.InkColor, DrawingDebugUtils.PointsColor, this.points);
	        		
	        	}
	        }
	        
	        
	     //   pointsa.add(prev);
	        for(int i = 1; i < points.size(); i++)
	        {
	             point = points.get(i);
	            double dist = point.distance(prev);
	            if(dist > SystemSettings.MaxInterplotionLength)
	            {
	                int n = (int)Math.floor(dist / SystemSettings.MaxInterplotionLength);
	                double theta = prev.getAngle(point)  ;
	                long tStep = (point.getTime() - prev.getTime()) / (long)n;
	                double pStep = (point.getPresureValue() - prev.getPresureValue()) / n;
	                for(int j = 1; j < n + 1; j++)
	                {
	                    PointData p = new PointData(prev.getX() + (double)j * SystemSettings.MaxInterplotionLength * Math.cos(theta),
	                    		prev.getY() + (double)j * SystemSettings.MaxInterplotionLength * Math.sin(theta),
	                    		prev.getTime() + tStep * (long)j,
	                    		(int)((double)prev.getPresureValue() + pStep * (double)j));
	                    NewInterploated.addPoint(p);
	                  //  pointsa.add(p);
	                }
	                prev = point;
	  		            //NewInterploated.addPoint(point);

	            }
	            else{  // point is less than the interploaiton predected 
	            	// skip this point 
	            	//prev = point;
		      //      NewInterploated.addPoint(point);
	            }
	            
	        }
	        if (point!=null)
	        	NewInterploated.setEndPoint(point );
	        NewInterploated.setInterpolated(true);
	        if (debug)
	        {
	        	if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
	        		
	        		DrawingDebugUtils.drawPointPath(DrawingDebugUtils.getGraphics(), Color.cyan, Color.MAGENTA,NewInterploated.points);
	        		
	        	}
	        }
	        return   NewInterploated;
		
		}
		else{
			
			return this;
		}
		
		
	}
	
	
	
	/**
	 * @param endPoint
	 *            the endPoint to set
	 * @uml.property name="endPoint"
	 */
	public void setEndPoint(PointData EndPoint) {
		this.dirty = false;
		this.EndPoint = EndPoint;
  
		setLoopingEnd(this.StartPoint, this.EndPoint);

		if (!onLine) {
			updateStatiscal();
		}

		getStatisticalInfo().updateBatchFunctions();
	}

	public void setLoopingEnd(PointData start, PointData end) {
		if (start==null)
			if (points!=null)
				{
				if (points.size()>0)
					start=points.get(0);
		
			else 
				return ;
				}
			else return;
		
		Rectangle2D r = new Rectangle2D.Double(start.getPointLocation().getX(),
				start.getPointLocation().getY(), 0, 0);
		r.add(end.getPointLocation());
		double area = r.getWidth() * r.getHeight();

		if (area < RectangleLoopThreshold) {
			// add the start point to last point in the points array
			points.add((PointData) start.clone());
			this.EndPoint = start;

		}
	}

	/**
	 * @param startPoint
	 *            the startPoint to set
	 * @uml.property name="startPoint"
	 */
	public void setStartPoint(PointData StartPoint) {
		this.StartPoint = StartPoint;
		this.dirty = true;
	}

	public void addPoint(PointData point) {
		// System.out.println(point);
		// / adding point to the array
		this.points.add(point);
		if (onLine) {
			// updateBoundingBox(point);
			// if (onLine)
			// updateStatiscalData(point);
			updateStatiscal(point);
		}
	}

	private void updateStatiscal(PointData point) {
		StatisticalInfo.updateFunctions(point, this);
		StatisticalInfo.updateBoundingBox(point, this);
	}

	private void updateStatiscal() {
		StatisticalInfo.updateFunctionsAndBox(this);
	}

	// @Deprecated
	// private void updateStatiscalData(PointData point){
	//		
	//	  
	// if (points.size()==1)
	// {
	// distanceCalculationFeature function
	// =(distanceCalculationFeature)StatisticalInfo.getDistance().getFunc();
	// function.setNoPointInStroke(points.size());
	// // initalize the functions
	// StatisticalInfo.getDistance().updateFunctionWithPoint(point, this);
	// StatisticalInfo.getSpeed().updateFunctionWithPoint(point, this);
	// StatisticalInfo.getTimeDiff().updateFunctionWithPoint(point, this);
	// StatisticalInfo.getDirection().updateFunctionWithPoint(point, this);
	// curvatureCalculateionFeature funct =
	// (curvatureCalculateionFeature)StatisticalInfo.getCurvature().getFunc();
	// funct.setStroke(this);
	// StatisticalInfo.getCurvature().updateFunctionWithPoint(point, this);
	// }
	// if (points.size()>=3)
	// {
	// distanceCalculationFeature function
	// =(distanceCalculationFeature)StatisticalInfo.getDistance().getFunc();
	// function.setNoPointInStroke(points.size());
	// // start calcuating
	// StatisticalInfo.getDistance().updateFunctionWithPoint(point, this);
	//			
	// StatisticalInfo.getSpeed().updateFunctionWithPoint(point, this);
	// StatisticalInfo.getTimeDiff().updateFunctionWithPoint(point, this);
	// StatisticalInfo.getDirection().updateFunctionWithPoint(point, this);
	// curvatureCalculateionFeature funct =
	// (curvatureCalculateionFeature)StatisticalInfo.getCurvature().getFunc();
	// funct.setStroke(this);
	// StatisticalInfo.getCurvature().updateFunctionWithPoint(point, this);
	// //StatisticalInfo.getCurvature().updateFunctionWithPoint(point, this);
	//			
	// // now every function is updataed
	// }
	//		
	// }
	// private void updateBoundingBox(PointData point){
	//		
	// //if this is the first point then set it with the rectangle both
	// //top left and right low corners
	// if (points.size()==1)
	// {
	// Rectangle r=new Rectangle();
	// r.add(point.getPointLocation());
	// if (StatisticalInfo.getBox()!=null)
	// StatisticalInfo.getBox().setBounds(r);
	// else
	// StatisticalInfo.setBox(r);
	// }
	// // else this is just a new point i have to check
	// else if (points.size()>1){
	// Rectangle currentrectangle= StatisticalInfo.getBox();
	//				
	// //1 )if it is out of the current rectangle or in
	// if(currentrectangle.contains(point.getPointLocation()))
	// {
	// //it is contain
	// // if in do no action
	// }
	// else
	// {
	// // if out
	// //add this point to the rectangle to create a larger one
	// currentrectangle.add(point.getPointLocation());
	//					
	// }
	//			
	//			
	//			
	//		
	// }
	//		
	//		
	// }
	public void calculateStrokeData() {
		// / if the points exist without using the
		// add functions .
		// first compute the bounding box
		//
		
		if (StatisticalInfo == null){
		//	logger.info("genereating the statistical data at firest");
			StatisticalInfo = StrokeStatisticalData.BuildStorkeData(this);
		}
		
		computeRemainigStatists();
		generateAllDominatePoints();
		// StatisticalInfo.getData();
		// for (int i = 0; i < points.size(); i++) {
		//		
		// }
	}

	public void clearAllPoints() {
		this.points.clear();
		StatisticalInfo.clear();
	}

	public String toString() {
		String s = "";

		s = " Start Point " + points.get(0).toString() + "   points are  =  "
				+ points.toString();

		return s;

	}

	public void drawStroke(Graphics g) {
		// for (int i = 0; i < points.size() - 1; i++) {
		// g.drawLine((int) ((PointData) points.get(i)).getX(),
		// (int) ((PointData) points.get(i)).getY(),
		// (int) ((PointData) points.get(i + 1)).getX(),
		// (int) ((PointData) points.get(i + 1)).getY());
		// }
		
		drawStroke(g, Color.BLUE, Color.RED);
//		if (DrawingDebugUtils.DEBUG_GRAPHICALLY)
//			drawStroke(DrawingDebugUtils.getGraphics(),Color.BLUE, Color.RED);
	}

	public void drawStroke(Graphics g, Color color) {
		g.setColor(color);

		for (int i = 0; i < points.size() - 1; i++) {
			g.drawLine((int) ((PointData) points.get(i)).getX(),
					(int) ((PointData) points.get(i)).getY(),
					(int) ((PointData) points.get(i + 1)).getX(),
					(int) ((PointData) points.get(i + 1)).getY());
		}// for
	}// draw stroke

	public void drawStroke(Graphics g, Color linecolor, Color pointColor) {
		g.setColor(linecolor);
		
		// System.out.println( " the color is "+linecolor.toString());
		for (int i = 0; i < points.size() - 1; i++) {
			g.drawLine((int) ((PointData) points.get(i)).getX(),
					(int) ((PointData) points.get(i)).getY(),
					(int) ((PointData) points.get(i + 1)).getX(),
					(int) ((PointData) points.get(i + 1)).getY());
			
			
			if (SystemSettings.DrawPoints)
			{
				g.setColor(Color.BLUE);
				g.drawRect((int) ((PointData) points.get(i)).getX(),
						(int) ((PointData) points.get(i)).getY(),
						2,2);
				g.fillRect((int) ((PointData) points.get(i)).getX(),
						(int) ((PointData) points.get(i)).getY(),
						2,2);
			}
		}

		PointData temp;
		// g.setColor(Color.green);
		if (StatisticalInfo != null) {
			
		}// if of teh StatisticalInfo check

		if (StatisticalInfo != null) {

			// now draw the rectangle of bounds
			if (StatisticalInfo.getBox() != null && (!dirty)) {

				if (SystemSettings.STATISTICAL_POINTS_DRAW)
					StatisticalInfo.paint(g);
				// System.out.println( " x "+StatisticalInfo.getBox().getX()+" y
				// "+StatisticalInfo.getBox().getY());
				g.setColor(Color.GRAY);
				g.drawRect((int) StatisticalInfo.getBox().getX()-1,
						(int) StatisticalInfo.getBox().getY()+1,
						(int) StatisticalInfo.getBox().getWidth()+1,
						(int) StatisticalInfo.getBox().getHeight()+1);

			}
		}
	}// close of paint

	/**
	 * @return the startPoint
	 * @uml.property name="startPoint"
	 */
	public PointData getStartPoint() {
		return StartPoint;
	}

	/**
	 * @return the endPoint
	 * @uml.property name="endPoint"
	 */
	public PointData getEndPoint() {

		return EndPoint;
	}

	public long getStrokeTime() {
		long returnlong = (EndPoint.getTime() - StartPoint.getTime());
		return returnlong;
	}

	/**
	 * @return the StatisticalInfo
	 * @uml.property name="StatisticalInfo"
	 */
	public StrokeStatisticalData getStatisticalInfo() {
		if (StatisticalInfo == null) {
			calculateStrokeData();

		}
		return StatisticalInfo;
	}

	/**
	 * @param StatisticalInfo
	 *            the StatisticalInfo to set
	 * @uml.property name="StatisticalInfo"
	 */
	public void setStatisticalInfo(StrokeStatisticalData data) {
		this.StatisticalInfo = data;
	}

	public void computeAreaOfStroke() {
		// first are then try to get the centroid
		double area = 0.0;

		double a = 0.0;

		PointData pi, pj;

		// System.out.println("------------------------------Error---------
		// -------------------");
		// System.out.println(this.toString());
		// System.out.println("nubmer of polygong in this
		// solution"+polygonVertices.size());
		// for (int i = 0; i < points.size()-1; i++) {
		// //now i
		// pi=points.get(i);
		// // {
		// pj=points.get(i+1);
		// a = (pi.getX()*pj.getY())-(pj.getX()*pi.getY());
		// area+=a;
		//        
		//		
		// }
		// area*=0.5;
		area = ComputationsGeometry.computeArea(points);
		getStatisticalInfo().setArea(area);
	}

	public void computeRemainigStatists() {
		computeAreaOfStroke();
	}

	public void generateAllDominatePoints() {
		getStatisticalInfo().generateAllDominatePoints();

		// //System.out
		// // .println("---------------Compute critical
		// point-----------------");
		// FileLog
		// .addString("---------------get critical point-----------------");
		// // corners,bounding box ....
		// // StrokeLib.computeSpeedCriticalPoints(Evt.getEventStroke());
		// // StrokeLib.computeCurvatureCriticalPoints(Evt.getEventStroke());
		//			
		// //for sped StatisticalInfo get min of a certian threhold.
		// // for speed make it the average of spped as threshold
		// ArrayList indeces=null;
		// // StrokeStatisticalData tempData=getData();
		// ArrayList tempD=new ArrayList();
		//			
		// FeatureFunction func=getStatisticalInfo().getSpeed();
		// //
		// //
		//			
		//		
		//	
		// func.setDataThreshold(func.getAverage());
		//			
		// tempD=new ArrayList();
		// //
		// indeces=func.calcuateLocalMinPoints();
		//			
		// getFunctionDominatePoints(indeces, null, tempD);
		// //////////////////////finish speed now get time
		//					
		// func=StatisticalInfo.getTimeDiff();
		//		
		//
		// func.setDataThreshold(func.getAverage());
		// ArrayList indeces2=func.calcuateLocalMaxPoints();
		//			
		// //
		// getFunctionDominatePoints(indeces2, indeces, tempD);
		//			
		//
		//			
		// ////////////////now the direction
		// func=StatisticalInfo.getDirection();
		// func.setDataThreshold(func.getAverage());
		//
		// ArrayList indeces3=func.calcuateLocalMaxPoints();
		// getFunctionDominatePoints(indeces3, indeces, tempD);
		//			
		//			
		// ///////////////////now checck curvature
		//			
		// func=StatisticalInfo.getCurvature();
		// func.setDataThreshold(func.getAbsaverage());
		// System.out.println("the avergaeof curvature is "+func.getAverage());
		// System.out.println("the threshold average is "
		// +func.getAbsaverage());
		// ArrayList tempindeces4=func.calcuateLocalAbsolutePoints(1);
		// ArrayList indeces4= new ArrayList();
		// int orignal=0,newi;
		// for (int i = 0; i < tempindeces4.size(); i++) {
		// orignal=(Integer)tempindeces4.get(i);
		// newi=orignal+SystemSettings.STROKE_CONSTANT_NEIGHBOURS;
		// if (newi>=points.size())
		// indeces4.add(0);
		// else
		// indeces4.add(newi);
		//				
		// }
		// getFunctionDominatePoints(indeces4,indeces, tempD);
		//			
		//
		// StatisticalInfo.setDominatePoints(tempD);
		//			
		// System.out.println("number of dominat points is "+tempD.size());
	}

	@Deprecated
	private void addSorted(ArrayList list, int index) {
		int temp;
		for (int i = 0; i < list.size(); i++) {
			temp = (Integer) list.get(i);

			if (temp > index) {
				list.add(i + 1, index);
				return;
			}
		}
		// upp till now it is the largest
		// add it now
		list.add(index);
	}

	@Deprecated
	private boolean indexExist(ArrayList list, int index) {
		int temp;
		for (int i = 0; i < list.size(); i++) {
			temp = (Integer) list.get(i);
			if (temp == index)
				return true;
			if (temp > index)
				return false;
		}
		return false;
	}

	@Deprecated
	private ArrayList getFunctionDominatePoints(ArrayList indeces3,
			ArrayList except, ArrayList tempD) {
		PointData point;

		// ArrayList tempD1;

		// tempD1=new ArrayList();
		if ((indeces3 != null)) {
			// System.out.println("frome time "+indeces2.size());
			if (except != null) {

				for (int i = 0; i < indeces3.size(); i++) {
					if (indexExist(except, (Integer) indeces3.get(i))) {
						addSorted(except, (Integer) indeces3.get(i));
						point = points.get((Integer) indeces3.get(i));
						tempD.add(point);
					}

				}
			} else {
				for (int i = 0; i < indeces3.size(); i++) {

					point = points.get((Integer) indeces3.get(i));
					tempD.add(point);

				}

			}
		}
		return tempD;

	}

	/** @link dependency */
	/* # BasicImageStructure lnkBasicImageStructure; */

	public void paint(Graphics2D g) {
		drawStroke(g);
	}

	public void setParam(ArrayList Param) {

	}

	// public InkObject createSubInkObject(int start, int end){
	//		
	// // return a segment from this stroke that will contain the points of the
	// stroke.
	// return createSubSegment(start,end);
	//	
	// }
	@Deprecated
	public Segment createSubSegment(int start, int end) {
		// return a segment from this stroke that will contain the points of the
		// stroke.
		return null;

	}

	@Deprecated
	public SegmentCluster createSubSegments() {
		// from fit or dividtion
		return null;
	}

	@Deprecated
	public SegmentCluster createSubSegments(int count) {
		// each count points create a new segment and add then to the stroke
		return null;
	}

	@Deprecated
	public SegmentCluster createSubSegments(ArrayList vertices) {
		return null;
	}

	public ArrayList<Segment> createSubSegments(SegmentedShape segmentShape) {
		// set subSegments......

		subSegments = new ArrayList<Segment>();
		Segment temp;
		// get segments count
		int Segmentscount = segmentShape.getSegmentsCount();
		if (logger.isDebugEnabled()) {
			//  logger.debug("createSubSegments(SegmentedShape) - @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@----  segment `count is " + Segmentscount + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		// for each shape
		for (int i = 0; i < Segmentscount; i++) {

			temp = segmentShape.getSegmentOfIndex(i);

			subSegments.add(temp);

		}
		return subSegments;
	}

	private ArrayList<Segment> subSegments;

	@Override
	public InkInterface createSubInkObject(int start, int end) {
		// {
		//	
		//	
		//	
		Stroke ink = new Stroke();
		ArrayList<PointData> po = new ArrayList<PointData>();
		if (this.points != null) {
//			System.out.println("   number of ponit in this stroke = "+this.points.size()+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			for (int i = start; (i < this.points.size()) && (i < end); i++) {
				
				po.add(this.points.get(i));
			}

		}
		ink.setPoints(po);
		// return a segment from this stroke that will contain the points of the
		// stroke.
		return ink;
		//
		// }

	}

	/**
	 * @return the interpolated
	 */
	public boolean isInterpolated() {
		return interpolated;
	}

	/**
	 * @param interpolated the interpolated to set
	 */
	public void setInterpolated(boolean interpolated) {
		this.interpolated = interpolated;
	}

	public void wirte(Logger log) {
		String str="Stroke has "+ points.size() +" points = [";
		 for (int i = 0; i < this.points.size(); i++) {
		str+="P("+points.get(i).x+","+points.get(i).y+"),";
	}
		 log.info(str);
		 log.info(" Bonding box is  = Corner P("+ this.getBox().getX()+", "+this.getBox().getY()+"), w= "+ this.getBox().getWidth()+", h=  "+this.getBox().getHeight());
		 
	}

	
	
	
    /**
     * Transform the polyline with the given transform.
     */


}
