package SketchMaster.Stroke.StrokeData;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;




import SketchMaster.system.SystemSettings;
import SketchMaster.Stroke.features.FeatureFunction;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.Line;
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
	protected ArrayList<PointData> Orginalpoints = null;
	/**
	 * 
	 */
   // ArrayList<pointChange>  changes=null;
    ArrayList<Integer>  SortedXIndex=null;
    ArrayList<Integer>  SortedYIndex=null;
	int OverTraceHyposes=0;
	ArrayList<Integer> OverTracePoints=null;
	ArrayList<Point >OverTracePair=null;
    
    ArrayList<Integer>  turnsIndex=null;
      ArrayList<Integer> SortedPointIndex=null;
    //ArrayList<Double>  DistantFromStart=null;
 //   ArrayList<Rectangle2D>  boxes=null;
    double NDDE;
	double DCR;
	double LongestChordtoLengthRatio;
	double EndPointtoLengthRation;
	double LongestDistanceBetweenPointsInStroke;
		private static final int ORIENTATION_VERTICAL = 0;
	final int  ORIENTATION_HORIZONATAL=1;
	int Orientation=ORIENTATION_HORIZONATAL;
	boolean OverTraced=false;
	boolean TailRemoved=false;
	ArrayList<Integer> TailPartIndex=null;
   
	private static final long serialVersionUID = -4866211701068294061L;
	//private static final int MaxChangeIndex = 10;
	private static final int	DEFAULT_NEIGHBOURS	= 3;
	


	private PointData StartPoint = null;

	private PointData EndPoint = null;

	private boolean dirty = true;// dirty mean not finish editing.

	private transient StrokeStatisticalData StatisticalInfo = null; // stroke


	private PointData MaxDirection;
	private PointData MinDirection;
	private boolean	OverTracePointsDeleted=false;
	private boolean	OverTraceRemoved=false;
	
	// this window is used in testproximity to check if there is over lap
	//surronding pixels and check that are not ins the same locatinos... 
	int 		window=SystemSettings.WINDOW_SCAN_SIZE;
	// distance that decide if the points are near or not. (distance range for testproximity function)
	double LocationRange=	SystemSettings.LOCATION_RANGE;
	
	// computed
																	// StatisticalInfo

	public Stroke() {
		StatisticalInfo = new StrokeStatisticalData();
		StatisticalInfo.initAll();
		points = new ArrayList<PointData>();
		if (SystemSettings.USE_NEW_CHANGES){
		   SortedXIndex=new ArrayList<Integer>();
		      SortedYIndex=new ArrayList<Integer>();
		      SortedPointIndex=new ArrayList<Integer>();
		}
		  //    DistantFromStart=new ArrayList<Double>();
	}

	public   Stroke(SimpleInkObject ink) {
		// get all info from theh simple ink to the stroke 
		
		this.points=new ArrayList<PointData>();
		int noPoints=ink.getPointsCount();
		StatisticalInfo = null;
		this.setStartPoint((PointData)points.get(0).clone());
		this.setEndPoint((PointData)ink.getPoint(noPoints-1).clone());
		//this.addPoint( (PointData)ink.getPoint(i).clone());
		for (int i = 0; i < noPoints; i++) {
		//	points.add((PointData)ink.getPoint(i).clone());
	     this.addPoint( (PointData)ink.getPoint(i).clone());		
			
		}
		
	
		//StatisticalInfo.initAll();
		
		
	}

	public Stroke InterpolatePoints(){
		
		
		
		if (getPoints().size()>0){
		Stroke NewInterploated=new Stroke();
		//  ArrayList pointsa = new ArrayList<PointData>();
		 
	        PointData prev =getPoints().get(0);
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
	        for(int i = 1; i < getPoints().size(); i++)
	        {
	             point = getPoints().get(i);
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
	        		
	        		DrawingDebugUtils.drawPointPath(DrawingDebugUtils.getGraphics(), Color.cyan, Color.MAGENTA,NewInterploated.getPoints());
	        		
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
          
		if (SystemSettings.USE_NEW_CHANGES){
		logger.info( "  now all point are intered i need to display the sort list of x an y ");
		logger.info( " points   "+getPoints());
		logger.info(" $%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5");
		logger.info("SortedXIndex    " + SortedXIndex);
		logger.info(" $%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5");
		logger.info(" SortedYIndex   "+ SortedYIndex);
		//setLoopingEnd(this.StartPoint, this.EndPoint);

		}
		if (!onLine) {
			updateStatiscal();
		}
	//	else {
		getStatisticalInfo().updateBatchFunctions();
		//}
	}

	private void updateOtherFeatures() {
		ArrayList<PointData> pointsTemp = getPoints();
		//get the maximum hightest direction value,
		
		ArrayList<FeatureFunction> function = getStatisticalInfo().getFunctions();
		FeatureFunction direction=null;
		for (int i = 0; i <function.size(); i++) {
			//get the direction 
			if (function.get(i).getName().startsWith("Direction")){  // direction is better. 
			//if (function.get(i).getName().startsWith("Slope")){
				direction=function.get(i);
			}
		}
		
	// change try using 
	//	direction=getStatisticalInfo().getCurvatureRotation();
			//NDDE
		if (direction!=null){
			
			int max=direction.getMaxLocation();
			int min=direction.getMinLocation();
			// compute dcr 
	 
			DCR=direction.getMax()/direction.getAverage();
			
			logger.info("  the dcr is  =  "+DCR);
			//double revolution=TotalRotation()/(2.0*Math.PI);
			logger.info("  the total rotation of the  = " + TotalRotation());
			logger.info( " The sum of the  drection graph is  "+direction.getSumUpNow() );
			
			logger.info("  revolution is   "+revolution);
			MaxDirection=pointsTemp.get(max);
			MinDirection=pointsTemp.get(min);
			
		double distance=pointsTemp.get(min).distance(pointsTemp.get(max));
		
		logger.info(" the point of max direction is "+pointsTemp.get(max)+" and point of min direction is "+pointsTemp.get(min));
		logger.info(" distance between them is  "+distance);
		
		NDDE=Math.abs(distance/this.getLength());
		
		logger.info(" NDDE is = "+NDDE);
		
		}
		
		
	
		
	}

	@Deprecated
	public void setLoopingEnd(PointData start, PointData end) {
		ArrayList<PointData> pointsTemp = getPoints();
		if (start==null)
			if (pointsTemp!=null)
				{
				if (pointsTemp.size()>0)
					start=pointsTemp.get(0);
		
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
			pointsTemp.add((PointData) start.clone());
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
		// logger.info(point);
		ArrayList<PointData> pointsTemp = getPoints();
		// / adding point to the array
		pointsTemp .add(point);
		// after add point chek that this point 
		if (SystemSettings.USE_NEW_CHANGES){
		addPointToSortedLists(point);
		logger.info(" this is poing number "+pointsTemp.size());
		//addPointToIncDec(point);
		}
	//	addPointDistance(point);
		if (onLine) {
			// updateBoundingBox(point);
			// if (onLine)
			// updateStatiscalData(point);
			updateStatiscal(point);
		}
	}


//	private void addPointDistance(PointData point){
//		Double dis;
//		
//		if (StartPoint==null)
//			return;
//		 dis=StartPoint.distance(point);
//		//this.DistantFromStart.add(dis);
//	}
	
	
	private int addToList(  ArrayList<Integer>  list, double value, int type ){
		ArrayList<PointData> pointsTemp = getPoints();
		if ( pointsTemp.size()==1){
			// this is the first point... 
			// do the follwoing 
			list.add(0);
		}
		
	 
		int newIndexpoint=BinarySearch(list,value, type);
		 //  logger.info( "  the index found by binary search is "+newIndexpoint);
			if (newIndexpoint==-1){
				// add at the begining 
				list.add(0,  pointsTemp.size()-1);
			}
			else if (newIndexpoint==-2){
				list.add( pointsTemp.size()-1);
			}
			else {
				list.add(newIndexpoint,  pointsTemp.size()-1);
			}
			
		   return newIndexpoint;
		
		
	}
	private boolean testProximity(PointData point, int insertLoc, ArrayList<Integer> list, Point pairmag){
		ArrayList<PointData> pointsTemp = getPoints();
		if (insertLoc>0){// this is insert is in the middle then i need to look for the

			// get the list from windo to window 
			//first make sure that the size is ok 
			int b=insertLoc-window;
			int e=insertLoc+window;
			// now make sure that b >0 and e < size 
			if (b<0)  b=0;			
			if (e>list.size()) e=list.size();
			int pointIndex=pointsTemp.size()-1;
			int in;
			for (int i = b; i < e; i++) {
				in=list.get(i);
				if (pointIndex-in<window){
					continue;
				}
				// get the index of the
				PointData tempPoint = pointsTemp.get(in);
			
				if (tempPoint.isNearPoint(point, LocationRange))
				{
						logger.info( " NEEEARRRRRRRRRRRRR" );
								logger.info(" the distance between points of index of  "+(pointsTemp.size()-1)+" and is  "+point);
						logger.info("  with tempoint has index of   "+ list.get(i)+"     and is" +tempPoint);
						
						pairmag.setLocation(pointIndex, in);
						logger.info("  the distance is "+point.distance(tempPoint));
						
						return true;
				}
				
				
			}
			
			
			
			
		}
		
		return false;
	}

	private void addPointToSortedLists(PointData point){
		
		double x,y;
		x=point.x;
		y=point.y;
		double loc=point.magnitude();
		ArrayList<PointData> pointsTemp = getPoints();

//	if (testProximity(point, insertLoc, SortedXIndex)	 ){
//		
//	
//		OverTraceHyposes++;
//		 if (OverTracePoints==null){
//			 OverTracePoints=new ArrayList<Integer>();
//		 }
//		 OverTracePoints.add(pointsTemp.size()-1);
//	}
		
		Point  pairx=new Point(0,0);
		 		 int insertLocx=addToList( SortedXIndex,x, 0);
		 		 
				 boolean testx=testProximity(point, insertLocx, SortedXIndex, pairx)	;
		 int insertLocy= addToList(SortedYIndex, y, 1);
			Point  pairy=new Point(0,0);
		 
		 boolean testy=testProximity(point, insertLocy, SortedYIndex,pairy)	;
			Point  pairmag=new Point(0,0);
		 int insertLocloc= addToList (SortedPointIndex,loc, 2);
		 
		 boolean testmag=testProximity(point, insertLocloc, SortedPointIndex, pairmag)	;
		 
		 if (testx&testy || testy&testmag || testmag &testx ){
				logger.info( " test correct...........   ."+pointsTemp.size());
				
				OverTraceHyposes++;
				 if (OverTracePoints==null){
					 OverTracePoints=new ArrayList<Integer>();
					 OverTracePair=new ArrayList<Point>();
				 }
				 OverTracePoints.add(pointsTemp.size()-1);
				 pointsTemp.get(pointsTemp.size()-1).setOvertrace(true);
				 if (testx){
					 
					 OverTracePair.add(pairx);
				 }
				 else if (testy){
					 OverTracePair.add(pairy);
				 }
				 else {
					 OverTracePair.add(pairmag);
				 }
				 
			}
		 
		 
	}

	private double getTestValue (ArrayList<Integer> arr, int index, int type ){
		ArrayList<PointData> pointsTemp = getPoints();
	     double testValue=0;
		 if (type==0){
	            testValue=pointsTemp.get( arr.get(index)).x;
	        }
	            else if (type==1){ 
	                testValue=pointsTemp.get( arr.get(index)).y;
	            }
	            else if (type==2){ 
	                testValue=pointsTemp.get( arr.get(index)).magnitude();
	            }
	            else if (type==3){
	            	testValue= arr.get(index);
	            }
		 
		 return testValue;
		
	}
    /**
     * Binary search finds item in sorted array.
     * And returns index (zero based) of item
     * If item is not found returns -1
     * Based on C++ example at
     * http://en.wikibooks.org/wiki/Algorithm_implementation/Search/Binary_search#C.2B.2B_.28common_Algorithm.29
     **/
   private int BinarySearch(ArrayList<Integer> arr, double value, int type)
    {       
        int low = 0, high = arr.size()- 1, midpoint = 0;
        double testValue=0,tpointb,tpointa;
        testValue=getTestValue(arr, high, type);
        if (value>testValue){
        	return -2;
        	}

        testValue=getTestValue(arr, low, type);
        
        
        if (value<testValue){
        	return 0;
        	}
       
        
        
        while (low <= high)
        {
        	//logger.info( "  low is "+low+"  hight is "+high);
            midpoint = (low + high) / 2;
                 testValue=getTestValue(arr, midpoint, type);
             if (midpoint>1&&midpoint<arr.size()-1){

            	  tpointb=getTestValue(arr, midpoint-1, type);
            	  tpointa=getTestValue(arr, midpoint+1, type);
   
             }
             else {
                tpointa=testValue;
            	 tpointb=testValue;
             }

             
            // check to see if value is equal to item in array
            if (value == testValue)
            {                    
                return midpoint;
            }
            else if (value>=tpointb && value<=testValue){
            	return midpoint;
            }
            else if (value>=testValue && value<=tpointa){
            	return midpoint+1;
            }
            else if (value < testValue)
                high = midpoint - 1;
            else
                low = midpoint + 1;
        }

        // item was not found
        return -1;
    }

	private void updateStatiscal(PointData point) {
		StatisticalInfo.updateFunctions(point, this);
		StatisticalInfo.updateBoundingBox(point, this);
	}

	private void updateStatiscal() {
		StatisticalInfo.updateFunctionsAndBox(this);
	}

 
	public void calculateStrokeData() {
		// / if the points exist without using the
		// add functions .
		// first compute the bounding box
		//
		ArrayList<PointData> pointsTemp = getPoints();
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
		ArrayList<PointData> pointsTemp = getPoints();
		pointsTemp.clear();
		StatisticalInfo.clear();
	}

	public String toString() {
		String s = "";
		ArrayList<PointData> pointsTemp = getPoints();
		s = " Start Point " + pointsTemp.get(0).toString() + "   points are  =  "
				+ pointsTemp.toString();

		return s;

	}

	public void drawStroke(Graphics g) {

		drawStroke(g, Color.BLUE, Color.RED);
//		if (DrawingDebugUtils.DEBUG_GRAPHICALLY)
//			drawStroke(DrawingDebugUtils.getGraphics(),Color.BLUE, Color.RED);
	}

	public void drawStroke(Graphics g, Color color) {
		ArrayList<PointData> pointsTemp = getPoints();
		g.setColor(color);

		for (int i = 0; i < pointsTemp.size() - 1; i++) {
			g.drawLine((int) ((PointData) pointsTemp.get(i)).getX(),
					(int) ((PointData) pointsTemp.get(i)).getY(),
					(int) ((PointData) pointsTemp.get(i + 1)).getX(),
					(int) ((PointData) pointsTemp.get(i + 1)).getY());
		}// for
	}// draw stroke

	public void drawStroke(Graphics g, Color linecolor, Color pointColor) {
		g.setColor(linecolor);
		ArrayList<PointData> pointsTemp = getPoints();
		// This draw the storke... 
		for (int i = 0; i < pointsTemp.size() - 1; i++) {
			if ( ((PointData) pointsTemp.get(i)).isDeleted())
				continue;
			
			g.drawLine((int) ((PointData) pointsTemp.get(i)).getX(),
					(int) ((PointData) pointsTemp.get(i)).getY(),
					(int) ((PointData) pointsTemp.get(i + 1)).getX(),
					(int) ((PointData) pointsTemp.get(i + 1)).getY());
			
			
			if (SystemSettings.DrawPoints)
			{	
				if ( ((PointData) pointsTemp.get(i)).isDeleted())
				continue;
				g.setColor(Color.BLUE);
				g.drawRect((int) ((PointData) pointsTemp.get(i)).getX(),
						(int) ((PointData) pointsTemp.get(i)).getY(),
						2,2);
				g.fillRect((int) ((PointData) pointsTemp.get(i)).getX(),
						(int) ((PointData) pointsTemp.get(i)).getY(),
						2,2);
			}
			
			
			
		}
		

		if (SystemSettings.USE_NEW_CHANGES){
			// i want to draw the places of turns....
			 
			 if (OverTracePoints!=null ){
				 
				if (!OverTracePointsDeleted){
				   g.setColor(Color.ORANGE);
				 for (int i = 0; i <OverTracePoints.size(); i++) {
					  int ind =OverTracePoints.get(i);
					 
						g.drawRect((int) ((PointData) pointsTemp.get(ind)).getX(),
								(int) ((PointData) pointsTemp.get(ind)).getY(),
								4,4);
						g.fillRect((int) ((PointData) pointsTemp.get(ind)).getX(),
								(int) ((PointData) pointsTemp.get(ind)).getY(),
								3,3);
					 
				 }
				}
				 
			 }
			 
			
			
		}
		
		
		PointData temp;
		if (SystemSettings.DrawChords&&SmallestX!=null){
			g.setColor(Color.GREEN);
			
			g.drawLine((int) (SmallestX).getX(),
					(int) (SmallestX).getY(),
					(int) (LargestX).getX(),
					(int) (LargestX).getY());
			
			g.drawRect((int) (SmallestX).getX(),
					(int) (SmallestX).getY(),
					4,4);
			g.drawRect((int) (LargestX).getX(),
					(int) (LargestX).getY(),
					4,4);
			g.setColor(Color.ORANGE);
			g.drawLine((int) (SmallestY).getX(),
					(int) (SmallestY).getY(),
					(int) (LargestY).getX(),
					(int) (LargestY).getY());
			
			g.drawRect((int) (SmallestY).getX(),
					(int) (SmallestY).getY(),
					4,4);
			g.drawRect((int) (LargestY).getX(),
					(int) (LargestY).getY(),
					4,4);
		}
		
		if (MaxDirection!=null){
			g.setColor(Color.BLACK);
				g.drawRect((int) (MaxDirection).getX(),
						(int) (MaxDirection).getY(),
						4,4);
			g.fillRect((int) (MaxDirection).getX(),
					(int) (MaxDirection).getY(),
					4,4);
			
			
			
			g.drawRect((int) (MinDirection).getX(),
					(int) (MinDirection).getY(),
					4,4);
		g.fillRect((int) (MinDirection).getX(),
				(int) (MinDirection).getY(),
				4,4);
			
		}
		// g.setColor(Color.green);
//		if (StatisticalInfo != null) {
//			
//		}// if of teh StatisticalInfo check

		if (StatisticalInfo != null) {

			// now draw the rectangle of bounds
			if (StatisticalInfo.getBox() != null && (!dirty)) {

				if (SystemSettings.STATISTICAL_POINTS_DRAW)
					StatisticalInfo.paint(g);
				// logger.info( " x "+StatisticalInfo.getBox().getX()+" y
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

		ArrayList<PointData> pointsTemp = getPoints();
		
		area = ComputationsGeometry.computeArea(pointsTemp);
		getStatisticalInfo().setArea(area);
	}

	public void computeRemainigStatists() {
		computeAreaOfStroke();
	}

	public void generateAllDominatePoints() {
		getStatisticalInfo().generateAllDominatePoints();

		
	}


	/** @link dependency */
	/* # BasicImageStructure lnkBasicImageStructure; */

	public void paint(Graphics2D g) {
		drawStroke(g);
	}

	public void setParam(ArrayList Param) {

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
	private boolean RepeatRemoved;
	private PointData SmallestX=null;
	private PointData SmallestY=null;
	private PointData LargestX=null;
	private PointData LargestY=null;
	private boolean	SelfIntersect;
	private int	SelfIntersectionCount;
	private int	OverTraceBlockCount;

	@Override
	public InkInterface createSubInkObject(int start, int end) {
		// {
		//	
		//	
		//	
		ArrayList<PointData> pointsTemp = getPoints();
		Stroke ink = new Stroke();
		ArrayList<PointData> po = new ArrayList<PointData>();
		if (pointsTemp != null) {
//			logger.info("   number of ponit in this stroke = "+pointsTemp.size()+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			for (int i = start; (i < pointsTemp.size()) && (i < end); i++) {
				
				po.add(pointsTemp.get(i));
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
		ArrayList<PointData> pointsTemp = getPoints();
		StringBuilder  str=new StringBuilder();
		str.append("Stroke has ").append( pointsTemp.size()).append(" points = [");
		 for (int i = 0; i < pointsTemp.size(); i++) {
		str.append ("P(").append(pointsTemp.get(i).x).append(",").append(pointsTemp.get(i).y).append("),");
	}
		 log.info(str);
		 log.info(" Bonding box is  = Corner P("+ this.getBox().getX()+", "+this.getBox().getY()+"), w= "+ this.getBox().getWidth()+", h=  "+this.getBox().getHeight());
		 
	}
	public Stroke RemoveRepeatedPoints(){
		ArrayList<PointData> pointsTemp = getPoints();
		if (pointsTemp.size()>0){
			logger.info(" the number of points before removal is "+pointsTemp.size());
		double	thershold=SystemSettings.ThresholdDistancePoint*this.getLength();
			Stroke NewInterploated=new Stroke();
			//  ArrayList pointsa = new ArrayList<PointData>();
			 
		        PointData prev =pointsTemp.get(0);
		        PointData point=null;
		        NewInterploated.addPoint(prev);
		        NewInterploated.setStartPoint(prev);
		        
		        if (debug)
		        {
		        	if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
		        		
		        		DrawingDebugUtils.drawPointPath(DrawingDebugUtils.getGraphics(), DrawingDebugUtils.InkColor, DrawingDebugUtils.PointsColor, getPoints());
		        		
		        	}
		        }
		        
		        
		     //   pointsa.add(prev);
		        for(int i = 1; i < pointsTemp.size(); i++)
		        {
		             point = pointsTemp.get(i);
	//	            double dist = point.distance(prev);
		             // check the x and y and time of 
		             
		             if (point.x!=prev.x && point.time!=prev.time && point.y!=prev.y  )
		             {  // no x or y or time  
		            	 
		            	 prev=point;
		            	 NewInterploated.addPoint(point);
		             }
		             else {
		            	 logger.info("the point has same in one dimention... "+prev +"prev   point "+point);
		            	 if (point.x==prev.x && point.time==prev.time)
			             {// same x 
			            	 //skip 
			             }
		            	 else if (point.y==prev.y && point.time==prev.time){
			            	 // same y 
			            	// skip  
			             } 
		            	 else {
		            		 // only one is the same...
		            		 
		            		 // check if distance is larger than thershol 
		            	  double dist = point.distance(prev); 
		            		  // Maybe not same x or same y but same time... 
		            		 if (point.time==prev.time){
		            			 //same time 
		            			 if (dist>thershold){
		            				 prev=point;
					            	 NewInterploated.addPoint(point);
		            			 }
		            			 ////////////
		            			// 
		            	 }
		            	  else {/// either x 
		            	//	 logger.info(" in state where not equal time but equal either x or y");
		            		 // x may = x but we not in same time.
		            		 /// y may = prev y but not in same time 
		            			 prev=point;
				            	 NewInterploated.addPoint(point);
		            			 
//		            		 long diffTime=point.time-prev.time;
//		            		 if (diffTime>SystemSettings.ThresholdTimeADD){
//		            			 
//		            			 prev=point;
//				            	 NewInterploated.addPoint(point);
//		            		 }
//		            		 else {
//		            			 
//		            			 
//		            		 }
		            		 // check time. 
		            		 
		            	 }
		            	 }
		            	
		             }
		             

		            
		        }
		        if (point!=null)
		        	NewInterploated.setEndPoint(point );
		        
		        
		        NewInterploated.setRepeatedRemoved(true);
		        logger.info("number of ponits after removal of repeated "+NewInterploated.getPointsCount());
		        
		        if (debug)
		        {
		        	if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
		        		
		        		DrawingDebugUtils.drawPointPath(DrawingDebugUtils.getGraphics(), Color.green, Color.ORANGE,NewInterploated.getPoints());
		        		
		        	}
		        }
		        
		        
		        
		        return   NewInterploated;
			
			}
			else{
				
				return this;
			}
		
	}

	private void setRepeatedRemoved(boolean b) {
	 RepeatRemoved=b;
		
	}
	public PointData getLargestChordStart(){
		if ( Orientation==ORIENTATION_HORIZONATAL)
		{
			return LargestX;
		}
		else {
			return LargestY;
		}
	}
public PointData getLargestChordEnd(){
	if ( Orientation==ORIENTATION_HORIZONATAL)
	{
		return SmallestX;
	}
	else {
		return  SmallestY;
	}
	}
private void computeLongestDistance(){
		LongestDistanceBetweenPointsInStroke =0;
		ArrayList<PointData> pointsTemp = getPoints();
		 if (pointsTemp==null)
			 return ;
		 
		 SmallestX=pointsTemp.get(SortedXIndex.get(0));
		 SmallestY=pointsTemp.get(SortedYIndex.get(0));
		 LargestX=pointsTemp.get(SortedXIndex.get(SortedXIndex.size()-1));
		 LargestY=pointsTemp.get(SortedYIndex.get(SortedYIndex.size()-1));
		
		 double dis1=LargestX.distance(SmallestX);
		 double dis2=LargestY.distance(SmallestY);
		 double dis3=this.EndPoint.distance(StartPoint);
		 
		 logger.info(" The distance from last x to first x is "+dis1);
		 logger.info(" The distance from last y to first y is "+dis2);
		 logger.info(" The distance from end point to start   "+dis3);
		 
		 if (dis1>dis2){
			 LongestDistanceBetweenPointsInStroke=dis1;
			 Orientation=ORIENTATION_HORIZONATAL; //|
			 
				 
		 }
		 else {
			 LongestDistanceBetweenPointsInStroke=dis2;
			 Orientation=ORIENTATION_VERTICAL;
			
		 }
		 logger.info("the length is  "+getLength());
		 EndPointtoLengthRation=dis3/getLength();
		 logger.info("  EndPointtoLengthRation  "+ EndPointtoLengthRation);
		 
		 LongestChordtoLengthRatio=LongestDistanceBetweenPointsInStroke/getLength();
		
		 logger.info(" 	 LongestChordtoLengthRatio "+	 LongestChordtoLengthRatio );
		 
		 
		 
}
private void checkClosedShape(){
 
}
 class OverTraceBlock{
	 
	 /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OverTraceBlock [Overtrace=" + Overtrace + "  F(s,e) = (" +startF 
				+ ","+endF+")    with the O =(" + startO +", "+endO+" ) "+ ", intersection=" + intersection
				+ ", size=" + size +  "]";
	}
	int startF, endF; 
	 int startO, endO;
	 int size;
	 
	 boolean intersection=false;
	 boolean  Overtrace=false; 
	 
	 
 }
private void checkOverTraceAndSelfIntersect(){
	
	logger.info("  OverTraceHyposes  = "+OverTraceHyposes+"  number of points is "+getPoints().size());
	 logger.info( "   if overtrace/points  "+( (double)OverTraceHyposes/(double)getPoints().size() )+
			 "  if  overtrace/(size/2) " +((double)OverTraceHyposes/((double)getPoints().size()/2.0)));
	 
	 logger.info(  "  and location of point is   "+   OverTracePoints);
	 
	 
	 logger.info( "  the pair points are "+OverTracePair);

	// double OverTracePercent=(double)OverTraceHyposes/(double)points.size();
	 
	 // now in need to add the add the list use overtrace point into a sorted list to get the overstroked sectin....
	 
	// iit should be ordered as it is the sequence of adding 
	 

		ArrayList<OverTraceBlock> z=new ArrayList<OverTraceBlock>();
		OverTraceBlock zone=null;
		 // now get zones of overtracesss....
		// the sturcture of zone is as follwing 
	 int window=SystemSettings.WINDOW_SCAN_SIZE;
	 int count=0;
	 int prev=0;
	 int prevIndex=0;
	 SelfIntersectionCount=0;
	 OverTraceBlockCount=0;
	 if (OverTracePoints!=null)
		// now to detect part we needd to trace to check if it contious part... 
		 for (int i = 0; i < OverTracePoints.size()-1; i++) {
			 if (zone==null){
			 zone=new OverTraceBlock();
			 zone.startO=OverTracePair.get(i).y;
			 zone.startF=OverTracePair.get(i).x;
			 count=0;
			 }
			 
		 
				 int current=OverTracePoints.get(i);
				 int next=OverTracePoints.get(i+1);
				 
				if ((next-current)>window){// differenct between next overtrace and current is > window
				// jump or single overtrace section...
					logger.info("  BREAAAAA KK of OVERTRACEE>>>>>>>>>>>>>>> at   "+i);
					if (i-prevIndex>window){
						// this is a large over traced part
						zone.Overtrace=true;
						 zone.endO=OverTracePair.get(i).y;
						 zone.endF=OverTracePair.get(i).x;
						 count++;
						 zone.size=count;
						 OverTraced=true;
						 OverTraceBlockCount++;
						// prevIndex=i+1;
						 
					}
					else {
						// this may be a single pont... 
						zone.intersection=true;
						SelfIntersect=true;
						 zone.endO=OverTracePair.get(i).y;
						 zone.endF=OverTracePair.get(i).x;
						 SelfIntersectionCount++;
						 count++;
						 zone.size=count;
					}
					
					z.add(zone);
					zone=null;
					prev=current;
					prevIndex=i+1;
				}
				else {
					// the are less than window 
					// connut to add size 
					 zone.endO=OverTracePair.get(i).y;
					 zone.endF=OverTracePair.get(i).x;
					 count++;
					 zone.size=count;
					
				}
				 
		 }
		 if (zone!=null){
				zone.Overtrace=true;
				OverTraced=true;
			 z.add(zone);
		 }
	// setting the deleted points from the  stroke... 
		 for (int j = 0; j < z.size(); j++) {
			if (z.get(j).Overtrace){
		 
				int s , e; 
				s=z.get(j).startF;
				e=z.get(j).endF;
				for (int j2 = s; j2 < e; j2++) {
					getPoints().get(j2).setDeleted( true);
				}
				
				
				
			}
		}
		 
	 logger.info("  zones of the over trace ... "+z);
	logger.info("  there are "+ SelfIntersectionCount+"   self intersection in this stroke "+"  and  "+ OverTraceBlockCount+"  overtraced blocks.. ");
	OverTracePointsDeleted=true;
//		}
//		 logger.info(" after    delte size is "+points.size());
			 logger.info("  this is overtrace  "+OverTraced+"   and self intersection "+SelfIntersect);
}
private boolean isOverTraced( ){
	
	//TODO: Commplet the over traceb by finishing this function ( check if zone is an overtraced by detecting is neear enough and if parallel )
	return OverTraced;
} 
private boolean isSelfIntersect( ){
	//TODO: Commplet the  self intersecting by finishing this function ( check if zone is an overtraced by detecting is neear enough and if intersect )

	return SelfIntersect;
} 
private void checkTails(){
	ArrayList<PointData> pointsTemp = getPoints();
	// check if thre is tails in the 
	//create a parts with the last 10% and first 10 of the stroke...
	int part=(int) (Math.ceil((0.1)*pointsTemp.size()));
	
	if (part<3){
		part=3;
	}
    InkInterface start = this.createSubInkObject(0,part);
	InkInterface end = this.createSubInkObject(pointsTemp.size()-part-1,pointsTemp.size()-1);
	 
	if (start.canIntersect(end)){
		
		//TODO: now i need to detect the intersections... 
		
	}
	
	
}

	public void PreProcess() {
	  	//TODO: IMPLEMENT THIS FUNCTION 28 JAN
		 logger.info(" PreProcess	//TODO: IMPLEMENT THIS FUNCTION 28 JAN  ");
		 updateOtherFeatures();
//		 logger.info("  OverTraceHyposes  = "+OverTraceHyposes+"  number of points is "+points.size());
//		 logger.info( "   if overtrace/points  "+( (double)OverTraceHyposes/(double)points.size() )+
//				 "  if  overtrace/(size/2) " +((double)OverTraceHyposes/((double)points.size()/2.0)));
		 
	
		 
		 
		computeLongestDistance();
	
  
		checkOverTraceAndSelfIntersect();
				checkTails();
			checkClosedShape();
	}
 public Stroke getUnTracedStroke(){
	ArrayList<PointData> pointsTemp = getPoints();
	if (OverTraced){
	
		// if this stroke is over traced then do the following...
		
		// create a new stroke with the same points... 
		
		Stroke st=new Stroke();
	st.LocationRange=this.LocationRange*2.0;
	st.window=this.window*2;
	st.Orginalpoints=this.getPoints();
	st.setStartPoint(StartPoint);
for (int i = 0; i <pointsTemp.size(); i++) {
		if (!pointsTemp.get(i).isDeleted())
		{
			st.addPoint(pointsTemp.get(i));

		}
}


		st.setEndPoint(st.getPoints().get( st.getPointsCount()-1));
                // check if there is another over trace in the new stroke... 
		
			st.PreProcess();
			
			if (st.OverTraced){
				
				
				Stroke st2=new Stroke();
				st2.LocationRange=st.LocationRange*2.0;
				st2.window=st.window*2;
				
			for (int i = 0; i <st.getPoints().size(); i++) {
					if (!st.getPoints().get(i).isDeleted())
					{
						st2.addPoint(st.getPoints().get(i));

						
						if (st2.getPoints().size()==1){
			           st2.setStartPoint(st.getPoints().get(i));
						}
					}
			}


					st2.setEndPoint(st2.getPoints().get( st2.getPointsCount()-1));
					
					
					st2.PreProcess();
					st2.Orginalpoints=this.getPoints();
					 st2.OverTraceRemoved=true;;
					return st2;
					
					
			}
//             if (st.OverTraced){
//            	 
//            	 return st.getUnTracedStroke();
//             }
//             else {
//            	 
//            	
//             }
		 st.OverTraceRemoved=true;;
            	 return st;
	}
	
	
	return this;
}

	
	
    /**
     * Transform the polyline with the given transform.
     */


}
